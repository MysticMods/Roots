package teamroots.roots.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class NBTUtil {
	public static BlockPos getBlockPos(NBTTagCompound tag, String key){
		return new BlockPos(tag.getInteger(key+"_x"),tag.getInteger(key+"_y"),tag.getInteger(key+"_z"));
	}
	
	public static void setBlockPos(NBTTagCompound tag, String key, BlockPos pos){
		tag.setInteger(key+"_x", pos.getX());
		tag.setInteger(key+"_y", pos.getY());
		tag.setInteger(key+"_z", pos.getZ());
	}
	
	public static boolean hasBlockPosKey(NBTTagCompound tag, String key){
		return tag.hasKey(key+"_x");
	}
}
