package mysticmods.roots.api.spells;

import mysticmods.roots.api.DescribedRegistryEntry;
import mysticmods.roots.api.herbs.Cost;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.registry.Registries;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Spell extends DescribedRegistryEntry<Spell> {
  private final Type type;
  private final List<Cost> costs;
  private final Set<Modifier> modifiers = new HashSet<>();

  public Spell(Type type, List<Cost> costs) {
    this.type = type;
    this.costs = new ArrayList<>(costs);
  }

  // TODO: API
  public List<Cost> getCosts() {
    return costs;
  }

  // TODO: ick? :/
  public void setCosts(List<Cost> costs) {
    this.costs.clear();
    this.costs.addAll(costs);
  }

  public Set<Modifier> getModifiers() {
    return modifiers;
  }

  public void addModifier(Modifier modifier) {
    modifiers.add(modifier);
  }

  public void initialize() {
  }

  @Override
  protected String getDescriptor() {
    return "spell";
  }

  @Override
  public ResourceLocation getKey() {
    return Registries.SPELL_REGISTRY.get().getKey(this);
  }

  public enum Type {
    INSTANT,
    CONTINUOUS
  }
}
