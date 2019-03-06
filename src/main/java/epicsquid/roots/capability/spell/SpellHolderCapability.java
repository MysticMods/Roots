package epicsquid.roots.capability.spell;

import net.minecraft.nbt.NBTTagCompound;

public class SpellHolderCapability implements ISpellHolderCapability {
    @Override
    public NBTTagCompound getData() {
        return null;
    }

    @Override
    public void setData(NBTTagCompound tag) {

    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public void clean() {

    }
}
