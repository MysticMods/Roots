package mysticmods.roots.spell;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import mysticmods.roots.action.CropGrowthAction;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.registry.ICosted;
import mysticmods.roots.api.registry.ICostedChild;
import mysticmods.roots.api.spell.*;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.init.ModActions;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModModifiers;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.network.client.fx.GrowthFXPacket;
import mysticmods.roots.network.client.fx.RampantGrowthFXPacket;
import mysticmods.roots.spell.mode.AOEGrowthMode;
import mysticmods.roots.util.GrowthUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// TODO for multi-phase spell based on modifiers:
// - Handle text colour change
// - Handle particle colour change
// - Handle texture colour change
// - Handle icon change(?)
public class GrowthInfusionSpell extends TwoRadiusSpell {
  private int interval, count;

  public GrowthInfusionSpell(Properties properties) {
    super(properties);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.GROWTH_INFUSION_COOLDOWN;
  }

  @Override
  public PropertyHolder<Property.DoubleProperty> getReachProperty() {
    return ModSpells.GROWTH_INFUSION_ADDED_REACH;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModSpells.RAMPANT_GROWTH_RADIUS_Y;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusZXProperty() {
    return ModSpells.RAMPANT_GROWTH_RADIUS_ZX;
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    var properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.interval = properties.get(ModSpells.RAMPANT_GROWTH_INTERVAL);
    this.count = properties.get(ModSpells.RAMPANT_GROWTH_COUNT);
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModSpells.RAMPANT_GROWTH_INTERVAL);
    properties.add(ModSpells.RAMPANT_GROWTH_COUNT);
  }

  @Override
  @Nullable
  public DataComponentType<? extends Cycling<?>> getCycleComponent(ISpellInstance instance) {
    if (instance.has(ModModifiers.RAMPANT_GROWTH)) {
      return ModAttachments.AOE_GROWTH_MODE.get();
    }

    return null;
  }

  @Override
  public boolean hasBlockTarget(Player pPlayer, ISpellInstance instance) {
    return !instance.has(ModModifiers.RAMPANT_GROWTH);
  }

  @Override
  public @Nullable Vec3 getBlockTarget(Player pPlayer, ISpellInstance spell) {
    if (spell.has(ModModifiers.RAMPANT_GROWTH)) {
      return null;
    }
    return pickBlock(pPlayer, spell).getLocation();
  }

  @Override
  public SpellCastResult cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    if (instance.has(ModModifiers.RAMPANT_GROWTH)) {
      if (ticks % interval == 0) {
        AOEGrowthMode mode = instance.getSpellData(ModAttachments.AOE_GROWTH_MODE);
        ItemStack offHandItem = pPlayer.getOffhandItem();
        Block tempBlock = offHandItem.getItemHolder().getData(DataMaps.GROWTH_SEED_TO_CROP);
        if (tempBlock == null) {
          if (offHandItem.getItem() instanceof BlockItem blockItem) {
            tempBlock = blockItem.getBlock();
          }
        }

        if (getBoundingBox() == null) {
          RootsAPI.LOG.error("For some reason the Rampant Growth spell does not have a bounding box");
          costs.noCharge();
          return SpellCastResult.fail();
        }

        final Block block = tempBlock;

        boolean offHand = mode == AOEGrowthMode.HELD_IN_OFFHAND && !offHandItem.isEmpty() && tempBlock != null;

        BoundingBox search = getBoundingBox().moved((int) pPlayer.getX(), (int) pPlayer.getY(), (int) pPlayer.getZ());
        List<BlockPos> positions = new ArrayList<>();
        BlockPos.betweenClosedStream(search).forEach(pos -> {
          BlockState state = pLevel.getBlockState(pos);
          if (GrowthUtil.growthTicks(pLevel, pos, state, pPlayer) > 0) {
            if (mode == AOEGrowthMode.IGNORE_TAGGED && state.is(RootsTags.Blocks.RAMPANT_GROWTH_EXCLUDE_MODE)) {
              return;
            }
            if (offHand && !state.is(block)) {
              return;
            }
            positions.add(pos.immutable());
          }
        });
        if (positions.isEmpty()) {
          costs.noCharge();
          return SpellCastResult.nothing();
        }
        int growCount = 0;
        for (int i = 0; i < count; i++) {
          if (positions.isEmpty()) {
            break;
          }
          BlockPos pos = positions.get(pLevel.random.nextInt(positions.size()));
          int doTicks = GrowthUtil.growthTicks(pLevel, pos, null, pPlayer);
          if (doTicks > 0) {
            if (pLevel.random.nextInt(doTicks) == 0) {
              BlockState oldState = pLevel.getBlockState(pos);
              oldState.randomTick((ServerLevel) pLevel, pos, pLevel.random);
              PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, new RampantGrowthFXPacket(pos));
              BlockState newState = pLevel.getBlockState(pos);
              CropGrowthAction.Context context = new CropGrowthAction.Context((ServerLevel) pLevel, (ServerPlayer) pPlayer, pos, newState, oldState, pHand, pStack, instance);
              ModActions.CROP_GROWTH.get().accept(context);
              growCount++;
            }
          }
        }
        if (growCount == 0) {
          costs.noCharge();
          return SpellCastResult.nothing();
        } else {
          costs.operations(growCount);
        }

        return SpellCastResult.success(growCount, cooldown);
      } else {
        costs.noCharge();
        return SpellCastResult.tick();
      }
    } else {
      BlockHitResult result = pickBlock(pPlayer, instance);
      BlockPos pos = result.getBlockPos();
      BlockState at = pLevel.getBlockState(pos);

      int doTicks = GrowthUtil.growthTicks(pLevel, pos, at, pPlayer);
      if (doTicks > 0) {
        if (pLevel.random.nextInt(doTicks) == 0) {
          at.randomTick((ServerLevel) pLevel, pos, pLevel.random);
          BlockState newState = pLevel.getBlockState(pos);
          CropGrowthAction.Context context = new CropGrowthAction.Context((ServerLevel) pLevel, (ServerPlayer) pPlayer, pos, newState, at, pHand, pStack, instance);
          ModActions.CROP_GROWTH.get().accept(context);
          PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) pLevel, new ChunkPos(result.getBlockPos()), new GrowthFXPacket(pos));
        }
      } else {
        costs.noCharge();
        pPlayer.stopUsingItem();
        return SpellCastResult.nothing();
      }

      costs.operations(1);
      return SpellCastResult.success(cooldown);
    }
  }

  @Override
  public int getMaximumOperations (Object2BooleanMap<ICosted> modifierMap) {
    if (modifierMap.getBoolean(ModModifiers.RAMPANT_GROWTH)) {
      return count;
    }

    return super.getMaximumOperations(modifierMap);
  }
}
