package epicsquid.roots.entity.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModDamage;
import epicsquid.roots.mechanics.Growth;
import epicsquid.roots.network.fx.MessageWildfireFX;
import epicsquid.roots.spell.SpellWildfire;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;
import java.util.List;

public class EntityFireJet extends EntitySpellModifiable<SpellWildfire> {
  public EntityFireJet(World worldIn) {
    super(worldIn, SpellWildfire.instance, SpellWildfire.instance.lifetime);
  }

  @Override
  public void onUpdate() {
    super.onUpdate();

    if (!world.isRemote) {
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
            boolean hit = false;
            for (EntityLivingBase entity : entities) {
              if (entity != player && !(entity instanceof EntityPlayer && !FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled())) {
                if (modifiers != null) {
                  if (modifiers.has(SpellWildfire.PEACEFUL) && EntityUtil.isFriendly(entity)) {
                    continue;
                  }
                  hit = true;
                  entity.setFire(modifiers.ampInt(instance.fire_duration));
                  entity.attackEntityFrom(ModDamage.fireDamageFrom(player), modifiers.ampFloat(instance.damage));
                  if (modifiers.has(SpellWildfire.PARALYSIS)) {
                    entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, modifiers.ampInt(instance.paralysis_duration), 10));
                  }
                  if (modifiers.has(SpellWildfire.SLOW)) {
                    entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, modifiers.ampInt(instance.slow_duration), instance.slow_amplifier));
                  }
                  if (modifiers.has(SpellWildfire.LEVITATE)) {
                    entity.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, modifiers.ampInt(instance.levitate_duration), 0));
                  }
                  if (modifiers.has(SpellWildfire.POISON)) {
                    entity.addPotionEffect(new PotionEffect(MobEffects.POISON, modifiers.ampInt(instance.poison_duration), instance.poison_amplifier));
                  }
                  if (modifiers.has(SpellWildfire.ICICLES)) {
                    int count = instance.icicle_count;
                    Vec3d pos = entity.getPositionVector();
                    Vec3d playerPos = player.getPositionVector().add(0, player.getEyeHeight(), 0);
                    Vec3d accel = pos.subtract(playerPos);
                    for (int k = 0; k < count; k++) {
                      EntityIcicle icicle = new EntityIcicle(world, player, accel.x, accel.y, accel.z);
                      icicle.posX = playerPos.x;
                      icicle.posY = playerPos.y + Util.rand.nextDouble() - 0.5;
                      icicle.posZ = playerPos.z;
                      icicle.setModifiers(modifiers);
                      world.spawnEntity(icicle);
                    }
                  }
                  if (modifiers.has(SpellWildfire.WILDFIRE)) {
                    wildFire(getPosition());
                  }
                  if (modifiers.has(SpellWildfire.GROWTH) && entity.isDead) {
                    List<BlockPos> positions = Growth.collect(world, getPosition(), instance.radius_x, instance.radius_y, instance.radius_z);
                    if (!world.isRemote) {
                      for (BlockPos pos : positions) {
                        IBlockState state = world.getBlockState(pos);
                        for (int j = 0; j < instance.growth_ticks; j++) {
                          state.getBlock().randomTick(world, pos, state, world.rand);
                        }
                      }
                    }
                  }
                }
              }
            }
            if (hit) {
              setDead();
            }
          }
        }
      }
      if (this.onGround && modifiers != null && modifiers.has(SpellWildfire.WILDFIRE) && !world.isRemote) {
        wildFire(getPosition());
      }
      if (this.ticksExisted % 2 == 0) {
        MessageWildfireFX packet = new MessageWildfireFX(posX, posY, posZ, motionX, motionY, motionZ, rotationYaw, modifiers);
        PacketHandler.INSTANCE.sendToAllTracking(packet, this);
      }
    }
  }

  @Nullable
  private BlockPos airPos(BlockPos pos, int max) {
    IBlockState state = world.getBlockState(pos);
    if (!world.isAirBlock(pos) && !state.getBlock().isReplaceable(world, pos)) {
      for (int i = 0; i <= max; i++) {
        if (world.isAirBlock(pos) || state.getBlock().isReplaceable(world, pos)) {
          return pos;
        }
        pos = pos.add(0, 1, 0);
      }
    } else {
      for (int i = 0; i <= max; i++) {
        pos = pos.add(0, -1, 0);
        if (!world.isAirBlock(pos) && !state.getBlock().isReplaceable(world, pos)) {
          return pos.up();
        }
      }
    }
    return null;
  }

  private void wildFire(BlockPos center) {
    for (BlockPos pos : Util.getPositionsWithinCircle(center, instance.fire_radius)) {
      BlockPos air = airPos(pos, 10);
      if (air != null) {
        world.setBlockState(air, ModBlocks.fey_fire.getDefaultState(), 2);
      }
    }
  }
}
