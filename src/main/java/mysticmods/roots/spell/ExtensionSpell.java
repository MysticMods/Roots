package mysticmods.roots.spell;

import mysticmods.roots.api.data.DataMaps;
import mysticmods.roots.api.data.PropertyDataMap;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellInstance;
import mysticmods.roots.init.ModSpells;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
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

  @Override
  public void cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, SpellInstance instance, int ticks) {
/*    pPlayer.getCapability(Capabilities.SNAPSHOT_CAPABILITY).ifPresent(snapshot -> {
      pPlayer.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, nightVisionDuration, 0, false, false));
      pPlayer.addEffect(new MobEffectInstance(ModEffects.SENSE_DANGER, senseDangerDuration, 0, false, false));
      snapshot.addSnapshot(pPlayer, ModSerializers.EXTENSION, new ExtensionSnapshot(pPlayer, radiusZX, radiusY));
    });*/


  }
}
