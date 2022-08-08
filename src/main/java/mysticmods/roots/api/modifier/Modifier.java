package mysticmods.roots.api.modifier;

import com.google.common.base.Suppliers;
import mysticmods.roots.api.DescribedRegistryEntry;
import mysticmods.roots.api.herbs.Cost;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.spells.Spell;
import net.minecraft.resources.ResourceLocation;
import noobanidus.libs.noobutil.type.LazySupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Modifier extends DescribedRegistryEntry<Modifier> {
  protected final Supplier<Spell> spell;
  protected final List<Cost> costs;

  public Modifier(Supplier<Spell> spell, List<Cost> costs) {
    this.spell = new LazySupplier<>(spell);
    this.costs = new ArrayList<>(costs);
  }

  // TODO: API
  public List<Cost> getCosts () {
    return costs;
  }

  public Spell getSpell () {
    return spell.get();
  }

  // TODO: ick? :/
  public void setCosts (List<Cost> costs) {
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
