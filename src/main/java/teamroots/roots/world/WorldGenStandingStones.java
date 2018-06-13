package teamroots.roots.world;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.BlockGrass;
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
import teamroots.roots.util.Misc;

public class WorldGenStandingStones implements IWorldGenerator {
	ArrayList<IBlockState> blocks = new ArrayList<IBlockState>();
	public WorldGenStandingStones(){
		blocks.add(Blocks.COBBLESTONE.getDefaultState());
		blocks.add(Blocks.MOSSY_COBBLESTONE.getDefaultState());
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		if (ConfigManager.stoneCircleChance <= 0){
			return;
		}
		if (world.provider.getDimension() == DimensionType.OVERWORLD.getId()){
			int xx = chunkX*16 + 13 + Misc.random.nextInt(6);
			int zz = chunkZ*16 + 13 + Misc.random.nextInt(6);
			if (random.nextInt(ConfigManager.stoneCircleChance) == 0){
				if (world.getHeight(xx, zz)-1 > 0 && world.getBlockState(new BlockPos(xx,world.getHeight(xx, zz)-1,zz)).getBlock() instanceof BlockGrass){
					EntityAuspiciousPoint entity = new EntityAuspiciousPoint(world);
					entity.setPosition(xx,world.getHeight(xx,zz)+6,zz);
					
					world.spawnEntity(entity);
					for (int i = 0; i < 360; i += 36){
						if (random.nextInt(3) != 0){
							int height = random.nextInt(3)+4;
							int tx = (int)(xx+5.0*Math.sin(Math.toRadians(i)));
							int tz = (int)(zz+5.0*Math.cos(Math.toRadians(i)));
							BlockPos pos = new BlockPos(tx,world.getHeight(tx,tz), tz);
							if (world.getBlockState(pos.down()).getBlock() instanceof BlockGrass){
								for (int j = 0; j < height; j ++){
									world.setBlockState(pos.add(0,j,0),blocks.get(random.nextInt(blocks.size())));
								}
							}
						}
					}
				}
			}
		}
	}
	
}
