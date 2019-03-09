package epicsquid.roots.capability.spell;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class SpellHolderCapabilityStorage  implements Capability.IStorage<ISpellHolderCapability> {

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<ISpellHolderCapability> capability, ISpellHolderCapability instance, EnumFacing side) {
        return instance.getData();
    }

    @Override
    public void readNBT(Capability<ISpellHolderCapability> capability, ISpellHolderCapability instance, EnumFacing side, NBTBase nbt) {
        instance.setData((NBTTagCompound) nbt);
    }
}
