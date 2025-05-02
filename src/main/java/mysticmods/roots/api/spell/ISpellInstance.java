package mysticmods.roots.api.spell;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.SpellLike;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.registry.ICosted;
import mysticmods.roots.api.registry.ICostedParent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

public interface ISpellInstance extends SpellLike, ICostedParent {
  Spell getSpell();

  default MutableComponent getStyledName() {
    return getSpell().getStyledName();
  }

  Set<SpellModifier> getEnabledModifiers();

  int getCooldown();

  default int getMaxUse() {
    return getSpell().getMaxUse();
  }

  default CostInstance getDefaultCosts() {
    return getSpell().getDefaultCosts();
  }

  default CostInstance getCosts() {
    return getSpell().getCosts();
  }

  default CostInstance.ChargeType getChargeType () {
    return getCosts().chargeType();
  }

  default int getMaximumOperations () {
    return getSpell().getMaximumOperations();
  }

  Set<ICosted> getChildren ();

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
    return getCooldown() <= 0;
  }

  // Returns length of cooldown
  default int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, int ticks) {
    // TODO: Cooldown reduction and and cost reduction are applied here
    int cooldown = getSpell().cast(pLevel, pPlayer, pStack, pHand, costs, this, ticks);
    double costReduction = RootsAPI.getInstance().getCostReduction(pPlayer);
    double cooldownReduction = RootsAPI.getInstance().getCooldownReduction(pPlayer);
    costs.discount(costReduction);
    return cooldown - (int) (cooldown * cooldownReduction);
  }

  default boolean hasBlockTarget(Player pPlayer) {
    return getSpell().hasBlockTarget(pPlayer);
  }

  @Nullable
  default Vec3 getBlockTarget(Player pPlayer) {
    return getSpell().getBlockTarget(pPlayer);
  }

  @Nullable
  default SpellInstanceData getSpellData() {
    return null;
  }

  @Override
  default Spell asSpell() {
    return getSpell();
  }

  default boolean isEmpty() {
    return false;
  }

  static SimpleSpell of(Spell spell) {
    return new SimpleSpell(spell);
  }

  record SimpleSpell(Spell spell) implements ISpellInstance {

    @Override
    public Spell getSpell() {
      return spell();
    }

    @Override
    public Set<SpellModifier> getEnabledModifiers() {
      return Collections.emptySet();
    }

    @Override
    public int getCooldown() {
      return 0;
    }

    @Override
    public Set<ICosted> getChildren() {
      return Collections.emptySet();
    }
  }
}
