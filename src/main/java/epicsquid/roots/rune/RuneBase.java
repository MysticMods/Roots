package epicsquid.roots.rune;

import epicsquid.roots.tileentity.TileEntityWildrootRune;
import epicsquid.roots.util.RgbColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

public abstract class RuneBase {

    private Item incense;
    private RgbColor color = new RgbColor(0, 0, 0);
    private String runeName;

    public RuneBase(){

    }

    public void saveToEntity(NBTTagCompound tag){
        tag.setString("rune", getRuneName());
    }

    public void readFromEntity(NBTTagCompound tag){

    }

    public abstract void activate(TileEntityWildrootRune entity, EntityPlayer player);

    public boolean isCharged(TileEntityWildrootRune entity){
        if(incense != null){
            if(entity.getIncenseBurner() == null){
                return false;
            }
            if(entity.getIncenseBurner().isLit() && entity.getIncenseBurner().inventory.getStackInSlot(0).getItem() == incense){
                return true;
            }
        }

        return false;
    }

    public Item getIncense() {
        return incense;
    }

    public void setIncense(Item incense) {
        this.incense = incense;
    }

    public RgbColor getColor() {
        return color;
    }

    public void setColor(RgbColor color) {
        this.color = color;
    }

    public void setRuneName(String runeName) {
        this.runeName = runeName;
    }

    public String getRuneName() {
        return runeName;
    }
}
