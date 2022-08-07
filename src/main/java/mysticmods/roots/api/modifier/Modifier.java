package mysticmods.roots.api.modifier;

import mysticmods.roots.api.DescribedRegistryEntry;
import mysticmods.roots.api.herbs.Cost;
import mysticmods.roots.api.registry.Registries;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class Modifier extends DescribedRegistryEntry<Modifier> {
  protected List<Cost> costs;

  public Modifier(List<Cost> costs) {
    this.costs = costs;
  }

  public void initialize() {
  }

  @Override
  public ResourceLocation getKey() {
    return Registries.MODIFIER_REGISTRY.get().getKey(this);
  }

  @Override
  protected String getDescriptor() {
    return "modifier";
  }
}
