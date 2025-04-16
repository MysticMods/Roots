package mysticmods.roots.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;

import javax.annotation.Nullable;
import java.util.Optional;

public record Dyeable(@Nullable DyeColor color) {
  public static final MapCodec<Dyeable> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      DyeColor.CODEC.optionalFieldOf("color", null).forGetter(Dyeable::color)
  ).apply(instance, Dyeable::new));
  public static final StreamCodec<ByteBuf, Dyeable> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.optional(DyeColor.STREAM_CODEC), o -> Optional.ofNullable(o.color()), (o ) -> Dyeable.fromColor(o.orElse(null)));
  public static final Codec<Dyeable> CODEC = MAP_CODEC.codec();

  public static final Dyeable DEFAULT = new Dyeable(null);
  public static final Dyeable WHITE = new Dyeable(DyeColor.WHITE);
  public static final Dyeable ORANGE = new Dyeable(DyeColor.ORANGE);
  public static final Dyeable MAGENTA = new Dyeable(DyeColor.MAGENTA);
  public static final Dyeable LIGHT_BLUE = new Dyeable(DyeColor.LIGHT_BLUE);
  public static final Dyeable YELLOW = new Dyeable(DyeColor.YELLOW);
  public static final Dyeable LIME = new Dyeable(DyeColor.LIME);
  public static final Dyeable PINK = new Dyeable(DyeColor.PINK);
  public static final Dyeable GRAY = new Dyeable(DyeColor.GRAY);
  public static final Dyeable LIGHT_GRAY = new Dyeable(DyeColor.LIGHT_GRAY);
  public static final Dyeable CYAN = new Dyeable(DyeColor.CYAN);
  public static final Dyeable PURPLE = new Dyeable(DyeColor.PURPLE);
  public static final Dyeable BLUE = new Dyeable(DyeColor.BLUE);
  public static final Dyeable BROWN = new Dyeable(DyeColor.BROWN);
  public static final Dyeable GREEN = new Dyeable(DyeColor.GREEN);
  public static final Dyeable RED = new Dyeable(DyeColor.RED);
  public static final Dyeable BLACK = new Dyeable(DyeColor.BLACK);

  public static Dyeable fromColor(@Nullable DyeColor color) {
    return switch (color) {
      case null -> DEFAULT;
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
    };
  }
}
