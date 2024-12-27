package mysticmods.roots.worldgen.structure;

import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModFeatures;
import mysticmods.roots.util.GrowthUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;

public class StandingStonePiece extends ScatteredFeaturePiece {
  public StandingStonePiece(WorldgenRandom worldgenRandom, int pX, int pZ) {
    super(ModFeatures.STANDING_STONES_PIECE.get(), pX, 64, pZ, 12, 10, 12, getRandomHorizontalDirection(worldgenRandom));
  }

  public StandingStonePiece(CompoundTag pTag) {
    super(ModFeatures.STANDING_STONES_PIECE.get(), pTag);
  }

  @Override
  public void postProcess(WorldGenLevel pLevel, StructureManager pStructureManager, ChunkGenerator pGenerator, RandomSource pRandom, BoundingBox pBox, ChunkPos pChunkPos, BlockPos pPos) {
    if (this.updateAverageGroundHeight(pLevel, pBox, 0)) {
      Set<PillarSpot> pillarSpots = new ObjectLinkedOpenHashSet<>();
      int x = pPos.getX(); //pChunkPos.x * 16 + pRandom.nextInt(6);
      int z = pPos.getZ(); //pChunkPos.z * 16 + pRandom.nextInt(6);
      for (int i = 0; i < 360; i += 36) {
        if (pRandom.nextBoolean()) {
          int height = pRandom.nextInt(3) + 4;
          int tx = (int) (x + 5.0 * Mth.sin((float) Math.toRadians(i)));
          int tz = (int) (z + 5.0 * Mth.cos((float) Math.toRadians(i)));
          BlockPos pos = new BlockPos(tx, pLevel.getHeight(Heightmap.Types.WORLD_SURFACE_WG, tx, tz), tz);
          if (pLevel.getBlockState(pos.below()).is(BlockTags.DIRT)) {
            for (int j = 0; j < height; j++) {
              pLevel.setBlock(pos.offset(0, j, 0), j == height - 1 ? ModBlocks.CHISELED_RUNESTONE.getDefaultState() : pRandom.nextFloat() < 0.4f ? ModBlocks.MOSSY_RUNESTONE.getDefaultState() : ModBlocks.RUNESTONE.getDefaultState(), 2);
            }
            pillarSpots.add(new PillarSpot(tx, tz));
          }
        }
      }
      BlockPos center = new BlockPos(x, pLevel.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z), z).below();
      if (pLevel.getBlockState(center).is(BlockTags.DIRT)) {
        BlockPos chestPos = center.offset(0, -2, 0);
        pLevel.setBlock(chestPos, Blocks.CHEST.defaultBlockState(), 2);
        if (pLevel.getBlockEntity(chestPos) instanceof RandomizableContainerBlockEntity lootChest) {
          lootChest.setLootTable(RootsAPI.rl("standing_stones"), pRandom.nextLong());
        }
      }
      for (int i = 0; i < 5; i++) {
        BlockPos offset = center.offset(pRandom.nextInt(-4, 4), 0, pRandom.nextInt(-4, 4));
        if (pillarSpots.contains(new PillarSpot(offset.getX(), offset.getZ())) || center.equals(offset)) {
          continue;
        }
        BlockPos pillarSpot = new BlockPos(offset.getX(), pLevel.getHeight(Heightmap.Types.WORLD_SURFACE_WG, offset.getX(), offset.getZ()), offset.getZ());
        if (pLevel.getBlockState(pillarSpot.below()).is(BlockTags.DIRT)) {
          ForgeRegistries.BLOCKS.tags().getTag(RootsTags.Blocks.STANDING_STONE_CROPS).getRandomElement(pRandom).ifPresent(block -> {
            pLevel.setBlock(pillarSpot.below(), Blocks.FARMLAND.defaultBlockState(), 2);
            BlockState newState = block.defaultBlockState();
            GrowthUtil.CropData cropData = GrowthUtil.getCropData(newState);
            if (cropData.isEmpty()) {
              pLevel.setBlock(pillarSpot, newState, 2);
            } else {
              pLevel.setBlock(pillarSpot, newState.setValue(cropData.ageProperty(), cropData.maxAge()), 2);
            }
          });
        }
      }

      ForgeRegistries.BLOCKS.tags().getTag(BlockTags.SMALL_FLOWERS).getRandomElement(pRandom).ifPresent(block -> {
        pLevel.setBlock(center.above(), block.defaultBlockState(), 2);
      });
    }
  }

  private record PillarSpot (int x, int z) {
    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      PillarSpot that = (PillarSpot) o;

      if (x != that.x) return false;
      return z == that.z;
    }

    @Override
    public int hashCode() {
      int result = x;
      result = 31 * result + z;
      return result;
    }
  };
}
