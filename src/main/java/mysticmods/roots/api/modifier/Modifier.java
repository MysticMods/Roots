package mysticmods.roots.api.modifier;

import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.registry.ICosted;
import mysticmods.roots.api.registry.IDataMapInitialize;
import mysticmods.roots.api.registry.IDescribed;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public abstract class Modifier<V, T extends Modifier<V, T>> implements IDescribed, TooltipComponent, IModifier<V, T>, IDataMapInitialize<T>, ICosted {
  protected final ResourceKey<Grove> grove;
  @Nullable
  protected final ResourceKey<T> parent;
  protected final ResourceKey<V> applicable;
  private String descriptionId;

  public Modifier(ResourceKey<Grove> grove, @NotNull ResourceKey<T> parent, ResourceKey<V> applicable) {
    this.grove = grove;
    this.parent = parent;
    this.applicable = applicable;
  }

  public Modifier(ResourceKey<Grove> grove, ResourceKey<V> applicable) {
    this.grove = grove;
    this.applicable = applicable;
    this.parent = null;
  }

  @Override
  @Nullable
  public ResourceKey<T> getParent() {
    return parent;
  }

  @Override
  public ResourceKey<V> getApplicable() {
    return applicable;
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

  public abstract ModifierRecord<V, T> record ();

  public ModifierRecord<V, T> record (boolean enabled, boolean disabled) {
    ModifierRecord<V, T> record = record();
    record.setDisabled(disabled);
    record.setEnabled(enabled);
    return record;
  }

  public interface ModifierRecord<V, T extends Modifier<V, T>> {
    Holder<T> modifier();

    boolean enabled();

    void setEnabled(boolean enabled);

    boolean disabled();

    void setDisabled(boolean disabled);
  }
}
