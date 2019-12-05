package epicsquid.roots.entity.spell;

import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellRoseThorns;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("AccessStaticViaInstance")
public class ThornTrapEntity extends Entity {
  private static final DataParameter<Integer> lifetime = EntityDataManager.<Integer>createKey(ThornTrapEntity.class, DataSerializers.VARINT);
  private UUID playerId = null;

  private float damage;
  private int slownessDuration, slownessAmplifier, poisonDuration, poisonAmplifier, duration;

  public ThornTrapEntity(EntityType<?> entityTypeIn, World worldIn) {
    super(entityTypeIn, worldIn);
  }

  public ThornTrapEntity(World worldIn) {
    this(worldIn, SpellRoseThorns.damage, SpellRoseThorns.duration, SpellRoseThorns.slownessDuration, SpellRoseThorns.slownessAmplifier, SpellRoseThorns.poisonDuration, SpellRoseThorns.poisonAmplifier);
  }

  public ThornTrapEntity(World world, float damage, int duration, int slownessDuration, int slownessAmplifier, int poisonDuration, int poisonAmplifier) {
    this(null, null);
    this.setInvisible(false);
    this.setNoGravity(false);
    this.noClip = false;
    this.damage = damage;
    this.slownessDuration = slownessDuration;
    this.slownessAmplifier = slownessAmplifier;
    this.poisonDuration = poisonDuration;
    this.poisonAmplifier = poisonAmplifier;
    this.duration = duration;
  }

  @Override
  protected void registerData() {
    getDataManager().register(lifetime, duration);
  }


  public void setPlayer(UUID id) {
    this.playerId = id;
  }

  @Override
  public boolean isEntityInsideOpaqueBlock() {
    return false;
  }

  // TODO: Custom packet

  @Override
  public IPacket<?> createSpawnPacket() {
    return null;
  }

  @Override
  public void tick() {
    super.tick();
    Vec3d motion = getMotion();
    this.move(MoverType.SELF, motion);

    if (this.onGround) {
      this.setMotion(0, motion.y - 0.04, 0);
    } else {
      this.setMotion(motion.x, motion.y - 0.04, motion.z);
    }
    getDataManager().set(lifetime, getDataManager().get(lifetime) - 1);
    if (getDataManager().get(lifetime) <= 0) {
      remove();
    }
    if (world.isRemote) {
      if (onGround) {
        if (rand.nextBoolean()) {
          ParticleUtil
              .spawnParticleThorn(world, (float) posX, (float) posY, (float) posZ, 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f),
                  0.125f * (rand.nextFloat() - 0.5f), SpellRoseThorns.instance.getRed1(), SpellRoseThorns.instance.getGreen1(),
                  SpellRoseThorns.instance.getBlue1(), 0.5f, 2.5f, 12, rand.nextBoolean());
        } else {
          ParticleUtil
              .spawnParticleThorn(world, (float) posX, (float) posY, (float) posZ, 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f),
                  0.125f * (rand.nextFloat() - 0.5f), SpellRoseThorns.instance.getRed2(), SpellRoseThorns.instance.getGreen2(),
                  SpellRoseThorns.instance.getBlue2(), 0.5f, 2.5f, 12, rand.nextBoolean());
        }
      } else {
        if (rand.nextBoolean()) {
          ParticleUtil
              .spawnParticleGlow(world, (float) posX, (float) posY, (float) posZ, 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f),
                  0.125f * (rand.nextFloat() - 0.5f), SpellRoseThorns.instance.getRed1(), SpellRoseThorns.instance.getGreen1(),
                  SpellRoseThorns.instance.getBlue1(), 0.5f, 5f, 12);
        } else {
          ParticleUtil
              .spawnParticleGlow(world, (float) posX, (float) posY, (float) posZ, 0.125f * (rand.nextFloat() - 0.5f), 0.125f * (rand.nextFloat() - 0.5f),
                  0.125f * (rand.nextFloat() - 0.5f), SpellRoseThorns.instance.getRed2(), SpellRoseThorns.instance.getGreen2(),
                  SpellRoseThorns.instance.getBlue2(), 0.5f, 5f, 12);
        }
      }
    }
    if (playerId != null) {
      PlayerEntity player = world.getPlayerByUuid(playerId);
      if (player != null) {
        List<LivingEntity> entities = world
            .getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(posX - 1.5, posY - 1.5, posZ - 1.5, posX + 1.5, posY + 1.5, posZ + 1.5));
        entities.remove(player);
        if (entities.size() > 0) {
          remove();
          for (LivingEntity entity : entities) {
            if (!(entity instanceof PlayerEntity && world.getServer().isPVPEnabled())) {
              entity.attackEntityFrom(DamageSource.CACTUS.causeMobDamage(player), damage);
              entity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, slownessDuration, slownessAmplifier));
              entity.addPotionEffect(new EffectInstance(Effects.POISON, poisonDuration, poisonAmplifier));
              entity.setLastAttackedEntity(player);
              entity.setRevengeTarget(player);
            }
          }
          if (world.isRemote) {
            for (int i = 0; i < 30; i++) {
              if (rand.nextBoolean()) {
                ParticleUtil.spawnParticleThorn(world, (float) posX + 0.25f * (rand.nextFloat() - 0.5f), (float) posY + 0.25f * (rand.nextFloat() - 0.5f),
                    (float) posZ + 0.25f * (rand.nextFloat() - 0.5f), 0.375f * rand.nextFloat() - 0.1875f, 0.1f + 0.125f * rand.nextFloat(),
                    0.375f * rand.nextFloat() - 0.1875f, SpellRoseThorns.instance.getRed1(), SpellRoseThorns.instance.getGreen1(),
                    SpellRoseThorns.instance.getBlue1(), 0.5f, 4.0f, 24, rand.nextBoolean());
              } else {
                ParticleUtil.spawnParticleThorn(world, (float) posX + 0.25f * (rand.nextFloat() - 0.5f), (float) posY + 0.25f * (rand.nextFloat() - 0.5f),
                    (float) posZ + 0.25f * (rand.nextFloat() - 0.5f), 0.375f * rand.nextFloat() - 0.1875f, 0.1f + 0.125f * rand.nextFloat(),
                    0.375f * rand.nextFloat() - 0.1875f, SpellRoseThorns.instance.getRed2(), SpellRoseThorns.instance.getGreen2(),
                    SpellRoseThorns.instance.getBlue2(), 0.5f, 4.0f, 24, rand.nextBoolean());
              }
            }
          }
        }
      }
    }
  }

  @Override
  protected void readAdditional(CompoundNBT compound) {
    this.playerId = NBTUtil.readUniqueId(compound.getCompound("id"));
  }

  @Override
  protected void writeAdditional(CompoundNBT compound) {
    compound.put("id", NBTUtil.writeUniqueId(playerId));
  }
}
