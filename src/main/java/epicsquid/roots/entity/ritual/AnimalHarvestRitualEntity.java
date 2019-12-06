package epicsquid.roots.entity.ritual;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.AnimalHarvestFishRecipe;
import epicsquid.roots.ritual.RitualAnimalHarvest;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.util.types.WeightedRegistry;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class AnimalHarvestRitualEntity extends BaseRitualEntity {

  private ObjectOpenHashSet<Class<? extends Entity>> harvestClasses;
  private RitualAnimalHarvest ritual;

  public AnimalHarvestRitualEntity(EntityType<?> entityTypeIn, World worldIn) {
    super(entityTypeIn, worldIn);
  }

/*  public AnimalHarvestRitualEntity(World worldIn) {
    super(worldIn);

    this.harvestClasses = ModRecipes.getAnimalHarvestClasses();
    this.ritual = (RitualAnimalHarvest) RitualRegistry.ritual_animal_harvest;
  }*/

  @Override
  protected void registerData() {
    this.getDataManager().register(lifetime, RitualRegistry.ritual_animal_harvest.getDuration() + 20);
  }

  @Override
  public void tick() {
    super.tick();

    if (this.ticksExisted % ritual.interval == 0) {
      for (int i = 0; i < ritual.count; i++) {
        if (doHarvest()) break;
      }
    }
  }

  private boolean doHarvest() {
    List<LivingEntity> entityList = Util.getEntitiesWithinRadius(world, (entity) -> harvestClasses.contains(entity.getClass()), getPosition(), ritual.radius_x, ritual.radius_y, ritual.radius_z);
    if (true /*RitualConfig.animalHarvestDoFish*/) {
      List<BlockPos> waterSourceBlocks = Util.getBlocksWithinRadius(world, getPosition(), ritual.radius_x, ritual.radius_y, ritual.radius_z, (p) -> {
        BlockState state = world.getBlockState(p);
        return (state.getMaterial() == Material.WATER && state.getProperties().contains(FlowingFluidBlock.LEVEL) && state.get(FlowingFluidBlock.LEVEL) == 0);
      });
      WeightedRegistry<AnimalHarvestFishRecipe> recipes = new WeightedRegistry<>(ModRecipes.getFishRecipes());
      if (!recipes.isEmpty() && !waterSourceBlocks.isEmpty() && (rand.nextInt(ritual.fish_chance) == 0 || entityList.isEmpty())) {
        AnimalHarvestFishRecipe recipe = recipes.getRandomItem(rand);
        BlockPos pos = waterSourceBlocks.get(rand.nextInt(waterSourceBlocks.size()));
        if (!world.isRemote) {
          ItemStack stack = recipe.getItemStack().copy();
          // Protection against those nasty "must be positive" errors
          stack.setCount(ritual.fish_count + Math.max(0, (rand.nextInt(ritual.fish_additional) - 2)));
          InventoryHelper.spawnItemStack(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, stack);
        }
        //PacketHandler.sendToAllTracking(new MessageRampantLifeInfusionFX(pos.getX(), pos.getY() + 1, pos.getZ()), this);
        return true;
      }
    }
    if (entityList.isEmpty()) return false;
    LivingEntity entity = entityList.get(Util.rand.nextInt(entityList.size()));

    boolean didDrops = false;

    if (!world.isRemote) {
      // TODO: What replaced capture drops?
/*      entity.captureDrops = true;
      entity.capturedDrops.clear();
      int looting = Util.rand.nextInt(ritual.looting_chance) == 0 ? ritual.looting_value : 0;
      dropLoot(entity, looting);
      entity.captureDrops = false;
      if (!ForgeHooks.onLivingDrops(entity, DamageSource.GENERIC, entity.capturedDrops, looting, false)) {
        for (ItemEntity item : entity.capturedDrops) {
          item.motionY = 0;
          item.motionX = 0;
          item.motionZ = 0;
          world.spawnEntity(item);
          didDrops = true;
        }
      }*/
    }

    if (didDrops) {
      entity.addPotionEffect(new EffectInstance(Effects.GLOWING, ritual.glowing, 0, false, false));
    }
    return didDrops;
  }

  private static Method getLootTable = null;

  private ResourceLocation getLootTable(MobEntity entity) {
    if (getLootTable == null) {
      getLootTable = ObfuscationReflectionHelper.findMethod(MobEntity.class, "func_184647_J", ResourceLocation.class);
      getLootTable.setAccessible(true);
    }

    try {
      return (ResourceLocation) getLootTable.invoke(entity);
    } catch (IllegalAccessException | InvocationTargetException e) {
      return null;
    }
  }

  // TODO: Reimplement this

/*  private void dropLoot(MobEntity entity, int looting) {
    ResourceLocation resourcelocation = entity.deathLootTable;

    if (resourcelocation == null) {
      resourcelocation = getLootTable(entity);
    }

    if (resourcelocation != null) {
      LootTable loottable = entity.world.getLootTableManager().getLootTableFromLocation(resourcelocation);
      entity.deathLootTable = null;
      FakePlayer fakePlayer = FakePlayerFactory.getInstance((ServerWorld) entity.world);
      LootContext context = new LootContext(looting, (ServerWorld) entity.world, entity.world.getLootTableManager(), entity, fakePlayer, DamageSource.GENERIC);

      for (ItemStack itemstack : loottable.generateLootForPools(entity.deathLootTableSeed == 0L ? entity.rand : new Random(entity.deathLootTableSeed), context)) {
        entity.entityDropItem(itemstack, 0.0F);
      }
    }
  }*/
}