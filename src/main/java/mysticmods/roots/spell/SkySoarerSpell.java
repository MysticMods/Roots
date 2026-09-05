package mysticmods.roots.spell;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
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
import mysticmods.roots.init.ModModifiers;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.network.client.fx.CastSkySoarerFXPacket;
import mysticmods.roots.snapshot.SkySoarerSnapshot;
import mysticmods.roots.snapshot.SnapshotHelper;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class SkySoarerSpell extends Spell {
  private float amplifier, amplifier_increase, duration_incrase;
  private int duration, friendly_earth_duration;

  public SkySoarerSpell(Properties properties) {
    super(properties);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.SKY_SOARER_COOLDOWN;
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModSpells.SKY_SOARER_AMPLIFIER);
    properties.add(ModSpells.SKY_SOARER_AMPLIFIER_INCREASE);
    properties.add(ModSpells.SKY_SOARER_DURATION);
    properties.add(ModSpells.SKY_SOARER_DURATION_INCREASE);
    properties.add(ModSpells.SKY_SOARER_FRIENDLY_EARTH_DURATION);
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.amplifier = properties.get(ModSpells.SKY_SOARER_AMPLIFIER);
    this.amplifier_increase = properties.get(ModSpells.SKY_SOARER_AMPLIFIER_INCREASE);
    this.duration = properties.get(ModSpells.SKY_SOARER_DURATION);
    this.duration_incrase = properties.get(ModSpells.SKY_SOARER_DURATION_INCREASE);
    this.friendly_earth_duration = properties.get(ModSpells.SKY_SOARER_FRIENDLY_EARTH_DURATION);
  }

  public int getFriendlyEarthDuration() {
    return friendly_earth_duration;
  }

  @Override
  public CastResult cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    int durationCount = instance.count(RootsTags.SpellModifiers.SKY_SOARER_DURATION_INCREASES);
    int thisDuration = durationCount > 0 ? Mth.floor(((float) duration) * (1f + (duration_incrase * durationCount))) : duration;
    int amplifierCount = instance.count(RootsTags.SpellModifiers.SKY_SOARER_AMPLIFIER_INCREASES);
    float thisAmplifier = amplifierCount > 0 ? amplifier * (1f + (amplifier_increase * amplifierCount)) : amplifier;

    pPlayer.addEffect(new MobEffectInstance(ModEffects.SKY_SOARER, thisDuration, 0, false, false), pPlayer);
    Vec3 vehicleMovement = pPlayer.getVehicle() != null ? pPlayer.getVehicle().getDeltaMovement() : Vec3.ZERO;
    SnapshotHelper.addLiving(pPlayer, ModSerializers.SKY_SOARER.get(), new SkySoarerSnapshot(pPlayer, thisDuration + 40, pPlayer.getDeltaMovement(), vehicleMovement, thisAmplifier, thisDuration, durationCount, amplifierCount, instance.has(ModModifiers.SKY_SOARER_FRIENDLY_EARTH)));
    //RootsAPI.LOG.info("Duration base: {}, this duration: {}, amplifier base: {}, this amplifier: {}", duration, thisDuration, amplifier, thisAmplifier);
    PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, new CastSkySoarerFXPacket(pPlayer.getId(), thisDuration));
    return CastResult.success(cooldown);
  }

  @SuppressWarnings("removal")
  @Override
  public Component[] createExtendedDescriptionComponents() {
    double inSprint = amplifier / RootsAPI.SPRINT_SPEED;
    return new Component[]{
        Component.literal(String.format("%.1f", inSprint)),
        Component.literal(String.format("%.1f", amplifier)),
        Component.literal(String.format("%.1f", duration / 20.0)),
        Component.literal(String.valueOf(duration))
    };
  }

  @SuppressWarnings("removal")
  @Override
  public Component[] createModifierDescriptionComponents(SpellModifier spellModifier) {
    if (spellModifier.is(ModModifiers.SKY_SOARER_FRIENDLY_EARTH)) {
      return new Component[]{
          Component.literal(String.format("%.1f", friendly_earth_duration / 20.0)),
          Component.literal(String.valueOf(friendly_earth_duration))
      };
    } else if (spellModifier.is(ModModifiers.SKY_SOARER_SPEEDY_1)) {
      double total = (1 * duration_incrase);
      double duration = this.duration + (((double) this.duration) * total);
      double seconds = duration / 20.0;

      return new Component[]{
          Component.literal(String.format("%.1f", total)),
          Component.literal(String.format("%.1f", seconds)),
          Component.literal(String.format("%.0f", duration))
      };
    } else if (spellModifier.is(ModModifiers.SKY_SOARER_SPEEDY_2)) {
      double total = (2 * duration_incrase);
      double duration = this.duration + (((double) this.duration) * total);
      double seconds = duration / 20.0;

      return new Component[]{
          Component.literal(String.format("%.1f", total)),
          Component.literal(String.format("%.1f", seconds)),
          Component.literal(String.format("%.0f", duration))
      };
    } else if (spellModifier.is(ModModifiers.SKY_SOARER_AMPLIFIED_1)) {
      double total = amplifier + (amplifier * (1 * amplifier_increase));
      double inSprint = total / RootsAPI.SPRINT_SPEED;

      return new Component[]{
          Component.literal(String.format("%.1f", inSprint)),
          Component.literal(String.format("%.1f", total))
      };
    } else if (spellModifier.is(ModModifiers.SKY_SOARER_AMPLIFIED_2)) {
      double total = amplifier + (amplifier * (2 * amplifier_increase));
      double inSprint = total / RootsAPI.SPRINT_SPEED;

      return new Component[]{
          Component.literal(String.format("%.1f", inSprint)),
          Component.literal(String.format("%.1f", total))
      };
    } else {
      RootsAPI.LOG.error("Tried to create description components for modifiers not associated with {}: {}", this, spellModifier);
      return new Component[]{};
    }
  }
}
