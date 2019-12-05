package epicsquid.roots.rune;

import epicsquid.roots.tileentity.TileEntityWildrootRune;
import epicsquid.roots.util.RgbColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class RuneBase {

  private Item incense;
  private RgbColor color = new RgbColor(0, 0, 0);
  private String runeName;

  public RuneBase() {

  }

  public void saveToEntity(CompoundNBT tag) {
    tag.setString("rune", getRuneName());
  }

  public void readFromEntity(CompoundNBT tag) {

  }

  public abstract void activate(TileEntityWildrootRune entity, PlayerEntity player);

  public boolean isCharged(TileEntityWildrootRune entity) {
    if (incense != null) {
      if (entity.getIncenseBurner() == null) {
        return false;
      }
      if (entity.getIncenseBurner().isLit() && entity.getIncenseBurner().inventory.getStackInSlot(0).getItem() == incense) {
        return true;
      }
    }

    return false;
  }

  public void update(World world, BlockPos pos) {

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
