package epicsquid.roots.entity.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModDamage;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageRoseThornsBurstFX;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellRoseThorns;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("AccessStaticViaInstance")
public class EntityThornTrap extends EntitySpellModifiable<SpellRoseThorns> {
  protected static final DataParameter<Float> scale1 = EntityDataManager.createKey(EntityThornTrap.class, DataSerializers.FLOAT);
  protected static final DataParameter<Float> scale2 = EntityDataManager.createKey(EntityThornTrap.class, DataSerializers.FLOAT);

  public EntityThornTrap(World world) {
    super(world, SpellRoseThorns.instance, SpellRoseThorns.instance.duration);
    this.setNoGravity(false);
    this.noClip = false;
    getDataManager().register(scale1, 2.5f);
    getDataManager().register(scale2, 5f);
  }

  @Override
  public void setModifiers(StaffModifierInstanceList modifiers) {
    super.setModifiers(modifiers);
    if (modifiers.has(SpellRoseThorns.BIGGER)) {
      getDataManager().set(scale1, 4.5f);
      getDataManager().set(scale2, 9f);
      getDataManager().setDirty(scale1);
      getDataManager().setDirty(scale2);
    }
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
      float scale1 = getDataManager().get(EntityThornTrap.scale1);
      float scale2 = getDataManager().get(EntityThornTrap.scale2);
      if (this.onGround) {
        if (Util.rand.nextBoolean()) {
          ParticleUtil.spawnParticleThorn(world, (float) this.posX, (float) this.posY, (float) this.posZ, 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), SpellRoseThorns.instance.getFirstColours(0.5f), scale1, 12, Util.rand.nextBoolean());
        } else {
          ParticleUtil.spawnParticleThorn(world, (float) this.posX, (float) this.posY, (float) this.posZ, 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), SpellRoseThorns.instance.getSecondColours(0.5f), scale1, 12, Util.rand.nextBoolean());
        }
      } else {
        if (Util.rand.nextBoolean()) {
          ParticleUtil.spawnParticleGlow(world, (float) this.posX, (float) this.posY, (float) this.posZ, 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), SpellRoseThorns.instance.getFirstColours(0.5f), scale2, 12);
        } else {
          ParticleUtil.spawnParticleGlow(world, (float) this.posX, (float) this.posY, (float) this.posZ, 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), SpellRoseThorns.instance.getSecondColours(0.5f), scale2, 12);
        }
      }
    } else {
      if (modifiers == null) {
        setDead();
        return;
      }

      if (playerId != null) {
        double offset = modifiers.has(SpellRoseThorns.BIGGER) ? 3 : 1.5;
        EntityPlayer player = world.getPlayerEntityByUUID(playerId);
        if (player != null) {
          List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX - offset, posY - offset, posZ - offset, posX + offset, posY + offset, posZ + offset), o -> o != player);
          for (EntityLivingBase entity : entities) {
            if (!(entity instanceof EntityPlayer && !FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled())) {
              if (modifiers.has(instance.PEACEFUL) && EntityUtil.isFriendly(entity, SpellRoseThorns.instance)) {
                continue;
              }
              setDead();
              entity.attackEntityFrom(ModDamage.roseDamageFrom(player), SpellRoseThorns.instance.damage);
              if (modifiers.has(SpellRoseThorns.UNDEAD) & entity.isEntityUndead()) {
                entity.attackEntityFrom(ModDamage.roseDamageFrom(player), SpellRoseThorns.instance.undead_damage);
              }
              if (modifiers.has(SpellRoseThorns.STRENGTH)) {
                player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, SpellRoseThorns.instance.strength_duration, SpellRoseThorns.instance.slowness_amplifier, false, false));
              }
              if (modifiers.has(SpellRoseThorns.FIRE)) {
                int fire_dur = SpellRoseThorns.instance.fire_duration;
                entity.setFire(fire_dur);
              }
              if (modifiers.has(SpellRoseThorns.WEAKNESS)) {
                entity.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, SpellRoseThorns.instance.weakness_duration, SpellRoseThorns.instance.weakness_amplifier));
              }
              if (modifiers.has(SpellRoseThorns.SLOW)) {
                entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, SpellRoseThorns.instance.slowness_duration, SpellRoseThorns.instance.slowness_amplifier));
              }
              if (modifiers.has(SpellRoseThorns.POISON)) {
                entity.addPotionEffect(new PotionEffect(MobEffects.POISON, SpellRoseThorns.instance.poison_duration, SpellRoseThorns.instance.poison_amplifier));
              }
              if (modifiers.has(SpellRoseThorns.BOOST)) {
                entity.motionY = SpellRoseThorns.instance.knockup;
                entity.velocityChanged = true;
              }
              if (modifiers.has(SpellRoseThorns.KNOCKBACK)) {
                entity.knockBack(this, SpellRoseThorns.instance.knockback, this.posX - entity.posX, this.posZ - entity.posZ);
              }
            }
            PacketHandler.sendToAllTracking(new MessageRoseThornsBurstFX(this.posX, this.posY, this.posZ), this);
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
