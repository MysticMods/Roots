package mysticmods.roots.spell;

import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellCastResult;
import mysticmods.roots.init.ModSpells;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.NotImplementedException;

public class EmptySpell extends Spell {
  public EmptySpell(Properties properties) {
    super(properties);
  }

  @Override
  public Component[] createExtendedDescriptionComponents() {
    return new Component[0];
  }

  @Override
  public Component[] createModifierDescriptionComponents(SpellModifier spellModifier) {
    return new Component[0];
  }

  @Override
  public PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return ModSpells.EMPTY_COOLDOWN;
  }

  @Override
  public void initialize(Holder<Spell> holder) {

  }

  @Override
  public SpellCastResult cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks) {
    throw new NotImplementedException("The empty spell cannot be cast!");
  }
}
