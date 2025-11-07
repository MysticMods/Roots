package mysticmods.roots.api.spell;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.SpellLike;
import mysticmods.roots.api.attachment.CooldownStorage;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.registry.ICosted;
import mysticmods.roots.api.registry.ICostedParent;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
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

  default int getMaxUse() {
    return getSpell().getMaxUse();
  }

  @Override
  default CostInstance getDefaultCosts() {
    return getSpell().getDefaultCosts();
  }

  @Override
  default CostInstance getCosts() {
    return getSpell().getCosts();
  }

  @Override
  default CostInstance.ChargeType getChargeType() {
    return getCosts().chargeType();
  }

  @Override
  default int getMaximumOperations() {
    return getSpell().getMaximumOperations();
  }

  @Override
  default Set<? extends ICosted> getChildren() {
    return getEnabledModifiers();
  }

  default boolean hasModifier(SpellModifier modifier) {
    return getEnabledModifiers().contains(modifier);
  }

  default boolean hasModifier (Holder<SpellModifier> modifier) {
    return hasModifier(modifier.value());
  }

  default Spell.Type getType() {
    return getSpell().getType();
  }

  default int getDefaultCooldown() {
    return getSpell().getCooldown();
  }

  default boolean canCast(Entity pCaster) {
    if (!pCaster.hasData(RootsAPI.getInstance().getCooldownStorageType())) {
      return true;
    }

    CooldownStorage storage = pCaster.getData(RootsAPI.getInstance().getCooldownStorageType());
    return storage.getCooldown(this.asSpell()) <= 0;
  }

  // Returns length of cooldown
  default int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, int ticks) {
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
  default AABB getAABB() {
    return getSpell().getAABB();
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
    public boolean hasModifier(SpellModifier modifier) {
      return false;
    }

    @Override
    public boolean hasModifier(Holder<SpellModifier> modifier) {
      return false;
    }
  }
}
