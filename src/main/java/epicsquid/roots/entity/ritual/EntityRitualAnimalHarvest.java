package epicsquid.roots.entity.ritual;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.ritual.RitualRegistry;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
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
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

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
    super.onUpdate();
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
      dropLoot(entity, true);
      entity.captureDrops = false;
      if (!ForgeHooks.onLivingDrops(entity, DamageSource.GENERIC, entity.capturedDrops, 0, false)) {
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
      entity.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 30, 0));
    }
    return didDrops;
  }

  @Override
  public DataParameter<Integer> getLifetime() {
    return lifetime;
  }

  private static Method getLootTable = null;

  public static ResourceLocation getLootTable(EntityLiving entity) {
    if (getLootTable == null) {
      getLootTable = ObfuscationReflectionHelper.findMethod(EntityLiving.class, "func_184647_J", ResourceLocation.class);
      getLootTable.setAccessible(true);
    }

    try {
      return (ResourceLocation) getLootTable.invoke(entity);
    } catch (IllegalAccessException | InvocationTargetException e) {
      return null;
    }
  }

  public static void dropLoot(EntityLiving entity, boolean player) {
    ResourceLocation resourcelocation = entity.deathLootTable;

    if (resourcelocation == null) {
      resourcelocation = getLootTable(entity);
    }

    if (resourcelocation != null) {
      LootTable loottable = entity.world.getLootTableManager().getLootTableFromLocation(resourcelocation);
      entity.deathLootTable = null;
      FakePlayer fakePlayer = FakePlayerFactory.getMinecraft((WorldServer) entity.world);
      LootContext context = new LootContext(random.nextInt(6) == 0 ? 1 : 0, (WorldServer) entity.world, entity.world.getLootTableManager(), entity, fakePlayer, DamageSource.GENERIC);

      for (ItemStack itemstack : loottable.generateLootForPools(entity.deathLootTableSeed == 0L ? entity.rand : new Random(entity.deathLootTableSeed), context)) {
        entity.entityDropItem(itemstack, 0.0F);
      }
    }
  }
}