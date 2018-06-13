package teamroots.roots.capability;

import net.minecraft.nbt.NBTTagCompound;

public interface IPlayerDataCapability {
	public NBTTagCompound getData();
	public void setData(NBTTagCompound tag);
	public void markDirty();
	public boolean isDirty();
	public void clean();
}
