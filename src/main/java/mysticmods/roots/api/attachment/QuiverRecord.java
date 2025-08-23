package mysticmods.roots.api.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.UUID;

public record QuiverRecord (UUID quiverId, int slotId) {
  public static final MapCodec<QuiverRecord> MAP_CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
          UUIDUtil.CODEC.fieldOf("quiver_id").forGetter(QuiverRecord::quiverId),
          Codec.INT.fieldOf("slot_id").forGetter(QuiverRecord::slotId)
      ).apply(instance, QuiverRecord::new)
  );
  public static final Codec<QuiverRecord> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<ByteBuf, QuiverRecord> STREAM_CODEC = StreamCodec.composite(
      UUIDUtil.STREAM_CODEC,
      QuiverRecord::quiverId,
      ByteBufCodecs.INT,
      QuiverRecord::slotId,
      QuiverRecord::new
  );
}
