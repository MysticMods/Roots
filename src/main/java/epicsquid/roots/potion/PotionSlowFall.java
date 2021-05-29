package epicsquid.roots.potion;

import epicsquid.roots.Roots;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionSlowFall extends Potion {

  private ResourceLocation texture = new ResourceLocation(Roots.MODID, "textures/gui/thaumcraft.potions.png");

  public PotionSlowFall() {
    super(false, 0xe8e7d3);
    setPotionName("Slow Fall");
    setBeneficial();
    setIconIndex(0, 1);
  }

  @Override
  public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {
    super.performEffect(entityLivingBaseIn, amplifier);
    if (!entityLivingBaseIn.onGround && entityLivingBaseIn.motionY < 0.0D && !entityLivingBaseIn.isInWater() && !entityLivingBaseIn.isOnLadder() && !entityLivingBaseIn.isElytraFlying() && !entityLivingBaseIn.isSneaking()) {
      entityLivingBaseIn.fallDistance *= 0.6f;
      entityLivingBaseIn.motionY *= 0.6d;
    }
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
}
