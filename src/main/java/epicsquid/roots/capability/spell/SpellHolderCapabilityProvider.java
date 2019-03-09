package epicsquid.roots.capability.spell;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;

public class SpellHolderCapabilityProvider implements ICapabilitySerializable<NBTTagCompound> {

    @CapabilityInject(ISpellHolderCapability.class) public static final Capability<SpellHolderCapability> ENERGY_CAPABILITY = injected();

    private SpellHolderCapability instance = ENERGY_CAPABILITY.getDefaultInstance();

    @Override
    public NBTTagCompound serializeNBT() {
        return (NBTTagCompound) ENERGY_CAPABILITY.getStorage().writeNBT(ENERGY_CAPABILITY, this.instance, null);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        ENERGY_CAPABILITY.getStorage().readNBT(ENERGY_CAPABILITY, this.instance, null, nbt);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == ENERGY_CAPABILITY;
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        return capability == ENERGY_CAPABILITY ? ENERGY_CAPABILITY.cast(this.instance) : null;
    }

    private static final Object NULL = null;

    @SuppressWarnings("unchecked")
    private static <T> T injected() {
        return (T) NULL;
    }

}