package mysticmods.roots.spell.mode;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.Cycling;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;
import java.util.function.IntFunction;

public enum AOEGrowthMode implements Cycling<AOEGrowthMode>, StringRepresentable {
  // TODO: Encode properly
  EVERYTHING(ChatFormatting.GREEN.getColor()),
  IGNORE_TAGGED(ChatFormatting.LIGHT_PURPLE.getColor()),
  HELD_IN_OFFHAND(ChatFormatting.AQUA.getColor());

  public static final Codec<AOEGrowthMode> CODEC = StringRepresentable.fromEnum(AOEGrowthMode::values);
  public static final IntFunction<AOEGrowthMode> BY_ID = ByIdMap.continuous(AOEGrowthMode::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
  public static final StreamCodec<ByteBuf, AOEGrowthMode> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, AOEGrowthMode::ordinal);

  private final TextColor textColor;
  private Style style;
  private String descriptionId;

  AOEGrowthMode(int color) {
    this.textColor = TextColor.fromRgb(color);
  }

  @Override
  public TextColor getTextColor() {
    return this.textColor;
  }

  @Override
  public Style getOrCreateStyle() {
    if (this.style == null) {
      this.style = Style.EMPTY.withColor(getTextColor());
    }
    return this.style;
  }

  @Override
  public String getOrCreateDescriptionId() {
    if (descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId("spell_mode", RootsAPI.rl(getSerializedName()));
    }
    return this.descriptionId;
  }

  @Override
  public String getSerializedName() {
    return name().toLowerCase(Locale.ROOT);
  }

  @Override
  public AOEGrowthMode[] valuesInternal() {
    return values();
  }
}
