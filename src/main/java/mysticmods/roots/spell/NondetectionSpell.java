package mysticmods.roots.spell;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.spell.*;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.init.ModSpells;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class NondetectionSpell extends Spell {
  private int potionDuration;

  public NondetectionSpell(ChatFormatting color, CostInstance costs) {
    super(SpellCastType.INSTANT, color, costs, ParentChargeType.INSTANCE, 0x8f32b8, 0x4c94ed);
  }


  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.NONDETECTION_COOLDOWN;
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    var properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.potionDuration = properties.get(ModSpells.NONDETECTION_DURATION);
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModSpells.NONDETECTION_DURATION);
  }

  @Override
  public SpellCastResult cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    pPlayer.addEffect(new MobEffectInstance(ModEffects.NONDETECTION, potionDuration, 0, false, false));
    // TODO: Do we need a snapshot?
    /*    SnapshotHelper.addLiving(pPlayer, ModSerializers.PETAL_SHELL.get(), new PetalShellSnapshot(pPlayer, duration + 40, count));*/
    return SpellCastResult.success(cooldown);
  }
}
