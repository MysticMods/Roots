package epicsquid.roots.entity.ritual;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.ritual.RitualRegistry;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

public class EntityRitualAnimalHarvest extends EntityRitualBase {

  protected static Random random = new Random();
  protected static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntityRitualAnimalHarvest.class, DataSerializers.VARINT);
  private ObjectOpenHashSet<Class<? extends Entity>> harvestClasses;

  public EntityRitualAnimalHarvest(World worldIn) {
    super(worldIn);
    this.getDataManager().register(lifetime, RitualRegistry.ritual_animal_harvest.getDuration() + 20);
    this.harvestClasses = ModRecipes.getAnimalHarvestClasses();
  }

  @Override
  public void onUpdate() {
    int curLifetime = getDataManager().get(lifetime);
    getDataManager().set(lifetime, curLifetime - 1);
    getDataManager().setDirty(lifetime);
    if (getDataManager().get(lifetime) < 0) {
      setDead();
    }
    if (this.ticksExisted % 110 == 0) {
      for (int i = 0; i < 5; i++) {
        if (doHarvest()) break;
      }
    }
  }

  public boolean doHarvest() {
    List<EntityLiving> entityList = Util.getEntitiesWithinRadius(world, (entity) -> harvestClasses.contains(entity.getClass()), getPosition(), 15, 10, 15);
    if (entityList.isEmpty()) return false;
    EntityLiving entity = entityList.get(random.nextInt(entityList.size()));

    boolean didDrops = false;

    if (!world.isRemote) {
      entity.captureDrops = true;
      entity.capturedDrops.clear();
      dropLoot(entity);
      entity.captureDrops = false;
      if (!ForgeHooks.onLivingDrops(entity, DamageSource.GENERIC, entity.capturedDrops, 0, false)) {
        for (EntityItem item : entity.capturedDrops) {
          world.spawnEntity(item);
          didDrops = true;
        }
      }
    }
    return didDrops;
  }

  @Override
  public DataParameter<Integer> getLifetime() {
    return lifetime;
  }

  private static Method dropLoot = null;

  public void dropLoot(EntityLiving entity) {
    if (dropLoot == null) {
      dropLoot = ReflectionHelper.findMethod(EntityLiving.class, "dropLoot", "func_184610_a", boolean.class, int.class, DamageSource.class);
    }

    try {
      dropLoot.invoke(entity, false, 0, DamageSource.GENERIC);
    } catch (IllegalAccessException | InvocationTargetException e) {
      return;
    }
  }
}