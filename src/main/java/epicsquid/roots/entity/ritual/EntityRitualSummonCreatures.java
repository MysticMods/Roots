package epicsquid.roots.entity.ritual;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.item.ItemLifeEssence;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.recipe.SummonCreatureRecipe;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.util.RitualUtil;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public class EntityRitualSummonCreatures extends EntityRitualBase {

  protected static Random random = new Random();
  protected static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntityRitualSummonCreatures.class, DataSerializers.VARINT);

  private SummonCreatureRecipe summonRecipe = null;
  private ItemStack essence = ItemStack.EMPTY;

  public EntityRitualSummonCreatures(World worldIn) {
    super(worldIn);
    this.getDataManager().register(lifetime, RitualRegistry.ritual_summoning.getDuration() + 20);
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
    int curLifetime = getDataManager().get(lifetime);
    getDataManager().set(lifetime, curLifetime - 1);
    getDataManager().setDirty(lifetime);
    if (getDataManager().get(lifetime) < 0) {
      setDead();
    }
    float alpha = (float) Math.min(40, (RitualRegistry.ritual_summoning.getDuration() + 20) - getDataManager().get(lifetime)) / 40.0f;
    if (world.isRemote && getDataManager().get(lifetime) > 0) {
      ParticleUtil.spawnParticleStar(world, (float) posX, (float) posY, (float) posZ, 0, 0, 0, 70, 70, 70, 0.5f * alpha, 20.0f, 40);
      for (float i = 0; i < 360; i += 120) {
        float ang = (float) (ticksExisted % 360);
        float tx = (float) posX + 2.5f * (float) Math.sin(Math.toRadians(2.0f * (i + ang)));
        float ty = (float) posY + 0.5f * (float) Math.sin(Math.toRadians(4.0f * (i + ang)));
        float tz = (float) posZ + 2.5f * (float) Math.cos(Math.toRadians(2.0f * (i + ang)));
        ParticleUtil.spawnParticleStar(world, tx, ty, tz, 0, 0, 0, 70, 70, 70, 0.5f * alpha, 10.0f, 40);
      }
      if (rand.nextInt(5) == 0) {
        ParticleUtil.spawnParticleSpark(world, (float) posX, (float) posY, (float) posZ, -(0.125f * (rand.nextFloat() - 0.5f)), -(0.0625f * (rand.nextFloat())), -(0.125f * (rand.nextFloat() - 0.5f)), 70, 70, 70, 1.0f * alpha, 1.0f + rand.nextFloat(), 160);
      }
    }
    if (this.ticksExisted % 150 == 0) {
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

      for (int i = 0; i < 10; i++) {
        suitable = RitualUtil.getRandomPosRadial(getPosition(), 2, 2);
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

      if (!world.isRemote) {
        world.spawnEntity(entity);
      } else {
        for (int i = 0; i < 10; i++) {
          ParticleUtil.spawnParticleStar(world, (float) entity.posX + 0.5f * (Util.rand.nextFloat() - 0.5f),
              (float) (entity.posY + entity.height / 2.5f + (Util.rand.nextFloat())), (float) entity.posZ + 0.5f * (Util.rand.nextFloat() - 0.5f),
              0.125f * (Util.rand.nextFloat() - 0.5f), 0.01875f * (Util.rand.nextFloat()), 0.125f * (Util.rand.nextFloat() - 0.5f), 100, 255, 100, 1.0f,
              1.0f + 2.0f * Util.rand.nextFloat(), 40);
        }
      }
    }
  }

  @Override
  public DataParameter<Integer> getLifetime() {
    return lifetime;
  }

  @Override
  protected void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    if (compound.hasKey("summonRecipe")) {
      this.summonRecipe = ModRecipes.getSummonCreatureEntry(new ResourceLocation(compound.getString("summonRecipe")));
    }
    this.essence = new ItemStack(compound.getCompoundTag("essence"));
  }

  @Override
  protected void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    if (this.summonRecipe != null) {
      compound.setString("summonRecipe", this.summonRecipe.getRegistryName().toString());
    }
    compound.setTag("essence", this.essence.serializeNBT());
  }
}