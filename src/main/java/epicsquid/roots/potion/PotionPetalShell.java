package epicsquid.roots.potion;

import epicsquid.roots.Roots;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellPetalShell;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionPetalShell extends Potion {

  private ResourceLocation texture = new ResourceLocation(Roots.MODID, "textures/gui/potions.png");

  public PotionPetalShell() {
    super(false, 0xcd9dc6);
    setPotionName("Petal Shell");
    setBeneficial();
    setIconIndex(3, 0);
  }

  @Override
  public boolean isReady(int duration, int amplifier) {
    return true;
  }

  @Override
  public boolean shouldRender(PotionEffect effect) {
    return true;
  }

  @Override
  public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {
    if (entityLivingBaseIn instanceof EntityPlayer && entityLivingBaseIn.world.isRemote) {
      int count = amplifier;
      EntityPlayer player = (EntityPlayer) entityLivingBaseIn;
      float r, g, b;
      r = SpellPetalShell.instance.getFirstColours()[0];
      g = SpellPetalShell.instance.getFirstColours()[1];
      b = SpellPetalShell.instance.getFirstColours()[2];
      for (float i = 0; i < 360; i += 120) {
        float ang = (float) (player.ticksExisted % 360);
        float tx = (float) player.posX + 0.8f * (float) Math.sin(Math.toRadians(2.0f * (i + ang)));
        float ty = (float) player.posY + 1.0f;
        float tz = (float) player.posZ + 0.8f * (float) Math.cos(Math.toRadians(2.0f * (i + ang)));
        ParticleUtil.spawnParticlePetal(player.world, tx, ty, tz, 0, 0, 0, r, g, b, 1f, 3.5f, 10);
        count--;
        if (count < 0) {
          break;
        }
      }
    }
  }


  @SideOnly(Side.CLIENT)
  @Override
  public int getStatusIconIndex() {
    Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
    return super.getStatusIconIndex();
  }
}
