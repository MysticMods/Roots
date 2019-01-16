package epicsquid.roots.rune;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.tileentity.TileEntityWildrootRune;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;

public class AerRune extends Rune {

    public AerRune(){
        setIncense(ModItems.moonglow_leaf);
    }

    @Override
    public void saveToEntity(NBTTagCompound tag) {
        tag.setString("rune", "aer_rune");
    }

    @Override
    public void readFromEntity(NBTTagCompound tag) {

    }

    @Override
    public void activate(TileEntityWildrootRune entity, EntityPlayer player) {
        if(isCharged(entity)){
            player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 1200, 1));
        }
    }

}
