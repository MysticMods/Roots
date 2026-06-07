package mysticmods.roots.spell;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.spell.ParentChargeType;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.entity.other.TemporalMorassEntity;
import mysticmods.roots.init.ModEntities;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.snapshot.SnapshotHelper;
import mysticmods.roots.snapshot.TemporalMorassEntitySnapshot;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class TemporalMorassSpell extends TwoRadiusSpell {
  private int duration, amplifier;

  public TemporalMorassSpell(ChatFormatting color, CostInstance costs) {
    super(Type.INSTANT, color, costs, ParentChargeType.INSTANCE, 0x404040, 0xc020ff);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.TEMPORAL_MORASS_COOLDOWN;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModSpells.TEMPORAL_MORASS_RADIUS_Y;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusZXProperty() {
    return ModSpells.TEMPORAL_MORASS_RADIUS_ZX;
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    var properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.duration = properties.get(ModSpells.TEMPORAL_MORASS_DURATION);
    this.amplifier = properties.get(ModSpells.TEMPORAL_MORASS_AMPLIFIER);
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModSpells.TEMPORAL_MORASS_DURATION);
    properties.add(ModSpells.TEMPORAL_MORASS_AMPLIFIER);
  }

  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    TemporalMorassEntity timeStop = ModEntities.TEMPORAL_MORASS.get().create(pLevel);
    if (timeStop != null) {
      timeStop.setLifetime(duration);
      timeStop.setPos(pPlayer.getX(), pPlayer.getY(), pPlayer.getZ());
      // Don't use the helper
      pLevel.addFreshEntity(timeStop);
      SnapshotHelper.addLiving(timeStop, ModSerializers.TEMPORAL_MORASS.get(), new TemporalMorassEntitySnapshot(timeStop.tickCount, -1, radiusZX, radiusY, duration, amplifier));
      return cooldown;
    } else {
      costs.noCharge();
      return 0;
    }
  }
}
