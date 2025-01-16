package mysticmods.roots.spell;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.snapshot.ExtensionSnapshot;
import mysticmods.roots.snapshot.SnapshotHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class ExtensionSpell extends TwoRadiusSpell {
  private int nightVisionDuration, senseDangerDuration;

  public ExtensionSpell(ChatFormatting color, List<Cost> costs) {
    super(Type.INSTANT, color, costs, 0xcde645, 0xb872b1);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.EXTENSION_COOLDOWN;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModSpells.EXTENSION_RADIUS_Y;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusZXProperty() {
    return ModSpells.EXTENSION_RADIUS_ZX;
  }

  @Override
  public List<PropertyHolder<?>> getProperties() {
    List<PropertyHolder<?>> result = super.getProperties();
    result.add(ModSpells.EXTENSION_NIGHT_VISION_DURATION);
    result.add(ModSpells.EXTENSION_SENSE_DANGER_DURATION);
    return result;
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.nightVisionDuration = properties.get(ModSpells.EXTENSION_NIGHT_VISION_DURATION);
    this.senseDangerDuration = properties.get(ModSpells.EXTENSION_SENSE_DANGER_DURATION);
  }

  public int getMaxDuration() {
    return Math.max(nightVisionDuration, senseDangerDuration) + 40;
  }

  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    pPlayer.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, nightVisionDuration, 0, false, false));
    pPlayer.addEffect(new MobEffectInstance(ModEffects.SENSE_DANGER, senseDangerDuration, 0, false, false));
    SnapshotHelper.addPlayer(pPlayer, ModSerializers.EXTENSION.get(), new ExtensionSnapshot(pPlayer, getMaxDuration(), getRadiusZX(), getRadiusY()));
    return cooldown;
  }
}
