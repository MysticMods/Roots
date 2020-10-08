package epicsquid.roots.entity.spell;

import epicsquid.roots.init.ModDamage;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellRoseThorns;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("AccessStaticViaInstance")
public class EntityThornTrap extends EntitySpellModifiable<SpellRoseThorns> {
  public EntityThornTrap(World world) {
    super(world, SpellRoseThorns.instance, SpellRoseThorns.instance.duration);
    this.setNoGravity(false);
    this.noClip = false;
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
    float scale1 = modifiers.has(SpellRoseThorns.BIGGER) ? 4.5f : 2.5f;
    float scale2 = modifiers.has(SpellRoseThorns.BIGGER) ? 9f : 5f;
    if (world.isRemote) {
      if (onGround) {
        if (rand.nextBoolean()) {
          ParticleUtil.spawnParticleThorn(world, (float) posX, (float) posY, (float) posZ, 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), SpellRoseThorns.instance.getFirstColours(0.5f), scale1, 12, rand.nextBoolean());
        } else {
          ParticleUtil.spawnParticleThorn(world, (float) posX, (float) posY, (float) posZ, 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), SpellRoseThorns.instance.getSecondColours(0.5f), scale1, 12, rand.nextBoolean());
        }
      } else {
        if (rand.nextBoolean()) {
          ParticleUtil.spawnParticleGlow(world, (float) posX, (float) posY, (float) posZ, 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), SpellRoseThorns.instance.getFirstColours(0.5f), scale2, 12);
        } else {
          ParticleUtil.spawnParticleGlow(world, (float) posX, (float) posY, (float) posZ, 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f), SpellRoseThorns.instance.getSecondColours(0.5f), scale2, 12);
        }
      }
    }
    if (playerId != null) {
      double offset = modifiers.has(SpellRoseThorns.BIGGER) ? 3 : 1.5;
      EntityPlayer player = world.getPlayerEntityByUUID(playerId);
      if (player != null) {
        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX - offset, posY - offset, posZ - offset, posX + offset, posY + offset, posZ + offset), o -> o != player);
        for (EntityLivingBase entity : entities) {
          if (!(entity instanceof EntityPlayer && !FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled())) {
            if (modifiers != null && modifiers.has(instance.PEACEFUL) && EntityUtil.isFriendly(entity)) {
              continue;
            }
            setDead();
            entity.attackEntityFrom(ModDamage.roseDamageFrom(player), modifiers.ampFloat(SpellRoseThorns.instance.damage));
            if (modifiers.has(SpellRoseThorns.UNDEAD) & entity.isEntityUndead()) {
              entity.attackEntityFrom(ModDamage.roseDamageFrom(player), modifiers.ampFloat(SpellRoseThorns.instance.undead_damage));
            }
            if (modifiers.has(SpellRoseThorns.STRENGTH)) {
              player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, modifiers.ampInt(SpellRoseThorns.instance.strength_duration), SpellRoseThorns.instance.slowness_amplifier, false, false));
            }
            if (modifiers.has(SpellRoseThorns.FIRE)) {
              entity.setFire(modifiers.ampInt(SpellRoseThorns.instance.fire_duration));
            }
            if (modifiers.has(SpellRoseThorns.PARALYSIS)) {
              entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, modifiers.ampInt(SpellRoseThorns.instance.root_duration), 10));
            }
            if (modifiers.has(SpellRoseThorns.SLOW)) {
              entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, modifiers.ampInt(SpellRoseThorns.instance.slowness_duration), SpellRoseThorns.instance.slowness_amplifier));
            }
            if (modifiers.has(SpellRoseThorns.POISON)) {
              entity.addPotionEffect(new PotionEffect(MobEffects.POISON, modifiers.ampInt(SpellRoseThorns.instance.poison_duration), SpellRoseThorns.instance.poison_amplifier));
            }
            if (modifiers.has(SpellRoseThorns.BOOST)) {
              entity.motionY = SpellRoseThorns.instance.knockup;
              entity.velocityChanged = true;
            }
            if (modifiers.has(SpellRoseThorns.KNOCKBACK)) {
              entity.knockBack(this, SpellRoseThorns.instance.knockback, this.posX - entity.posX, this.posZ - entity.posZ);
            }
          }
          if (world.isRemote) {
            for (int i = 0; i < 30; i++) {
              if (rand.nextBoolean()) {
                ParticleUtil.spawnParticleThorn(world, (float) posX + 0.25f * (rand.nextFloat() - 0.5f), (float) posY + 0.25f * (rand.nextFloat() - 0.5f), (float) posZ + 0.25f * (rand.nextFloat() - 0.5f), 0.375f * rand.nextFloat() - 0.1875f, 0.1f + 0.125f * rand.nextFloat(), 0.375f * rand.nextFloat() - 0.1875f, SpellRoseThorns.instance.getFirstColours(0.5f), 4.0f, 24, rand.nextBoolean());
              } else {
                ParticleUtil.spawnParticleThorn(world, (float) posX + 0.25f * (rand.nextFloat() - 0.5f), (float) posY + 0.25f * (rand.nextFloat() - 0.5f), (float) posZ + 0.25f * (rand.nextFloat() - 0.5f), 0.375f * rand.nextFloat() - 0.1875f, 0.1f + 0.125f * rand.nextFloat(), 0.375f * rand.nextFloat() - 0.1875f, SpellRoseThorns.instance.getSecondColours(0.5f), 4.0f, 24, rand.nextBoolean());
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
