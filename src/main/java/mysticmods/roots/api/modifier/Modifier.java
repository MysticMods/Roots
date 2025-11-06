package mysticmods.roots.api.modifier;

import mysticmods.roots.api.grove.Grove;
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

public abstract class Modifier<T> implements IDescribed, TooltipComponent, IModifier<T>, IDataMapInitialize<Modifier<T>> {
  protected final ResourceKey<Grove> grove;
  @Nullable
  protected final ResourceKey<Modifier<T>> parent;
  protected final ResourceKey<T> applicable;
  private String descriptionId;

  public Modifier(ResourceKey<Grove> grove, @NotNull ResourceKey<Modifier<T>> parent, ResourceKey<T> applicable) {
    this.grove = grove;
    this.parent = parent;
    this.applicable = applicable;
  }

  public Modifier(ResourceKey<Grove> grove, ResourceKey<T> applicable) {
    this.grove = grove;
    this.applicable = applicable;
    this.parent = null;
  }

  @Override
  @Nullable
  public ResourceKey<Modifier<T>> getParent() {
    return parent;
  }

  @Override
  public ResourceKey<T> getApplicable() {
    return applicable;
  }

  public abstract Holder<Modifier<T>> builtInRegistryHolder();

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

  public boolean is(ResourceKey<Modifier<T>> key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean is(Predicate<ResourceKey<Modifier<T>>> key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean is(TagKey<Modifier<T>> key) {
    return builtInRegistryHolder().is(key);
  }

  @Override
  public void init(Holder<Modifier<T>> holder) {

  }
}
