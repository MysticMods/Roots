package epicsquid.roots.entity.spell;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModDamage;
import epicsquid.roots.mechanics.Growth;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellWildfire;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.List;

public class EntityFireJet extends EntitySpellModifiable<SpellWildfire> {
  protected static final DataParameter<Integer> color = EntityDataManager.createKey(EntityFireJet.class, DataSerializers.VARINT);

  public EntityFireJet(World worldIn) {
    super(worldIn, SpellWildfire.instance, SpellWildfire.instance.lifetime);
    getDataManager().register(color, 0);
  }

  @Override
  public void setModifiers(StaffModifierInstanceList modifiers) {
    super.setModifiers(modifiers);
    int color = 0; // RED
    if (modifiers.has(SpellWildfire.PURPLE)) {
      color = 1; // PURPLE
    } else if (modifiers.has(SpellWildfire.GREEN)) {
      color = 2; // GREEN
    }
    getDataManager().set(EntityFireJet.color, color);
    getDataManager().setDirty(EntityFireJet.color);
  }

  private static final float[] redColor = new float[]{255.0f / 255.0f, 96.0f / 255.0f, 32.0f / 255.0f, 0.5f};
  private static final float[] purpleColor = new float[]{212 / 255.0f, 11 / 255.0f, 74 / 255.0f, 0.5f};
  private static final float[] greenColor = new float[]{148 / 255.0f, 212 / 255.0f, 11 / 255.0f, 0.5f};

  @Override
  public void onUpdate() {
    super.onUpdate();
    if (this.onGround && modifiers != null && modifiers.has(SpellWildfire.WILDFIRE) && !world.isRemote) {
      wildFire(getPosition());
    }
    if (world.isRemote) {
      float[] color;
      switch (getDataManager().get(EntityFireJet.color)) {
        default:
        case 0:
          color = redColor;
          break;
        case 1:
          color = purpleColor;
          break;
        case 2:
          color = greenColor;
          break;
      }
      float offX = 0.5f * (float) Math.sin(Math.toRadians(rotationYaw));
      float offZ = 0.5f * (float) Math.cos(Math.toRadians(rotationYaw));
      ParticleUtil.spawnParticleFiery(world, (float) posX + (float) motionX * 2.5f + offX, (float) posY + 1.62F + (float) motionY * 2.5f, (float) posZ + (float) motionZ * 2.5f + offZ, (float) motionX + 0.125f * (rand.nextFloat() - 0.5f), (float) motionY + 0.125f * (rand.nextFloat() - 0.5f), (float) motionZ + 0.125f * (rand.nextFloat() - 0.5f), color, 7.5f, 24);
    } else {
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
                  if (modifiers.has(SpellWildfire.PEACEFUL) && EntityUtil.isFriendly(entity, SpellWildfire.instance)) {
                    continue;
                  }
                  hit = true;
                  entity.setFire(instance.fire_duration);
                  entity.attackEntityFrom(ModDamage.fireDamageFrom(player), instance.damage);
                  if (modifiers.has(SpellWildfire.WEAKNESS)) {
                    entity.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, instance.weakness_duration, instance.weakness_amplifier));
                  }
                  if (modifiers.has(SpellWildfire.SLOW)) {
                    entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, instance.slow_duration, instance.slow_amplifier));
                  }
                  if (modifiers.has(SpellWildfire.LEVITATE)) {
                    entity.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, instance.levitate_duration, 0));
                  }
                  if (modifiers.has(SpellWildfire.POISON)) {
                    entity.addPotionEffect(new PotionEffect(MobEffects.POISON, instance.poison_duration, instance.poison_amplifier));
                  }
                  if (modifiers.has(SpellWildfire.ICICLES)) {
                    int count = instance.icicle_count;
                    Vec3d pos = entity.getPositionVector();
                    Vec3d playerPos = player.getPositionVector().add(0, player.getEyeHeight(), 0);
                    Vec3d accel = pos.subtract(playerPos);
                    for (int k = 0; k < count; k++) {
                      EntityIcicle icicle = new EntityIcicle(world, player, accel.x, accel.y, accel.z, EntityIcicle.SpellType.WILDFIRE);
                      icicle.posX = playerPos.x;
                      icicle.posY = playerPos.y + Util.rand.nextDouble() - 0.5;
                      icicle.posZ = playerPos.z;
                      icicle.setModifiers(modifiers);
                      world.spawnEntity(icicle);
                    }
                  }
                  if (modifiers.has(SpellWildfire.WILDFIRE)) {
                    wildFire(entity.getPosition());
                  }
                  if (modifiers.has(SpellWildfire.DUALITY) && entity.isDead) {
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
    }
  }

  private void wildFire(BlockPos center) {
    IBlockState state = world.getBlockState(center);
    if (world.isAirBlock(center) || state.getBlock().isReplaceable(world, center)) {
      // TODO: Check down
      int x = 4;
      while ((world.isAirBlock(center.down()) || state.getBlock().isReplaceable(world, center)) && x >= 0) {
        center = center.down();
        state = world.getBlockState(center);
        x--;
      }
      if (world.isAirBlock(center) || state.getBlock().isReplaceable(world, center)) {
        world.setBlockState(center.up(), Blocks.FIRE.getDefaultState());
      }
    }
  }
}
