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
  public static final PillarType ACACIA = new PillarType("acacia", RootsTags.Blocks.ACACIA_PILLARS, RootsTags.Blocks.ACACIA_CAPSTONES);
  public static final PillarType BIRCH = new PillarType("birch", RootsTags.Blocks.BIRCH_PILLARS, RootsTags.Blocks.BIRCH_CAPSTONES);
  public static final PillarType DARK_OAK = new PillarType("dark_oak", RootsTags.Blocks.DARK_OAK_PILLARS, RootsTags.Blocks.DARK_OAK_CAPSTONES);
  public static final PillarType JUNGLE = new PillarType("jungle", RootsTags.Blocks.JUNGLE_PILLARS, RootsTags.Blocks.JUNGLE_CAPSTONES);
  public static final PillarType OAK = new PillarType("oak", RootsTags.Blocks.OAK_PILLARS, RootsTags.Blocks.OAK_CAPSTONES);
  public static final PillarType SPRUCE = new PillarType("spruce", RootsTags.Blocks.SPRUCE_PILLARS, RootsTags.Blocks.SPRUCE_CAPSTONES);
  public static final PillarType CRIMSON = new PillarType("crimson", RootsTags.Blocks.CRIMSON_PILLARS, RootsTags.Blocks.CRIMSON_CAPSTONES);
  public static final PillarType WARPED = new PillarType("warped", RootsTags.Blocks.WARPED_PILLARS, RootsTags.Blocks.WARPED_CAPSTONES);
  public static final PillarType WILDWOOD = new PillarType("wildwood", RootsTags.Blocks.WILDWOOD_PILLARS, RootsTags.Blocks.WILDWOOD_CAPSTONES);
  public static final PillarType MANGROVE = new PillarType("mangrove", RootsTags.Blocks.MANGROVE_PILLARS, RootsTags.Blocks.MANGROVE_CAPSTONES);
  public static final PillarType RUNESTONE = new PillarType("runestone", RootsTags.Blocks.RUNE_PILLARS, RootsTags.Blocks.RUNE_CAPSTONES);
  public static final PillarType RUNED_OBSIDIAN = new PillarType("runed_obsidian", RootsTags.Blocks.RUNED_PILLARS, RootsTags.Blocks.RUNED_CAPSTONES);
  public static final PillarType ANY_LOG = new PillarType("any_log", RootsTags.Blocks.LOG_PILLARS, RootsTags.Blocks.LOG_CAPSTONES);
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
