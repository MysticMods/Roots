package mysticmods.roots.spell;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.entity.other.TimeStopEntity;
import mysticmods.roots.init.ModEntities;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.snapshot.TimeStopEntitySnapshot;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class TimeStopSpell extends TwoRadiusSpell{
  private int duration;

  public TimeStopSpell(ChatFormatting color, CostInstance costs) {
    super(Type.INSTANT, color, costs, 0x404040, 0xc020ff);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.TIME_STOP_COOLDOWN;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModSpells.TIME_STOP_RADIUS_Y;
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getRadiusZXProperty() {
    return ModSpells.TIME_STOP_RADIUS_ZX;
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    var properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.duration = properties.get(ModSpells.TIME_STOP_DURATION);
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModSpells.TIME_STOP_DURATION);
  }

  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    TimeStopEntity timeStop = ModEntities.TIME_STOP.get().create(pLevel);
    if (timeStop != null) {
      timeStop.setLifetime(duration);
      timeStop.setPos(pPlayer.getX(), pPlayer.getY(), pPlayer.getZ());
      // Don't use the helper
      timeStop.setSnapshot(new TimeStopEntitySnapshot(timeStop.tickCount, -1, radiusZX, radiusY, duration));
      pLevel.addFreshEntity(timeStop);
      return cooldown;
    } else {
      costs.noCharge();
      return 0;
    }
  }
}
