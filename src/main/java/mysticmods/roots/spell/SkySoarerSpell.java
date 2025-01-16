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
import mysticmods.roots.snapshot.SkySoarerSnapshot;
import mysticmods.roots.snapshot.SnapshotHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SkySoarerSpell extends Spell {
  private float amplifier, boosted_amplifier;
  private int duration, boosted_duration;

  public SkySoarerSpell(ChatFormatting color, List<Cost> costs) {
    super(Type.INSTANT, color, costs, 0x20c8ff, 0x2040ff);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.SKY_SOARER_COOLDOWN;
  }

  @Override
  public List<PropertyHolder<?>> getProperties() {
    List<PropertyHolder<?>> properties = super.getProperties();
    properties.add(ModSpells.SKY_SOARER_AMPLIFIER);
    properties.add(ModSpells.SKY_SOARER_BOOSTED_AMPLIFIER);
    properties.add(ModSpells.SKY_SOARER_DURATION);
    properties.add(ModSpells.SKY_SOARER_BOOSTED_DURATION);
    return properties;
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.amplifier = properties.get(ModSpells.SKY_SOARER_AMPLIFIER);
    this.boosted_amplifier = properties.get(ModSpells.SKY_SOARER_BOOSTED_AMPLIFIER);
    this.duration = properties.get(ModSpells.SKY_SOARER_DURATION);
    this.boosted_duration = properties.get(ModSpells.SKY_SOARER_BOOSTED_DURATION);
  }

  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    pPlayer.addEffect(new MobEffectInstance(ModEffects.SKY_SOARER, duration, 0));
    Vec3 vehicleMovement = pPlayer.getVehicle() != null ? pPlayer.getVehicle().getDeltaMovement() : Vec3.ZERO;
    SnapshotHelper.addPlayerOrVehicle(pPlayer, ModSerializers.SKY_SOARER.get(), new SkySoarerSnapshot(pPlayer, duration + 40, pPlayer.getDeltaMovement(), vehicleMovement, amplifier));
    return cooldown;
  }
}
