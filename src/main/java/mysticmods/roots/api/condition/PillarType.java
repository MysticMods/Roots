package mysticmods.roots.api.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public record PillarType(String name, TagKey<Block> pillar, TagKey<Block> capstone) {
  public static final PillarType RUNESTONE = new PillarType("runestone", RootsTags.Blocks.RUNE_PILLARS, RootsTags.Blocks.RUNE_CAPSTONES);
  public static final PillarType RUNED_OBSIDIAN = new PillarType("runed_obsidian", RootsTags.Blocks.RUNED_PILLARS, RootsTags.Blocks.RUNED_CAPSTONES);
  public static final PillarType ANY_RUNE = new PillarType("any_rune", RootsTags.Blocks.RUNES_PILLARS, RootsTags.Blocks.RUNES_CAPSTONES);
  public static final PillarType ANY = new PillarType("any", RootsTags.Blocks.PILLARS, RootsTags.Blocks.CAPSTONES);

  public static final MapCodec<PillarType> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      Codec.STRING.fieldOf("name").forGetter(PillarType::name),
      TagKey.codec(Registries.BLOCK).fieldOf("pillar").forGetter(PillarType::pillar),
      TagKey.codec(Registries.BLOCK).fieldOf("capstone").forGetter(PillarType::capstone)
  ).apply(instance, PillarType::new));
  public static final Codec<PillarType> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<RegistryFriendlyByteBuf, PillarType> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.STRING_UTF8, PillarType::name,
      ExtraStreamCodecs.BLOCK_TAG_STREAM_CODEC, PillarType::pillar,
      ExtraStreamCodecs.BLOCK_TAG_STREAM_CODEC, PillarType::capstone,
      PillarType::new
  );
}
