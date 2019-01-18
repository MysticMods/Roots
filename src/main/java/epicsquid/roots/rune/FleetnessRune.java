package epicsquid.roots.rune;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.tileentity.TileEntityWildrootRune;
import epicsquid.roots.util.RgbColorUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;

public class FleetnessRune extends RuneBase {

    public FleetnessRune(){
        setIncense(ModItems.moonglow_leaf);
        setColor(RgbColorUtil.FLEETNESS);
        setRuneName("fleetness_rune");
    }

    @Override
    public void saveToEntity(NBTTagCompound tag) {
        super.saveToEntity(tag);
    }

    @Override
    public void readFromEntity(NBTTagCompound tag) {
        super.readFromEntity(tag);
    }

    @Override
    public void activate(TileEntityWildrootRune entity, EntityPlayer player) {
        if(isCharged(entity)){
            player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 1200, 1));
        }
    }

}
