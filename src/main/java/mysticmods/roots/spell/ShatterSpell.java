package mysticmods.roots.spell;

import mysticmods.roots.action.ShatterBlockAction;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.*;
import mysticmods.roots.client.particle.bolt.LightningPreset;
import mysticmods.roots.init.ModActions;
import mysticmods.roots.init.ModModifiers;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.network.client.fx.lightning.SemiDynamicLightningFXPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
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
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShatterSpell extends Spell {
  public ShatterSpell(Properties properties) {
    super(properties);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.SHATTER_COOLDOWN;
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
  }

  private int[] getAsymmetricOffsets(int value) {
    int right = (value + 1) / 2;
    int left = (value + 1) / 2;
    return new int[]{value, value};
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

    int width = spell.count(RootsTags.SpellModifiers.SHATTER_INCREASES_WIDTH);
    int height = spell.count(RootsTags.SpellModifiers.SHATTER_INCREASES_HEIGHT);
    int depth = spell.count(RootsTags.SpellModifiers.SHATTER_INCREASES_DEPTH);

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
  }

  @Override
  public boolean hasBlockTarget(Player pPlayer, ISpellInstance instance) {
    return true;
  }

  @Override
  public @Nullable Vec3 getBlockTarget(Player pPlayer, ISpellInstance spell) {
    return pickBlock(pPlayer, spell).getLocation();
  }


  public static boolean capturingDrops = false;

  @Override
  public SpellCastResult cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    //FakePlayerUtil.buildItems(pLevel, pLevel.getRandom());

    ServerPlayer player = (ServerPlayer) pPlayer;

    List<BlockPos> broken = new ArrayList<>();

    BlockHitResult rayTraceResult = pickBlock(pPlayer, instance);
    Map<BlockPos, BlockState> toBreak = getAffectedBlocks(pLevel, pPlayer, instance, pStack, rayTraceResult.getBlockPos(), pLevel.getBlockState(rayTraceResult.getBlockPos()), rayTraceResult);
    double count = 0;
    capturingDrops = instance.has(RootsTags.SpellModifiers.MAGNETISM);
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
/*
      // We check to see if the player is allowed to edit at this place
      if (!pPlayer.mayInteract(pLevel, pos)) {
        continue;
      }
      // Check if it's restricted by game mode
      if (pPlayer.blockActionRestricted(pLevel, pos, player.gameMode.getGameModeForPlayer())) {
        continue;
      }
      // Now fire a NeoForge event
      BlockEvent.BreakEvent event = CommonHooks.fireBlockBreak(pLevel, player.gameMode.getGameModeForPlayer(), player, pos, state);
*/
/*      BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(pLevel, pos, state, player);
      NeoForge.EVENT_BUS.post(event);*//*

      if (event.isCanceled()) {
        continue;
      }

      // Fake player -> pretend to be the player
      // Copy of the staff currently casting
      // -> Items in player's inventory
      // -> Effects on player
      // -> Attributes/attribute modifiers on player
      // -> Enchantments on player


*/


      if (((ServerPlayer) pPlayer).gameMode.destroyBlock(pos)) {/*          pLevel.destroyBlock(pos, true, pPlayer)) {*/
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

    //Vec3 start = pPlayer.getEyePosition().subtract(0, 0.3, 0);

    for (BlockPos pos : broken) {
      Vec3 stop = Vec3.atCenterOf(pos);
      PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, new SemiDynamicLightningFXPacket(LightningPreset.SHATTER, 2, pPlayer.getId(), stop, /*(int) Math.sqrt(start.subtract(stop).lengthSqr() * 2)*/8));
    }

    if (count == 0) {
      costs.noCharge();
      return SpellCastResult.nothing();
    } else {
      costs.operations(Mth.floor(count));
      return SpellCastResult.success(Mth.floor(count), Mth.floor(cooldown * count));
    }
  }

  @Override
  public Component[] createExtendedDescriptionComponents() {
    return new Component[0];
  }

  @Override
  public Component[] createModifierDescriptionComponents(SpellModifier spellModifier) {
    if (spellModifier.is(RootsTags.SpellModifiers.SHATTER_INCREASES_PARAMETERS)) {
      int count;
      if (spellModifier.is(ModModifiers.SHATTER_DEPTH_I) || spellModifier.is(ModModifiers.SHATTER_HEIGHT_I) || spellModifier.is(ModModifiers.SHATTER_WIDTH_I)) {
        count = 3;
      } else if (spellModifier.is(ModModifiers.SHATTER_DEPTH_II) || spellModifier.is(ModModifiers.SHATTER_HEIGHT_II) || spellModifier.is(ModModifiers.SHATTER_WIDTH_II)) {
        count = 5;
      } else {
        count = 1;
      }
      return new Component[]{
          Component.literal(String.valueOf(count))
      };
    }
    return new Component[]{};
  }
}
