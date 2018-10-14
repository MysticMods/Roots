package epicsquid.roots.world;

import java.util.Random;

import javax.vecmath.Vector2d;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.entity.grove.EntityNaturalGrove;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenNaturalGrove implements IWorldGenerator {

  private Vector2d lastChunkGenerated = new Vector2d(0, 0);

  @Override
  public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
    if(random.nextInt(70) != 0){
      return;
    }
    if (!isNumberAwayOf(5, chunkX, (int) this.lastChunkGenerated.x) || !isNumberAwayOf(5, chunkZ, (int) this.lastChunkGenerated.y)) {
      System.out.println("what");
      return;
    }
    if (world.provider.getDimension() == DimensionType.OVERWORLD.getId() && !world.isRemote) {

      //Generate random coordinate in the chunk
      int xx = chunkX * 16 + 13 + Util.rand.nextInt(6);
      int zz = chunkZ * 16 + 13 + Util.rand.nextInt(6);

      //Get the block to spawn the structure at.
      BlockPos blockpos = new BlockPos(xx, 100, zz);
      Biome biome = world.getBiomeForCoordsBody(blockpos);
      blockpos = world.getTopSolidOrLiquidBlock(blockpos);

      //Check if there is enough ground for the structure to spawn on;
      BlockPos checkForGroundPos = blockpos.add(0, -1, 0);
      int airBlocks = 0;
      for(int x = -2; x < 3; x++){
        for(int z = -2; z < 3; z++){
          Block checkBlock = world.getBlockState(checkForGroundPos.add(x, 0, z)).getBlock();
          System.out.println(checkBlock);
          if(checkBlock != Blocks.DIRT && checkBlock != Blocks.GRASS && checkBlock != Blocks.TALLGRASS && checkBlock != Blocks.STONE && checkBlock != Blocks.SAND){
            airBlocks++;
          }
          if(airBlocks > 3){
            System.out.println("Returning");
            return;
          }
        }
      }

      System.out.println("Building");

      //Check for biomes
      if(biome == Biomes.FOREST || biome == Biomes.BIRCH_FOREST || biome == Biomes.PLAINS  ){
        this.lastChunkGenerated = new Vector2d(chunkX, chunkZ);

        WorldServer worldserver = (WorldServer) world;
        MinecraftServer minecraftserver = world.getMinecraftServer();
        TemplateManager templatemanager = worldserver.getStructureTemplateManager();
        ResourceLocation loc = new ResourceLocation(Roots.MODID,"natural_grove");
        Template template = templatemanager.getTemplate(minecraftserver, loc);
        PlacementSettings placementsettings = (new PlacementSettings()).setMirror(Mirror.NONE)
            .setRotation(Rotation.NONE).setIgnoreEntities(false).setChunk(null)
            .setReplacedBlock(null).setIgnoreStructureBlock(false);

        template.addBlocksToWorld(world, blockpos.add(-8, -1, -9f), placementsettings);
        IBlockState iblockstate = world.getBlockState(blockpos);
        world.notifyBlockUpdate(blockpos, iblockstate, iblockstate, 3);

        System.out.println(blockpos);

        //Spawn entity grove
        EntityNaturalGrove naturalGrove = new EntityNaturalGrove(world);
        naturalGrove.setPosition(blockpos.getX() +0.5f, blockpos.getY() + 2, blockpos.getZ() +0.5f);
        world.spawnEntity(naturalGrove);
      }
    }
  }

  private boolean isNumberAwayOf(int difference, int number1, int number2) {
    if (number1 > number2) {
      return number1 - number2 > difference;
    } else {
      return number2 - number1 > 5;
    }

  }


}
