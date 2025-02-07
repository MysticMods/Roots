package mysticmods.roots.recipe.bark;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class OutputStateMapper {
  public static Codec<OutputStateMapper> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      Codec.unboundedMap(BuiltInRegistries.BLOCK.byNameCodec(), BuiltInRegistries.BLOCK.byNameCodec())
          .fieldOf("mapBlock").forGetter(OutputStateMapper::mapBlock)
  ).apply(instance, OutputStateMapper::new));
  public static StreamCodec<RegistryFriendlyByteBuf, OutputStateMapper> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.map(HashMap::new, ByteBufCodecs.registry(Registries.BLOCK), ByteBufCodecs.registry(Registries.BLOCK)), OutputStateMapper::mapBlock, OutputStateMapper::new);
  private final Map<Block, Block> mapBlock;

  public OutputStateMapper(Block... blocks) {
    if (blocks.length % 2 != 0) {
      throw new IllegalArgumentException("Invalid inputs for OutputStateMapper: need even number of blocks, got " + blocks.length);
    }
    this.mapBlock = new HashMap<>();
    for (int i = 0; i < blocks.length; i += 2) {
      mapBlock.put(blocks[i], blocks[i + 1]);
    }
  }

  public OutputStateMapper(Map<Block, Block> mapBlock) {
    this.mapBlock = mapBlock;
  }

  public Map<Block, Block> mapBlock() {
    return mapBlock;
  }

  @Nullable
  public Block get(Block block) {
    return mapBlock.get(block);
  }

  public OutputStateMapper copy() {
    return new OutputStateMapper(new HashMap<>(mapBlock));
  }

  public boolean isEmpty() {
    return mapBlock.isEmpty();
  }
}
