package mysticmods.roots.api.spells;

import mysticmods.roots.api.DescribedRegistryEntry;
import mysticmods.roots.api.herbs.Cost;
import mysticmods.roots.api.registry.Registries;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public abstract class Spell extends DescribedRegistryEntry<Spell> {
  private final Type type;
  private final List<Cost> costs;

  public Spell(Type type, List<Cost> costs) {
    this.type = type;
    this.costs = costs;
  }

  // TODO: API
  public List<Cost> getCosts () {
    return costs;
  }

  // TODO: ick? :/
  public void setCosts (List<Cost> costs) {
    this.costs.clear();
    this.costs.addAll(costs);
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
