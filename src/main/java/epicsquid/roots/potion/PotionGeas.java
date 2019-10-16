package epicsquid.roots.potion;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionGeas extends Potion {
  public PotionGeas() {
    super(false, 0xffe100);
    setPotionName("Geas");
    setIconIndex(0, 0);
    setBeneficial();
  }

  @Override
  public boolean isReady(int duration, int amplifier) {
    return true;
  }

  @Override
  public boolean shouldRender(PotionEffect effect) {
    return false;
  }
}
