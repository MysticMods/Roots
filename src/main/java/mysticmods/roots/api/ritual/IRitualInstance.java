package mysticmods.roots.api.ritual;

import mysticmods.roots.api.RitualLike;
import mysticmods.roots.api.modifier.*;
import mysticmods.roots.api.modifier.RitualModifier;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.util.PositionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

// TODO: Actually use this
public interface IRitualInstance extends RitualLike {
  Ritual getRitual();

  default MutableComponent getName() {
    return getRitual().getName(this);
  }

  int getLifetime();

  default int getDuration() {
    return getRitual().getDuration(this);
  }

  RitualModifierSet getEnabledModifiers();

  default Set<ResourceKey<RitualModifier>> getEnabledModifierKeys() {
    return getEnabledModifiers().getKeys();
  }

  default Set<ResourceKey<RitualModifier>> getTransformingKeys () {
    return getEnabledModifiers().getTransformingKeys();
  }

  default boolean hasModifier(RitualModifier modifier) {
    return getEnabledModifiers().contains(modifier);
  }

  default boolean hasModifier(Holder<RitualModifier> modifier) {
    return getEnabledModifiers().contains(modifier.value());
  }

  default DataComponentMap getRitualData() {
    return DataComponentMap.EMPTY;
  }

  default PatchedDataComponentMap getRitualDataPatch() {
    return PatchedDataComponentMap.fromPatch(getRitualData(), DataComponentPatch.EMPTY);
  }

  default <T> T getRitualData(DataComponentType<? extends T> component) {
    return getRitualData().get(component);
  }

  default <T> T getRitualData(Supplier<? extends DataComponentType<? extends T>> component) {
    return getRitualData().get(component.get());
  }

  @Override
  default Ritual asRitual() {
    return getRitual();
  }

  default boolean isEmpty() {
    return false;
  }

  default int count(TagKey<RitualModifier> tag) {
    return getEnabledModifiers().count(tag);
  }

  default boolean is(TagKey<Ritual> spell) {
    return asRitual().is(spell);
  }

  default boolean is(ResourceLocation spell) {
    return asRitual().is(spell);
  }

  default boolean is(ResourceKey<Ritual> spell) {
    return asRitual().is(spell);
  }

  default boolean is(Predicate<ResourceKey<Ritual>> spell) {
    return asRitual().is(spell);
  }

  @Nullable
  default RitualModifier getFirstModifier (TagKey<RitualModifier> modifier) {
    return getEnabledModifiers().getFirstModifier(modifier);
  }

  default boolean has(TagKey<RitualModifier> modifier) {
    return getEnabledModifiers().hasTag(modifier);
  }

  default boolean has(ResourceKey<RitualModifier> modifier) {
    return getEnabledModifierKeys().contains(modifier);
  }

  default boolean has(RitualModifier modifier) {
    return getEnabledModifiers().contains(modifier);
  }

  default boolean has(Holder<RitualModifier> modifier) {
    return has(modifier.value());
  }

  default int getTransformingHash () {
    return getEnabledModifiers().transformingHash();
  }

  default MutableComponent getStyledName() {
    return asRitual().getStyledName(this);
  }

  @Nullable
  default AABB getAABB () {
    return asRitual().getAABB(this);
  }

  @Nullable
  default BoundingBox getBoundingBox () {
    return asRitual().getBoundingBox(this);
  }

  default List<BiPredicate<Level, BlockPos>> getPredicates () {
    return asRitual().getPredicates(this);
  }

  default int getRadiusXZ () {
    return asRitual().getRadiusXZ(this);
  }

  default int getInterval () {
    return asRitual().getInterval(this);
  }

  default int getRadiusY () {
    return asRitual().getRadiusY(this);
  }

  default boolean requiresCache () {
    return asRitual().requiresCache(this);
  }

  default boolean providesLight() {
    return asRitual().providesLight(this);
  }

  default ItemStack getRitualIcon() {
    return asRitual().getRitualIcon(this);
  }

  // This function is always called when the ritual is first started.
  default void starts(Level pLevel, BlockPos pPos, BlockState pState, PyreBlockEntity blockEntity, RandomSource random) {
    asRitual().starts(this, pLevel, pPos, pState, blockEntity, random);
  }

  // This function is called when a ritual stops and is not about to immediately restart.
  default void stops(Level pLevel, BlockPos pPos, BlockState pState, PyreBlockEntity blockEntity, RandomSource random) {
    asRitual().stops(this, pLevel, pPos, pState, blockEntity, random);
  }

  // This function is always called when a ritual stops, even if it is about to immediately restart; it is also called whenever the pyre is broken.
  default void ends(Level pLevel, BlockPos pPos, BlockState pState, PyreBlockEntity blockEntity, RandomSource random) {
    asRitual().ends(this, pLevel, pPos, pState, blockEntity, random);
  }

  // This function is only called when the pyre is broken
  default void removed(Level pLevel, BlockPos pPos, BlockState pState, PyreBlockEntity blockEntity, RandomSource random) {
    asRitual().removed(this, pLevel, pPos, pState, blockEntity, random);
  }

  default void tick(Level pLevel, BlockPos pPos, BlockState pState, PyreBlockEntity blockEntity, @Nullable PositionCache cache, RandomSource random) {
    asRitual().tick(this, pLevel, pPos, pState, blockEntity, cache, random);
  }

  default void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, @Nullable PositionCache pCache, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {
    asRitual().functionalTick(this, pLevel, pPos, pState, pCache, blockEntity, duration, randomSource);
  }

  static IRitualInstance.SimpleRitual of(Ritual ritual) {
    return new IRitualInstance.SimpleRitual(ritual);
  }

  record SimpleRitual(Ritual ritual) implements IRitualInstance {
    @Override
    public Ritual getRitual() {
      return ritual();
    }

    @Override
    public int getLifetime() {
      return getDuration();
    }

    @Override
    public RitualModifierSet getEnabledModifiers() {
      return RitualModifierSet.EMPTY;
    }

    @Override
    public boolean hasModifier(RitualModifier modifier) {
      return false;
    }

    @Override
    public boolean hasModifier(Holder<RitualModifier> modifier) {
      return false;
    }
  }
}
