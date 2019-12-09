package epicsquid.roots.potion;

import epicsquid.mysticallib.util.AABBUtil;
import epicsquid.roots.Roots;
import epicsquid.roots.spell.SpellSenseDanger;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionDangerSense extends Potion {
  private ResourceLocation texture = new ResourceLocation(Roots.MODID, "textures/gui/potions.png");

  public PotionDangerSense() {
    super(false, 0xa82e2e);
    setPotionName("Danger Sense");
    setBeneficial();
    setIconIndex(4, 0);
  }

  @Override
  public boolean isReady(int duration, int amplifier) {
    return true;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public int getStatusIconIndex() {
    Minecraft.getMinecraft().renderEngine.bindTexture(texture);
    return super.getStatusIconIndex();
  }

  @Override
  public void performEffect(EntityLivingBase entity, int amplifier) {
    if (entity.world.isRemote) return;

    if (entity instanceof EntityPlayer) {
      int[] radius = SpellSenseDanger.instance.getRadius();
      AxisAlignedBB aabb = AABBUtil.buildFromEntity(entity).grow(radius[0], radius[1], radius[2]);
      for (EntityLivingBase mob : entity.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb, EntityUtil::isHostile)) {
        mob.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 10, 0, false, false));
      }
    }
  }

  @Override
  public boolean shouldRenderHUD(PotionEffect effect) {
    return false;
  }

  @Override
  public boolean shouldRenderInvText(PotionEffect effect) {
    return true;
  }
}
