package epicsquid.roots.world;

import java.util.Random;

import epicsquid.mysticallib.network.MessageTEUpdate;
import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.NoiseGenUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.ConfigManager;
import epicsquid.roots.entity.world.EntityBarrow;
import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockStoneSlab;
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

public class WorldGenBarrow extends StructureBase implements IWorldGenerator {
  public WorldGenBarrow() {
    super(9, 9);

    /*addBlockMapping("c", Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.COBBLESTONE));
    addBlockMapping("C", Blocks.COBBLESTONE.getDefaultState());
    addBlockMapping("T", ModBlocks.thatch.getDefaultState());
    addBlockMapping("W", Blocks.COBBLESTONE_WALL.getDefaultState());
    addBlockMapping("M", Blocks.MOSSY_COBBLESTONE.getDefaultState());
    addBlockMapping(" ", Blocks.AIR.getDefaultState());
    addBlockMapping("A", epicsquid.roots.init.ModBlocks.structure_marker.getDefaultState());
    addBlockMapping("L", Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.WEST));
    addBlockMapping("l", Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.EAST));
    addLayer(new String[] { "   MMM   ", "  CMMMM  ", " MMCMMCM ", "MMCCCMCCM", "MCCCCMCMC", "MMCMCMMCM", " MCMCMMM ", "  MCCCM  ", "   CMM   " });
    addLayer(new String[] { "   MMM   ", "  ClAAM  ", " MAAAAlM ", "MAAAAAAAM", "MAAAWAAAC", "MLAAAAAAM", " MAAAAAM ", "  MAALM  ", "   CMM   " });
    addLayer(new String[] { "   CCM   ", "  CAAAM  ", " MAAAAAM ", "CAAAAAAAC", "MAAAWAAAC", "CAAAAAAAC", " CAAAAAM ", "  CAAAC  ", "   CCM   " });
    addLayer(new String[] { "   CCC   ", "  CAAAC  ", " CAAAAAC ", "CAAAWAAAC", "CAAWWWAAC", "CAAAWAAAC", " CAAAAAC ", "  CAAAC  ", "   CCC   " });
    addLayer(new String[] { "   cCc   ", "  cTCTc  ", " cTTCAAc ", "cTTTCTATc", "CCCCACCCC", "cTTTCTATc", " cTTCTAc ", "  cACTc  ", "   cCc   " });
    addLayer(new String[] { "         ", "    c    ", "    c    ", "    c    ", " ccc ccc ", "    c    ", "    c    ", "    c    ", "         " });*/
  }

  @Override
  public void placeBlock(World world, BlockPos pos, IBlockState state) {
    super.placeBlock(world, pos, state);
    if (state.getBlock() == epicsquid.roots.init.ModBlocks.structure_marker) {
      world.setBlockToAir(pos);
    }
    if (state.getBlock() == Blocks.CHEST) {
      if (world.getTileEntity(pos) != null) {
        TileEntityChest chest = (TileEntityChest) world.getTileEntity(pos);
        chest.setLootTable(new ResourceLocation("roots:barrow"), NoiseGenUtil.getSeed((int) world.getSeed(), pos.getX(), pos.getZ()));
        PacketHandler.sendToAllTracking(new MessageTEUpdate(chest.getUpdateTag()), chest);
        chest.markDirty();
      }
    }
  }

  @Override
  public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
    if (ConfigManager.worldGen.barrowChance <= 0) {
      return;
    }
    if (world.provider.getDimension() == DimensionType.OVERWORLD.getId() && !world.isRemote) {
      int xx = chunkX * 16 + 13 + Util.rand.nextInt(6);
      int zz = chunkZ * 16 + 13 + Util.rand.nextInt(6);
      if (random.nextInt(ConfigManager.worldGen.barrowChance) == 0 && (Math.abs(xx) > 500 || Math.abs(zz) > 500)) {
        int height = world.getHeight(xx, zz) - 1;
        if (height > 0 && world.getBlockState(new BlockPos(xx, height, zz)).getBlock() instanceof BlockGrass) {
          boolean canGenerate = true;
          for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
              if (world.getBlockState(new BlockPos(xx + i, height, zz + j)).getBlock() == Blocks.AIR) {
                canGenerate = false;
              }
            }
          }
          if (canGenerate) {
            this.generateIn(world, xx, height - 3, zz);
            EntityBarrow entity = new EntityBarrow(world);
            entity.setPosition(xx, height - 1, zz);
            world.spawnEntity(entity);
          }
        }
      }
    }
  }

}