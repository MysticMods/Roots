package mysticmods.roots.item;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModItems;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;
import java.util.function.IntFunction;

public class GramaryItem extends Item {
  public GramaryItem(Properties properties) {
    super(properties);
  }

  public static GramaryMode getMode (ItemStack item) {
    if (item.has(ModAttachments.GRAMARY_MODE)) {
      return item.get(ModAttachments.GRAMARY_MODE);
    }

    return GramaryMode.NONE;
  }

  public enum GramaryMode implements StringRepresentable {
    NONE,
    ENTITY_INFO,
    BLOCK_ENTITY_INFO;

    public static final Codec<GramaryMode> CODEC = StringRepresentable.fromEnum(GramaryMode::values);
  public static final IntFunction<GramaryMode> BY_ID = ByIdMap.continuous(GramaryMode::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
  public static final StreamCodec<ByteBuf, GramaryMode> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, GramaryMode::ordinal);

    @Override
    public String getSerializedName() {
      return this.name().toLowerCase(Locale.ROOT);
    }
  }
}
