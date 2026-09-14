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

public abstract class Modifier<V, T extends Modifier<V, T>> implements IDescribed, IGroupDescribed, TooltipComponent, IModifier<V, T>, IDataMapInitialize<T> {
  @Nullable
  protected final ResourceKey<T> parent;
  protected final ResourceKey<V> applicable;
  protected final Set<ResourceKey<T>> conflicts;

  protected String descriptionId;
  protected int depth = 0;

  public Modifier(Modifier.Properties<V, T, ?> properties) {
    this.parent = properties.parent;
    this.applicable = properties.applicable;
    this.conflicts = properties.conflicts;
  }

  public abstract boolean isTransforming();

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
  public void init(Holder<T> holder) {
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

  public abstract static class Properties<V, T extends Modifier<V, T>, S extends Properties<V, T, S>> {
    final ResourceKey<T> key;
    ResourceKey<T> parent = null;
    ResourceKey<V> applicable;
    Set<ResourceKey<T>> conflicts = new HashSet<>();
    boolean transformer = false;

    public Properties(ResourceKey<T> key) {
      this.key = key;
    }

    @SuppressWarnings("unchecked")
    protected final S self () {
      return (S) this;
    }

    public final S parent(Holder<T> parent) {
      return parent(parent.getKey());
    }

    public final S parent(ResourceKey<T> parent) {
      this.parent = parent;
      return self();
    }

    public final S source(Holder<V> source) {
      return this.source(source.getKey());
    }

    public final S source(ResourceKey<V> source) {
      this.applicable = source;
      return self();
    }

    @SafeVarargs
    public final S conflicts(ResourceKey<T>... conflicts) {
      this.conflicts.addAll(Arrays.asList(conflicts));
      return self();
    }

    @SafeVarargs
    public final S conflicts(Holder<T>... conflicts) {
      for (Holder<T> mod : conflicts) {
        this.conflicts.add(mod.getKey());
      }
      return self();
    }
  }
}
