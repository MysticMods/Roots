package epicsquid.roots.world;

import java.util.Random;

import epicsquid.mysticallib.util.Util;
import epicsquid.mysticalworld.entity.EntityBeetle;
import epicsquid.mysticalworld.entity.EntityDeer;
import epicsquid.mysticalworld.entity.EntityFox;
import epicsquid.roots.entity.grove.EntityWildGrove;
import epicsquid.roots.entity.spell.EntityFireJet;
import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenWildlandGrove extends StructureBase implements IWorldGenerator {

  public WorldGenWildlandGrove() {
    super(12, 12);
    addBlockMapping("W", Blocks.WATER.getDefaultState());
    addBlockMapping("D", Blocks.DIRT.getDefaultState());
    addBlockMapping("G", Blocks.GRASS.getDefaultState());
    addBlockMapping("S", Blocks.REEDS.getDefaultState());
    addBlockMapping(" ", Blocks.AIR.getDefaultState());
    addBlockMapping("O", Blocks.TALLGRASS.getStateFromMeta(1));
    addBlockMapping("A", ModBlocks.structure_marker.getDefaultState());
    addBlockMapping("Y", Blocks.YELLOW_FLOWER.getDefaultState());
    addLayer(new String[]{
        "DDDDDDDDDDDD",
        "DDDDDDDDDDDD",
        "DDDDDDDDDDDD",
        "DDDDDDDDDDDD",
        "DDDDDDDDDDDD",
        "DDDDDDDDDDDD",
        "DDDDDDDDDDDD",
        "DDDDDDDDDDDD",
        "DDDDDDDDDDDD",
        "DDDDDDDDDDDD",
        "DDDDDDDDDDDD",
        "DDDDDDDDDDDD",
    });
    addLayer(new String[]{
        "DDDDDDDDDDDD",
        "DDDDDDDDDDDD",
        "DDDDDDDDDDDD",
        "DDDDDDWWDDDD",
        "DDDDDWWWWDDD",
        "DDDDWWWWWDDD",
        "DDDWWWWDDDDD",
        "DDDDWWWDDDDD",
        "DDDDDDDDDDDD",
        "DDDDDDDDDDDD",
        "DDDDDDDDDDDD",
        "DDDDDDDDDDDD",
    });
    addLayer(new String[]{
        "DDDDDDDDDDDD",
        "DDDDDDDDDDDD",
        "DDDDDDWWDDDD",
        "DDDDDWWWWDDD",
        "DDDDWWWWWWDD",
        "DDDWWWWWWWDD",
        "DDDWWWWWWDDD",
        "DDDDWWWWWDDD",
        "DDDDDWWWDDDD",
        "DDDDDDDDDDDD",
        "DDDDDDDDDDDD",
        "DDDDDDDDDDDD",
    });
    addLayer(new String[]{
        "GGGGGGGGGGGG",
        "GGGGGGWWGGGG",
        "GGGGWWWWWGGG",
        "GGGGWWWWWWGG",
        "GGGWWWWWWWWG",
        "GWWWWWWWWWWG",
        "GGWWWWWWWWGG",
        "GGGWWWWWWWGG",
        "GGGGWWWWWGGG",
        "GGGGGWWWGGGG",
        "GGGGGGGGGGGG",
        "GGGGGGGGGGGG",
    });
    addLayer(new String[]{
        "  OYOOOOOO  ",
        " YOOOOAAOOY ",
        "OOOSAAAAAYOO",
        "OYOOAAAAAAOO",
        "OOOAAAAAAAAS",
        "OAAAAAAAAAAY",
        "OSAAAAAAAAOO",
        "OOOAAAAAAAOO",
        "OYOOAAAAAOOO",
        "OOOOOAAASOOO",
        " OYOOOOOYYO ",
        "  OOOYOOOO  ",
    });
  }

  @Override
  public void placeBlock(World world, BlockPos pos, IBlockState state){
    super.placeBlock(world, pos, state);
    if (state.getBlock() == epicsquid.roots.init.ModBlocks.structure_marker){
      world.setBlockToAir(pos);
    }
  }

  @Override
  public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
    if(random.nextInt(10) != 0){
      return;
    }
    if (world.provider.getDimension() == DimensionType.OVERWORLD.getId() && !world.isRemote){
      int xx = chunkX*16 + 13 + Util.rand.nextInt(6);
      int zz = chunkZ*16 + 13 + Util.rand.nextInt(6);
      if ((Math.abs(xx) > 500 || Math.abs(zz) > 500)){
        int height = world.getHeight(xx, zz)-1;
        if (height > 0 && world.getBlockState(new BlockPos(xx,height,zz)).getBlock() instanceof BlockGrass){
          boolean canGenerate = true;
          for (int i = -5; i < 6; i ++){
            for (int j = -5; j < 6; j ++){
              if (world.getBlockState(new BlockPos(xx+i,height,zz+j)).getBlock() != Blocks.GRASS){
                canGenerate = false;
              }
            }
          }
          if (canGenerate){
            System.out.println("Generate wildlands: " + xx + " " + zz);
            this.generateIn(world, xx, height-3, zz);
            spawnEntities(world, random, xx, zz, height);
            EntityWildGrove wildGrove = new EntityWildGrove(world);
            wildGrove.setPosition(xx, height + 6, zz);
            world.spawnEntity(wildGrove);
          }
        }
      }
    }
  }

  private void spawnEntities(World world, Random rand, int xx, int zz, int height){
    EntityBeetle beetle1 = new EntityBeetle(world);
    EntityDeer deer1 = new EntityDeer(world);
    EntityFox fox1 = new EntityFox(world);
    EntityBeetle beetle2 = new EntityBeetle(world);
    EntityDeer deer2 = new EntityDeer(world);
    EntityFox fox2 = new EntityFox(world);

    beetle1.setPosition(xx + (rand.nextInt(12) - 6), height+1, zz + (rand.nextInt(12) - 6));
    deer1.setPosition(xx + (rand.nextInt(12) - 6), height+1, zz + (rand.nextInt(12) - 6));
    fox1.setPosition(xx + (rand.nextInt(12) - 6), height+1, zz + (rand.nextInt(12) - 6));
    beetle2.setPosition(xx + (rand.nextInt(12) - 6), height+1, zz + (rand.nextInt(12) - 6));
    deer2.setPosition(xx + (rand.nextInt(12) - 6), height+1, zz + (rand.nextInt(12) - 6));
    fox2.setPosition(xx + (rand.nextInt(12) - 6), height+1, zz + (rand.nextInt(12) - 6));

    world.spawnEntity(beetle1);
    world.spawnEntity(deer1);
    world.spawnEntity(fox1);
    world.spawnEntity(beetle2);
    world.spawnEntity(deer2);
    world.spawnEntity(fox2);
  }
}