package mysticmods.roots.api.grove;

import mysticmods.roots.api.RootsItemCallbacks;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.registry.IDataMapInitialize;
import mysticmods.roots.api.registry.IStyled;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class Grove implements IStyled, IDataMapInitialize<Grove> {
  private Style style;
  private final ChatFormatting color;
  private String descriptionId;

  private final ReputationRanks defaultReputationRanks = new ReputationRanks(1000, 5000, 15000, 30000);
  private ReputationRanks reputationRanks;

  private final int color1, color2;

  public Grove(ChatFormatting color, int color1, int color2) {
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

  @Override
  public void init(Holder<Grove> holder) {
    this.reputationRanks = holder.getData(DataMaps.GROVE_RANKS);
  }

  @Override
  @Nullable
  public ChatFormatting getTextColor() {
    return color;
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
