package epicsquid.roots.entity.spell;

import epicsquid.roots.init.ModDamage;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellWildfire;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.List;

public class EntityFireJet extends EntitySpellModifiable<SpellWildfire> {

  public EntityFireJet(World worldIn) {
    super(worldIn, SpellWildfire.instance, 12);
  }

  private static final float[] redColor = new float[]{255.0f/255.0f, 96.0f/255.0f, 32.0f/255.0f, 0.5f};
  private static final float[] purpleColor = new float[]{212/255.0f, 11/255.0f, 74/255.0f, 0.5f};
  private static final float[] greenColor = new float[]{148/255.0f, 212/255.0f, 11/255.0f, 0.5f};

  @Override
  public void onUpdate() {
    super.onUpdate();
    float[] color = redColor;
    if (modifiers.has(SpellWildfire.GREEN)) {
      color = greenColor;
    } else if (modifiers.has(SpellWildfire.PURPLE)) {
      color = purpleColor;
    }

    if (world.isRemote) {
      for (int i = 0; i < 8; i++) {
        float offX = 0.5f * (float) Math.sin(Math.toRadians(rotationYaw));
        float offZ = 0.5f * (float) Math.cos(Math.toRadians(rotationYaw));
        ParticleUtil.spawnParticleFiery(world, (float) posX + (float) motionX * 2.5f + offX, (float) posY + 1.62F + (float) motionY * 2.5f, (float) posZ + (float) motionZ * 2.5f + offZ, (float) motionX + 0.125f * (rand.nextFloat() - 0.5f), (float) motionY + 0.125f * (rand.nextFloat() - 0.5f), (float) motionZ + 0.125f * (rand.nextFloat() - 0.5f), color, 7.5f, 24);
      }
    }
    if (this.playerId != null) {
      EntityPlayer player = world.getPlayerEntityByUUID(this.playerId);
      if (player != null) {
        Vec3d lookVec = player.getLookVec();
        this.posX = player.posX;
        this.posY = player.posY;
        this.posZ = player.posZ;
        motionX = (float) lookVec.x * 0.5f;
        motionY = (float) lookVec.y * 0.5f;
        motionZ = (float) lookVec.z * 0.5f;
        rotationYaw = -90.0f - player.rotationYaw;
        for (float i = 0; i < 3; i++) {
          float vx = (float) lookVec.x * 3.0f;
          float vy = (float) lookVec.y * 3.0f;
          float vz = (float) lookVec.z * 3.0f;
          List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX + vx * i - 1.5, posY + vy * i - 1.5, posZ + vz * i - 1.5, posX + vx * i + 1.5, posY + vy * i + 1.5, posZ + vz * i + 1.5));
          for (EntityLivingBase entity : entities) {
            if (entity != player && !(entity instanceof EntityPlayer && !FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled())) {
              if (modifiers != null && modifiers.has(SpellWildfire.PEACEFUL) && EntityUtil.isFriendly(entity)) {
                continue;
              }
              entity.setFire((int) (instance.fire_duration + instance.fire_duration * amplifier));
              entity.attackEntityFrom(ModDamage.fireDamageFrom(player), (float) (instance.damage + instance.damage * amplifier));
              entity.setLastAttackedEntity(player);
              entity.setRevengeTarget(player);
            }
          }
        }
      }
    }
  }
}
