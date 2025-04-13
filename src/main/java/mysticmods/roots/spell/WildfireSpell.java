package mysticmods.roots.spell;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.entity.projectile.WildfireEntity;
import mysticmods.roots.init.ModEntities;
import mysticmods.roots.init.ModSpells;
import mysticmods.roots.snapshot.WildfireEntitySnapshot;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class WildfireSpell extends Spell {
  private float damage, velocity;

  public WildfireSpell(ChatFormatting color, CostInstance costs) {
    super(Type.INSTANT, color, costs, 0xff8020, 0xff4020);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.WILDFIRE_COOLDOWN;
  }

  @Override
  public void initialize(Holder<Spell> holder) {
    var properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.damage = properties.get(ModSpells.WILDFIRE_DAMAGE);
    this.velocity = properties.get(ModSpells.WILDFIRE_VELOCITY);
  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModSpells.WILDFIRE_DAMAGE);
    properties.add(ModSpells.WILDFIRE_VELOCITY);
  }

  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    WildfireEntity wildfire = new WildfireEntity(ModEntities.WILDFIRE.get(), pPlayer, pLevel);
    wildfire.setSnapshot(new WildfireEntitySnapshot(pPlayer, -1, damage));

    wildfire.shootFromRotation(pPlayer, pPlayer.getViewXRot(1), pPlayer.getViewYRot(1), 0, velocity, 0);
    pLevel.addFreshEntity(wildfire);

    return cooldown;
  }
}
