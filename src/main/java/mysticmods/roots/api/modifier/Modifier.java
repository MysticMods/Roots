package mysticmods.roots.api.modifier;

import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.registry.IDataMapInitialize;
import mysticmods.roots.api.registry.IDescribed;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class Modifier implements IDescribed, TooltipComponent, IModifier, IDataMapInitialize<Modifier> {
  protected final Set<ResourceKey<?>> applicables;
  protected final Set<ResourceKey<Modifier>> parents;
  protected final ResourceKey<Grove> grove;
  private String descriptionId;

  protected Modifier(ResourceKey<Grove> grove, Set<ResourceKey<?>> applicables, Set<ResourceKey<Modifier>> parents) {
    this.grove = grove;
    this.applicables = applicables;
    this.parents = parents;
  }

  @Override
  public Set<ResourceKey<Modifier>> getParents() {
    return parents;
  }

  @Override
  public Set<ResourceKey<?>> getApplicables() {
    return applicables;
  }

  @Override
  public boolean canApply(ResourceKey<?> applicable) {
    return applicables.contains(applicable);
  }

  public Holder<Modifier> builtInRegistryHolder() {
    return RootsRegistries.MODIFIERS.wrapAsHolder(this);
  }

  public boolean canApply(Holder<?> holder) {
    return holder.getKey() != null && this.canApply(holder.getKey());
  }

  @Override
  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId("modifier", builtInRegistryHolder().getKey().location());
    }

    return this.descriptionId;
  }

  public boolean is(ResourceLocation key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean is(ResourceKey<Modifier> key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean is(Predicate<ResourceKey<Modifier>> key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean is(TagKey<Modifier> key) {
    return builtInRegistryHolder().is(key);
  }

  @Override
  public void init(Holder<Modifier> holder) {

  }

  public static class Builder {
    protected final Set<ResourceKey<?>> applicables = new HashSet<>();
    protected final Set<ResourceKey<Modifier>> parents = new HashSet<>();
    protected ResourceKey<Grove> grove;

    public Builder grove (ResourceKey<Grove> grove) {
      this.grove = grove;
      return this;
    }


    public Builder applicable (ResourceKey<?> ... applicables) {
      this.applicables.addAll(Arrays.asList(applicables));
      return this;
    }

    public final Builder parent (ResourceKey<Modifier> ... parents) {
      this.parents.addAll(Arrays.asList(parents));
      return this;
    }

    public Modifier build () {
      return new Modifier(grove, applicables, parents);
    }
  }
}
