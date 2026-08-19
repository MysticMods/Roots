package mysticmods.roots.api.modifier;

import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.registry.*;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.Predicate;

public abstract class Modifier<V, T extends Modifier<V, T>> implements IDescribed, IGroupDescribed, TooltipComponent, IModifier<V, T>, IDataMapInitialize<T>, ICostedChild {
  @Nullable
  protected final ResourceKey<T> parent;
  protected final ResourceKey<V> applicable;
  protected final CostInstance defaultCosts;
  protected final Set<ResourceKey<T>> conflicts;
  @Nullable
  protected CostInstance costs;
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

  @Override
  @Nullable
  public ResourceKey<T> getParent() {
    return parent;
  }

  // TODO: There are instances where this should use getApplicableHolder instead
  @Deprecated
  @Override
  public ResourceKey<V> getApplicable() {
    return applicable;
  }

  public abstract Holder<V> getApplicableHolder ();

  @Override
  public Set<ResourceKey<T>> getConflicts() {
    return conflicts;
  }

  public abstract Holder<T> builtInRegistryHolder();

  public ResourceKey<T> getSelf () {
    return builtInRegistryHolder().getKey();
  }

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

  // ????? TODO:
  public boolean is (T value) {
    return this.equals(value);
  }

  public boolean is (Holder<T> value) {
    return builtInRegistryHolder().is(value);
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
  public void init(Holder<T> holder) {
    var costs = holder.getData(getDataMapType());
    if (costs != null) {
      this.costs = costs;
    }
  }
}
