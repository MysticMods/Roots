package mysticmods.roots.api.grove;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public interface GrovePowerGenerator {
  int getMaxPower();

  int getUsedPower();

  void generateTick(ServerLevel level, BlockPos pos, BlockState state);

  void consumeTick(ServerLevel level, BlockPos pos, BlockState state);

  static Set<Block> getAllBlocks(TagKey<Block> tag) {
    Set<Block> blocks = new ObjectOpenHashSet<>();
    for (Holder<Block> holder : BuiltInRegistries.BLOCK.getTagOrEmpty(tag)) {
      blocks.add(holder.value());
    }
    return blocks;
  }
}
