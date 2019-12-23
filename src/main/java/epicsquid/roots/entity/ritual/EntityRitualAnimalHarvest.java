package epicsquid.roots.entity.ritual;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.config.RitualConfig;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.network.fx.MessageRampantLifeInfusionFX;
import epicsquid.roots.recipe.AnimalHarvestFishRecipe;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.util.types.WeightedRegistry;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
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
  private ObjectOpenHashSet<Class<? extends EntityLivingBase>> harvestClasses;

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
    if (RitualConfig.animalHarvestDoFish) {
      List<BlockPos> waterSourceBlocks = Util.getBlocksWithinRadius(world, getPosition(), 15, 10, 15, (p) -> {
        IBlockState state = world.getBlockState(p);
        return (state.getMaterial() == Material.WATER && state.getPropertyKeys().contains(BlockLiquid.LEVEL) && state.getValue(BlockLiquid.LEVEL) == 0);
      });
      WeightedRegistry<AnimalHarvestFishRecipe> recipes = new WeightedRegistry<>(ModRecipes.getFishRecipes());
      if (!recipes.isEmpty() && !waterSourceBlocks.isEmpty() && (rand.nextInt(5) == 0 || entityList.isEmpty())) {
        AnimalHarvestFishRecipe recipe = recipes.getRandomItem(rand);
        BlockPos pos = waterSourceBlocks.get(rand.nextInt(waterSourceBlocks.size()));
        if (!world.isRemote) {
          ItemStack stack = recipe.getItemStack().copy();
          // Protection against those nasty "must be positive" errors
          stack.setCount(3 + Math.max(0, (rand.nextInt(3) - 2)));
          ItemUtil.spawnItem(world, pos.add(0, 1, 0), stack);
        }
        PacketHandler.sendToAllTracking(new MessageRampantLifeInfusionFX(pos.getX(), pos.getY() + 1, pos.getZ()), this);
        return true;
      }
    }
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
      entity.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 30, 0, false, false));
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