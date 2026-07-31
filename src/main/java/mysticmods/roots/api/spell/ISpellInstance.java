package mysticmods.roots.api.spell;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.SpellLike;
import mysticmods.roots.api.attachment.CooldownStorage;
import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.modifier.SpellModifierSet;
import mysticmods.roots.api.registry.ICostedChild;
import mysticmods.roots.api.registry.ICostedParent;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface ISpellInstance extends SpellLike, ICostedParent {
  Spell getSpell();

  default MutableComponent getStyledName() {
    return asSpell().getStyledName(this);
  }

  SpellModifierSet getEnabledModifiers();

  default int count(TagKey<SpellModifier> tag) {
    return getEnabledModifiers().count(tag);
  }

  default int getMaxUse() {
    return asSpell().getMaxUse();
  }

  @Override
  default CostInstance getDefaultCosts() {
    return asSpell().getDefaultCosts();
  }

  @Override
  default CostInstance getCosts() {
    return asSpell().getCosts();
  }

  default ParentChargeType getChargeType() {
    return asSpell().getChargeType();
  }

  @Override
  default int getMaximumOperations() {
    return asSpell().getMaximumOperations();
  }

  @Override
  default Set<? extends ICostedChild> getChildren() {
    return getEnabledModifiers();
  }

  default boolean has (TagKey<SpellModifier> modifier) {
    return hasModifier(modifier);
  }

  default boolean hasModifier (TagKey<SpellModifier> modifier) {
    return getEnabledModifiers().hasTag(modifier);
  }

  default boolean is (TagKey<Spell> spell) {
    return asSpell().is(spell);
  }

  default boolean is (ResourceLocation spell) {
    return asSpell().is(spell);
  }

  default boolean is (ResourceKey<Spell> spell) {
    return asSpell().is(spell);
  }

  default boolean is (Predicate<ResourceKey<Spell>> spell) {
    return asSpell().is(spell);
  }

  default boolean hasModifier(SpellModifier modifier) {
    return getEnabledModifiers().contains(modifier);
  }

  default boolean hasModifier(Holder<SpellModifier> modifier) {
    return hasModifier(modifier.value());
  }

  default SpellCastType getType() {
    return asSpell().getType();
  }

  default int getDefaultCooldown() {
    return asSpell().getCooldown();
  }

  default boolean offCooldown(ItemStack castingItem, Entity pCaster) {
    if (castingItem.is(RootsTags.Items.CREATIVE_CASTING_TOOLS)) {
      return true;
    }

    if (!pCaster.hasData(RootsAPI.getInstance().getCooldownStorageType())) {
      return true;
    }

    CooldownStorage storage = pCaster.getData(RootsAPI.getInstance().getCooldownStorageType());
    return storage.getCooldown(this.asSpell()) <= 0;
  }

  // Returns length of cooldown
  default int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, int ticks) {
    int cooldown = asSpell().cast(pLevel, pPlayer, pStack, pHand, costs, this, ticks);
    double costReduction = RootsAPI.getInstance().getCostReduction(pPlayer);
    double cooldownReduction = RootsAPI.getInstance().getCooldownReduction(pPlayer);
    costs.discount(costReduction);
    return cooldown - (int) (cooldown * cooldownReduction);
  }

  default boolean hasBlockTarget(Player pPlayer) {
    return asSpell().hasBlockTarget(pPlayer);
  }

  default List<Entity> selectTargets (HitResult hit, Player pPlayer) {
    return asSpell().selectTargets(this, hit, pPlayer);

  }

  @Nullable
  default Vec3 getBlockTarget(Player pPlayer) {
    return asSpell().getBlockTarget(pPlayer, this);
  }

  @Nullable
  default AABB getAABB() {
    return asSpell().getAABB();
  }

  default DataComponentMap getSpellData() {
    return DataComponentMap.EMPTY;
  }

  default <T> T getSpellData(DataComponentType<? extends T> component) {
    return getSpellData().get(component);
  }

  default <T> T getSpellData(Supplier<? extends DataComponentType<? extends T>> component) {
    return getSpellData().get(component.get());
  }

  @Override
  default Spell asSpell() {
    return getSpell();
  }

  default boolean isEmpty() {
    return false;
  }

  default double getEntityRange (Player pPlayer) {
    return asSpell().getEntityRange(pPlayer, this);
  }

  default double getBlockRange (Player pPlayer) {
    return asSpell().getBlockRange(pPlayer, this);
  }

  default boolean canTargetThroughFluids() {
    return asSpell().canTargetThroughFluids(this);
  }

  static SimpleSpell of(Spell spell) {
    return new SimpleSpell(spell);
  }

  default boolean canMarkEntityTargets() {
    return asSpell().canMarkEntityTargets(this);

  }

  default boolean canTargetEntity(Entity entity) {
    return asSpell().canTargetEntity(entity);
  }

  record SimpleSpell(Spell spell) implements ISpellInstance {

    @Override
    public Spell getSpell() {
      return spell();
    }

    @Override
    public SpellModifierSet getEnabledModifiers() {
      return SpellModifierSet.EMPTY;
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
