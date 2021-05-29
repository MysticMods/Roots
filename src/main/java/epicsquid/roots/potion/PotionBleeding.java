package epicsquid.roots.potion;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModDamage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionBleeding extends Potion {
  private ResourceLocation texture = new ResourceLocation(Roots.MODID, "textures/gui/thaumcraft.potions.png");

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
  public boolean shouldRender(PotionEffect effect) {
    return true;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public int getStatusIconIndex() {
    Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
    return super.getStatusIconIndex();
  }

  @Override
  public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {
    super.performEffect(entityLivingBaseIn, amplifier);

    if (Util.rand.nextInt(5) == 0) {
      entityLivingBaseIn.attackEntityFrom(ModDamage.BLEED_DAMAGE, 1 + amplifier);
    }
  }
}
