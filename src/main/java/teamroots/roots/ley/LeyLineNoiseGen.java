package teamroots.roots.ley;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teamroots.roots.util.NoiseGenUtil;

public class LeyLineNoiseGen {
	static Random random = new Random();
	public static BlockPos getNearestLeyLine(World world, BlockPos pos){
		int x = pos.getX();
		int z = pos.getZ();
		random.setSeed(NoiseGenUtil.simple_hash(new int[]{(int)world.getSeed(),(int)(world.getSeed() << 32),x,z}, 4));
		int nx = (int)(Math.floor((float)x/512.0f)*512)+(int)(512.0f*random.nextFloat());
		random.setSeed(NoiseGenUtil.simple_hash(new int[]{(int)(world.getSeed() << 32),(int)world.getSeed(),-x*3+80,z*2+64,x,z}, 6));
		int nz = (int)(Math.floor((float)z/512.0f)*512)+(int)(512.0f*random.nextFloat());
		return new BlockPos(nx,world.getHeight(nx, nz),nz);
	}
}
