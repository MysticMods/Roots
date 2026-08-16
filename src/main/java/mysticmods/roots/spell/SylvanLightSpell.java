package mysticmods.roots.spell;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.spell.*;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModSpells;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SylvanLightSpell extends Spell {
  protected double maxDistance = 0;

  public SylvanLightSpell(Properties properties) {
    super(properties);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.SYLVAN_LIGHT_COOLDOWN;
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> result) {
    super.buildProperties(result);
    result.add(ModSpells.SYLVAN_LIGHT_MAX_DISTANCE);
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.maxDistance = properties.get(ModSpells.SYLVAN_LIGHT_MAX_DISTANCE);
  }

  @Override
  public SpellCastResult cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    Vec3 look = pPlayer.getLookAngle().scale(1.5);
    BlockPos potentialPos = BlockPos.containing(pPlayer.getEyePosition().add(look.x, look.y, look.z));
    boolean doPlace = pLevel.isEmptyBlock(potentialPos);
    if (!doPlace) {
      look = pPlayer.getLookAngle().scale(0.2);
      potentialPos = BlockPos.containing(pPlayer.getEyePosition().add(look.x, look.y, look.z));
      doPlace = pLevel.isEmptyBlock(potentialPos);
    }
    if (!doPlace) {
      BlockPlaceContext context = new BlockPlaceContext(pLevel, null, InteractionHand.MAIN_HAND, ItemStack.EMPTY, new BlockHitResult(Vec3.ZERO, Direction.UP, potentialPos, false));
      BlockState stateAt = pLevel.getBlockState(potentialPos);
      if (stateAt.canBeReplaced(context)) {
        doPlace = true;
      }
    }

    if (doPlace) {
      pLevel.setBlock(potentialPos, ModBlocks.SYLVAN_LIGHT.get().defaultBlockState(), 3);
    } else {
      costs.noCharge();
      return SpellCastResult.nothing();
    }

    return SpellCastResult.success(cooldown);
  }

  @Override
  public Component[] createExtendedDescriptionComponents() {
    return new Component[]{
        Component.literal(String.format("%.1f", maxDistance))
    };
  }

  // TODO: when modifiers
  @Override
  public Component[] createModifierDescriptionComponents(SpellModifier spellModifier) {
    return new Component[]{};
  }
}
