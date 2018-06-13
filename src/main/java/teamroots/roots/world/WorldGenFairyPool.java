package teamroots.roots.world;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;
import teamroots.roots.ConfigManager;
import teamroots.roots.entity.EntityAuspiciousPoint;
import teamroots.roots.entity.EntityBarrow;
import teamroots.roots.entity.EntityFairyCircle;
import teamroots.roots.util.Misc;

public class WorldGenFairyPool implements IWorldGenerator {
	ArrayList<IBlockState> saplings = new ArrayList<IBlockState>();
	public WorldGenFairyPool(){
		saplings.add(Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, EnumType.OAK));
		saplings.add(Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, EnumType.BIRCH));
	}
	
	public int getDropForLocation(BlockPos center, BlockPos test){
		return (int) Math.min(Math.round((5.0f+Misc.random.nextFloat()*0.5f)*(Math.max(0, 1.0f-MathHelper.sqrt(center.distanceSq(test))/9.0f))),5);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		if (ConfigManager.fairyPoolChance <= 0 || world.provider.getDimension() != 0){
			return;
		}
		int xx = chunkX*16 + 8 + Misc.random.nextInt(16);
		int zz = chunkZ*16 + 8 + Misc.random.nextInt(16);
		BlockPos centerPos = world.getTopSolidOrLiquidBlock(new BlockPos(xx,0,zz));
		if (centerPos.getY() > 2 && world.getBlockState(centerPos.down()).getBlock() instanceof BlockGrass){
			boolean canGenerate = true;
			for (int i = -6; i < 7; i ++){
				for (int j = -6; j < 7; j ++){
					if (world.getBlockState(new BlockPos(xx+i,centerPos.getY()-2,zz+j)).getBlock() == Blocks.AIR){
						canGenerate = false;
					}
				}
			}
			IBlockState grassState = world.getBlockState(centerPos.down());
			if (canGenerate && random.nextInt(ConfigManager.fairyPoolChance) == 0){
				int minY = -1;
				for (int i = -8; i < 9; i ++){
					for (int j = -8; j < 9; j ++){
						BlockPos pos = world.getTopSolidOrLiquidBlock(centerPos.add(i, 0, j)).down();
						if (pos.getY() < minY || minY == -1){
							minY = pos.getY();
						}
					}
				}
				minY = Math.max(1, minY);
				if (minY != -1){
					for (int i = -8; i < 9; i ++){
						for (int j = -8; j < 9; j ++){
							BlockPos pos = world.getTopSolidOrLiquidBlock(centerPos.add(i, 0, j)).down();
							while (pos.getY() > minY && pos.getY() < minY + 8 && getDropForLocation(centerPos,pos) > 0){
								world.setBlockToAir(pos);
								pos = pos.down();
							}
						}
					}
					for (int i = -8; i < 9; i ++){
						for (int j = -8; j < 9; j ++){
							BlockPos pos = world.getTopSolidOrLiquidBlock(centerPos.add(i, 0, j)).down();
							int y = getDropForLocation(centerPos,pos);
							int k = 0;
							while (k < y && k <= minY && !(world.getBlockState(pos.down(k)).getBlock() instanceof BlockLog)){
								world.setBlockState(pos.down(k),Blocks.WATER.getDefaultState());
								if (world.isAirBlock(pos.down(k).east())){
									world.setBlockState(pos.down(k).east(), Blocks.FLOWING_WATER.getStateFromMeta(7));
								}
								if (world.isAirBlock(pos.down(k).west())){
									world.setBlockState(pos.down(k).west(), Blocks.FLOWING_WATER.getStateFromMeta(7));
								}
								if (world.isAirBlock(pos.down(k).north())){
									world.setBlockState(pos.down(k).north(), Blocks.FLOWING_WATER.getStateFromMeta(7));
								}
								if (world.isAirBlock(pos.down(k).south())){
									world.setBlockState(pos.down(k).south(), Blocks.FLOWING_WATER.getStateFromMeta(7));
								}
								k ++;
							}
						}
					}
				}
				for (int i = 0; i < 360; i += 3){
					if (random.nextInt(3) != 0){
						int height = random.nextInt(3)+4;
						int tx = (int)(xx+19.0*random.nextDouble()*Math.sin(Math.toRadians(i)));
						int tz = (int)(zz+19.0*random.nextDouble()*Math.cos(Math.toRadians(i)));
						BlockPos pos = new BlockPos(tx,world.getHeight(tx,tz), tz);
						if (world.getBlockState(pos.down()).getBlock() instanceof BlockGrass && saplings.size() > 0){
							int index = random.nextInt(saplings.size());
							((BlockSapling)saplings.get(index).getBlock()).generateTree(world, pos, saplings.get(index), random);
						}
					}
				}
				EntityFairyCircle entity = new EntityFairyCircle(world);
				entity.setPosition(centerPos.up(4).getX(),centerPos.up(4).getY(),centerPos.up(4).getZ());
				world.spawnEntity(entity);
			}
		}
	}
}