package epicsquid.roots.entity.ritual;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.item.ItemLifeEssence;
import epicsquid.roots.network.fx.MessageCreatureSummonedFX;
import epicsquid.roots.network.fx.MessageTimeStopStartFX;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.recipe.SummonCreatureRecipe;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.ritual.RitualSummonCreatures;
import epicsquid.roots.util.RitualUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class EntityRitualSummonCreatures extends EntityRitualBase {
  private RitualSummonCreatures ritual;

  private SummonCreatureRecipe summonRecipe = null;
  private ItemStack essence = ItemStack.EMPTY;

  public EntityRitualSummonCreatures(World worldIn) {
    super(worldIn);
    this.getDataManager().register(lifetime, RitualRegistry.ritual_summon_creatures.getDuration() + 20);
    this.ritual = (RitualSummonCreatures) RitualRegistry.ritual_summon_creatures;
  }

  public void setSummonRecipe(SummonCreatureRecipe summonRecipe) {
    this.summonRecipe = summonRecipe;
  }

  public void setEssence(ItemStack essence) {
    this.essence = essence;
  }

  @Override
  public void onUpdate() {
    super.onUpdate();
    float alpha = (float) Math.min(40, (RitualRegistry.ritual_summon_creatures.getDuration() + 20) - getDataManager().get(lifetime)) / 40.0f;
    if (world.isRemote && getDataManager().get(lifetime) > 0) {
      for (float i = 0; i < 360; i += rand.nextFloat() * 90.0f) {
        float vx = -(0.09f * (float) Math.sin(Math.toRadians(i)));
        float vz = -(0.09f * (float) Math.cos(Math.toRadians(i)));
        float tx = (float) posX + 2.5f * (float) Math.sin(Math.toRadians(i));
        float ty = (float) posY;
        float tz = (float) posZ + 2.5f * (float) Math.cos(Math.toRadians(i));
        ParticleUtil.spawnParticleSmoke(world, tx, ty, tz, vx, 0, vz, 67, 0, 87, 0.065f * alpha, 5.0f, 125, true);
      }
    }
    if (this.ticksExisted % ritual.interval == 0 && !world.isRemote) {
      Class<? extends Entity> entityClass = null;

      if (summonRecipe != null) {
        entityClass = summonRecipe.getClazz();
      } else if (!essence.isEmpty()) {
        entityClass = ((ItemLifeEssence) ModItems.life_essence).getEntityClass(essence);
      }

      Entity entity = null;

      if (entityClass != null) {
        try {
          Constructor<? extends Entity> constructor = entityClass.getDeclaredConstructor(World.class);
          entity = constructor.newInstance(world);
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
          Roots.logger.error("Unable to summon entity of class " + entityClass.toString(), e);
        }
      }

      if (entity == null) {
        return;
      }

      BlockPos suitable = BlockPos.ORIGIN;

      for (int i = 0; i < ritual.tries; i++) {
        suitable = RitualUtil.getRandomPosRadialXZ(getPosition(), ritual.radius_x, ritual.radius_z);
        BlockPos solid = suitable;
        while (world.isAirBlock(solid) || !world.isSideSolid(solid, EnumFacing.UP)) {
          solid = solid.down();
        }
        if (suitable == solid) {
          while (!world.isAirBlock(suitable)) {
            suitable = suitable.up();
          }
        }
        if (!world.isAirBlock(suitable) || world.isAirBlock(solid)) {
          suitable = BlockPos.ORIGIN;
          continue;
        }

        break;
      }

      if (suitable == BlockPos.ORIGIN) {
        return;
      }

      entity.setPosition(suitable.getX() + 0.5, suitable.getY() + 0.5, suitable.getZ() + 0.5);

      world.spawnEntity(entity);
      if (entity instanceof EntityLivingBase) {
        ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.GLOWING, ritual.glow_duration));
      }
      PacketHandler.sendToAllTracking(new MessageCreatureSummonedFX(entity), entity);
    }
  }

  @Override
  protected void readEntityFromNBT(NBTTagCompound compound) {
    if (compound.hasKey("summonRecipe")) {
      this.summonRecipe = ModRecipes.getSummonCreatureEntry(new ResourceLocation(compound.getString("summonRecipe")));
    }
    this.essence = new ItemStack(compound.getCompoundTag("essence"));
    super.readEntityFromNBT(compound);
  }

  @Override
  protected void writeEntityToNBT(NBTTagCompound compound) {
    if (this.summonRecipe != null) {
      compound.setString("summonRecipe", this.summonRecipe.getRegistryName().toString());
    }
    compound.setTag("essence", this.essence.serializeNBT());
    super.writeEntityToNBT(compound);
  }
}