package mysticmods.roots.api.modifier;

import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.registry.ICostedChild;
import mysticmods.roots.api.registry.IDataMapInitialize;
import mysticmods.roots.api.registry.IDescribed;
import mysticmods.roots.api.registry.IGroupDescribed;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class Modifier<V, T extends Modifier<V, T>> implements IDescribed, IGroupDescribed, TooltipComponent, IModifier<V, T>, IDataMapInitialize<T>, ICostedChild {
  @Nullable
  protected final ResourceKey<T> parent;
  protected final ResourceKey<V> applicable;

  protected final Set<ResourceKey<T>> conflicts;


  // Spells-only?
  protected final CostInstance defaultCosts;
  @Nullable
  protected CostInstance costs;
  protected boolean transformer = false;

  protected String descriptionId;
  protected int depth = 0;


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

  public boolean isTransforming() {
    return transformer;
  }

  protected abstract DataMapType<T, CostInstance> getDataMapType();

  @Override
  @Nullable
  public ResourceKey<T> getParent() {
    return parent;
  }

  @Override
  @Nullable
  public abstract Holder<T> getParentHolder();

  @Override
  public ResourceKey<V> getApplicable() {
    return applicable;
  }

  @Override
  public abstract Holder<V> getApplicableHolder();

  @Override
  public Set<ResourceKey<T>> getConflicts() {
    return conflicts;
  }

  public abstract Holder<T> builtInRegistryHolder();

  public ResourceKey<T> getSelf() {
    return builtInRegistryHolder().getKey();
  }

  protected abstract String getSignifier();

  @Override
  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId(getSignifier(), getSelf().location());
    }

    return this.descriptionId;
  }

  private String transformerDescriptionId = null;
  private String transformerDescriptionTooltipId = null;
  private String transformerDescriptionExtendedTooltipId = null;

  public String getOrCreateTransformerDescriptionId() {
    if (transformerDescriptionId == null) {
      this.transformerDescriptionId = Util.makeDescriptionId(getSignifier() + "_transformer", getApplicable().location()
          .withSuffix("/" + getSelf().location().getPath()).withSuffix("/description"));
    }
    return transformerDescriptionId;
  }

  public String getOrCreateTransformerDescriptionTooltipId() {
    if (transformerDescriptionTooltipId == null) {
      this.transformerDescriptionTooltipId = Util.makeDescriptionId(getSignifier() + "_transformer", getApplicable().location()
          .withSuffix("/" + getSelf().location().getPath()).withSuffix("/description/tooltip"));
    }
    return transformerDescriptionTooltipId;
  }

  public String getOrCreateTransformerDescriptionExtendedTooltipId() {
    if (transformerDescriptionExtendedTooltipId == null) {
      this.transformerDescriptionExtendedTooltipId = Util.makeDescriptionId(getSignifier(), getApplicable().location()
          .withSuffix("/" + getSelf().location().getPath()).withSuffix("/description/tooltip/extended"));
    }
    return transformerDescriptionExtendedTooltipId;
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

  public boolean is(T value) {
    return this.equals(value);
  }

  public boolean is(Holder<T> value) {
    return builtInRegistryHolder().is(value);
  }

  public boolean isFor(@Nullable ResourceKey<?> type) {
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

  @Override
  public int hashCode() {
    return getSelf().hashCode();
  }

  @Override
  public void setDepth(int depth) {
    this.depth = depth;
  }

  @Override
  public int depth() {
    return this.depth;
  }

  public static class Properties<V, T extends Modifier<V, T>> {
    final ResourceKey<T> key;
    ResourceKey<T> parent = null;
    ResourceKey<V> applicable;
    Set<ResourceKey<T>> conflicts = new HashSet<>();
    Supplier<CostInstance> costs = null;
    boolean transformer = false;

    public Properties(ResourceKey<T> key) {
      this.key = key;
    }

    public Properties<V, T> parent(Holder<T> parent) {
      return parent(parent.getKey());
    }

    public Properties<V, T> parent(ResourceKey<T> parent) {
      this.parent = parent;
      return this;
    }

    public Properties<V, T> source(Holder<V> source) {
      return this.source(source.getKey());
    }

    public Properties<V, T> source(ResourceKey<V> source) {
      this.applicable = source;
      return this;
    }

    @SafeVarargs
    public final Properties<V, T> conflicts(ResourceKey<T>... conflicts) {
      this.conflicts.addAll(Arrays.asList(conflicts));
      return this;
    }

    @SafeVarargs
    public final Properties<V, T> conflicts(Holder<T>... conflicts) {
      for (Holder<T> mod : conflicts) {
        this.conflicts.add(mod.getKey());
      }
      return this;
    }

    public Properties<V, T> transforms() {
      this.transformer = true;
      return this;
    }

    public Properties<V, T> costs(Supplier<CostInstance> costs) {
      this.costs = costs;
      return this;
    }

    public Properties<V, T> cost(Supplier<Cost> costs) {
      this.costs = () -> CostInstance.of(costs.get());
      return this;
    }

    public Properties<V, T> cost(Supplier<Holder<Herb>> herb, double amount) {
      this.costs = () -> CostInstance.add(herb.get(), amount);
      return this;
    }
  }
}
