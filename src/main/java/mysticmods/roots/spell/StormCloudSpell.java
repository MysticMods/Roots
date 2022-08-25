package mysticmods.roots.spell;

import mysticmods.roots.api.herbs.Cost;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.spells.Costing;
import mysticmods.roots.api.spells.Spell;
import mysticmods.roots.api.spells.SpellInstance;
import mysticmods.roots.init.ModSpells;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class StormCloudSpell extends Spell {
  public StormCloudSpell(List<Cost> costs) {
    super(Type.INSTANT, costs);
  }

  @Override
  public SpellProperty<Integer> getCooldownProperty() {
    return ModSpells.STORM_CLOUD_COOLDOWN.get();
  }

  @Override
  public void initialize() {

  }

  @Override
  public void cast(Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, SpellInstance instance, int ticks) {

  }
}
