package epicsquid.roots.entity.spell;

import epicsquid.mysticallib.util.AABBUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModDamage;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellDrizzle;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.FarmlandWaterManager;
import net.minecraftforge.common.ticket.AABBTicket;

import java.util.ArrayList;
import java.util.List;

public class EntitySpellDrizzle extends EntitySpellBase<SpellDrizzle> {
  public static AxisAlignedBB BOUNDING_BOX = null;

  private List<BlockPos> groundLevel = null;

  public EntitySpellDrizzle(World worldIn) {
    super(worldIn);
    this.instance = SpellDrizzle.instance;
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
    for (BlockPos pos : Util.getBlocksWithinCircle(getPosition(), instance.radius + 1)) {
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

    if (!world.isRemote && ticksExisted % 20 == 0) {
      List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, BOUNDING_BOX.offset(getPosition()));
      List<EntityLivingBase> hostiles = new ArrayList<>();
      for (EntityLivingBase entity : entities) {
        if (entity instanceof EntityPlayer) {
          if (instance.fire_resistance > 0) {
            entity.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 60, instance.fire_resistance, false, false));
          }
        }
        if (EntityUtil.isHostile(entity)) {
          hostiles.add(entity);
        } else {
          entity.extinguish();
        }
      }
      if (!hostiles.isEmpty()) {
        if (ticksExisted % instance.lightning_interval == 0) {
          if (rand.nextDouble() <= instance.lightning_chance) {
            EntityLivingBase hostile = hostiles.remove(rand.nextInt(hostiles.size()));

            // TODO: Lightning strike
          }
        }

        if (instance.targets >= 1) {
          for (int i = 0; i < instance.targets + (instance.additional_targets == 0 ? 0 : rand.nextInt(instance.additional_targets)); i++) {
            if (hostiles.isEmpty()) {
              break;
            }

            EntityLivingBase hostile = hostiles.remove(rand.nextInt(hostiles.size()));
            hostile.attackEntityFrom(ModDamage.waterDamageSource(getPlayerEntity()), instance.damage);
          }
        }
      }
    }

    if (world != null && world.isRemote) {
      if (groundLevel == null) {
        rebuildGroundLevel();
      }

      for (BlockPos pos : groundLevel) {
        IBlockState state = world.getBlockState(pos);
        IBlockState stateUp = world.getBlockState(pos.up());
        if (stateUp.getMaterial() != Material.AIR) {
          continue;
        }
        AxisAlignedBB box = state.getBoundingBox(world, pos);
        double d3 = Util.rand.nextDouble();
        double d4 = Util.rand.nextDouble();
        if (state.getMaterial() != Material.LAVA && state.getMaterial() != Material.AIR) {
          world.spawnParticle(EnumParticleTypes.WATER_DROP, (double) pos.getX() + d3, (double) ((float) pos.getY() + 0.1F) + box.maxY, (double) pos.getZ() + d4, 0.0D, 0.0D, 0.0D);
        }
      }

      for (float j = 0.3f; j < SpellDrizzle.instance.radius; j += 0.5f) {
        for (float i = Util.rand.nextInt(45); i < 360 - Util.rand.nextInt(6); i+= Util.rand.nextFloat() * 60.0f) {
          float tx = (float) posX + (j + 0.5f) * (float) Math.sin(Math.toRadians(i));
          float ty = (float) posY + 3.75f;
          float tz = (float) posZ + (j + 0.5f) * (float) Math.cos(Math.toRadians(i));
          ParticleUtil.spawnParticleSmoke(world, tx, ty, tz, 0, 0, 0, 25, 45, 120, 0.12f, 14 + j * Util.rand.nextFloat(), 40, false);
        }
      }
    }
  }
}
