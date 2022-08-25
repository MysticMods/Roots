package mysticmods.roots.spell;

import mysticmods.roots.api.herbs.Cost;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.spells.Costing;
import mysticmods.roots.api.spells.Spell;
import mysticmods.roots.api.spells.SpellInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class NondetectionSpell extends Spell {
  public NondetectionSpell(List<Cost> costs) {
    super(Type.INSTANT, costs);
  }

  @Override
  public SpellProperty<Integer> getCooldownProperty() {
    return null;
  }

  @Override
  public void initialize() {

  }

  @Override
  public void cast(Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, SpellInstance instance, int ticks) {

  }
}
