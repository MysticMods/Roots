package mysticmods.roots.blockentity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import mysticmods.roots.action.TradeFairyHutAction;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.blockentity.ClientTickBlockEntity;
import mysticmods.roots.api.blockentity.ServerTickBlockEntity;
import mysticmods.roots.api.grove.GrovePower;
import mysticmods.roots.api.grove.IGroveConsumer;
import mysticmods.roots.api.grove.IGroveInstance;
import mysticmods.roots.api.grove.PowerTicket;
import mysticmods.roots.api.reference.Constants;
import mysticmods.roots.block.FairyHutBlock;
import mysticmods.roots.blockentity.template.UseDelegatedBlockEntity;
import mysticmods.roots.entity.other.FairyHutEntity;
import mysticmods.roots.init.ModActions;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModBlockEntities;
import mysticmods.roots.init.ModEntities;
import mysticmods.roots.ritual.ProtectionRitual;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.UUID;

public class FairyHutBlockEntity extends UseDelegatedBlockEntity implements ServerTickBlockEntity, ClientTickBlockEntity, IGroveConsumer, Merchant {
  private static final PowerTicket.TicketDefinition TICKET_DEFINITION = new PowerTicket.TicketDefinition(
      ImmutableList.of(new GrovePower.Consumer(RootsTags.Groves.FAIRY, 15))
  );

  private boolean wasPoweredLastTick = false;
  private PowerTicket ticket;

  // Trading stuff
  private UUID tradingPlayerUUID = null;
  private Player tradingPlayer = null;
  private int xp = 0;
  private int xpLevel = 1; // Always starts at level 1
  private MerchantOffers offers;

  private boolean morningReset = false;
  private boolean afternoonReset = false;

  private boolean increaseProfessionLevelOnUpdate;
  private int updateMerchantTimer;

  private long lastPoweredTick = 0;

  private Tag offersTag = null;

  public FairyHutBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
    super(ModBlockEntities.FAIRY_HUT.get(), pWorldPosition, pBlockState);
  }

  private static VillagerProfession professionFromState (BlockState state) {
    if (state.is(RootsTags.Blocks.RED_HUTS)) {
      return VillagerProfession.WEAPONSMITH;
    } else if (state.is(RootsTags.Blocks.BROWN_HUTS)) {
      return VillagerProfession.FARMER;
    } else if (state.is(RootsTags.Blocks.BAFFLECAP_HUTS)) {
      return VillagerProfession.CLERIC;
    } else if (state.is(RootsTags.Blocks.CRIMSON_HUTS)) {
      return VillagerProfession.TOOLSMITH;
    } else {
      return VillagerProfession.LIBRARIAN;
    }
  }

  @Override
  public void clientTick(Level pLevel, BlockPos pPos, BlockState pState) {
    if (pState.getValue(FairyHutBlock.ACTIVE)) {
    }
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (getLevel() == null) {
      RootsAPI.LOG.error("I feel like this is a broken contract: onLoad called without a level for {}", this);
      return;
    }

    getLevel().getData(ModAttachments.GROVE_CONSUMERS).add(getBlockPos());
  }

  private void resetOffers () {
    this.getOffers().forEach(MerchantOffer::resetUses);
  }

  private void catchUpDemand() {
    int i = 2;
    if (morningReset) {
      i--;
    }
    if (afternoonReset) {
      i--;
    }
    if (i > 0) {
      this.resetOffers();
    }

    for (int j = 0; j < i; j++) {
      this.updateDemand();
    }
  }

  private void updateDemand () {
    this.getOffers().forEach(MerchantOffer::updateDemand);
  }

  @Override
  public void serverTick(ServerLevel pLevel, BlockPos pPos, BlockState pState) {
    getTicketForTick(pLevel.getGameTime());

    if (!pState.getValue(FairyHutBlock.ACTIVE) && wasPoweredLastTick) {
      pLevel.setBlock(pPos, pState.setValue(FairyHutBlock.ACTIVE, true), 3);
      BlockState aboveState = pLevel.getBlockState(pPos.above());
      pLevel.setBlock(pPos.above(), aboveState.setValue(FairyHutBlock.ACTIVE, true), 3);
    } else if (pState.getValue(FairyHutBlock.ACTIVE) && !wasPoweredLastTick) {
      pLevel.setBlock(pPos, pState.setValue(FairyHutBlock.ACTIVE, false), 3);
      BlockState aboveState = pLevel.getBlockState(pPos.above());
      if (aboveState.getBlock() instanceof FairyHutBlock) {
        pLevel.setBlock(pPos.above(), aboveState.setValue(FairyHutBlock.ACTIVE, false), 3);
      }
    }

    boolean changed = false;

    if (wasPoweredLastTick()) {
      if (!this.isTrading() && this.updateMerchantTimer > 0) {
        this.updateMerchantTimer--;
        if (this.updateMerchantTimer <= 0) {
          if (this.increaseProfessionLevelOnUpdate) {
            this.increaseMerchantCareer();
            this.increaseProfessionLevelOnUpdate = false;
          }
        }
        changed = true;
      }

      long time = pLevel.getDayTime() % ProtectionRitual.getDayLength();
      if (!morningReset && time >= Constants.FAIRY_HUT_MORNING_RESET) {
        morningReset = true;
        resetOffers();
        changed = true;
      } else if (!afternoonReset && time >= Constants.FAIRY_HUT_AFTERNOON_RESET) {
        afternoonReset = true;
        resetOffers();
        changed = true;
      }
      if (time < Constants.FAIRY_HUT_MORNING_RESET) {
        // Reset the morning and afternoon resets
        morningReset = false;
        afternoonReset = false;
        catchUpDemand();
        changed = true;
      }

      if (changed) {
        setChanged();
        updateViaState();
      }
    }
  }

  private void increaseMerchantCareer() {
    this.xpLevel += 1;
    updateTrades();
    setChanged();
    updateViaState();
  }

  private boolean isTrading() {
    return getTradingPlayer() != null;
  }

  @Override
  public boolean isBounded() {
    return false;
  }

  @Override
  public PowerTicket getTicketForTick(long tick) {
    if (ticket == null) {
      ticket = TICKET_DEFINITION.create(tick);
      return ticket;
    }

    if (ticket.isValid(tick)) {
      return ticket;
    }

    if (ticket.wasFullfilled()) {
      if (!this.wasPoweredLastTick) {
        this.wasPoweredLastTick = true;
        setChanged();
        updateViaState();
      }
    } else if (this.wasPoweredLastTick) {
      this.wasPoweredLastTick = false;
      setChanged();
      updateViaState();
    }

    ticket = TICKET_DEFINITION.create(tick);
    return ticket;
  }

  @Override
  public boolean wasPoweredLastTick() {
    return wasPoweredLastTick;
  }

  @Override
  protected void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
    super.loadAdditional(compound, registries);
    this.wasPoweredLastTick = compound.getBoolean("powered");
    this.tradingPlayer = null;
    if (compound.hasUUID("tradingPlayer")) {
      this.tradingPlayerUUID = compound.getUUID("tradingPlayer");
    }
    if (compound.contains("xp")) {
      this.xp = compound.getInt("xp");
    }
    if (compound.contains("level")) {
      this.xpLevel = compound.getInt("level");
    }
    if (compound.contains("Offers")) {
      this.offersTag = compound.get("Offers");
    }
    if (compound.contains("morningReset")) {
      this.morningReset = compound.getBoolean("morningReset");
    }
    if (compound.contains("afternoonReset")) {
      this.afternoonReset = compound.getBoolean("afternoonReset");
    }

  }

  @Override
  protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider lookup) {
    super.saveAdditional(pTag, lookup);
    pTag.putBoolean("powered", this.wasPoweredLastTick);
    if (tradingPlayerUUID != null) {
      pTag.putUUID("tradingPlayer", this.tradingPlayerUUID);
    } else if (tradingPlayer != null) {
      pTag.putUUID("tradingPlayer", this.tradingPlayer.getUUID());
    }
    pTag.putInt("xp", this.xp);
    pTag.putInt("level", this.xpLevel);
    pTag.putBoolean("morningReset", this.morningReset);
    pTag.putBoolean("afternoonReset", this.afternoonReset);
    if (!this.getLevel().isClientSide()) {
      MerchantOffers merchantoffers = this.getOffers();
      if (!merchantoffers.isEmpty()) {
        pTag.put("Offers", MerchantOffers.CODEC.encodeStart(this.getLevel().registryAccess()
            .createSerializationContext(NbtOps.INSTANCE), merchantoffers).getOrThrow()
        );
      }
    }
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult ray, InteractionHand hand, ItemStack stack) {
    if (!wasPoweredLastTick()) {
      return InteractionResult.FAIL;
    }

    if (!level.isClientSide()) {
      if (getTradingPlayer() != null && getTradingPlayer() != player) {
        // If another player is already trading, we can't open the trading screen
        return InteractionResult.FAIL;
      }

      this.setTradingPlayer(player);
      this.openTradingScreen(player, Component.empty(), xpLevel);
    }

    return InteractionResult.CONSUME;
  }

  @Override
  public void setTradingPlayer(@Nullable Player tradingPlayer) {
    this.tradingPlayer = tradingPlayer;
  }

  @Override
  public @Nullable Player getTradingPlayer() {
    if (tradingPlayerUUID != null) {
      this.tradingPlayer = getLevel().getPlayerByUUID(tradingPlayerUUID);
      this.tradingPlayerUUID = null;
    }
    return tradingPlayer;
  }

  @Override
  public MerchantOffers getOffers() {
    if (getLevel().isClientSide()) {
      throw new IllegalStateException("Cannot load trading offers on the client side.");
    }

    if (this.offersTag != null) {
      MerchantOffers.CODEC
          .parse(this.getLevel().registryAccess().createSerializationContext(NbtOps.INSTANCE), offersTag)
          .resultOrPartial(Util.prefix("Failed to load offers: ", RootsAPI.LOG::warn))
          .ifPresent(p_323775_ -> {
            this.offers = p_323775_;
            this.offersTag = null;
          });
    }

    if (this.offers == null) {
      this.offers = new MerchantOffers();
      this.updateTrades();
    }

    return this.offers;
  }

  private void updateTrades() {
    // TODO: Custom trades
    Int2ObjectMap<VillagerTrades.ItemListing[]> int2objectmap = VillagerTrades.TRADES.get(professionFromState(getBlockState()));

    if (int2objectmap != null && !int2objectmap.isEmpty()) {
      VillagerTrades.ItemListing[] avillagertrades$itemlisting = int2objectmap.get(xpLevel);
      if (avillagertrades$itemlisting != null) {
        MerchantOffers merchantoffers = this.getOffers();
        this.addOffersFromItemListings(merchantoffers, avillagertrades$itemlisting, 2);
      }
    }
  }

  protected void addOffersFromItemListings(MerchantOffers givenMerchantOffers, VillagerTrades.ItemListing[] newTrades, int maxNumbers) {
    ArrayList<VillagerTrades.ItemListing> arraylist = Lists.newArrayList(newTrades);
    int i = 0;

    FairyHutEntity entity = new FairyHutEntity(ModEntities.FAIRY_HUT.get(), this.getLevel());
    entity.setPos(getPosition());

    while (i < maxNumbers && !arraylist.isEmpty()) {
      MerchantOffer merchantoffer = arraylist.remove(this.getRandom().nextInt(arraylist.size()))
          .getOffer(entity, this.getRandom());
      if (merchantoffer != null) {
        givenMerchantOffers.add(merchantoffer);
        i++;
      }
    }
  }

  @Override
  public void overrideOffers(MerchantOffers offers) {
    // Not sure why this does nothing
  }

  @Override
  public void notifyTrade(MerchantOffer offer) {
    offer.increaseUses();
    /*    this.ambientSoundTime = -this.getAmbientSoundInterval();*/
    this.rewardTradeXp(offer);
    if (this.tradingPlayer instanceof ServerPlayer serverPlayer) {
      TradeFairyHutAction.Context context = new TradeFairyHutAction.Context(
          (ServerLevel) this.getLevel(), serverPlayer, this, getBlockPos(), getBlockState(), offer);
      ModActions.TRADE_FAIRY_HUT.get().accept(context);
      // TODO: New trigger
      /*      CriteriaTriggers.TRADE.trigger((ServerPlayer) this.tradingPlayer, this, offer.getResult());*/
    }
  }

  private void rewardTradeXp(MerchantOffer offer) {
    int i = 3 + this.getLevel().getRandom().nextInt(4);
    this.xp += offer.getXp();
    if (this.shouldIncreaseLevel()) {
      this.updateMerchantTimer = 40;
      this.increaseProfessionLevelOnUpdate = true;
      i += 5;
    }

    if (offer.shouldRewardExp()) {
      BlockPos pos = this.getBlockPos();
      this.getLevel()
          .addFreshEntity(new ExperienceOrb(this.getLevel(), pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, i));
    }
  }

  private boolean shouldIncreaseLevel() {
    return VillagerData.canLevelUp(xpLevel) && this.xp >= VillagerData.getMaxXpPerLevel(xpLevel);
  }


  @Override
  public void notifyTradeUpdated(ItemStack stack) {
/*    if (!this.level().isClientSide && this.ambientSoundTime > -this.getAmbientSoundInterval() + 20) {
      this.ambientSoundTime = -this.getAmbientSoundInterval();
      this.makeSound(this.getTradeUpdatedSound(!stack.isEmpty()));
    }*/
  }

  @Override
  public int getVillagerXp() {
    return xp;
  }

  @Override
  public void overrideXp(int xp) {
    this.xp = xp;
    if (!getLevel().isClientSide()) {
      setChanged();
      updateViaState();
    }
  }

  @Override
  public boolean showProgressBar() {
    return true;
  }

  @Override
  public SoundEvent getNotifyTradeSound() {
    return SoundEvents.WANDERING_TRADER_YES;
  }

  @Override
  public boolean isClientSide() {
    return getLevel().isClientSide();
  }

  private Vec3 pos;

  public Vec3 getPosition() {
    if (pos == null) {
      pos = Vec3.atCenterOf(getBlockPos());
    }
    return pos;
  }

}
