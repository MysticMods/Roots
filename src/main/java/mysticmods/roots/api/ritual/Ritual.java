package mysticmods.roots.api.ritual;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.registry.IDataMapInitialize;
import mysticmods.roots.api.registry.IDescribed;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.util.PositionCache;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class Ritual implements IDescribed, TooltipComponent, IDataMapInitialize<Ritual> {
  public static final Codec<Ritual> CODEC = RootsRegistries.RITUALS.byNameCodec();
  public static final StreamCodec<RegistryFriendlyByteBuf, Ritual> STREAM_CODEC = ByteBufCodecs.registry(RootsRegistries.Keys.RITUALS);

  protected String descriptionId;

  protected final Set<RitualModifier> modifiers = new HashSet<>();
  protected BoundingBox boundingBox;
  protected AABB aabb;
  protected int duration = 0;
  protected int radiusXZ = 0;
  protected int radiusY = 0;
  protected int interval = 0;

  protected ItemStack icon;

  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId("ritual", builtInRegistryHolder().getKey().location());
    }

    return this.descriptionId;
  }

  public Holder<Ritual> builtInRegistryHolder() {
    return RootsRegistries.RITUALS.wrapAsHolder(this);
  }

  public boolean providesLight() {
    return true;
  }

  // This function is always called when the ritual is first started.
  public void starts(Level pLevel, BlockPos pPos, BlockState pState, PyreBlockEntity blockEntity, RandomSource random) {
  }

  // This function is called when a ritual stops and is not about to immediately restart.
  public void stops (Level pLevel, BlockPos pPos, BlockState pState, PyreBlockEntity blockEntity, RandomSource random) {
  }

  // This function is always called when a ritual stops, even if it is about to immediately restart; it is also called whenever the pyre is broken.
  public void ends(Level pLevel, BlockPos pPos, BlockState pState, PyreBlockEntity blockEntity, RandomSource random) {
  }

  // This function is only called when the pyre is broken
  public void removed (Level pLevel, BlockPos pPos, BlockState pState, PyreBlockEntity blockEntity, RandomSource random) {
  }

  public void tick(Level pLevel, BlockPos pPos, BlockState pState, PyreBlockEntity blockEntity, @Nullable PositionCache cache, RandomSource random) {
    int dur = getDuration() - blockEntity.getLifetime();
    functionalTick(pLevel, pPos, pState, cache, blockEntity, dur, random);
/*    BoundingBox box;
    if (cache == null) {
      box = getBoundingBox().moved(pPos.getX(), pPos.getY(), pPos.getZ());
    } else {
      box = cache.getBoundingBox();
    }
    animationTick(pLevel, pPos, pState, box, blockEntity, dur, random);*/
  }

  protected abstract void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, @Nullable PositionCache pCache, PyreBlockEntity blockEntity, int duration, RandomSource randomSource);

  @Deprecated
  protected abstract void animationTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration, RandomSource randomSource);

  protected void rebuildBounds() {
    boundingBox = new BoundingBox(-getRadiusXZ(), -getRadiusY(), -getRadiusXZ(), getRadiusXZ(), getRadiusY(), getRadiusXZ());
    aabb = AABB.of(getBoundingBox());
  }

  protected void buildProperties(List<PropertyHolder<?>> properties) {
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
  }

  public List<PropertyHolder<?>> getProperties() {
    List<PropertyHolder<?>> properties = new ArrayList<>();
    buildProperties(properties);
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

  @Override
  public void init(Holder<Ritual> holder) {
    icon = holder.getData(DataMaps.RITUAL_DISPLAY_ITEM);
    if (icon == null || icon.isEmpty()) {
      RootsAPI.LOG.error("Icon is missing for ritual: {}", holder.getKey());
      icon = ItemStack.EMPTY;
    }
    initProperties(holder);
    initialize(holder);
    rebuildBounds();
  }

  public List<BiPredicate<Level, BlockPos>> getPredicates() {
    return Collections.emptyList();
  }

  public ItemStack getIcon() {
    return icon;
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

  public Set<RitualModifier> getModifiers() {
    return modifiers;
  }

  public void addModifier(RitualModifier modifier) {
    modifiers.add(modifier);
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
