package mysticmods.roots.spell;

import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.Costing;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModSpells;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AugmentSpell extends Spell {
  public AugmentSpell(ChatFormatting color, CostInstance costs) {
    super(Type.INSTANT, color, costs, 0x45d17f, 0x1c1c94);
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.AUGMENT_COOLDOWN;
  }

  @Override
  public void initialize(Holder<Spell> holder) {

  }

  @Override
  public int cast(Level Plevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    return cooldown;
  }
}
