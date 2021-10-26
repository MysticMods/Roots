package epicsquid.roots.potion;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModDamage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.OnlyIn;

public class PotionBleeding extends Effect {
  private ResourceLocation texture = new ResourceLocation(Roots.MODID, "textures/gui/potions.png");

  public PotionBleeding() {
    super(false, 0x8a0303);
    setPotionName("Bleeding");
    setBeneficial();
    setIconIndex(7, 0);
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

  @Override
  public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
    super.performEffect(entityLivingBaseIn, amplifier);

    if (Util.rand.nextInt(5) == 0) {
      entityLivingBaseIn.attackEntityFrom(ModDamage.BLEED_DAMAGE, 1 + amplifier);
    }
  }
}
