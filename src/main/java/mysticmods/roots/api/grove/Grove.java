package mysticmods.roots.api.grove;

import mysticmods.roots.api.registry.IStyled;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class Grove implements IStyled {
  private Style style;
  private final ChatFormatting color;
  private String descriptionId;

  public Grove(ChatFormatting color) {
    this.color = color;
  }

  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId("grove", builtInRegistryHolder().getKey().location());
    }

    return this.descriptionId;
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

/*  public boolean aligned(Herb herb) {
    return herb.is(herbs);
  }

  public boolean aligned(Spell spell) {
    return spell.is(alignedSpells);
  }

  public boolean aligned(Grove grove) {
    return grove.is(alignedGroves);
  }

  public boolean aligned(Ritual ritual) {
    return ritual.is(alignedRituals);
  }

  public boolean opposed(Spell spell) {
    return spell.is(opposedSpells);
  }

  public boolean opposed(Grove grove) {
    return grove.is(opposedGroves);
  }

  public boolean opposed(Ritual ritual) {
    return ritual.is(opposedRituals);
  }*/

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
