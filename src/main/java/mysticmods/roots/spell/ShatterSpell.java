package mysticmods.roots.spell;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import mysticmods.roots.action.ShatterBlockAction;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.client.particle.bolt.LightningPreset;
import mysticmods.roots.init.ModActions;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.network.client.fx.lightning.SemiDynamicLightningFXPacket;
import mysticmods.roots.util.FakePlayerUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ShatterSpell extends Spell {
  private int maxWidth, maxDepth, maxHeight;

  public ShatterSpell(ChatFormatting color, CostInstance costs) {
    super(Type.INSTANT, color, costs, 0x606060, 0xc0c0c0);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.SHATTER_COOLDOWN;
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.maxWidth = properties.get(ModSpells.SHATTER_MAXIMUM_WIDTH);
    this.maxDepth = properties.get(ModSpells.SHATTER_MAXIMUM_DEPTH);
    this.maxHeight = properties.get(ModSpells.SHATTER_MAXIMUM_HEIGHT);
  }

  @Override
  protected void fillDataKeyMap(Object2IntMap<String> map) {
    super.fillDataKeyMap(map);
    map.put("mode", 0);
    map.put("width", 1);
    map.put("height", 2);
    map.put("depth", 3);
  }

  @Override
  public Set<String> getTooltipDataKeys() {
    return Set.of("width", "height", "depth");
  }

  @Override
  protected void fillDataMaximumValues(Int2IntMap map) {
    super.fillDataMaximumValues(map);
    map.put(0, 3);
    map.put(1, maxWidth);
    map.put(2, maxHeight);
    map.put(3, maxDepth);
  }

  private int[] getAsymmetricOffsets(int value) {
    int right = (value + 1) / 2;
    int left = value / 2;
    return new int[]{left, right};
  }

  @Override
  public Map<BlockPos, BlockState> getAffectedBlocks(Level level, Player player, ISpellInstance spell, ItemStack stack, BlockPos pos, BlockState blockState, BlockHitResult rayTraceResult) {
    Map<BlockPos, BlockState> result = new HashMap<>();

    float yaw = player.getViewYRot(1f);
    Direction playerFacing = Direction.fromYRot(yaw);
    Direction sideDir = rayTraceResult.getDirection();
    Direction widthDir = Direction.fromYRot(playerFacing.toYRot() + 90);
    Direction heightDir = sideDir == Direction.DOWN ? playerFacing : sideDir.getAxis() == Direction.Axis.Y ? playerFacing.getOpposite() : Direction.DOWN;
    Direction depthDir = sideDir.getOpposite();

    int width = getDataValue(spell, "width");
    int height = getDataValue(spell, "height");
    int depth = getDataValue(spell, "depth");

    BlockPos start = pos;
    BlockPos stop = pos;

    if (width > 0) {
      int[] widthOffsets = getAsymmetricOffsets(width);
      start = start.relative(widthDir, -widthOffsets[0]);
      stop = stop.relative(widthDir, widthOffsets[1]);
    }
    if (height > 0) {
      int[] heightOffsets = getAsymmetricOffsets(height);
      start = start.relative(heightDir, -heightOffsets[0]);
      stop = stop.relative(heightDir, heightOffsets[1]);
    }
    if (depth > 0) {
      int[] depthOffsets = getAsymmetricOffsets(depth);
      start = start.relative(depthDir, -depthOffsets[0]);
      stop = stop.relative(depthDir, depthOffsets[1]);
    }

    for (BlockPos blockPos : BlockPos.betweenClosed(start, stop)) {
      BlockState state = level.getBlockState(blockPos);
      if (state.isAir()) {
        continue;
      }
      result.put(blockPos.immutable(), state);
    }

    return result;
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModSpells.SHATTER_MAXIMUM_DEPTH);
    properties.add(ModSpells.SHATTER_MAXIMUM_HEIGHT);
    properties.add(ModSpells.SHATTER_MAXIMUM_WIDTH);
  }

  @Override
  public boolean hasBlockTarget(Player pPlayer) {
    return true;
  }

  @Override
  public @Nullable Vec3 getBlockTarget(Player pPlayer) {
    return pickBlock(pPlayer).getLocation();
  }

  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    FakePlayerUtil.buildItems(pLevel, pLevel.getRandom());

    ServerPlayer player = (ServerPlayer) pPlayer;

    List<BlockPos> broken = new ArrayList<>();

    BlockHitResult rayTraceResult = pickBlock(pPlayer);
    Map<BlockPos, BlockState> toBreak = getAffectedBlocks(pLevel, pPlayer, instance, pStack, rayTraceResult.getBlockPos(), pLevel.getBlockState(rayTraceResult.getBlockPos()), rayTraceResult);
    double count = 0;
    for (Map.Entry<BlockPos, BlockState> entry : toBreak.entrySet()) {
      BlockPos pos = entry.getKey();
      BlockState state = entry.getValue();
      // We don't try to destroy air
      if (pLevel.isEmptyBlock(pos)) {
        continue;
      }
      // We ignored explicitly tagged blocks
      if (state.is(RootsTags.Blocks.SHATTER_EXCLUDE)) {
        continue;
      }
      // We also ignore blocks that have a negative destroy speed, unless we forcefully include them
      if (state.getDestroySpeed(pLevel, pos) < 0 && !state.is(RootsTags.Blocks.SHATTER_INCLUDE)) {
        continue;
      }
      // We check to see if the player is allowed to edit at this place
      if (!pPlayer.mayInteract(pLevel, pos)) {
        continue;
      }
      // Check if it's restricted by game mode
      if (pPlayer.blockActionRestricted(pLevel, pos, player.gameMode.getGameModeForPlayer())) {
        continue;
      }
      // Now fire a NeoForge event
      BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(pLevel, pos, state, player);
      NeoForge.EVENT_BUS.post(event);
      if (event.isCanceled()) {
        continue;
      }

      if (pLevel.destroyBlock(pos, true, pPlayer)) {
        ShatterBlockAction.Context context = new ShatterBlockAction.Context((ServerLevel) pLevel, player, pos, state, instance);
        ModActions.SHATTER_BLOCK.get().accept(context);
        count += DataMaps.getShatterCostMultiplier(state.getBlock());
        broken.add(pos);
      }
    }

    // TODO:
    /*if (!broken.isEmpty()) {
      PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, new CastShatterFX(pPlayer.getId(), broken));
    }*/

    Vec3 start = pPlayer.getEyePosition().subtract(0, 0.3, 0);

    for (BlockPos pos : broken) {
      Vec3 stop = Vec3.atCenterOf(pos);
      PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, new SemiDynamicLightningFXPacket(LightningPreset.SHATTER, 2, pPlayer.getId(), stop, /*(int) Math.sqrt(start.subtract(stop).lengthSqr() * 2)*/8));
    }

    if (count == 0) {
      costs.noCharge();
      return -1;
    } else {
      costs.operations(Mth.floor(count));
      return Mth.floor(cooldown * count);
    }
  }

  @Override
  public CostInstance.ChargeType getChargeType() {
    return CostInstance.ChargeType.OPERATION;
  }
}
