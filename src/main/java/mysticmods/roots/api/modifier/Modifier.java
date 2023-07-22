package mysticmods.roots.api.modifier;

import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.registry.DescribedRegistryEntry;
import mysticmods.roots.api.registry.ICostedRegistryEntry;
import mysticmods.roots.api.registry.IParentChild;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import noobanidus.libs.noobutil.type.LazySupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Modifier extends DescribedRegistryEntry<Modifier> implements ICostedRegistryEntry, IParentChild<Modifier> {
  protected final Supplier<Modifier> parent;
  protected final Set<Modifier> children = new ObjectLinkedOpenHashSet<>();
  protected final Supplier<Spell> spell;
  protected final List<Cost> costs = new ArrayList<>();

  // Modifier with parent
  public Modifier(Supplier<Modifier> parent, Supplier<Spell> spell, List<Cost> costs) {
    this.spell = LazySupplier.of(spell);
    this.parent = LazySupplier.of(parent);
    setCosts(costs);
  }

  // Modifier with no parent
  public Modifier(Supplier<Spell> spell, List<Cost> costs) {
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

  public boolean is(ResourceLocation key) {
    return Registries.MODIFIER_REGISTRY.get().getHolder(this).map(o -> o.is(key)).orElse(false);
  }

  public boolean is(ResourceKey<Modifier> key) {
    return Registries.MODIFIER_REGISTRY.get().getHolder(this).map(o -> o.is(key)).orElse(false);
  }

  public boolean is(Predicate<ResourceKey<Modifier>> key) {
    return Registries.MODIFIER_REGISTRY.get().getHolder(this).map(o -> o.is(key)).orElse(false);
  }

  public boolean is(TagKey<Modifier> key) {
    return Registries.MODIFIER_REGISTRY.get().getHolder(this).map(o -> o.is(key)).orElse(false);
  }

  @Override
  public ResourceLocation getKey() {
    return Registries.MODIFIER_REGISTRY.get().getKey(this);
  }

  @Override
  protected String getDescriptor() {
    return "modifier";
  }

  @Override
  public Modifier getParent() {
    return parent.get();
  }

  @Override
  public Set<Modifier> getChildren() {
    return children;
  }

  @Override
  public void addChild(Modifier child) {
    children.add(child);
  }


}
