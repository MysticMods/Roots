package mysticmods.roots.api.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Set;

public record UniqueReputation (ResourceLocation grove, ResourceLocation id) {
  public static final MapCodec<UniqueReputation> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      ResourceLocation.CODEC.fieldOf("grove").forGetter(UniqueReputation::grove),
      ResourceLocation.CODEC.fieldOf("id").forGetter(UniqueReputation::id)
  ).apply(instance, UniqueReputation::new));
  public static final Codec<UniqueReputation> CODEC = MAP_CODEC.codec();
  public static final Codec<Set<UniqueReputation>> SET_CODEC = CODEC.listOf().xmap(ObjectOpenHashSet::new, ArrayList::new);
  public static final StreamCodec<FriendlyByteBuf, UniqueReputation> STREAM_CODEC = StreamCodec.composite(ResourceLocation.STREAM_CODEC, UniqueReputation::grove, ResourceLocation.STREAM_CODEC, UniqueReputation::id, UniqueReputation::new);
  public static final StreamCodec<FriendlyByteBuf, Set<UniqueReputation>> SET_STREAM_CODEC = UniqueReputation.STREAM_CODEC.apply(ByteBufCodecs.list()).map(ObjectOpenHashSet::new, ArrayList::new);
}
