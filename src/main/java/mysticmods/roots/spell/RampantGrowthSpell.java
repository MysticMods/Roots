package mysticmods.roots.spell;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellCastResult;
import mysticmods.roots.init.ModSpells;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class RampantGrowthSpell extends Spell {
  private int interval, count;

  public RampantGrowthSpell(Spell.Properties properties) {
    super(properties);
  }

  @Override
  public Component[] createExtendedDescriptionComponents() {
    return new Component[0];
  }

  @Override
  public Component[] createModifierDescriptionComponents(SpellModifier spellModifier) {
    return new Component[0];
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.RAMPANT_GROWTH_COOLDOWN_UNUSED;
  }

/*  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModSpells.RAMPANT_GROWTH_RADIUS_Y;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusZXProperty() {
    return ModSpells.RAMPANT_GROWTH_RADIUS_ZX;
  }*/

  @Override
  public void initialize(Holder<Spell> holder) {
    var properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
/*    this.interval = properties.get(ModSpells.RAMPANT_GROWTH_INTERVAL);
    this.count = properties.get(ModSpells.RAMPANT_GROWTH_COUNT);*/
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
/*    properties.add(ModSpells.RAMPANT_GROWTH_INTERVAL);
    properties.add(ModSpells.RAMPANT_GROWTH_COUNT);*/
  }

/*  @Override
  public DataComponentType<? extends Cycling<?>> getCycleComponent(ISpellInstance iSpellInstance) {
    return ModAttachments.AOE_GROWTH_MODE.get();
  }*/

  @Override
  public SpellCastResult cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    return SpellCastResult.nothing();
/*    if (ticks % interval == 0) {
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
    }*/
  }

  @Override
  public int getBaseMaximumOperations() {
    return count;
  }
}
