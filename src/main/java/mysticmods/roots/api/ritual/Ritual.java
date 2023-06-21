package mysticmods.roots.api.ritual;

import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.registry.DescribedRegistryEntry;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.blockentity.PyreBlockEntity;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;

import java.util.function.Predicate;

public abstract class Ritual extends DescribedRegistryEntry<Ritual> {
  protected BoundingBox boundingBox;
  protected AABB aabb;
  protected int duration = 0;
  protected int radiusXZ = 0;
  protected int radiusY = 0;
  protected int interval = 0;

  public void tick(PyreBlockEntity blockEntity) {
    int dur = getDuration() - blockEntity.getLifetime();
    functionalTick(blockEntity, dur);
    animationTick(blockEntity, dur);
  }

  protected abstract void functionalTick(PyreBlockEntity blockEntity, int duration);

  // Still executed on the server
  protected abstract void animationTick(PyreBlockEntity blockEntity, int duration);

  protected void rebuildBounds() {
    boundingBox = new BoundingBox(-getRadiusXZ(), -getRadiusY(), -getRadiusXZ(), getRadiusXZ() + 1, getRadiusY() + 1, getRadiusXZ() + 1);
    aabb = AABB.of(getBoundingBox());
  }

  private void initProperties() {
    RitualProperty<Integer> prop;
    prop = getDurationProperty();
    if (prop != null) {
      duration = prop.getValue();
    }
    prop = getRadiusYProperty();
    if (prop != null) {
      radiusY = prop.getValue();
    }
    prop = getRadiusXZProperty();
    if (prop != null) {
      radiusXZ = prop.getValue();
    }
    prop = getIntervalProperty();
    if (prop != null) {
      interval = prop.getValue();
    }
  }

  protected abstract void initialize();

  public void init() {
    initProperties();
    initialize();
    rebuildBounds();
  }

  public int getDuration() {
    return duration;
  }

  public int getRadiusXZ() {
    return radiusXZ;
  }

  public int getRadiusY() {
    return radiusY;
  }

  public int getInterval() {
    return interval;
  }

  public BoundingBox getBoundingBox() {
    return boundingBox;
  }

  public AABB getAABB() {
    return aabb;
  }

  @Override
  protected String getDescriptor() {
    return "ritual";
  }

  public boolean is(ResourceLocation key) {
    return Registries.RITUAL_REGISTRY.get().getHolder(this).map(o -> o.is(key)).orElse(false);
  }

  public boolean is(ResourceKey<Ritual> key) {
    return Registries.RITUAL_REGISTRY.get().getHolder(this).map(o -> o.is(key)).orElse(false);
  }

  public boolean is(Predicate<ResourceKey<Ritual>> key) {
    return Registries.RITUAL_REGISTRY.get().getHolder(this).map(o -> o.is(key)).orElse(false);
  }

  public boolean is(TagKey<Ritual> key) {
    return Registries.RITUAL_REGISTRY.get().getHolder(this).map(o -> o.is(key)).orElse(false);
  }


  @Override
  public ResourceLocation getKey() {
    return Registries.RITUAL_REGISTRY.get().getKey(this);
  }

  protected abstract RitualProperty<Integer> getDurationProperty();

  protected abstract RitualProperty<Integer> getRadiusXZProperty();

  protected abstract RitualProperty<Integer> getRadiusYProperty();

  protected abstract RitualProperty<Integer> getIntervalProperty();
}
