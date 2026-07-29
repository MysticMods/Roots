package mysticmods.roots.api.spell;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.mojang.serialization.Codec;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsItemCallbacks;
import mysticmods.roots.api.SpellLike;
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
import net.minecraft.network.chat.MutableComponent;
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
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class Spell implements IStyled, ICosted, SpellLike, TooltipComponent, IDataMapInitialize<Spell>, IExtendedDescribed {
  public static final Codec<Spell> CODEC = RootsRegistries.SPELLS.byNameCodec();
  public static final StreamCodec<RegistryFriendlyByteBuf, Spell> STREAM_CODEC = ByteBufCodecs.registry(RootsRegistries.Keys.SPELLS);

  protected final SpellCastType type;
  protected final CostInstance defaultCosts;
  protected final ParentChargeType chargeType;
  protected DataComponentMap components;
  protected CostInstance costs;
  protected int cooldown = 0;
  protected double reach = 0.0;
  protected final int color1, color2;
  protected int maxUse;

  protected Style style;
  protected TextColor textColor;
  protected String descriptionId;
  protected String descriptionTooltipId;
  protected String descriptionTooltipExtendedId;
  protected Component[] extendedDescription = null;

  @Deprecated
  public Spell(SpellCastType type, ChatFormatting color, CostInstance defaultCosts, ParentChargeType chargeType, int color1, int color2) {
    this(type, TextColor.fromLegacyFormat(color), defaultCosts, chargeType, color1, color2);
  }

  public Spell (SpellCastType type, TextColor textColor, CostInstance defaultCosts, ParentChargeType chargeType, int color1, int color2) {
    this.type = type;
    this.textColor = textColor;
    this.defaultCosts = defaultCosts;
    this.chargeType = chargeType;
    this.color1 = color1;
    this.color2 = color2;
    this.components = DataComponentMap.builder().build();
  }

  public Spell (Properties properties) {
    this.type = properties.castType;
    this.textColor = properties.textColor;
    this.defaultCosts = properties.defaultCosts.get();
    this.chargeType = properties.chargeType;
    this.color1 = properties.color1;
    this.color2 = properties.color2;
    this.components = properties.buildAndValidateComponents();
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

  // TODO: Make this abstract
  @Deprecated(forRemoval = true)
  public Component[] createExtendedDescriptionComponents() {
    return new Component[]{};
  }

  // TODO: Make this abstract
  @Deprecated(forRemoval = true)
  public Component[] createModifierDescriptionComponents(SpellModifier spellModifier) {
    return new Component[]{};
  }

  @Override
  @Nullable
  public TextColor getTextColor() {
    return textColor;
  }

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

  @Override
  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId("spell", builtInRegistryHolder().getKey().location());
    }

    return this.descriptionId;
  }

  @Override
  public String getOrCreateTooltipDescriptionId() {
    if (this.descriptionTooltipId == null) {
      this.descriptionTooltipId = getOrCreateDescriptionId() + ".description";
    }

    return this.descriptionTooltipId;
  }


  @Override
  public String getOrCreateTooltipExtendedDescriptionId() {
    if (this.descriptionTooltipExtendedId == null) {
      this.descriptionTooltipExtendedId = getOrCreateDescriptionId() + ".description.extended";
    }

    return this.descriptionTooltipExtendedId;
  }

  public int getColor1() {
    return color1;
  }

  public int getColor2() {
    return color2;
  }

  public int getMaxUse() {
    if (maxUse == 0 && type == SpellCastType.CONTINUOUS) {
      return 72000;
    }

    return maxUse;
  }

  public Component getChargeText(int currentCharge) {
    return Component.translatable("roots.message.staff.charging", currentCharge, getMaxUse());
  }

  public int getCostChargeRate() {
    return 20;
  }

  @Override
  public CostInstance getDefaultCosts() {
    return defaultCosts;
  }

  @Override
  public CostInstance getCosts() {
    if (costs == null) {
      RootsAPI.LOG.error("Data maps haven't been initialized for spell: {}", builtInRegistryHolder().getKey());
    }
    return costs;
  }

  public ParentChargeType getChargeType() {
    return chargeType;
  }

  public abstract PropertyHolder<Property.IntegerProperty> getCooldownProperty();

  public PropertyHolder<Property.DoubleProperty> getReachProperty() {
    return null;
  }

  public PropertyHolder<Property.IntegerProperty> getMaxUseProperty() {
    return null;
  }

  public int getCooldown() {
    return cooldown;
  }

  public SpellCastType getType() {
    return type;
  }

  public void buildProperties(List<PropertyHolder<?>> properties) {
    if (getCooldownProperty() != null) {
      properties.add(getCooldownProperty());
    }
    if (getReachProperty() != null) {
      properties.add(getReachProperty());
    }
    if (getMaxUseProperty() != null) {
      properties.add(getMaxUseProperty());
    }
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

  public abstract void initialize(Holder<Spell> holder);

  @Override
  public void init(Holder<Spell> holder) {
    costs = holder.getData(DataMaps.SPELL_COST_DATA);
    initializeProperties(holder);
    initialize(holder);
  }

  public ItemStack getLibraryIcon() {
    return RootsItemCallbacks.getLibraryItemStack(this);
  }

  public ItemStack getStaffIcon() {
    return RootsItemCallbacks.getItemStack(this);
  }

  public abstract int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks);

  public Map<BlockPos, BlockState> getAffectedBlocks(Level level, Player player, ISpellInstance spell, ItemStack stack, BlockPos pos, BlockState blockState, BlockHitResult rayTraceResult) {
    return Collections.emptyMap();
  }

  public double getBlockRange(Player pPlayer) {
    return pPlayer.blockInteractionRange() + reach;
  }

  public double getEntityRange (Player pPlayer) {
    return pPlayer.entityInteractionRange() + reach;
  }

  // TODO: Entity targets
  protected BlockHitResult pickBlock(Player pPlayer, double range) {
    return pickBlock(pPlayer, range, false);
  }

  protected BlockHitResult pickBlock(Player pPlayer, double range, boolean fluids) {
    return (BlockHitResult) pPlayer.pick(range, 1f, fluids);
  }

  protected BlockHitResult pickBlock(Player pPlayer) {
    return pickBlock(pPlayer, false);
  }

  protected BlockHitResult pickBlock(Player pPlayer, boolean fluids) {
    return (BlockHitResult) pPlayer.pick(getBlockRange(pPlayer), 1f, fluids);
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

  @Override
  public boolean isBold() {
    return true;
  }

  @Override
  public Spell asSpell() {
    return this;
  }

  public boolean hasBlockTarget(Player pPlayer) {
    return false;
  }

  @Nullable
  public Vec3 getBlockTarget(Player pPlayer) {
    return null;
  }

  @Nullable
  public BoundingBox getBoundingBox() {
    return null;
  }

  @Nullable
  public AABB getAABB() {
    return null;
  }

  public String getDescriptionId(ISpellInstance spellSlot) {
    return getDescriptionId();
  }

  public MutableComponent getStyledName (ISpellInstance spellSlot) {
    return getStyledName();
  }

  public DataComponentMap getComponents() {
    return components;
  }

  public DataComponentType<? extends Cycling<?>> getCycleComponent () {
    return null;
  }

  public boolean canTargetThroughFluids() {
    return true;
  }

  public boolean canMarkEntityTargets() {
    return true;
  }

  public boolean canTargetEntity (Entity entity) {
    return false; // TODO: This function should handle tag-checking
  }

  public static class Properties {
    private static final Interner<DataComponentMap> COMPONENT_INTERNER = Interners.newStrongInterner();
    @Nullable
    DataComponentMap.Builder components;
    SpellCastType castType = SpellCastType.INSTANT;
    TextColor textColor;
    Supplier<CostInstance> defaultCosts;
    ParentChargeType chargeType = ParentChargeType.INSTANCE;
    int color1 = -1;
    int color2 = -1;

    public Properties type(SpellCastType type) {
      this.castType = type;
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

    public Properties cost (Supplier<Cost> costs) {
      this.defaultCosts = () -> CostInstance.of(costs.get());
      return this;
    }

    public Properties cost (Supplier<Holder<Herb>> herb, double amount) {
      this.defaultCosts = () -> CostInstance.add(herb.get(), amount);
      return this;
    }

    public Properties charge (ParentChargeType type) {
      this.chargeType = type;
      return this;
    }

    public Properties operations () {
      return charge(ParentChargeType.OPERATION);
    }

    public Properties build () {
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
      return this;
    }

    public <T> Properties component (Supplier<? extends DataComponentType<T>> component, T value) {
      return this.component(component.get(), value);
    }

    public <T> Properties component(DataComponentType<T> component, T value) {
      CommonHooks.validateComponent(value);
      if (this.components == null) {
        this.components = DataComponentMap.builder();
      }

      this.components.set(component, value);
      return this;
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
}
