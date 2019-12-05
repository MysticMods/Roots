package epicsquid.roots.rune;

import epicsquid.roots.init.ModItems;
import epicsquid.roots.tileentity.TileEntityWildrootRune;
import epicsquid.roots.util.RgbColorUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;

public class FleetnessRune extends RuneBase {

  public FleetnessRune() {
    setIncense(ModItems.moonglow_leaf);
    setColor(RgbColorUtil.FLEETNESS);
    setRuneName("fleetness_rune");
  }

  @Override
  public void saveToEntity(CompoundNBT tag) {
    super.saveToEntity(tag);
  }

  @Override
  public void readFromEntity(CompoundNBT tag) {
    super.readFromEntity(tag);
  }

  @Override
  public void activate(TileEntityWildrootRune entity, PlayerEntity player) {
    if (isCharged(entity)) {
      player.addPotionEffect(new EffectInstance(Effects.SPEED, 1200, 1));
    }
  }

}
