package mysticmods.roots.api.modifier;

import mysticmods.roots.api.herbs.Cost;
import mysticmods.roots.api.registry.CostedRegistryEntry;
import mysticmods.roots.api.registry.DescribedRegistryEntry;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.resources.ResourceLocation;
import noobanidus.libs.noobutil.type.LazySupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Modifier extends DescribedRegistryEntry<Modifier> implements CostedRegistryEntry {
  protected final Supplier<Spell> spell;
  protected final List<Cost> costs = new ArrayList<>();

  public Modifier(Supplier<Spell> spell, List<Cost> costs) {
    this.spell = new LazySupplier<>(spell);
    setCosts(costs);
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
