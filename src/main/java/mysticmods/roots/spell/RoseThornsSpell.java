package mysticmods.roots.spell;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.entity.other.RoseThornsEntity;
import mysticmods.roots.init.ModEntities;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.snapshot.RoseThornsEntitySnapshot;
import mysticmods.roots.snapshot.TimeStopEntitySnapshot;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class RoseThornsSpell extends Spell {
  private double radiusZX, radiusY;
  private int duration;
  private float damage;

  public RoseThornsSpell(ChatFormatting color, CostInstance costs) {
    super(Type.INSTANT, color, costs, 0xff2040, 0x20ff60);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.ROSE_THORNS_COOLDOWN;
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    var properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    radiusZX = properties.get(ModSpells.ROSE_THORNS_RADIUS_ZX);
    radiusY = properties.get(ModSpells.ROSE_THORNS_RADIUS_Y);
    duration = properties.get(ModSpells.ROSE_THORNS_DURATION);
    damage = properties.get(ModSpells.ROSE_THORNS_DAMAGE);
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModSpells.ROSE_THORNS_RADIUS_ZX);
    properties.add(ModSpells.ROSE_THORNS_RADIUS_Y);
    properties.add(ModSpells.ROSE_THORNS_DURATION);
    properties.add(ModSpells.ROSE_THORNS_DAMAGE);
  }

  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    BlockHitResult result = pickBlock(pPlayer);

    RoseThornsEntity rose = ModEntities.ROSE_THORNS.get().create(pLevel);
    if (rose != null) {
      rose.setOwner(pPlayer);
      rose.setLifetime(duration);
      rose.setPos(result.getLocation());
      rose.setSnapshot(new RoseThornsEntitySnapshot(rose.tickCount, -1, radiusZX, radiusY, duration, damage));
      pLevel.addFreshEntity(rose);
      return cooldown;
    } else {
      costs.noCharge();
      return 0;
    }
  }
}
