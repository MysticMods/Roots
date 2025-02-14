package mysticmods.roots.spell;

import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.entity.projectile.WildfireEntity;
import mysticmods.roots.init.ModEntities;
import mysticmods.roots.init.ModSpells;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class WildfireSpell extends Spell {
  public WildfireSpell(ChatFormatting color, List<Cost> costs) {
    super(Type.INSTANT, color, costs, 0xff8020, 0xff4020);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.WILDFIRE_COOLDOWN;
  }

  @Override
  public void initialize(Holder<Spell> holder) {

  }

  @Override
  public int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    WildfireEntity wildfire = new WildfireEntity(ModEntities.WILDFIRE.get(), pPlayer, pLevel);
    wildfire.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0, 0.1f, 0);
    pLevel.addFreshEntity(wildfire);

    return cooldown;
  }
}
