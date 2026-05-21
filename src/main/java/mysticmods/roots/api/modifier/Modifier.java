package mysticmods.roots.api.modifier;

import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.registry.ICosted;
import mysticmods.roots.api.registry.ICostedChild;
import mysticmods.roots.api.registry.IDataMapInitialize;
import mysticmods.roots.api.registry.IDescribed;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.Predicate;

public abstract class Modifier<V, T extends Modifier<V, T>> implements IDescribed, TooltipComponent, IModifier<V, T>, IDataMapInitialize<T>, ICostedChild {
  @Nullable
  protected final ResourceKey<T> parent;
  protected final ResourceKey<V> applicable;
  protected final CostInstance defaultCosts;
  protected final Set<ResourceKey<T>> conflicts;
  @Nullable
  protected CostInstance costs;
  private Item icon;
  private String descriptionId;

  @SafeVarargs
  public Modifier(CostInstance defaultCosts, @Nullable ResourceKey<T> parent, ResourceKey<V> applicable, ResourceKey<T>... conflicts) {
    this.parent = parent;
    this.applicable = applicable;
    this.defaultCosts = defaultCosts;
    this.conflicts = Set.of(conflicts);
  }

  @SafeVarargs
  public Modifier(CostInstance defaultCosts, ResourceKey<V> applicable, ResourceKey<T>... conflicts) {
    this(defaultCosts, null, applicable, conflicts);
  }

  protected abstract DataMapType<T, CostInstance> getDataMapType();

  protected abstract DataMapType<T, Item> getIconDataMapType();

  @Override
  @Nullable
  public ResourceKey<T> getParent() {
    return parent;
  }

  @Override
  public ResourceKey<V> getApplicable() {
    return applicable;
  }

  @Override
  public Set<ResourceKey<T>> getConflicts() {
    return conflicts;
  }

  public abstract Holder<T> builtInRegistryHolder();

  protected abstract String getSignifier();

  @Override
  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId(getSignifier(), builtInRegistryHolder().getKey().location());
    }

    return this.descriptionId;
  }

  public boolean is(ResourceLocation key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean is(ResourceKey<T> key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean is(Predicate<ResourceKey<T>> key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean is(TagKey<T> key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean isFor (@Nullable ResourceKey<?> type) {
    return applicable == type;
  }

  @Override
  public CostInstance getDefaultCosts() {
    return defaultCosts;
  }

  @Override
  public CostInstance getCosts() {
    if (costs == null) {
      return getDefaultCosts();
    }
    return costs;
  }

  @Override
  @Nullable
  public Item getIcon() {
    return icon;
  }

  @Override
  public void init(Holder<T> holder) {
    var costs = holder.getData(getDataMapType());
    if (costs != null) {
      this.costs = costs;
    }
    this.icon = holder.getData(getIconDataMapType());
  }
}
