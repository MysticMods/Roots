package mysticmods.roots.spell;

import mysticmods.roots.api.herbs.Cost;
import mysticmods.roots.api.spells.Costing;
import mysticmods.roots.api.spells.Spell;
import mysticmods.roots.api.spells.SpellInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class GrowthInfusionSpell extends Spell {
  public GrowthInfusionSpell(List<Cost> costs) {
    super(Type.CONTINUOUS, costs, -1, 0xFF00FF, 0xFF00FF);
  }

  @Override
  public void cast(Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, SpellInstance instance, int ticks) {
    System.out.println(ticks);
  }
}
