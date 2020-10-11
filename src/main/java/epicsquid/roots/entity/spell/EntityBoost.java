package epicsquid.roots.entity.spell;

import epicsquid.roots.init.ModPotions;
import epicsquid.roots.modifiers.instance.staff.ISnapshot;
import epicsquid.roots.modifiers.instance.staff.ModifierSnapshot;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellAquaBubble;
import epicsquid.roots.spell.SpellSkySoarer;
import it.unimi.dsi.fastutil.ints.IntSets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EntityBoost extends Entity {
  private static final Set<UUID> boostedPlayers = new HashSet<>();
  private static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntityBoost.class, DataSerializers.VARINT);
  private UUID playerId = null;
  private double origX;
  private double origY;
  private double origZ;
  private float amplifier;
  private ISnapshot modifiers;

  public static boolean beingBoosted (Entity player) {
    return boostedPlayers.contains(player.getUniqueID());
  }

  public EntityBoost(World worldIn) {
    super(worldIn);
    this.setInvisible(true);
    this.setSize(1, 1);
    getDataManager().register(lifetime, 20);
  }

  public void setModifiers(ISnapshot modifiers) {
    this.modifiers = modifiers;
  }

  public void setPlayer(UUID id) {
    this.playerId = id;
    Entity[] result = getTargets();
    if (result != null) {
      origX = result[1].motionX;
      origY = result[1].motionY;
      origZ = result[1].motionZ;
    }
    if (modifiers.has(SpellSkySoarer.NO_COLLIDE)) {
      boostedPlayers.add(id);
    }
  }

  public void setAmplifier(float value) {
    this.amplifier = value;
  }

  @Nullable
  private Entity[] getTargets() {
    if (this.playerId != null) {
      EntityPlayer player = world.getPlayerEntityByUUID(playerId);
      if (player != null) {
        Entity riding = player.getLowestRidingEntity();
        if (riding != player) {
          return new Entity[]{player, riding};
        } else {
          return new Entity[]{player, player};
        }
      }
    }

    return null;
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
    getDataManager().set(lifetime, getDataManager().get(lifetime) - 1);
    getDataManager().setDirty(lifetime);
    if (getDataManager().get(lifetime) <= 0) {
      setDead();
      boostedPlayers.remove(this.playerId);
      if (!world.isRemote) {
        Entity[] target = getTargets();
        if (target != null) {
          if (target[1] instanceof EntityBoat) {
            target[1].motionX += MathHelper.sin(-target[1].rotationYaw * 0.017453292F) * 0.25;
            target[1].motionY = 0;
            target[1].motionZ += MathHelper.cos(target[1].rotationYaw * 0.017453292F) * 0.25;
          } else {
            target[1].motionX = origX;
            target[1].motionY = origY;
            target[1].motionZ = origZ;
          }
          EntityPlayer player = (EntityPlayer) target[0];
          ModifierSnapshot mods = StaffModifierInstanceList.fromSnapshot(player.getEntityData(), SpellSkySoarer.instance);
          if (mods.has(SpellSkySoarer.SLOW_FALL)) {
            player.addPotionEffect(new PotionEffect(ModPotions.slow_fall, SpellSkySoarer.instance.slow_duration));
          }
        }
      }
    }
    if (world.isRemote) {
      for (int i = 0; i < 4; i++) {
        if (rand.nextBoolean()) {
          ParticleUtil.spawnParticleStar(world, (float) posX + (rand.nextFloat()) - 0.5f, (float) posY + (rand.nextFloat()) + 0.5f,
              (float) posZ + (rand.nextFloat()) - 0.5f, -0.125f * (float) motionX, -0.125f * (float) motionY, -0.125f * (float) motionZ,
              SpellSkySoarer.instance.getRed1(), SpellSkySoarer.instance.getGreen1(), SpellSkySoarer.instance.getBlue1(), 0.5f,
              5.0f * rand.nextFloat() + 5.0f, 40);
        } else {
          ParticleUtil.spawnParticleStar(world, (float) posX + (rand.nextFloat()) - 0.5f, (float) posY + (rand.nextFloat()) + 0.5f,
              (float) posZ + (rand.nextFloat()) - 0.5f, -0.125f * (float) motionX, -0.125f * (float) motionY, -0.125f * (float) motionZ,
              SpellSkySoarer.instance.getRed2(), SpellSkySoarer.instance.getGreen2(), SpellSkySoarer.instance.getBlue2(), 0.5f,
              5.0f * rand.nextFloat() + 5.0f, 40);
        }
      }
    } else {
      if (playerId != null) {
        Entity[] result = getTargets();
        if (result != null) {
          EntityPlayer player = (EntityPlayer) result[0];
          Entity target = result[1];
          boolean boat = target instanceof EntityBoat;
          this.posX = player.posX;
          this.posY = boat ? this.posY : player.posY + 1.0;
          this.posZ = player.posZ;
          double amp = 0.8 + (0.8 * amplifier);
          Vec3d vec = player.getLookVec();
          target.motionX = vec.x * amp;
          target.motionY = boat ? target.motionY : vec.y * amp;
          target.motionZ = vec.z * amp;
          this.motionX = vec.x + vec.x * amplifier;
          this.motionY = vec.y + vec.y * amplifier;
          this.motionZ = vec.z + vec.z * amplifier;
          target.fallDistance = 0;
          target.velocityChanged = true;
        }
      }
    }
  }

  @Override
  protected void readEntityFromNBT(NBTTagCompound compound) {
    this.playerId = net.minecraft.nbt.NBTUtil.getUUIDFromTag(compound.getCompoundTag("id"));
  }

  @Override
  protected void writeEntityToNBT(NBTTagCompound compound) {
    compound.setTag("id", net.minecraft.nbt.NBTUtil.createUUIDTag(playerId));
  }

}
