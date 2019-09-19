package epicsquid.roots.entity.ritual;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.ritual.RitualAnimalHarvest;
import epicsquid.roots.ritual.RitualRegistry;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

public class EntityRitualAnimalHarvest extends EntityRitualBase {

  private ObjectOpenHashSet<Class<? extends Entity>> harvestClasses;
  private RitualAnimalHarvest ritual;

  public EntityRitualAnimalHarvest(World worldIn) {
    super(worldIn);
    this.getDataManager().register(lifetime, RitualRegistry.ritual_animal_harvest.getDuration() + 20);
    this.harvestClasses = ModRecipes.getAnimalHarvestClasses();
    this.ritual = (RitualAnimalHarvest) RitualRegistry.ritual_animal_harvest;
  }

  @Override
  public void onUpdate() {
    super.onUpdate();

    if (this.ticksExisted % ritual.interval == 0) {
      for (int i = 0; i < ritual.count; i++) {
        if (doHarvest()) break;
      }
    }
  }

  private boolean doHarvest() {
    List<EntityLiving> entityList = Util.getEntitiesWithinRadius(world, (entity) -> harvestClasses.contains(entity.getClass()), getPosition(), ritual.radius_x, ritual.radius_y, ritual.radius_z);
    if (entityList.isEmpty()) return false;
    EntityLiving entity = entityList.get(Util.rand.nextInt(entityList.size()));

    boolean didDrops = false;

    if (!world.isRemote) {
      entity.captureDrops = true;
      entity.capturedDrops.clear();
      int looting = Util.rand.nextInt(ritual.looting_chance) == 0 ? ritual.looting_value : 0;
      dropLoot(entity, looting);
      entity.captureDrops = false;
      if (!ForgeHooks.onLivingDrops(entity, DamageSource.GENERIC, entity.capturedDrops, looting, false)) {
        for (EntityItem item : entity.capturedDrops) {
          item.motionY = 0;
          item.motionX = 0;
          item.motionZ = 0;
          world.spawnEntity(item);
          didDrops = true;
        }
      }
    }

    if (didDrops) {
      entity.addPotionEffect(new PotionEffect(MobEffects.GLOWING, ritual.glowing, 0));
    }
    return didDrops;
  }

  private static Method getLootTable = null;

  private ResourceLocation getLootTable(EntityLiving entity) {
    if (getLootTable == null) {
      getLootTable = ReflectionHelper.findMethod(EntityLiving.class, "getLootTable", "func_184647_J");
    }

    try {
      return (ResourceLocation) getLootTable.invoke(entity);
    } catch (IllegalAccessException | InvocationTargetException e) {
      return null;
    }
  }

  private void dropLoot(EntityLiving entity, int looting) {
    ResourceLocation resourcelocation = entity.deathLootTable;

    if (resourcelocation == null) {
      resourcelocation = getLootTable(entity);
    }

    if (resourcelocation != null) {
      LootTable loottable = entity.world.getLootTableManager().getLootTableFromLocation(resourcelocation);
      entity.deathLootTable = null;
      FakePlayer fakePlayer = FakePlayerFactory.getMinecraft((WorldServer) entity.world);
      LootContext context = new LootContext(looting, (WorldServer) entity.world, entity.world.getLootTableManager(), entity, fakePlayer, DamageSource.GENERIC);

      for (ItemStack itemstack : loottable.generateLootForPools(entity.deathLootTableSeed == 0L ? entity.rand : new Random(entity.deathLootTableSeed), context)) {
        entity.entityDropItem(itemstack, 0.0F);
      }
    }
  }
}