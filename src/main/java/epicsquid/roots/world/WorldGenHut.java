package epicsquid.roots.world;

import java.util.Random;

import epicsquid.mysticallib.network.MessageTEUpdate;
import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.NoiseGenUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.ConfigManager;
import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenHut extends StructureBase implements IWorldGenerator {
  public WorldGenHut(){
    super(7,7);
    addBlockMapping("L",Blocks.LOG.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.X));
    addBlockMapping("l",Blocks.LOG.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Z));
    addBlockMapping("W",Blocks.LOG.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y));
    addBlockMapping(" ",Blocks.AIR.getDefaultState());
    addBlockMapping("F",Blocks.OAK_FENCE.getDefaultState());
    addBlockMapping("T", epicsquid.mysticalworld.init.ModBlocks.thatch.getDefaultState());
    addBlockMapping("P",Blocks.FLOWER_POT.getDefaultState().withProperty(BlockFlowerPot.CONTENTS, BlockFlowerPot.EnumFlowerType.ORANGE_TULIP));
    addBlockMapping("M", ModBlocks.mortar.getDefaultState());
    addBlockMapping("C",Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.WEST));
    addBlockMapping("c",Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.EAST));
    addBlockMapping("K",Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.NORTH));
    addLayer(new String[]{
        "       ",
        "  LlL  ",
        " LlLLl ",
        " lLllL ",
        " LlLLl ",
        "  lLl  ",
        "       "
    });
    addLayer(new String[]{
        "       ",
        "  lLL  ",
        " lWWWl ",
        " lWWWL ",
        " LWWWl ",
        "  lLL  ",
        "       "
    });
    addLayer(new String[]{
        "       ",
        "  WWW  ",
        " WcKPW ",
        "     W ",
        " WCCMW ",
        "  WWW  ",
        "       "
    });
    addLayer(new String[]{
        " TTTTT ",
        "  WWW  ",
        " W   W ",
        "     W ",
        " W   W ",
        "  WWW  ",
        " TTTTT "
    });
    addLayer(new String[]{
        "       ",
        "TTTTTTT",
        " W   W ",
        " FW WF ",
        " W   W ",
        "TTTTTTT",
        "       "
    });
    addLayer(new String[]{
        "       ",
        "       ",
        "TTTTTTT",
        " W   W ",
        "TTTTTTT",
        "       ",
        "       "
    });
    addLayer(new String[]{
        "       ",
        "       ",
        "       ",
        "TTTTTTT",
        "       ",
        "       ",
        "       "
    });
  }

  @Override
  public void placeBlock(World world, BlockPos pos, IBlockState state){
    super.placeBlock(world, pos, state);
    if (state.getBlock() == Blocks.CHEST){
      if (world.getTileEntity(pos) != null){
        TileEntityChest chest = (TileEntityChest)world.getTileEntity(pos);
        chest.setLootTable(new ResourceLocation("roots:hut"), NoiseGenUtil.getSeed((int)world.getSeed(), pos.getX(), pos.getZ()));
        PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(chest.getUpdateTag()));
        chest.markDirty();
      }
    }
  }

  @Override
  public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
      IChunkProvider chunkProvider) {
    if (ConfigManager.worldGen.hutChance <= 0){
      return;
    }
    if (world.provider.getDimension() == DimensionType.OVERWORLD.getId() && !world.isRemote){
      int xx = chunkX*16 + 12 + Util.rand.nextInt(8);
      int zz = chunkZ*16 + 12 + Util.rand.nextInt(8);
      if (random.nextInt(ConfigManager.worldGen.hutChance) == 0){
        int height = world.getHeight(xx, zz)-1;
        if (height > 0 && world.getBlockState(new BlockPos(xx,height,zz)).getBlock() instanceof BlockGrass){
          boolean canGenerate = true;
          for (int i = -2; i < 3; i ++){
            for (int j = -2; j < 3; j ++){
              if (world.getBlockState(new BlockPos(xx+i,height,zz+j)).getBlock() == Blocks.AIR){
                canGenerate = false;
              }
            }
          }
          for (int i = -4; i < 5; i ++){
            for (int j = -4; j < 5; j ++){
              if (world.getBlockState(new BlockPos(xx+i,height+3,zz+j)).getBlock() != Blocks.AIR){
                canGenerate = false;
              }
            }
          }
          if (canGenerate){
            this.generateIn(world, xx, height-1, zz);
            for (int i = 0; i < 20; i ++){
              int tx = xx+(random.nextInt(20)-10);
              int tz = zz+(random.nextInt(20)-10);
              int ty = world.getHeight(xx, zz)-1;
              if (world.getBlockState(new BlockPos(tx,ty,tz)).getBlock() instanceof BlockGrass){
                if (world.getBlockState(new BlockPos(tx,ty+1,tz)).getBlock().isReplaceable(world, new BlockPos(tx,ty+1,tz))){
                  world.setBlockState(new BlockPos(tx,ty+1,tz), Blocks.RED_FLOWER.getStateFromMeta(random.nextInt(9)));
                }
              }
            }
          }
        }
      }
    }
  }

}