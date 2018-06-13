package teamroots.roots.world;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.BlockBeetroot;
import net.minecraft.block.BlockCarrot;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockPotato;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;
import teamroots.roots.ConfigManager;
import teamroots.roots.entity.EntityAuspiciousPoint;
import teamroots.roots.entity.EntitySprout;
import teamroots.roots.util.Misc;

public class WorldGenGarden implements IWorldGenerator {
	ArrayList<IBlockState> blocks = new ArrayList<IBlockState>();
	public WorldGenGarden(){
		blocks.add(Blocks.WHEAT.getDefaultState().withProperty(BlockCrops.AGE, 7));
		blocks.add(Blocks.BEETROOTS.getDefaultState().withProperty(BlockBeetroot.BEETROOT_AGE, 3));
		blocks.add(Blocks.POTATOES.getDefaultState().withProperty(BlockPotato.AGE, 7));
		blocks.add(Blocks.CARROTS.getDefaultState().withProperty(BlockCarrot.AGE, 7));
		blocks.add(Blocks.OAK_FENCE.getDefaultState());
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		if (ConfigManager.gardenChance <= 0 || world.provider.getDimension() != 0){
			return;
		}
		if (world.provider.getDimension() == DimensionType.OVERWORLD.getId()){
			int xx = chunkX*16 + 8 + Misc.random.nextInt(16);
			int zz = chunkZ*16 + 8 + Misc.random.nextInt(16);
			if (random.nextInt(ConfigManager.gardenChance) == 0){
				if (world.getHeight(xx, zz)-1 > 0 && world.getBlockState(new BlockPos(xx,world.getHeight(xx, zz)-1,zz)).getBlock() instanceof BlockGrass){
					for (int i = 0; i < 360; i += random.nextInt(36)){
						if (random.nextInt(3) != 0){
							int height = random.nextInt(3)+4;
							int tx = (int)(xx+random.nextDouble()*5.0*Math.sin(Math.toRadians(i)));
							int tz = (int)(zz+random.nextDouble()*5.0*Math.cos(Math.toRadians(i)));
							BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(tx,world.getHeight(tx,tz), tz)).down();
							if (world.getBlockState(pos).getBlock() instanceof BlockGrass){
								IBlockState state = this.blocks.get(random.nextInt(blocks.size()));
								if (!(state.getBlock() instanceof BlockFence)){
									if (random.nextBoolean() && !world.isRemote){
										EntitySprout sprout = new EntitySprout(world);
										sprout.onInitialSpawn(world.getDifficultyForLocation(pos), null);
										sprout.setPosition(pos.getX()+0.5, pos.getY()+1.5, pos.getZ()+0.5);
										world.spawnEntity(sprout);
									}
									world.setBlockState(pos, Blocks.FARMLAND.getDefaultState());
								}
								world.setBlockState(pos.up(), state);
							}
						}
					}
				}
			}
		}
	}
	
}
