package epicsquid.roots.potion;

import epicsquid.roots.Roots;
import epicsquid.roots.modifiers.instance.staff.ModifierSnapshot;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellPetalShell;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
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
  public void performEffect(EntityLivingBase entity, int amplifier) {
    if (entity.world.isRemote) {
      ModifierSnapshot mods = StaffModifierInstanceList.fromSnapshot(entity.getEntityData(), SpellPetalShell.instance);
      int count = amplifier;
      for (float i = 0; i < 360; i += 120) {
        float ang = (float) (entity.ticksExisted % 360);
        float tx = (float) entity.posX + 0.8f * (float) Math.sin(Math.toRadians(2.0f * (i + ang)));
        float ty = (float) entity.posY + 1.0f;
        float tz = (float) entity.posZ + 0.8f * (float) Math.cos(Math.toRadians(2.0f * (i + ang)));
        ParticleUtil.spawnParticlePetal(entity.world, tx, ty, tz, 0, 0, 0, mods.has(SpellPetalShell.COLOUR) ? SpellPetalShell.mossFirst : SpellPetalShell.instance.getFirstColours(), 3.5f, 10);
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
