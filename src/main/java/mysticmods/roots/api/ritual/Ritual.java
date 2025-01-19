package mysticmods.roots.api.ritual;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.registry.IDescribed;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.blockentity.PyreBlockEntity;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class Ritual implements IDescribed, TooltipComponent {
  public static final Codec<Ritual> CODEC = RootsRegistries.RITUALS.byNameCodec();
  public static final StreamCodec<RegistryFriendlyByteBuf, Ritual> STREAM_CODEC = ByteBufCodecs.registry(RootsRegistries.Keys.RITUALS);

  protected String descriptionId;

  protected BoundingBox boundingBox;
  protected AABB aabb;
  protected int duration = 0;
  protected int radiusXZ = 0;
  protected int radiusY = 0;
  protected int interval = 0;

  protected Item icon;

  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId("ritual", builtInRegistryHolder().getKey().location());
    }

    return this.descriptionId;
  }

  public Holder<Ritual> builtInRegistryHolder() {
    return RootsRegistries.RITUALS.wrapAsHolder(this);
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

  public List<PropertyHolder<?>> getProperties() {
    List<PropertyHolder<?>> properties = new ArrayList<>();
    if (getDurationProperty() != null) {
      properties.add(getDurationProperty());
    }
    if (getRadiusXZProperty() != null) {
      properties.add(getRadiusXZProperty());
    }
    if (getRadiusYProperty() != null) {
      properties.add(getRadiusYProperty());
    }
    if (getIntervalProperty() != null) {
      properties.add(getIntervalProperty());
    }
    return properties;
  }

  private void initProperties(Holder<Ritual> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.RITUAL_PROPERTY_DATA);
    if (getDurationProperty() != null) {
      // if it's not null it should throw an error
      duration = properties.get(getDurationProperty());
    }
    if (getRadiusXZProperty() != null) {
      radiusXZ = properties.get(getRadiusXZProperty());
    }
    if (getRadiusYProperty() != null) {
      radiusY = properties.get(getRadiusYProperty());
    }
    if (getIntervalProperty() != null) {
      interval = properties.get(getIntervalProperty());
    }
  }

  protected abstract void initialize(Holder<Ritual> holder);

  public void init(Holder<Ritual> holder) {
    icon = holder.getData(DataMaps.RITUAL_DISPLAY_ITEM);
    if (icon == null) {
      RootsAPI.LOG.error("Icon is null for ritual: {}", holder.getKey());
    }
    initProperties(holder);
    initialize(holder);
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

  protected abstract PropertyHolder<Property.IntegerProperty> getDurationProperty();

  protected abstract PropertyHolder<Property.IntegerProperty> getRadiusXZProperty();

  protected abstract PropertyHolder<Property.IntegerProperty> getRadiusYProperty();

  protected abstract PropertyHolder<Property.IntegerProperty> getIntervalProperty();
}
