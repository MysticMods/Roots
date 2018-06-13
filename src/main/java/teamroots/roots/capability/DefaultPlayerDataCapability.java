package teamroots.roots.capability;

import net.minecraft.nbt.NBTTagCompound;

public class DefaultPlayerDataCapability implements IPlayerDataCapability {
	public NBTTagCompound tag = new NBTTagCompound();
	public boolean dirty = true;
	
	@Override
	public NBTTagCompound getData(){
		return tag;
	}

	@Override
	public void setData(NBTTagCompound tag) {
		this.tag = tag;
		markDirty();
	}

	@Override
	public void markDirty() {
		this.dirty = true;
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public void clean() {
		this.dirty = false;
	}
}
