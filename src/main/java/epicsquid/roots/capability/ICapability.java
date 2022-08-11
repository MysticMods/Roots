package epicsquid.roots.capability;

import net.minecraft.nbt.NBTTagCompound;

public interface ICapability {
	NBTTagCompound getData();
	
	void setData(NBTTagCompound tag);
	
	void markDirty();
	
	boolean isDirty();
	
	void clean();
}
