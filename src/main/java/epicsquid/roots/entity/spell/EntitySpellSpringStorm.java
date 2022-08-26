/*package epicsquid.roots.entity.spell;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellSpringStorm;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class EntitySpellSpringStorm extends EntitySpellBase<SpellSpringStorm> {
  public static AxisAlignedBB BOUNDING_BOX = null;
  public static Map<UUID, UUID> playerToEntity = new HashMap<>();

  public static boolean hasCloud(EntityPlayer player) {
    return hasCloud(player.getUniqueID());
  }

  public static boolean hasCloud(UUID uuid) {
    return playerToEntity.containsKey(uuid);
  }

  private List<BlockPos> groundLevel = null;
  private int strikes = 0;

  public EntitySpellSpringStorm(World worldIn) {
    super(worldIn);
    this.instance = SpellSpringStorm.instance;
    getDataManager().register(lifetime, instance.duration + 20);

    if (BOUNDING_BOX == null) {
      int r = instance.radius + 1;
      BOUNDING_BOX = new AxisAlignedBB(-r, -3.5, -r, r, 3.5, r);
    }
  }

  private void rebuildGroundLevel() {
    if (groundLevel == null) {
      groundLevel = new ArrayList<>();
    } else {
      groundLevel.clear();
    }
    int minY = (int) posY - 3;
    for (BlockPos pos : Util.getPositionsWithinCircle(getPosition(), instance.radius + 1)) {
      IBlockState state = world.getBlockState(pos);
      while (state.getMaterial() == Material.AIR && pos.getY() >= minY) {
        pos = pos.down();
        state = world.getBlockState(pos);
      }
      if (state.getMaterial() != Material.AIR) {
        groundLevel.add(pos);
      }
    }
  }

  @Override
  public void onUpdate() {
    super.onUpdate();
    if (isDead) {
      if (playerToEntity.containsValue(this.getUniqueID())) {
        playerToEntity.remove(getPlayer(), this.getUniqueID());
      }
    } else {
      if (!hasCloud(getPlayer())) {
        playerToEntity.put(getPlayer(), this.getUniqueID());
      }
    }

    if (ticksExisted % 20 == 0) {
      if (!world.isRemote) {
        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, BOUNDING_BOX.offset(getPosition()));
        List<EntityLivingBase> hostiles = new ArrayList<>();
        for (EntityLivingBase entity : entities) {
          if (entity instanceof EntityPlayer) {
            if (instance.fire_resistance >= 0) {
              entity.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, (int) (60 + 60 * amplifier), instance.fire_resistance, false, false));
            }
            if (instance.resistance >= 0) {
              entity.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, (int) (60 + 60 * amplifier), instance.resistance, false, false));
            }
          }
          if (EntityUtil.isHostile(entity)) {
            hostiles.add(entity);
          } else {
            entity.extinguish();
          }
        }
        if (!hostiles.isEmpty()) {
          if (strikes < instance.lightning_strikes) {
            if (rand.nextInt((int) (instance.lightning_chance - instance.lightning_chance * amplifier)) == 0) {
              EntityLivingBase hostile = hostiles.remove(rand.nextInt(hostiles.size()));

              world.weatherEffects.add(new EntityLightningBolt(world, hostile.posX, hostile.posY, hostile.posZ, false));
              strikes++;
            }
          }
        }
      }
    }
    if (ticksExisted % 5 == 0) {
      List<BlockPos> positions = Util.getBlocksWithinCircle(world, getPosition(), instance.radius + 1, Blocks.FIRE, Blocks.SNOW_LAYER);
      if (!positions.isEmpty()) {
        BlockPos pos = positions.remove(rand.nextInt(positions.size()));
        if (!world.isRemote) {
          world.setBlockToAir(pos);
        }
      }
    }

    if (world != null && world.isRemote) {
      if (groundLevel == null) {
        rebuildGroundLevel();
      }

      for (BlockPos pos : groundLevel) { // TODO: UM
        IBlockState state = world.getBlockState(pos);
        IBlockState stateUp = world.getBlockState(pos.up());
        IBlockState stateDown = world.getBlockState(pos.down());
        if (state.getMaterial() == Material.AIR && stateDown.getMaterial() != Material.AIR) {
          pos = pos.down();
          stateUp = state;
          state = stateDown;
        }

        if (stateUp.getMaterial() != Material.AIR) {
          continue;
        }
        AxisAlignedBB box = state.getBoundingBox(world, pos);
        double d3 = Util.rand.nextDouble();
        double d4 = Util.rand.nextDouble();
        if (state.getMaterial() != Material.LAVA && state.getMaterial() != Material.AIR) {
          world.spawnParticle(EnumParticleTypes.WATER_DROP, (double) pos.getX() + d3, (double) ((float) pos.getY() + 0.1F) + box.maxY, (double) pos.getZ() + d4, 0.0D, 0.0D, 0.0D);
        } else if (state.getMaterial() == Material.LAVA) {
          world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double) pos.getX() + d3, (double) ((float) pos.getY() + 0.1F) + box.maxY, (double) pos.getZ() + d4, 0.0D, 0.0D, 0.0D);
        }

        if (rand.nextInt(40) == 0) {
          ParticleUtil.spawnParticleRain(world, pos.getX() + rand.nextFloat(), (float) posY + 2.75f, pos.getZ() + rand.nextFloat(), 0, 0.005f, 0, 0, 54, 204, 0.7f, 3 * (1 + Util.rand.nextFloat()), 30);
        }

        if (rand.nextInt(20) == 0) {
          world.playSound(posX, posY, posZ, SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.2F, 1.0F, false);
        }

        if (ticksExisted % 3 == 0) {
          for (float j = 0f; j < SpellSpringStorm.instance.radius; j += 0.5f) {
            for (float i = Util.rand.nextInt(45); i <= 360 - Util.rand.nextInt(20); i += Util.rand.nextFloat() * 110f) {
              float tx = (float) posX + (j + 0.5f) * (float) Math.sin(Math.toRadians(i));
              float ty = (float) posY + 3.95f;
              float tz = (float) posZ + (j + 0.5f) * (float) Math.cos(Math.toRadians(i));
              ParticleUtil.spawnParticleCloud(world, tx, ty, tz, 0, 0, 0, 0, 0, 10, 0.5f, 12.5f, 10, false);
            }
          }
        }
      }
    }
  }
}*/
