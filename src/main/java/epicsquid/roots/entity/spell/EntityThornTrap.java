package epicsquid.roots.entity.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.init.ModDamage;
import epicsquid.roots.network.fx.MessageRoseThornsBurstFX;
import epicsquid.roots.network.fx.MessageRoseThornsTickFX;
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
    // TODO: Make this server-side

    if (!world.isRemote) {
      if (modifiers == null) {
        setDead();
        return;
      }

      PacketHandler.sendToAllTracking(new MessageRoseThornsTickFX(this.posX, this.posY, this.posZ, this.onGround, modifiers), this);
      if (playerId != null) {
        double offset = modifiers.has(SpellRoseThorns.BIGGER) ? 3 : 1.5;
        EntityPlayer player = world.getPlayerEntityByUUID(playerId);
        if (player != null) {
          List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX - offset, posY - offset, posZ - offset, posX + offset, posY + offset, posZ + offset), o -> o != player);
          for (EntityLivingBase entity : entities) {
            if (!(entity instanceof EntityPlayer && !FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled())) {
              if (modifiers.has(instance.PEACEFUL) && EntityUtil.isFriendly(entity)) {
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
              if (modifiers.has(SpellRoseThorns.WEAKNESS)) {
                entity.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, modifiers.ampInt(SpellRoseThorns.instance.weakness_duration), SpellRoseThorns.instance.weakness_amplifier));
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
            PacketHandler.sendToAllTracking(new MessageRoseThornsBurstFX(this.posX, this.posY, this.posZ, modifiers), this);
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
