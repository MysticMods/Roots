package mysticmods.roots.spell;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.herb.ChargeType;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.network.client.fx.CastSkySoarerFXPacket;
import mysticmods.roots.snapshot.SkySoarerSnapshot;
import mysticmods.roots.snapshot.SnapshotHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
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
  private int duration;

  public SkySoarerSpell(ChatFormatting color, CostInstance costs) {
    super(Type.INSTANT, color, costs, ChargeType.INSTANCE, 0x20c8ff, 0x2040ff);
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
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.amplifier = properties.get(ModSpells.SKY_SOARER_AMPLIFIER);
    this.amplifier_increase = properties.get(ModSpells.SKY_SOARER_AMPLIFIER_INCREASE);
    this.duration = properties.get(ModSpells.SKY_SOARER_DURATION);
    this.duration_incrase = properties.get(ModSpells.SKY_SOARER_DURATION_INCREASE);
  }

  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    int durationCount = instance.count(RootsTags.SpellModifiers.SKY_SOARER_DURATION_INCREASES);
    int thisDuration = durationCount > 0 ? Mth.floor(((float) duration) * (1f + (duration_incrase * durationCount))) : duration;
    int amplifierCount = instance.count(RootsTags.SpellModifiers.SKY_SOARER_AMPLIFIER_INCREASES);
    float thisAmplifier = amplifierCount > 0 ? amplifier * (1f + (amplifier_increase * amplifierCount)) : amplifier;

    pPlayer.addEffect(new MobEffectInstance(ModEffects.SKY_SOARER, thisDuration, 0, false, false));
    Vec3 vehicleMovement = pPlayer.getVehicle() != null ? pPlayer.getVehicle().getDeltaMovement() : Vec3.ZERO;
    SnapshotHelper.addLiving(pPlayer, ModSerializers.SKY_SOARER.get(), new SkySoarerSnapshot(pPlayer, thisDuration + 40, pPlayer.getDeltaMovement(), vehicleMovement, thisAmplifier, thisDuration, durationCount, amplifierCount));
    PacketDistributor.sendToPlayersTrackingEntityAndSelf(pPlayer, new CastSkySoarerFXPacket(pPlayer.getId(), duration));
    return cooldown;
  }
}
