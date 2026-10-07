package mysticmods.roots.api.grove.power.generator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import mysticmods.roots.api.grove.Symmetry;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.List;

public record BlockGenerationInfo(TagKey<Block> tag, int maxCount, Symmetry symmetry) implements GenerationInfo<Block> {
  public static final MapCodec<BlockGenerationInfo> MAP_CODEC = GenerationInfo.mapCodec(Registries.BLOCK, BlockGenerationInfo::new);
  public static final Codec<BlockGenerationInfo> CODEC = MAP_CODEC.codec();
  public static final Codec<List<BlockGenerationInfo>> LIST_CODEC = CODEC.listOf();
}
