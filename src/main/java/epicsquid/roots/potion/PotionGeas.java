package epicsquid.roots.potion;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

public class PotionGeas extends Effect {
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
  public boolean shouldRender(EffectInstance effect) {
    return false;
  }
}
