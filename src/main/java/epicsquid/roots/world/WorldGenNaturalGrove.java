package epicsquid.roots.world;

import java.util.Random;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.entity.grove.EntityNaturalGrove;
import epicsquid.roots.entity.grove.EntityWildGrove;
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

  @Override
  public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
    if(random.nextInt(100) != 0){
      return;
    }
    if (world.provider.getDimension() == DimensionType.OVERWORLD.getId() && !world.isRemote) {

      int xx = chunkX * 16 + 13 + Util.rand.nextInt(6);
      int zz = chunkZ * 16 + 13 + Util.rand.nextInt(6);

      BlockPos blockpos = new BlockPos(xx, 100, zz);
      Biome biome = world.getBiomeForCoordsBody(blockpos);

      if(biome == Biomes.FOREST || biome == Biomes.BIRCH_FOREST || biome == Biomes.PLAINS  ){
        WorldServer worldserver = (WorldServer) world;
        MinecraftServer minecraftserver = world.getMinecraftServer();
        TemplateManager templatemanager = worldserver.getStructureTemplateManager();
        ResourceLocation loc = new ResourceLocation(Roots.MODID,"natural_grove");
        Template template = templatemanager.getTemplate(minecraftserver, loc);
        PlacementSettings placementsettings = (new PlacementSettings()).setMirror(Mirror.NONE)
            .setRotation(Rotation.NONE).setIgnoreEntities(false).setChunk(null)
            .setReplacedBlock(Blocks.AIR).setIgnoreStructureBlock(false);
        BlockPos pos = world.getTopSolidOrLiquidBlock(blockpos);
        blockpos = pos;
        template.addBlocksToWorld(world, pos.add(-8, -1, -9f), placementsettings);
        IBlockState iblockstate = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);

        //Spawn entity grove
        EntityNaturalGrove naturalGrove = new EntityNaturalGrove(world);
        naturalGrove.setPosition(blockpos.getX() +0.5f, blockpos.getY() + 2, blockpos.getZ() +0.5f);
        world.spawnEntity(naturalGrove);
      }
    }
  }


}
