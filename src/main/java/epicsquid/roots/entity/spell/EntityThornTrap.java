package epicsquid.roots.entity.spell;

import epicsquid.roots.init.ModDamage;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellRoseThorns;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("AccessStaticViaInstance")
public class EntityThornTrap extends EntitySpellModifiable<SpellRoseThorns> {
  private float damage;
  private int slownessDuration, slownessAmplifier, poisonDuration, poisonAmplifier, duration;

  public EntityThornTrap(World worldIn) {
    this(worldIn, SpellRoseThorns.damage, SpellRoseThorns.duration, SpellRoseThorns.slownessDuration, SpellRoseThorns.slownessAmplifier, SpellRoseThorns.poisonDuration, SpellRoseThorns.poisonAmplifier);
  }

  public EntityThornTrap(World world, float damage, int duration, int slownessDuration, int slownessAmplifier, int poisonDuration, int poisonAmplifier) {
    super(world, SpellRoseThorns.instance);
    this.setNoGravity(false);
    this.noClip = false;
    this.damage = damage;
    this.slownessDuration = slownessDuration;
    this.slownessAmplifier = slownessAmplifier;
    this.poisonDuration = poisonDuration;
    this.poisonAmplifier = poisonAmplifier;
    this.duration = duration;
  }

  public void setPlayer(UUID id) {
    this.playerId = id;
  }

  @Override
  public boolean isEntityInsideOpaqueBlock() {
    return false;
  }

  @Override
  protected void entityInit() {
  }

  @Override
  public void onUpdate() {
    super.onUpdate();
    this.move(MoverType.SELF, motionX, motionY, motionZ);
    this.motionY -= 0.04f;
    if (this.onGround) {
      this.motionX = 0;
      this.motionZ = 0;
    }
    if (world.isRemote) {
      if (onGround) {
        if (rand.nextBoolean()) {
          ParticleUtil.spawnParticleThorn(world, (float) posX, (float) posY, (float) posZ, 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), SpellRoseThorns.instance.getRed1(), SpellRoseThorns.instance.getGreen1(), SpellRoseThorns.instance.getBlue1(), 0.5f, 2.5f, 12, rand.nextBoolean());
        } else {
          ParticleUtil.spawnParticleThorn(world, (float) posX, (float) posY, (float) posZ, 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), SpellRoseThorns.instance.getRed2(), SpellRoseThorns.instance.getGreen2(), SpellRoseThorns.instance.getBlue2(), 0.5f, 2.5f, 12, rand.nextBoolean());
        }
      } else {
        if (rand.nextBoolean()) {
          ParticleUtil.spawnParticleGlow(world, (float) posX, (float) posY, (float) posZ, 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), SpellRoseThorns.instance.getRed1(), SpellRoseThorns.instance.getGreen1(), SpellRoseThorns.instance.getBlue1(), 0.5f, 5f, 12);
        } else {
          ParticleUtil.spawnParticleGlow(world, (float) posX, (float) posY, (float) posZ, 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), SpellRoseThorns.instance.getRed2(), SpellRoseThorns.instance.getGreen2(), SpellRoseThorns.instance.getBlue2(), 0.5f, 5f, 12);
        }
      }
    }
    if (playerId != null) {
      EntityPlayer player = world.getPlayerEntityByUUID(playerId);
      if (player != null) {
        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX - 1.5, posY - 1.5, posZ - 1.5, posX + 1.5, posY + 1.5, posZ + 1.5));
        entities.remove(player);
        if (entities.size() > 0) {
          setDead();
          for (EntityLivingBase entity : entities) {
            if (!(entity instanceof EntityPlayer && !FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled())) {
              if (modifiers != null && instance.peaceful(modifiers) && EntityUtil.isFriendly(entity)) {
                continue;
              }
              entity.attackEntityFrom(ModDamage.roseDamageFrom(player), damage);
              // TODO: Handle other damage types/etc here
              entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, slownessDuration, slownessAmplifier));
              entity.addPotionEffect(new PotionEffect(MobEffects.POISON, poisonDuration, poisonAmplifier));
              entity.setLastAttackedEntity(player);
              entity.setRevengeTarget(player);
            }
          }
          if (world.isRemote) {
            for (int i = 0; i < 30; i++) {
              if (rand.nextBoolean()) {
                ParticleUtil.spawnParticleThorn(world, (float) posX + 0.25f * (rand.nextFloat() - 0.5f), (float) posY + 0.25f * (rand.nextFloat() - 0.5f), (float) posZ + 0.25f * (rand.nextFloat() - 0.5f), 0.375f * rand.nextFloat() - 0.1875f, 0.1f + 0.125f * rand.nextFloat(), 0.375f * rand.nextFloat() - 0.1875f, SpellRoseThorns.instance.getRed1(), SpellRoseThorns.instance.getGreen1(), SpellRoseThorns.instance.getBlue1(), 0.5f, 4.0f, 24, rand.nextBoolean());
              } else {
                ParticleUtil.spawnParticleThorn(world, (float) posX + 0.25f * (rand.nextFloat() - 0.5f), (float) posY + 0.25f * (rand.nextFloat() - 0.5f), (float) posZ + 0.25f * (rand.nextFloat() - 0.5f), 0.375f * rand.nextFloat() - 0.1875f, 0.1f + 0.125f * rand.nextFloat(), 0.375f * rand.nextFloat() - 0.1875f, SpellRoseThorns.instance.getRed2(), SpellRoseThorns.instance.getGreen2(), SpellRoseThorns.instance.getBlue2(), 0.5f, 4.0f, 24, rand.nextBoolean());
              }
            }
          }
        }
      }
    }
  }

  @Override
  protected void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
  }

  @Override
  protected void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
  }

}
