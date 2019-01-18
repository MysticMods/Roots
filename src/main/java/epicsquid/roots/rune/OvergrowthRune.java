package epicsquid.roots.rune;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.tileentity.TileEntityWildrootRune;
import epicsquid.roots.util.RgbColorUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class OvergrowthRune extends RuneBase {

    public OvergrowthRune(){
        setIncense(ModItems.terra_moss);
        setColor(RgbColorUtil.OVERGROWTH);
        setRuneName("overgrowth_rune");
    }

    @Override
    public void saveToEntity(NBTTagCompound tag) {
        super.saveToEntity(tag);
    }

    @Override
    public void readFromEntity(NBTTagCompound tag) {

    }

    @Override
    public void activate(TileEntityWildrootRune entity, EntityPlayer player) {

    }
}
