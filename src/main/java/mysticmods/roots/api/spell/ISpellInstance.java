package mysticmods.roots.api.spell;

import mysticmods.roots.api.SpellLike;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Set;

public interface ISpellInstance extends SpellLike {
  Spell getSpell();

  default MutableComponent getStyledName() {
    return getSpell().getStyledName();
  }

  Set<SpellModifier> getEnabledModifiers();

  int getCooldown();

  default boolean hasModifier(SpellModifier modifier) {
    return getEnabledModifiers().contains(modifier);
  }

  default Spell.Type getType() {
    return getSpell().getType();
  }

  default int getMaxCooldown() {
    return getSpell().getCooldown();
  }

  default boolean canCast(Player pCaster) {
    return getCooldown() == 0;
  }

  // Returns length of cooldown
  default int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, int ticks) {
    return getSpell().cast(pLevel, pPlayer, pStack, pHand, costs, this, ticks);
  }

  @Override
  default Spell asSpell () {
    return getSpell();
  }

  default boolean isEmpty () {
    return false;
  }
}
