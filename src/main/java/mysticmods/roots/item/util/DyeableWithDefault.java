package mysticmods.roots.item.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;

import javax.annotation.Nullable;
import java.util.Optional;

public record DyeableWithDefault(@Nullable DyeColor color) {
  public static final MapCodec<DyeableWithDefault> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      DyeColor.CODEC.optionalFieldOf("color", null).forGetter(DyeableWithDefault::color)
  ).apply(instance, DyeableWithDefault::new));
  public static final StreamCodec<ByteBuf, DyeableWithDefault> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.optional(DyeColor.STREAM_CODEC), o -> Optional.ofNullable(o.color()), (o) -> DyeableWithDefault.fromColor(o.orElse(null)));
  public static final Codec<DyeableWithDefault> CODEC = MAP_CODEC.codec();

  public static final DyeableWithDefault DEFAULT = new DyeableWithDefault(null);
  public static final DyeableWithDefault WHITE = new DyeableWithDefault(DyeColor.WHITE);
  public static final DyeableWithDefault ORANGE = new DyeableWithDefault(DyeColor.ORANGE);
  public static final DyeableWithDefault MAGENTA = new DyeableWithDefault(DyeColor.MAGENTA);
  public static final DyeableWithDefault LIGHT_BLUE = new DyeableWithDefault(DyeColor.LIGHT_BLUE);
  public static final DyeableWithDefault YELLOW = new DyeableWithDefault(DyeColor.YELLOW);
  public static final DyeableWithDefault LIME = new DyeableWithDefault(DyeColor.LIME);
  public static final DyeableWithDefault PINK = new DyeableWithDefault(DyeColor.PINK);
  public static final DyeableWithDefault GRAY = new DyeableWithDefault(DyeColor.GRAY);
  public static final DyeableWithDefault LIGHT_GRAY = new DyeableWithDefault(DyeColor.LIGHT_GRAY);
  public static final DyeableWithDefault CYAN = new DyeableWithDefault(DyeColor.CYAN);
  public static final DyeableWithDefault PURPLE = new DyeableWithDefault(DyeColor.PURPLE);
  public static final DyeableWithDefault BLUE = new DyeableWithDefault(DyeColor.BLUE);
  public static final DyeableWithDefault BROWN = new DyeableWithDefault(DyeColor.BROWN);
  public static final DyeableWithDefault GREEN = new DyeableWithDefault(DyeColor.GREEN);
  public static final DyeableWithDefault RED = new DyeableWithDefault(DyeColor.RED);
  public static final DyeableWithDefault BLACK = new DyeableWithDefault(DyeColor.BLACK);

  public static DyeableWithDefault fromColor(@Nullable DyeColor color) {
    return switch (color) {
      case WHITE -> WHITE;
      case ORANGE -> ORANGE;
      case MAGENTA -> MAGENTA;
      case LIGHT_BLUE -> LIGHT_BLUE;
      case YELLOW -> YELLOW;
      case LIME -> LIME;
      case PINK -> PINK;
      case GRAY -> GRAY;
      case LIGHT_GRAY -> LIGHT_GRAY;
      case CYAN -> CYAN;
      case PURPLE -> PURPLE;
      case BLUE -> BLUE;
      case BROWN -> BROWN;
      case GREEN -> GREEN;
      case RED -> RED;
      case BLACK -> BLACK;
      // Note: see #1253. Mods may use mixins to inject into dyecolor, no default branch will cause a `MatchException` in that case.
      //noinspection UnnecessaryDefault
      case null, default -> DEFAULT;
    };
  }
}
