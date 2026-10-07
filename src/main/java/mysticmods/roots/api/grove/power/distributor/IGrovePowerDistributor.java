package mysticmods.roots.api.grove.power.distributor;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public interface IGrovePowerDistributor {
  int getMaxPower();

  int getUsedPower();

  void generateTick(ServerLevel level, BlockPos pos, BlockState state);

  void consumeTick(ServerLevel level, BlockPos pos, BlockState state);
}
