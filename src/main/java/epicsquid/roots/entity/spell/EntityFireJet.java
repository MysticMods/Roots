package epicsquid.roots.entity.spell;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModDamage;
import epicsquid.roots.mechanics.Growth;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellWildfire;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
    super(worldIn, SpellWildfire.instance, 12);
  }

  private static final float[] redColor = new float[]{255.0f / 255.0f, 96.0f / 255.0f, 32.0f / 255.0f, 0.5f};
  private static final float[] purpleColor = new float[]{212 / 255.0f, 11 / 255.0f, 74 / 255.0f, 0.5f};
  private static final float[] greenColor = new float[]{148 / 255.0f, 212 / 255.0f, 11 / 255.0f, 0.5f};

  @Override
  public void onUpdate() {
    super.onUpdate();
    float[] color = redColor;
    if (modifiers.has(SpellWildfire.GREEN)) {
      color = greenColor;
    } else if (modifiers.has(SpellWildfire.PURPLE)) {
      color = purpleColor;
    }

    if (world.isRemote) {
      for (int i = 0; i < 8; i++) {
        float offX = 0.5f * (float) Math.sin(Math.toRadians(rotationYaw));
        float offZ = 0.5f * (float) Math.cos(Math.toRadians(rotationYaw));
        ParticleUtil.spawnParticleFiery(world, (float) posX + (float) motionX * 2.5f + offX, (float) posY + 1.62F + (float) motionY * 2.5f, (float) posZ + (float) motionZ * 2.5f + offZ, (float) motionX + 0.125f * (rand.nextFloat() - 0.5f), (float) motionY + 0.125f * (rand.nextFloat() - 0.5f), (float) motionZ + 0.125f * (rand.nextFloat() - 0.5f), color, 7.5f, 24);
      }
    }
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
            for (EntityLivingBase entity : entities) {
              if (entity != player && !(entity instanceof EntityPlayer && !FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled())) {
                if (modifiers != null) {
                  if (modifiers.has(SpellWildfire.PEACEFUL) && EntityUtil.isFriendly(entity)) {
                    continue;
                  }
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
                    // TODO
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
          }
        }
      }
      if (this.onGround && modifiers != null && modifiers.has(SpellWildfire.WILDFIRE) && !world.isRemote) {
        wildFire(getPosition());
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
