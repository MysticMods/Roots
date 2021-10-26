package epicsquid.roots.potion;

import epicsquid.roots.Roots;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.OnlyIn;

public class PotionNondetection extends Effect {
  private ResourceLocation texture = new ResourceLocation(Roots.MODID, "textures/gui/potions.png");

  public PotionNondetection() {
    super(false, 0x5c5d6b);
    setPotionName("Non-Detection");
    setBeneficial();
    setIconIndex(1, 2);
  }

  @Override
  public boolean isReady(int duration, int amplifier) {
    return true;
  }

  @Override
  public boolean shouldRender(EffectInstance effect) {
    return true;
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public int getStatusIconIndex() {
    Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
    return super.getStatusIconIndex();
  }
}
