package mysticmods.roots.api.spell;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.mojang.serialization.Codec;
import mysticmods.roots.api.*;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.herb.Costing;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.registry.*;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.*;

public abstract class Spell implements IStyledInstance<ISpellInstance>, ICosted, SpellLike, TooltipComponent, IDataMapInitialize<Spell>, IExtendedDescribedInstance<ISpellInstance> {
  public static final Codec<Spell> CODEC = RootsRegistries.SPELLS.byNameCodec();
  public static final StreamCodec<RegistryFriendlyByteBuf, Spell> STREAM_CODEC = ByteBufCodecs.registry(RootsRegistries.Keys.SPELLS);

  private final ResourceKey<Spell> resourceKey;

  protected final PropertyHolder<Property.IntegerProperty> cooldownProperty;
  protected final PropertyHolder<Property.DoubleProperty> reachProperty;
  protected final PropertyHolder<Property.IntegerProperty> maxUseProperty;
  protected final List<PropertyHolder<?>> allProperties;
  protected final CostInstance defaultCosts;
  protected DataComponentMap components;
  protected CostInstance costs;
  protected int cooldown = 0;
  protected double reach = 0.0;
  protected int maxUse;

  // These values can fluctuate based on modifiers
  protected final SpellType.Cast castType;
  protected final SpellType.Charge chargeType;
  protected final int color1, color2;

  protected Style style;
  protected TextColor textColor;

  protected String descriptionId;
  protected String descriptionTooltipId;
  protected String descriptionTooltipExtendedId;
  protected Component[] extendedDescription = null;

  private final LayeredProperties layeredProperties;

  public Spell(Properties properties) {
    properties.build(); // TODO: Handle this some other way
    this.castType = properties.castType;
    this.textColor = properties.textColor;
    this.chargeType = properties.chargeType;
    this.color1 = properties.color1;
    this.color2 = properties.color2;

    this.resourceKey = properties.resourceKey;

    this.defaultCosts = properties.defaultCosts.get();
    this.components = properties.buildAndValidateComponents();
    this.reachProperty = properties.reachProperty;
    this.cooldownProperty = properties.cooldownProperty;
    this.maxUseProperty = properties.maxUseProperty;
    this.allProperties = new ArrayList<>(properties.allProperties);

    this.layeredProperties = new LayeredProperties(properties, this);
  }

  public ResourceKey<Spell> getKey () {
    return resourceKey;
  }

  public Holder<Spell> builtInRegistryHolder() {
    return RootsRegistries.SPELLS.wrapAsHolder(this);
  }

  @Override
  public Component[] getOrCreateDescriptionComponents() {
    if (extendedDescription == null) {
      this.extendedDescription = createExtendedDescriptionComponents();
    }

    return this.extendedDescription;
  }

  public abstract Component[] createExtendedDescriptionComponents();

  public abstract Component[] createModifierDescriptionComponents(SpellModifier spellModifier);

  @Override
  @Nullable
  public TextColor getTextColor() {
    return textColor;
  }

  @Nullable
  public TextColor getTextColor(ISpellInstance instance) {
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

  // TODO: Cache this
  @Override
  public Style getOrCreateStyle(ISpellInstance instance) {
    var props = layeredProperties.get(instance);
    return Style.EMPTY.withColor(props.color()).withBold(true);
  }

  @Override
  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId("spell", getKey().location());
    }

    return this.descriptionId;
  }

  @Override
  public String getOrCreateDescriptionId(ISpellInstance instance) {
    return layeredProperties.get(instance).descriptionId();
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
  public String getOrCreateTooltipDescriptionId(ISpellInstance iSpellInstance) {
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
  public String getOrCreateTooltipExtendedDescriptionId(ISpellInstance instance) {
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

  public int getColor1(ISpellInstance instance) {
    return layeredProperties.get(instance).color1();
  }

  public int getColor2(ISpellInstance instance) {
    return layeredProperties.get(instance).color2();
  }

  public int getMaxUse(ISpellInstance iSpellInstance) {
    if (maxUse == 0 && getType(iSpellInstance) == SpellType.Cast.CONTINUOUS) {
      return 72000;
    }

    return maxUse;
  }

  public Component getChargeText(ISpellInstance iSpellInstance, int currentCharge) {
    return Component.translatable("roots.message.staff.charging", currentCharge, getMaxUse(iSpellInstance));
  }

  @Override
  public CostInstance getDefaultCosts() {
    return defaultCosts;
  }

  @Override
  public CostInstance getCosts() {
    if (costs == null) {
      RootsAPI.LOG.error("Data maps haven't been initialized for spell: {}", getKey());
    }
    return costs;
  }

  public SpellType.Charge getChargeType (ISpellInstance instance) {
    return layeredProperties.get(instance).charge();
  }

  public final PropertyHolder<Property.IntegerProperty> getCooldownProperty() {
    return cooldownProperty;
  }

  public final PropertyHolder<Property.DoubleProperty> getReachProperty() {
    return reachProperty;
  }

  public final PropertyHolder<Property.IntegerProperty> getMaxUseProperty() {
    return maxUseProperty;
  }

  public int getCooldown(ISpellInstance instance) {
    return cooldown;
  }

  public SpellType.Cast getType(ISpellInstance iSpellInstance) {
    return layeredProperties.get(iSpellInstance).cast();
  }

  public void buildProperties(List<PropertyHolder<?>> properties) {
    properties.addAll(allProperties);
  }

  public List<PropertyHolder<?>> getProperties() {
    List<PropertyHolder<?>> properties = new ArrayList<>();
    buildProperties(properties);
    return properties;
  }

  protected void initializeProperties(Holder<Spell> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.cooldown = properties.get(getCooldownProperty());
    if (getReachProperty() != null) {
      this.reach = properties.get(getReachProperty());
    }
    if (getMaxUseProperty() != null) {
      this.maxUse = properties.get(getMaxUseProperty());
    }
  }

  public void initialize(Holder<Spell> holder) {

  }

  @Override
  public void init(Holder<Spell> holder) {
    costs = holder.getData(DataMaps.SPELL_COST_DATA);
    initializeProperties(holder);
    initialize(holder);
  }

  // TODO: This does not function how it should function
  public ItemStack getSpellIcon(@Nullable ISpellInstance instance) {
    if (instance == null) {
      return getSpellIcon(this.simple());
    }

    return RootsItemCallbacks.getLibraryItemStack(instance);
  }

  @Deprecated
  public ItemStack getSpellIcon() {
    return RootsItemCallbacks.getLibraryItemStack(this.simple());
  }

  public abstract CastResult cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks);

  public Map<BlockPos, BlockState> getAffectedBlocks(Level level, Player player, ISpellInstance spell, ItemStack stack, BlockPos pos, BlockState blockState, BlockHitResult rayTraceResult) {
    return Collections.emptyMap();
  }

  public double getBlockRange(Player pPlayer, ISpellInstance spell) {
    return pPlayer.blockInteractionRange() + reach;
  }

  public double getEntityRange(Player pPlayer, ISpellInstance spell) {
    return pPlayer.entityInteractionRange() + reach;
  }

  protected BlockHitResult pickBlock(Player pPlayer, ISpellInstance spell) {
    return pickBlock(pPlayer, spell, false);
  }

  protected BlockHitResult pickBlock(Player pPlayer, ISpellInstance spell, boolean fluids) {
    return (BlockHitResult) pPlayer.pick(getBlockRange(pPlayer, spell), 1f, fluids);
  }

  public boolean is(ResourceLocation key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean is(ResourceKey<Spell> key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean is(Predicate<ResourceKey<Spell>> key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean is(TagKey<Spell> key) {
    return builtInRegistryHolder().is(key);
  }

  // TODO: Collapse this into the style by default
  @Override
  public boolean isBold() {
    return true;
  }

  @Override
  public Spell asSpell() {
    return this;
  }

  public boolean hasBlockTarget(ISpellInstance instance, Player pPlayer) {
    return false;
  }

  @Nullable
  public Vec3 getBlockTarget(ISpellInstance spell, Player pPlayer) {
    return null;
  }

  @Nullable
  public BoundingBox getBoundingBox() {
    return null;
  }

  @Nullable
  public AABB getAABB(ISpellInstance iSpellInstance) {
    return null;
  }

  @Override
  public String getDescriptionId(ISpellInstance spellSlot) {
    return layeredProperties.get(spellSlot).descriptionId();
  }

  public DataComponentMap getComponents() {
    return components;
  }

  public DataComponentType<? extends Cycling<?>> getCycleComponent(ISpellInstance iSpellInstance) {
    return layeredProperties.get(iSpellInstance).cycleComponent();
  }

  public boolean canTargetThroughFluids(ISpellInstance iSpellInstance) {
    return true;
  }

  public boolean canMarkEntityTargets(ISpellInstance iSpellInstance) {
    return true;
  }

  public boolean canTargetEntity(ISpellInstance instance, Entity entity) {
    return false; // TODO: This function should handle tag-checking
  }

  public List<Entity> selectTargets(ISpellInstance iSpellInstance, HitResult hit, Player pPlayer) {
    return Collections.emptyList();
  }

  public float getIconPredicate(ISpellInstance iSpellInstance) {
    return layeredProperties.get(iSpellInstance).predicateValue();
  }

  public static class Properties {
    private static final Interner<DataComponentMap> COMPONENT_INTERNER = Interners.newStrongInterner();
    @Nullable
    DataComponentMap.Builder components;
    DataComponentType<? extends Cycling<?>> cycleComponent;
    SpellType.Cast castType = SpellType.Cast.INSTANT;
    TextColor textColor;
    Supplier<CostInstance> defaultCosts;
    SpellType.Charge chargeType = SpellType.Charge.INSTANCE;
    int color1 = -1;
    int color2 = -1;

    PropertyHolder<Property.IntegerProperty> cooldownProperty;
    PropertyHolder<Property.DoubleProperty> reachProperty = null;
    PropertyHolder<Property.IntegerProperty> maxUseProperty = null;

    final List<PropertyHolder<?>> allProperties = new ArrayList<>();

    final ResourceKey<Spell> resourceKey;

    public PropertyHolder<Property.IntegerProperty> radiusXProperty = null;
    public PropertyHolder<Property.IntegerProperty> radiusYProperty = null;
    public PropertyHolder<Property.IntegerProperty> radiusZProperty = null;

    public Map<ResourceKey<SpellModifier>, Transformer> transformers = new HashMap<>();

    public Properties(ResourceKey<Spell> resourceKey) {
      this.resourceKey = resourceKey;
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

    public Properties radiusZ(PropertyHolder<Property.IntegerProperty> property) {
      this.radiusZProperty = property;
      if (property != null && !this.allProperties.contains(property)) {
        this.allProperties.add(property);
      }
      return this;
    }

    public Properties radius(PropertyHolder<Property.IntegerProperty> radiusX, PropertyHolder<Property.IntegerProperty> radiusY, PropertyHolder<Property.IntegerProperty> radiusZ) {
      return radiusX(radiusX).radiusY(radiusY).radiusZ(radiusZ);
    }

    public Properties radius(PropertyHolder<Property.IntegerProperty> radiusZX, PropertyHolder<Property.IntegerProperty> radiusY) {
      return radiusX(radiusZX).radiusY(radiusY).radiusZ(radiusZX);
    }

    public Properties radius(PropertyHolder<Property.IntegerProperty> radius) {
      return radius(radius, radius, radius);
    }


    public Properties type(SpellType.Cast type) {
      this.castType = type;
      return this;
    }

    public Properties property(PropertyHolder<?> property) {
      this.allProperties.add(property);
      return this;
    }

    public Properties properties(PropertyHolder<?>... properties) {
      this.allProperties.addAll(Arrays.asList(properties));
      return this;
    }

    public Properties cooldown(PropertyHolder<Property.IntegerProperty> property) {
      this.cooldownProperty = property;
      allProperties.add(property);
      return this;
    }

    public Properties reach(PropertyHolder<Property.DoubleProperty> property) {
      this.reachProperty = property;
      if (property != null) {
        allProperties.add(property);
      }
      return this;
    }

    public Properties maxUse(PropertyHolder<Property.IntegerProperty> property) {
      this.maxUseProperty = property;
      if (property != null) {
        allProperties.add(property);
      }
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

    public Properties costs(Supplier<CostInstance> costs) {
      this.defaultCosts = costs;
      return this;
    }

    public Properties cost(Supplier<Cost> costs) {
      this.defaultCosts = () -> CostInstance.of(costs.get());
      return this;
    }

    public Properties cost(Supplier<Holder<Herb>> herb, double amount) {
      this.defaultCosts = () -> CostInstance.add(herb.get(), amount);
      return this;
    }

    public Properties charge(SpellType.Charge type) {
      this.chargeType = type;
      return this;
    }

    public Properties operations() {
      return charge(SpellType.Charge.OPERATION);
    }

    public Properties build() {
      // TODO: Validate everything
      if (this.castType == null) {
        throw new NullPointerException("SpellProperties requires a `castType`");
      }
      if (this.textColor == null) {
        throw new NullPointerException("SpellProperties requires a `textColor`");
      }
      if (this.defaultCosts == null) {
        throw new NullPointerException("SpellProperties requires `defaultCosts`");
      }
      if (this.chargeType == null) {
        throw new NullPointerException("SpellProperties requires a `chargeType`");
      }
      if (this.color1 == -1 && this.color2 == -1) {
        throw new IllegalStateException("Invalid colors for SpellProperties");
      }
      if (this.cooldownProperty == null) {
        throw new IllegalStateException("Invalid cooldown property: `cooldown` property must be supplied.");
      }
      return this;
    }

    public <T extends Cycling<T>> Properties cycle (DataComponentType<T> component, T value) {
      this.cycleComponent = component;
      return this.component(component, value);
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

    public Properties transformer (ResourceKey<SpellModifier> modifier, Transformer transformer) {
      this.transformers.put(modifier, transformer);
      return this;
    }

    public Properties transformer (Holder<SpellModifier> modifier, Transformer transformer) {
      return this.transformer(modifier.getKey(), transformer);
    }

    public Properties transformer (Holder<SpellModifier> modifier, BiFunction <SpellModifier, Transformer, Transformer> operator) {
      return this.transformer(modifier.getKey(), operator.apply(modifier.value(), new Spell.Transformer()));
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
    SpellType.Cast castType = null;
    SpellType.Charge unitType = null;
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

    public Transformer cast(SpellType.Cast castType) {
      this.castType = castType;
      return this;
    }

    public Transformer charge(SpellType.Charge unitType) {
      this.unitType = unitType;
      return this;
    }

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

    public Transformer component (Supplier<Component[]> extendedComponents) {
      this.extendedComponents = extendedComponents;
      return this;
    }
  }
}
