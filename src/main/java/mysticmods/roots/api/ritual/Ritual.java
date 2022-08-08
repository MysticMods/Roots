package mysticmods.roots.api.ritual;

import mysticmods.roots.api.DescribedRegistryEntry;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.blockentity.PyreBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;

public abstract class Ritual extends DescribedRegistryEntry<Ritual> {
  protected String descriptionId;
  protected BoundingBox boundingBox;
  protected AABB aabb;
  protected int duration;
  protected int radiusXZ;
  protected int radiusY;
  protected int interval;

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

  protected abstract void initialize();

  public void init() {
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

  @Override
  public ResourceLocation getKey() {
    return Registries.RITUAL_REGISTRY.get().getKey(this);
  }
}
