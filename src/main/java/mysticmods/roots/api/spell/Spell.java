package mysticmods.roots.api.spell;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.SpellLike;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.registry.*;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public abstract class Spell implements IStyled, ICosted, SpellLike, TooltipComponent, IDataMapInitialize<Spell> {
  public static final Codec<Spell> CODEC = RootsRegistries.SPELLS.byNameCodec();
  public static final StreamCodec<RegistryFriendlyByteBuf, Spell> STREAM_CODEC = ByteBufCodecs.registry(RootsRegistries.Keys.SPELLS);

  protected final Type type;
  protected final CostInstance defaultCosts;
  protected final Set<SpellModifier> modifiers = new HashSet<>();
  protected CostInstance costs;
  protected int cooldown = 0;
  protected double reach = 0.0;
  protected final int color1, color2;
  protected int maxUse;

  protected Style style;
  protected ChatFormatting textColor;
  protected String descriptionId;

  protected ItemStack icon;

  private final Object2IntMap<String> keyToDataIndex = new Object2IntOpenHashMap<>();
  private final Int2IntMap dataIndexMaximums = new Int2IntOpenHashMap();

  public Spell(Type type, ChatFormatting color, CostInstance defaultCosts, int color1, int color2) {
    this.type = type;
    this.textColor = color;
    this.defaultCosts = defaultCosts;
    this.color1 = color1;
    this.color2 = color2;
    fillDataKeyMap(keyToDataIndex);
    dataIndexMaximums.defaultReturnValue(-1);
  }

  public Holder<Spell> builtInRegistryHolder() {
    return RootsRegistries.SPELLS.wrapAsHolder(this);
  }

  public boolean hasDataSlot(int slot) {
    return slot >= 0 && slot <= keyToDataIndex.size();
  }

  public int getDataSlots() {
    // The 0 slot is always the index
    return keyToDataIndex.size() + 1;
  }

  // Contract: slot 0 is *always* the "mode" key.
  public int getDataSlotValue(ISpellInstance instance) {
    if (instance.getSpellData() == null) {
      return -1;
    }
    return instance.getSpellData().get(0);
  }

  public int getDataValue(ISpellInstance instance, String key) {
    if (instance.getSpellData() == null) {
      return -1;
    }
    return getDataValue(instance.getSpellData(), key);
  }

  public int getDataValue(SpellInstanceData data, String key) {
    int index = getDataIndex(key);
    if (index == -1 || !data.has(index)) {
      return -1;
    }
    return data.get(index);
  }

  public int getDataValue(ISpellInstance instance, int index) {
    if (instance.getSpellData() == null) {
      return -1;
    }
    return getDataValue(instance.getSpellData(), index);
  }

  public int getDataValue(SpellInstanceData data, int index) {
    if (!data.has(index)) {
      return -1;
    }
    return data.get(index);
  }

  public int getDataIndex(String key) {
    if (keyToDataIndex.containsKey(key)) {
      return keyToDataIndex.getInt(key);
    }
    return -1;
  }

  public int getDataMaximumValue(int index) {
    return dataIndexMaximums.get(index);
  }

  public Set<String> getDataKeys() {
    return keyToDataIndex.keySet();
  }

  public Set<String> getTooltipDataKeys() {
    return getDataKeys();
  }

  @Nullable
  public String getDataKey(int index) {
    for (Object2IntMap.Entry<String> entry : keyToDataIndex.object2IntEntrySet()) {
      if (entry.getIntValue() == index) {
        return entry.getKey();
      }
    }
    return null;
  }

  public Component describeData(int index, int value) {
    if (index != 0) {
      String keyName = getDataKey(index);
      if (keyName == null) {
        keyName = "unknown";
      }

      return Component.translatable(getOrCreateDescriptionId() + ".data." + keyName, value);
    } else {
      String mode = getDataKey(value);
      if (mode == null) {
        mode = "unknown";
      }

      return Component.translatable(getOrCreateDescriptionId() + ".data.mode." + mode);
    }
  }

  public int getDataMaximumValue(String key) {
    int index = getDataIndex(key);
    if (index == -1) {
      return -1;
    }
    return getDataMaximumValue(index);
  }

  protected void fillDataKeyMap(Object2IntMap<String> map) {
  }

  protected void fillDataMaximumValues(Int2IntMap map) {
  }

  @Override
  @Nullable
  public ChatFormatting getTextColor() {
    return textColor;
  }

  @Override
  public Style getOrCreateStyle() {
    if (style == null) {
      ChatFormatting color = getTextColor();
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

  public int getColor1() {
    return color1;
  }

  public int getColor2() {
    return color2;
  }

  public int getMaxUse() {
    if (maxUse == 0 && type == Type.CONTINUOUS) {
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

  public Set<SpellModifier> getModifiers() {
    return modifiers;
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

  public Type getType() {
    return type;
  }

  public void addModifier(SpellModifier modifier) {
    modifiers.add(modifier);
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
    icon = holder.getData(DataMaps.SPELL_DISPLAY_ITEM);
    if (icon == null || icon.isEmpty()) {
      RootsAPI.LOG.error("Icon is missing for spell: {}", holder.getKey());
      icon = ItemStack.EMPTY;
    }
    initializeProperties(holder);
    initialize(holder);
    fillDataMaximumValues(dataIndexMaximums);
    if (dataIndexMaximums.size() != keyToDataIndex.size()) {
      // TODO: Rampant growth breaks this contract?
      RootsAPI.LOG.error("Key-to-data index and data index maximum mismatch: {}", holder.getKey());
    }
  }

  public ItemStack getIcon() {
    return icon;
  }

  public abstract int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks);

  public Map<BlockPos, BlockState> getAffectedBlocks(Level level, Player player, ISpellInstance spell, ItemStack stack, BlockPos pos, BlockState blockState, BlockHitResult rayTraceResult) {
    return Collections.emptyMap();
  }

  protected double getRange(Player pPlayer) {
    return pPlayer.blockInteractionRange() + reach;
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
    return (BlockHitResult) pPlayer.pick(getRange(pPlayer), 1f, fluids);
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

  public enum Type {
    INSTANT,
    CONTINUOUS,
    CHARGED
  }

}
