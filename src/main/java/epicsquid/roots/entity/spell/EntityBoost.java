package epicsquid.roots.entity.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.modifiers.instance.staff.ISnapshot;
import epicsquid.roots.modifiers.instance.staff.ModifierSnapshot;
import epicsquid.roots.network.fx.MessageChemTrailsFX;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellSkySoarer;
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
import java.util.*;

public class EntityBoost extends Entity {
  private static final Set<UUID> boostedPlayers = new HashSet<>();
  private static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntityBoost.class, DataSerializers.VARINT);
  private UUID playerId = null;
  private double origX;
  private double origY;
  private double origZ;
  private ISnapshot modifiers;

  public static boolean beingBoosted(Entity player) {
    return boostedPlayers.contains(player.getUniqueID());
  }

  public EntityBoost(World worldIn) {
    this(worldIn, SpellSkySoarer.instance.lifetime);
  }

  public EntityBoost(World worldIn, int life) {
    super(worldIn);
    this.setInvisible(true);
    this.setSize(1, 1);
    getDataManager().register(lifetime, life);
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
          if (this.modifiers.has(SpellSkySoarer.SLOW_FALL)) {
            player.addPotionEffect(new PotionEffect(ModPotions.slow_fall, SpellSkySoarer.instance.slow_duration));
          }
          if (this.modifiers.has(SpellSkySoarer.NO_FALL_DAMAGE)) {
            markSafe(player);
          }
        }
      }
    }
    if (world.isRemote) {
      for (int i = 0; i < 4; i++) {
        if (rand.nextBoolean()) {
          ParticleUtil.spawnParticleStar(world, (float) posX + (rand.nextFloat()) - 0.5f, (float) posY + (rand.nextFloat()) + 0.5f, (float) posZ + (rand.nextFloat()) - 0.5f, -0.125f * (float) motionX, -0.125f * (float) motionY, -0.125f * (float) motionZ, SpellSkySoarer.instance.getFirstColours(0.5f), 5.0f * rand.nextFloat() + 5.0f, 40);
        } else {
          ParticleUtil.spawnParticleStar(world, (float) posX + (rand.nextFloat()) - 0.5f, (float) posY + (rand.nextFloat()) + 0.5f, (float) posZ + (rand.nextFloat()) - 0.5f, -0.125f * (float) motionX, -0.125f * (float) motionY, -0.125f * (float) motionZ, SpellSkySoarer.instance.getSecondColours(0.5f), 5.0f * rand.nextFloat() + 5.0f, 40);
        }
      }
    } else {
      if (modifiers.has(SpellSkySoarer.CHEM_TRAILS)) {
        PacketHandler.sendToAllTracking(new MessageChemTrailsFX(this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ), this);
      }
      if (playerId != null) {
        float amplifier = SpellSkySoarer.instance.amplifier;
        if (modifiers.has(SpellSkySoarer.FASTER)) {
          amplifier += SpellSkySoarer.instance.extended_amplifier;
        }
        amplifier = modifiers.ampFloat(amplifier);
        Entity[] result = getTargets();
        if (result != null) {
          EntityPlayer player = (EntityPlayer) result[0];
          markSafe(player);
          Entity target = result[1];
          boolean boat = target instanceof EntityBoat;
          this.posX = player.posX;
          this.posY = boat ? this.posY : player.posY + 1.0;
          this.posZ = player.posZ;
          Vec3d vec = player.getLookVec();
          if (!modifiers.has(SpellSkySoarer.VERTICAL)) {
            target.motionX = vec.x * amplifier;
            target.motionZ = vec.z * amplifier;
            this.motionX = vec.x;
            this.motionZ = vec.z;
          }
          if (!modifiers.has(SpellSkySoarer.HORIZONTAL)) {
            target.motionY = boat ? target.motionY : vec.y * amplifier;
            this.motionY = vec.y;
          }
          target.fallDistance = 0;
          target.velocityChanged = true;
        }
      }
    }
  }

  @Override
  protected void readEntityFromNBT(NBTTagCompound compound) {
    this.playerId = net.minecraft.nbt.NBTUtil.getUUIDFromTag(compound.getCompoundTag("id"));
    if (compound.hasKey("modifiers")) {
      this.modifiers = new ModifierSnapshot(compound.getIntArray("modifiers"));
    }
  }

  @Override
  protected void writeEntityToNBT(NBTTagCompound compound) {
    compound.setTag("id", net.minecraft.nbt.NBTUtil.createUUIDTag(playerId));
    if (this.modifiers != null) {
      this.modifiers.toCompound(compound);
    }
  }

  private static Map<UUID, PlayerTracker> playerMap = new HashMap<>();

  public static Map<UUID, PlayerTracker> getPlayers() {
    return playerMap;
  }

  public static boolean safe(EntityPlayer player) {
    PlayerTracker result = playerMap.get(player.getUniqueID());
    if (result == null) {
      return false;
    }

    boolean res = result.safe(player);
    if (!res) {
      playerMap.remove(player.getUniqueID());
    }
    return res;
  }

  public static void markSafe(EntityPlayer player) {
    PlayerTracker track = playerMap.computeIfAbsent(player.getUniqueID(), PlayerTracker::new);
    track.setStart(player.ticksExisted);
  }

  public static class PlayerTracker {
    private int start;

    public PlayerTracker(UUID id) {
    }

    public void setStart(int start) {
      this.start = start;
    }

    public boolean safe(EntityPlayer player) {
      return player.ticksExisted - start < SpellSkySoarer.instance.fall_duration;

    }
  }
}
