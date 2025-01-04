package mysticmods.roots.api.ritual;

import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.registry.IDescribedRegistryEntry;
import mysticmods.roots.api.registry.IHasHolder;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.blockentity.PyreBlockEntity;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;

import java.util.function.Predicate;

public abstract class Ritual implements IDescribedRegistryEntry {
  protected String descriptionId;

  protected BoundingBox boundingBox;
  protected AABB aabb;
  protected int duration = 0;
  protected int radiusXZ = 0;
  protected int radiusY = 0;
  protected int interval = 0;

  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId("ritual", builtInRegistryHolder().getKey().location());
    }

    return this.descriptionId;
  }

  public Holder.Reference<Ritual> builtInRegistryHolder() {
    return RootsRegistries.RITUALS.getHolderOrThrow(RootsRegistries.RITUALS.getResourceKey(this).orElseThrow());
  }

  public void tick(Level pLevel, BlockPos pPos, BlockState pState, PyreBlockEntity blockEntity) {
    int dur = getDuration() - blockEntity.getLifetime();
    BoundingBox moved = getBoundingBox();
    if (moved != null) {
      moved = moved.moved(pPos.getX(), pPos.getY(), pPos.getZ());
    }
    functionalTick(pLevel, pPos, pState, moved, blockEntity, dur);
    animationTick(pLevel, pPos, pState, moved, blockEntity, dur);
  }

  protected abstract void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration);

  // Still executed on the server
  protected abstract void animationTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration);

  protected void rebuildBounds() {
    boundingBox = new BoundingBox(-getRadiusXZ(), -getRadiusY(), -getRadiusXZ(), getRadiusXZ() + 1, getRadiusY() + 1, getRadiusXZ() + 1);
    aabb = AABB.of(getBoundingBox());
  }

  private void initProperties() {
    // Data map todo!
/*    RitualProperty<Integer> prop;
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
    }*/
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

  public boolean is(ResourceLocation key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean is(ResourceKey<Ritual> key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean is(Predicate<ResourceKey<Ritual>> key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean is(TagKey<Ritual> key) {
    return builtInRegistryHolder().is(key);
  }

  protected abstract RitualProperty<Integer> getDurationProperty();

  protected abstract RitualProperty<Integer> getRadiusXZProperty();

  protected abstract RitualProperty<Integer> getRadiusYProperty();

  protected abstract RitualProperty<Integer> getIntervalProperty();
}
