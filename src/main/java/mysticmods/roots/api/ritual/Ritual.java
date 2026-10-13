package mysticmods.roots.api.ritual;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.mojang.serialization.Codec;
import mysticmods.roots.api.Cycling;
import mysticmods.roots.api.RootsItemCallbacks;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.modifier.RitualModifier;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.registry.IDataMapInitialize;
import mysticmods.roots.api.registry.IExtendedDescribedInstance;
import mysticmods.roots.api.registry.IStyledInstance;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.util.PositionCache;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
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
import net.neoforged.neoforge.common.CommonHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.swing.*;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class Ritual implements IExtendedDescribedInstance<IRitualInstance>, TooltipComponent, IDataMapInitialize<Ritual>, IStyledInstance<IRitualInstance> {
  public static final Codec<Ritual> CODEC = RootsRegistries.RITUALS.byNameCodec();
  public static final StreamCodec<RegistryFriendlyByteBuf, Ritual> STREAM_CODEC = ByteBufCodecs.registry(RootsRegistries.Keys.RITUALS);

  private final ResourceKey<Ritual> resourceKey;
  private final int color1;
  private final int color2;
  private final TextColor textColor;
  private final List<PropertyHolder<?>> allProperties;

  private Style style = null;

  protected String descriptionId;

  protected BoundingBox boundingBox;
  protected AABB aabb;
  protected int duration = 0;
  protected int radiusXZ = 0;
  protected int radiusY = 0;
  protected int interval = 0;
  protected int count = 0;

  private final PropertyHolder<Property.IntegerProperty> radiusXProperty, radiusYProperty, intervalProperty, durationProperty;
  @Nullable
  private final PropertyHolder<Property.IntegerProperty> countProperty;

  private final LayeredRitualProperties layeredProperties;
  private String descriptionTooltipExtendedId;
  private String descriptionTooltipId;

  public Ritual(Properties properties) {
    properties.build();

    this.color1 = properties.color1;
    this.color2 = properties.color2;
    this.textColor = properties.textColor;

    this.resourceKey = properties.resourceKey;

    this.radiusXProperty = properties.radiusXProperty;
    this.radiusYProperty = properties.radiusYProperty;

    this.countProperty = properties.countProperty;
    this.intervalProperty = properties.intervalProperty;
    this.durationProperty = properties.durationProperty;

    this.allProperties = new ArrayList<>(properties.allProperties);

    this.layeredProperties = new LayeredRitualProperties(properties, this);
  }

  public ResourceKey<Ritual> getKey() {
    return resourceKey;
  }

  @Override
  public Style getOrCreateStyle(IRitualInstance instance) {
    var props = layeredProperties.get(instance);
    return Style.EMPTY.withColor(props.color()).withBold(true);
  }

  @Override
  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId("ritual", getKey().location());
    }

    return this.descriptionId;
  }

  @Override
  public String getOrCreateDescriptionId(IRitualInstance instance) {
    return layeredProperties.get(instance).descriptionId();
  }

  public Holder<Ritual> builtInRegistryHolder() {
    return RootsRegistries.RITUALS.wrapAsHolder(this);
  }

  public Component[] createExtendedDescriptionComponents() {
    return new Component[]{};
  }

  public Component[] createModifierDescriptionComponents(RitualModifier modifier) {
    return new Component[]{};
  }

  @Override
  public Component[] getOrCreateDescriptionComponents() {
    return new Component[]{};
  }

  @Override
  public Component[] getOrCreateDescriptionComponents(IRitualInstance instance) {
    return layeredProperties.get(instance).componentGetter().get();
  }

  // TODO: Tooltip doesn't have SpelInstance?
  @Override
  public String getOrCreateTooltipDescriptionId() {
    if (this.descriptionTooltipId == null) {
      this.descriptionTooltipId = getOrCreateDescriptionId() + ".description";
    }

    return this.descriptionTooltipId;
  }

  @Override
  public String getOrCreateTooltipDescriptionId(IRitualInstance iSpellInstance) {
    return layeredProperties.get(iSpellInstance).descriptionTooltipId();
  }

  @Override
  public String getOrCreateTooltipExtendedDescriptionId() {
    if (this.descriptionTooltipExtendedId == null) {
      this.descriptionTooltipExtendedId = getOrCreateTooltipDescriptionId() + ".extended";
    }

    return this.descriptionTooltipExtendedId;
  }

  @Override
  public String getOrCreateTooltipExtendedDescriptionId(IRitualInstance instance) {
    return layeredProperties.get(instance).descriptionTooltipExtendedId();
  }

  @Deprecated
  public int getRawColor1() {
    return color1;
  }

  @Deprecated
  public int getRawColor2() {
    return color2;
  }

  public int getColor1(IRitualInstance instance) {
    return layeredProperties.get(instance).color1();
  }

  public int getColor2(IRitualInstance instance) {
    return layeredProperties.get(instance).color2();
  }

  @Override
  @Nullable
  public TextColor getTextColor() {
    return textColor;
  }

  @Nullable
  @Override
  public TextColor getTextColor(IRitualInstance instance) {
    return layeredProperties.get(instance).color();
  }

  // TODO: Override this
  @Override
  public Style getOrCreateStyle() {
    if (style == null) {
      TextColor color = getTextColor();
      if (color != null) {
        style = Style.EMPTY.withColor(color).withBold(isBold());
      } else {
        style = Style.EMPTY.withBold(isBold());
      }
    }
    return style;
  }

  public boolean providesLight(IRitualInstance iRitualInstance) {
    return true;
  }

  // This function is always called when the ritual is first started.
  public void starts(IRitualInstance instance, Level pLevel, BlockPos pPos, BlockState pState, PyreBlockEntity blockEntity, RandomSource random) {
  }

  // This function is called when a ritual stops and is not about to immediately restart.
  public void stops(IRitualInstance instance, Level pLevel, BlockPos pPos, BlockState pState, PyreBlockEntity blockEntity, RandomSource random) {
  }

  // This function is always called when a ritual stops, even if it is about to immediately restart; it is also called whenever the pyre is broken.
  public void ends(IRitualInstance instance, Level pLevel, BlockPos pPos, BlockState pState, PyreBlockEntity blockEntity, RandomSource random) {
  }

  // This function is only called when the pyre is broken
  public void removed(IRitualInstance instance, Level pLevel, BlockPos pPos, BlockState pState, PyreBlockEntity blockEntity, RandomSource random) {
  }

  public void tick(IRitualInstance instance, Level pLevel, BlockPos pPos, BlockState pState, PyreBlockEntity blockEntity, @Nullable PositionCache cache, RandomSource random) {
    int dur = instance.getDuration() - blockEntity.getLifetime();
    functionalTick(instance, pLevel, pPos, pState, cache, blockEntity, dur, random);
/*    BoundingBox box;
    if (cache == null) {
      box = getBoundingBox().moved(pPos.getX(), pPos.getY(), pPos.getZ());
    } else {
      box = cache.getBoundingBox();
    }*/
  }

  protected abstract void functionalTick(IRitualInstance instance, Level pLevel, BlockPos pPos, BlockState pState, @Nullable PositionCache pCache, PyreBlockEntity blockEntity, int duration, RandomSource randomSource);

  // TODO:
  protected void rebuildBounds() {
    boundingBox = new BoundingBox(-getRadiusXZ(this.simple()), -getRadiusY(this.simple()), -getRadiusXZ(this.simple()), getRadiusXZ(this.simple()), getRadiusY(this.simple()), getRadiusXZ(this.simple()));
    aabb = AABB.of(getBoundingBox(this.simple()));
  }

  protected void buildProperties(List<PropertyHolder<?>> properties) {
    properties.addAll(allProperties);
  }

  public List<PropertyHolder<?>> getProperties() {
    List<PropertyHolder<?>> properties = new ArrayList<>();
    buildProperties(properties);
    return properties;
  }

  protected void initializeProperties(Holder<Ritual> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.RITUAL_PROPERTY_DATA);
    duration = properties.get(getDurationProperty());
    if (getRadiusXZProperty() != null) {
      radiusXZ = properties.get(getRadiusXZProperty());
    }
    if (getRadiusYProperty() != null) {
      radiusY = properties.get(getRadiusYProperty());
    }
    interval = properties.get(getIntervalProperty());
    if (getCountProperty() != null) {
      count = properties.get(getCountProperty());
    }
  }

  protected abstract void initialize(Holder<Ritual> holder);

  @Override
  public void init(Holder<Ritual> holder) {
    initializeProperties(holder);
    initialize(holder);
    rebuildBounds();
  }

  public List<BiPredicate<Level, BlockPos>> getPredicates(IRitualInstance iRitualInstance) {
    return Collections.emptyList();
  }

  public ItemStack getRitualIcon (@Nullable IRitualInstance instance) {
    if (instance == null) {
      return getRitualIcon(this.simple());
    }

    return RootsItemCallbacks.getItemStack(instance);
  }

  @Deprecated
  public ItemStack getRitualIcon() {
    return RootsItemCallbacks.getItemStack(this);
  }

  public int getDuration(IRitualInstance iRitualInstance) {
    return duration;
  }

  public int getRadiusXZ(IRitualInstance iRitualInstance) {
    return radiusXZ;
  }

  public int getRadiusY(IRitualInstance iRitualInstance) {
    return radiusY;
  }

  public int getInterval(IRitualInstance iRitualInstance) {
    return interval;
  }

  public BoundingBox getBoundingBox(IRitualInstance iRitualInstance) {
    return boundingBox;
  }

  public AABB getAABB(IRitualInstance iRitualInstance) {
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

  @NotNull
  public final PropertyHolder<Property.IntegerProperty> getDurationProperty() {
    return durationProperty;
  }

  @Nullable
  public final PropertyHolder<Property.IntegerProperty> getRadiusXZProperty() {
    return radiusXProperty;
  }

  @Nullable
  public final PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return radiusYProperty;
  }

  @NotNull
  public final PropertyHolder<Property.IntegerProperty> getIntervalProperty() {
    return intervalProperty;
  }

  @Nullable
  public final PropertyHolder<Property.IntegerProperty> getCountProperty () {
    return countProperty;
  }

  public boolean requiresCache(IRitualInstance iRitualInstance) {
    return !getPredicates(iRitualInstance).isEmpty();
  }

  public IRitualInstance simple () {
    return IRitualInstance.of(this);
  }

  public int getCount(IRitualInstance iRitualInstance) {
    return count;
  }

  // Predicates
  public static class Properties {
    private static final Interner<DataComponentMap> COMPONENT_INTERNER = Interners.newStrongInterner();
    @org.jetbrains.annotations.Nullable
    DataComponentMap.Builder components;

    TextColor textColor;
    int color1 = -1;
    int color2 = -1;

    final List<PropertyHolder<?>> allProperties = new ArrayList<>();

    final ResourceKey<Ritual> resourceKey;

    public PropertyHolder<Property.IntegerProperty> intervalProperty;
    public PropertyHolder<Property.IntegerProperty> durationProperty;
    public PropertyHolder<Property.IntegerProperty> countProperty;

    public PropertyHolder<Property.IntegerProperty> radiusXProperty = null;
    public PropertyHolder<Property.IntegerProperty> radiusYProperty = null;

    public Map<ResourceKey<RitualModifier>, Transformer> transformers = new HashMap<>();

    public Properties(ResourceKey<Ritual> resourceKey) {
      this.resourceKey = resourceKey;
    }

    public Properties duration(PropertyHolder<Property.IntegerProperty> property) {
      this.durationProperty = property;
      if (property != null && !this.allProperties.contains(property)) {
        this.allProperties.add(property);
      }
      return this;
    }

    public Properties interval(PropertyHolder<Property.IntegerProperty> property) {
      this.intervalProperty = property;
      if (property != null && !this.allProperties.contains(property)) {
        this.allProperties.add(property);
      }
      return this;
    }

    public Properties count(PropertyHolder<Property.IntegerProperty> property) {
      this.countProperty = property;
      if (property != null && !this.allProperties.contains(property)) {
        this.allProperties.add(property);
      }
      return this;
    }

    public Properties radiusY(PropertyHolder<Property.IntegerProperty> property) {
      this.radiusYProperty = property;
      if (property != null && !this.allProperties.contains(property)) {
        this.allProperties.add(property);
      }
      return this;
    }

    public Properties radiusX(PropertyHolder<Property.IntegerProperty> property) {
      this.radiusXProperty = property;
      if (property != null && !this.allProperties.contains(property)) {
        this.allProperties.add(property);
      }
      return this;
    }

    public Properties radius(PropertyHolder<Property.IntegerProperty> radiusZX, PropertyHolder<Property.IntegerProperty> radiusY) {
      return radiusX(radiusZX).radiusY(radiusY);
    }

    public Properties radius(PropertyHolder<Property.IntegerProperty> radius) {
      return radius(radius, radius);
    }


    public Properties property(PropertyHolder<?> property) {
      this.allProperties.add(property);
      return this;
    }

    public Properties properties(PropertyHolder<?>... properties) {
      this.allProperties.addAll(Arrays.asList(properties));
      return this;
    }

    public Properties textColor(ChatFormatting format) {
      this.textColor = TextColor.fromLegacyFormat(format);
      return this;
    }

    public Properties textColor(int color) {
      this.textColor = TextColor.fromRgb(color);
      return this;
    }

    public Properties textColor(TextColor color) {
      this.textColor = color;
      return this;
    }

    public Properties color(int color1, int color2) {
      this.color1 = color1;
      this.color2 = color2;
      return this;
    }

    public Properties build() {
      // TODO: Validate everything
/*      if (this.textColor == null) {
        throw new NullPointerException("RitualProperties requires a `textColor`");
      }
      if (this.color1 == -1 && this.color2 == -1) {
        throw new IllegalStateException("Invalid colors for RitualProperties");
      }*/
      return this;
    }

    public <T> Properties component(Supplier<? extends DataComponentType<T>> component, T value) {
      return this.component(component.get(), value);
    }

    public <T> Properties component(DataComponentType<T> component, T value) {
      //noinspection UnstableApiUsage
      CommonHooks.validateComponent(value);
      if (this.components == null) {
        this.components = DataComponentMap.builder();
      }

      this.components.set(component, value);
      return this;
    }

    public Properties transformer(ResourceKey<RitualModifier> modifier, Transformer transformer) {
      this.transformers.put(modifier, transformer);
      return this;
    }

    public Properties transformer(Holder<RitualModifier> modifier, Transformer transformer) {
      return this.transformer(modifier.getKey(), transformer);
    }

    // TODO: I'm concerned that this may cause issues
    public Properties transformer(Holder<RitualModifier> modifier, BiFunction<RitualModifier, Transformer, Transformer> operator) {
      return this.transformer(modifier.getKey(), operator.apply(modifier.value(), new Transformer()));
    }

    DataComponentMap buildAndValidateComponents() {
      DataComponentMap datacomponentmap = this.buildComponents();
      return validateComponents(datacomponentmap);
    }

    public static DataComponentMap validateComponents(DataComponentMap datacomponentmap) {
      return datacomponentmap;
    }

    private DataComponentMap buildComponents() {
      return this.components == null ? DataComponentMap.EMPTY : COMPONENT_INTERNER.intern(this.components.build());
    }
  }

  public static class Transformer {
    boolean hasColors = false;
    int color1 = -1;
    int color2 = -1;
    String descriptionId = null;
    String descriptionTooltipId = null;
    String descriptionExtendedTooltipId = null;
    Supplier<Component[]> extendedComponents = null;
    TextColor textColor = null;
    boolean hasComponent = false;
    DataComponentType<? extends Cycling<?>> cyclingComponent = null;
    boolean hasPredicateValue = false;
    float predicateValue = -1;

    public Transformer color(int color1, int color2) {
      this.color1 = color1;
      this.color2 = color2;
      return this;
    }

    // TODO: Supplier<String>
    public Transformer description(String descriptionId) {
      this.descriptionId = descriptionId;
      return this;
    }

    public Transformer tooltip(String descriptionTooltipId) {
      this.descriptionTooltipId = descriptionTooltipId;
      return this;
    }

    public Transformer extended(String descriptionExtendedTooltipId) {
      this.descriptionExtendedTooltipId = descriptionExtendedTooltipId;
      return this;
    }

    public Transformer textColor(TextColor textColor) {
      this.textColor = textColor;
      return this;
    }

    public Transformer cycle(DataComponentType<? extends Cycling<?>> cyclingComponent) {
      this.hasComponent = true;
      this.cyclingComponent = cyclingComponent;
      return this;
    }

    public Transformer predicate(float predicateValue) {
      this.hasPredicateValue = true;
      this.predicateValue = predicateValue;
      return this;
    }

    public Transformer component(Supplier<Component[]> extendedComponents) {
      this.extendedComponents = extendedComponents;
      return this;
    }
  }
}
