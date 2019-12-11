/*package epicsquid.roots.potion;

import epicsquid.mysticallib.util.AABBUtil;
import epicsquid.roots.Roots;
import epicsquid.roots.spell.SpellSenseDanger;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.OnlyIn;

public class PotionDangerSense extends Effect {
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
  @OnlyIn(Dist.CLIENT)
  public int getStatusIconIndex() {
    Minecraft.getInstance().renderEngine.bindTexture(texture);
    return super.getStatusIconIndex();
  }

  @Override
  public void performEffect(LivingEntity entity, int amplifier) {
    if (entity.world.isRemote) return;

    if (entity instanceof PlayerEntity) {
      int[] radius = SpellSenseDanger.instance.getRadius();
      AxisAlignedBB aabb = AABBUtil.buildFromEntity(entity).grow(radius[0], radius[1], radius[2]);
      for (LivingEntity mob : entity.world.getEntitiesWithinAABB(LivingEntity.class, aabb, EntityUtil::isHostile)) {
        mob.addPotionEffect(new EffectInstance(Effects.GLOWING, 10, 0, false, false));
      }
    }
  }

  @Override
  public boolean shouldRenderHUD(EffectInstance effect) {
    return false;
  }

  @Override
  public boolean shouldRenderInvText(EffectInstance effect) {
    return true;
  }
}*/
