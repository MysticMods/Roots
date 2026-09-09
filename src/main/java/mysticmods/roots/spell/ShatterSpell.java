package mysticmods.roots.spell;

import mysticmods.roots.action.ShatterBlockAction;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.CastResult;
import mysticmods.roots.client.particle.bolt.LightningPreset;
import mysticmods.roots.init.ModActions;
import mysticmods.roots.init.ModModifiers;
import mysticmods.roots.network.client.fx.lightning.SemiDynamicLightningFXPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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
  public static boolean IS_CASTING_SHATTER = false;

  public ShatterSpell(Properties properties) {
    super(properties);
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
      start = start.relative(widthDir, -width);
      stop = stop.relative(widthDir, width);
    }
    if (height > 0) {
      start = start.relative(heightDir, -height);
      stop = stop.relative(heightDir, height);
    }
    if (depth > 0) {
      start = start.relative(depthDir, -depth);
      stop = stop.relative(depthDir, depth);
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
  public boolean hasBlockTarget(ISpellInstance instance, Player pPlayer) {
    return true;
  }

  @Override
  public @Nullable Vec3 getBlockTarget(ISpellInstance spell, Player pPlayer) {
    return pickBlock(pPlayer, spell).getLocation();
  }


  public static boolean capturingDrops = false;

  @Override
  public CastResult cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
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

      IS_CASTING_SHATTER = true;
      if (((ServerPlayer) pPlayer).gameMode.destroyBlock(pos)) {/*          pLevel.destroyBlock(pos, true, pPlayer)) {*/
        if (ModActions.SHATTER_BLOCK.get().shouldTest()) {
          ShatterBlockAction.Context context = new ShatterBlockAction.Context((ServerLevel) pLevel, player, pos, state, instance);
          ModActions.SHATTER_BLOCK.get().accept(context);
        }
        pLevel.levelEvent(2001, pos, Block.getId(state));
        count += DataMaps.getShatterCostMultiplier(state.getBlock());
        broken.add(pos);
      }
      IS_CASTING_SHATTER = false;
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
      return CastResult.nothing();
    } else {
      costs.operations(Mth.floor(count));
      return CastResult.success(Mth.floor(count), Mth.floor(cooldown * count));
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
      if (spellModifier.is(ModModifiers.SHATTER_DEPTH_1) || spellModifier.is(ModModifiers.SHATTER_HEIGHT_1) || spellModifier.is(ModModifiers.SHATTER_WIDTH_1)) {
        count = 3;
      } else if (spellModifier.is(ModModifiers.SHATTER_DEPTH_2) || spellModifier.is(ModModifiers.SHATTER_HEIGHT_2) || spellModifier.is(ModModifiers.SHATTER_WIDTH_2)) {
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
