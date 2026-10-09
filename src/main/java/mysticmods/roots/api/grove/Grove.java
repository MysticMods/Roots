package mysticmods.roots.api.grove;

import mysticmods.roots.api.RootsItemCallbacks;
import mysticmods.roots.api.condition.GroveType;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.registry.IDataMapInitialize;
import mysticmods.roots.api.registry.IStyled;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class Grove implements IStyled, IDataMapInitialize<Grove> {
  private Style style;
  private final TextColor color;
  private String descriptionId;

  private final ReputationRanks defaultReputationRanks = new ReputationRanks(1000, 2000, 3000, 4000);
  private ReputationRanks reputationRanks;

  private final int color1, color2;
  private final GroveType type;

  @Deprecated
  public Grove(GroveType type, ChatFormatting color, int color1, int color2) {
    this(type, TextColor.fromLegacyFormat(color), color1, color2);
  }

  public Grove(GroveType type, TextColor color, int color1, int color2) {
    this.type = type;
    this.color = color;
    this.color1 = color1;
    this.color2 = color2;
  }

  @Override
  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId("grove", builtInRegistryHolder().getKey().location());
    }

    return this.descriptionId;
  }

  public ReputationRanks getDefaultRanks() {
    return defaultReputationRanks;
  }

  public ReputationRanks getRanks() {
    if (reputationRanks == null) {
      return getDefaultRanks();
    }
    return reputationRanks;
  }

  public ItemStack getIcon() {
    return RootsItemCallbacks.getItemStack(this);
  }

  public int getColor1() {
    return color1;
  }

  public int getColor2() {
    return color2;
  }

  public TagKey<Grove> getGroveTag() {
    return type.grove();
  }

  public TagKey<Block> getBlockTag() {
    return type.tag();
  }

  public GroveType getType () {
    return this.type;
  }

  @Override
  public void init(Holder<Grove> holder) {
    this.reputationRanks = holder.getData(DataMaps.GROVE_RANKS);
  }

  @Override
  @Nullable
  public TextColor getTextColor() {
    return color;
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

  public Holder<Grove> builtInRegistryHolder() {
    return RootsRegistries.GROVES.wrapAsHolder(this);
  }

  public boolean is(ResourceLocation location) {
    return builtInRegistryHolder().is(location);
  }

  public boolean is(ResourceKey<Grove> key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean is(Predicate<ResourceKey<Grove>> predicate) {
    return builtInRegistryHolder().is(predicate);
  }

  public boolean is(TagKey<Grove> tag) {
    return builtInRegistryHolder().is(tag);
  }
}
