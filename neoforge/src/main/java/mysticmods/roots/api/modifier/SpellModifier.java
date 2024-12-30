package mysticmods.roots.api.modifier;

import com.google.common.base.Suppliers;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.registry.DescribedRegistryEntry;
import mysticmods.roots.api.registry.ICostedRegistryEntry;
import mysticmods.roots.api.registry.IParentChild;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SpellModifier extends DescribedRegistryEntry<SpellModifier> implements ICostedRegistryEntry, IParentChild<SpellModifier> {
  protected final Supplier<SpellModifier> parent;
  protected final Set<SpellModifier> children = new ObjectLinkedOpenHashSet<>();
  protected final Supplier<Spell> spell;
  protected final List<Cost> costs = new ArrayList<>();

  private final Holder.Reference<SpellModifier> builtinRegistryHolder = RootsRegistries.SPELL_MODIFIERS.createIntrusiveHolder(this);

  // Modifier with parent
  public SpellModifier(Supplier<SpellModifier> parent, Supplier<Spell> spell, List<Cost> costs) {
    this.spell = Suppliers.memoize(spell::get);
    this.parent = Suppliers.memoize(parent::get);
    setCosts(costs);
  }

  // Modifier with no parent
  public SpellModifier(Supplier<Spell> spell, List<Cost> costs) {
    this(IParentChild.NO_PARENT, spell, costs);
  }

  @Override
  public List<Cost> getCosts() {
    return costs;
  }

  public Spell getSpell() {
    return spell.get();
  }

  @Override
  public void setCosts(List<Cost> costs) {
    this.costs.clear();
    this.costs.addAll(costs);
  }

  public void initialize() {
    getSpell().addModifier(this);
    resolve();
  }

  public Holder.Reference<SpellModifier> getBuiltinRegistryHolder() {
    return builtinRegistryHolder;
  }

  public boolean is(ResourceLocation key) {
    return getBuiltinRegistryHolder().is(key);
  }

  public boolean is(ResourceKey<SpellModifier> key) {
    return getBuiltinRegistryHolder().is(key);
  }

  public boolean is(Predicate<ResourceKey<SpellModifier>> key) {
    return getBuiltinRegistryHolder().is(key);
  }

  public boolean is(TagKey<SpellModifier> key) {
    return getBuiltinRegistryHolder().is(key);
  }

  @Override
  public ResourceLocation getKey() {
    return getBuiltinRegistryHolder().getKey().location();
  }

  @Override
  protected String getDescriptor() {
    return "modifier";
  }

  @Override
  public SpellModifier getParent() {
    return parent.get();
  }

  @Override
  public Set<SpellModifier> getChildren() {
    return children;
  }

  @Override
  public void addChild(SpellModifier child) {
    children.add(child);
  }


}
