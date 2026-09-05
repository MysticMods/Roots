package mysticmods.roots.spell;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.CastResult;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.network.client.fx.CastExtensionFXPacket;
import mysticmods.roots.snapshot.ExtensionSnapshot;
import mysticmods.roots.snapshot.SnapshotHelper;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class ExtensionSpell extends TwoRadiusSpell {
  private int nightVisionDuration, senseDangerDuration;

  public ExtensionSpell(Properties properties) {
    super(properties);
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
  public void buildProperties(List<PropertyHolder<?>> result) {
    super.buildProperties(result);
    result.add(ModSpells.EXTENSION_NIGHT_VISION_DURATION);
    result.add(ModSpells.EXTENSION_SENSE_DANGER_DURATION);
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
  public CastResult cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    pPlayer.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, nightVisionDuration, 0, false, false), pPlayer);
    pPlayer.addEffect(new MobEffectInstance(ModEffects.SENSE_DANGER, senseDangerDuration, 0, false, false), pPlayer);
    SnapshotHelper.addLiving(pPlayer, ModSerializers.EXTENSION.get(), new ExtensionSnapshot(pPlayer, getMaxDuration(), getRadiusZX(), getRadiusY()));
    PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, new CastExtensionFXPacket(pPlayer.getId()));
    return CastResult.success(cooldown);
  }

  @Override
  public Component[] createExtendedDescriptionComponents() {
    return new Component[]{
        Component.literal(String.format("%.1f", nightVisionDuration / 20.0)),
        Component.literal(String.valueOf(nightVisionDuration)),
        Component.literal(String.format("%.1f", senseDangerDuration / 20.0)),
        Component.literal(String.valueOf(senseDangerDuration)),
        Component.literal(String.valueOf(radiusZX)),
        Component.literal(String.valueOf(radiusY))
    };
  }

  @Override
  public Component[] createModifierDescriptionComponents(SpellModifier spellModifier) {
    return new Component[]{};
  }
}
