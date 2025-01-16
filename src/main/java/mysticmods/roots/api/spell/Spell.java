package mysticmods.roots.api.spell;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.SpellLike;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.registry.ICosted;
import mysticmods.roots.api.registry.IStyled;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
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
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public abstract class Spell implements IStyled, ICosted, SpellLike, TooltipComponent {
  public static final Codec<Spell> CODEC = RootsRegistries.SPELLS.byNameCodec();
  public static final StreamCodec<RegistryFriendlyByteBuf, Spell> STREAM_CODEC = ByteBufCodecs.registry(RootsRegistries.Keys.SPELLS);

  protected final Type type;
  protected final List<Cost> defaultCosts;
  protected final Set<SpellModifier> modifiers = new HashSet<>();
  protected List<Cost> costs;
  protected int cooldown = 0;
  protected double reach = 0.0;
  protected final int color1, color2;

  protected Style style;
  protected ChatFormatting textColor;
  protected String descriptionId;

  public Spell(Type type, ChatFormatting color, List<Cost> defaultCosts, int color1, int color2) {
    this.type = type;
    this.textColor = color;
    this.defaultCosts = defaultCosts;
    this.color1 = color1;
    this.color2 = color2;
  }

  public Holder<Spell> builtInRegistryHolder() {
    return RootsRegistries.SPELLS.wrapAsHolder(this);
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

  @Override
  public List<Cost> getDefaultCosts() {
    return defaultCosts;
  }

  @Override
  public List<Cost> getCosts() {
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

  public int getCooldown() {
    return cooldown;
  }

  public Type getType() {
    return type;
  }

  public void addModifier(SpellModifier modifier) {
    modifiers.add(modifier);
  }

  public List<PropertyHolder<?>> getProperties() {
    List<PropertyHolder<?>> properties = new ArrayList<>();
    if (getCooldownProperty() != null) {
      properties.add(getCooldownProperty());
    }
    if (getReachProperty() != null) {
      properties.add(getReachProperty());
    }
    return properties;
  }

  protected void initializeProperties(Holder<Spell> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.SPELL_PROPERTY_DATA);
    this.cooldown = properties.get(getCooldownProperty());
    if (getReachProperty() != null) {
      this.reach = properties.get(getReachProperty());
    }
  }

  public abstract void initialize(Holder<Spell> holder);

  public void init(Holder<Spell> holder) {
    costs = holder.getData(DataMaps.SPELL_COST_DATA);
    initializeProperties(holder);
    initialize(holder);
  }

  public abstract int cast(Level pLevel, Player pPlayer, ItemStack pStack, InteractionHand pHand, Costing costs, ISpellInstance instance, int ticks);

  // TODO: How to handle reach
  protected double getRange(Player pPlayer) {
    return 0.0; //pPlayer.getReachDistance() + reach;
  }

  protected BlockHitResult pick(Player pPlayer, double range) {
    return pick(pPlayer, range, false);
  }

  protected BlockHitResult pick(Player pPlayer, double range, boolean fluids) {
    return (BlockHitResult) pPlayer.pick(range, 1f, fluids);
  }

  protected BlockHitResult pick(Player pPlayer) {
    return pick(pPlayer, false);
  }

  protected BlockHitResult pick(Player pPlayer, boolean fluids) {
    return (BlockHitResult) pPlayer.pick(getRange(pPlayer), 1f, fluids);
  }

  // TODO: Pick entity
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

  public enum Type {
    INSTANT,
    CONTINUOUS
  }
}
