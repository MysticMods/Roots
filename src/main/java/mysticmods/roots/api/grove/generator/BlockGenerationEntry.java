package mysticmods.roots.api.grove.generator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.grove.Symmetry;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.List;

public record BlockGenerationEntry(TagKey<Block> tag, int maxCount, Symmetry symmetry) implements GenerationEntry<Block> {
  public static final MapCodec<BlockGenerationEntry> MAP_CODEC = GenerationEntry.mapCodec(Registries.BLOCK, BlockGenerationEntry::new);
  public static final Codec<BlockGenerationEntry> CODEC = MAP_CODEC.codec();
  public static final Codec<List<BlockGenerationEntry>> LIST_CODEC = CODEC.listOf();
}
