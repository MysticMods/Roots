package epicsquid.roots.util;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class XPUtil {
	// Code directly below taken form EnderIO's XpUtil
	// which has been released into the public domain.
	private static final int MAX_LEVEL = 21862;
	private static final int[] xpmap = new int[MAX_LEVEL + 1];
	
	static {
		int res = 0;
		for (int i = 0; i <= MAX_LEVEL; i++) {
			if (res < 0) {
				res = Integer.MAX_VALUE;
			}
			xpmap[i] = res;
			res += getXpBarCapacity(i);
		}
	}
	
	public static int getXpBarCapacity(int level) {
		if (level >= 30) {
			return 112 + (level - 30) * 9;
		} else {
			return level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;
		}
	}
	
	public static int getExperienceForLevel(int level) {
		if (level <= 0) {
			return 0;
		}
		if (level > MAX_LEVEL) {
			return Integer.MAX_VALUE;
		}
		return xpmap[level];
	}
	
	// And now the usual inadequacy.
	public static void spawnXP(World world, BlockPos pos, int xp) {
		if (world.isRemote) return;
		
		xp = getExperienceForLevel(xp);
		
		while (xp > 0) {
			int k = EntityXPOrb.getXPSplit(xp);
			xp -= k;
			world.spawnEntity(new EntityXPOrb(world, pos.getX(), pos.getY() + 0.5D, pos.getZ() + 0.5D, k));
		}
	}
}
