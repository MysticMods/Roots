package epicsquid.roots.ritual.wild;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

import epicsquid.mysticallib.util.Util;
import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.grove.GroveType;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.recipe.conditions.ConditionGroveFaith;
import epicsquid.roots.recipe.conditions.ConditionItems;
import epicsquid.roots.ritual.RitualBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class RitualAnimalHarvest extends RitualBase {

  public RitualAnimalHarvest(String name, int duration) {
    super(name, duration);

    addCondition(new ConditionItems(
            new ItemStack(ModItems.wildewheet),
            new ItemStack(Items.CHICKEN),
            new ItemStack(Items.MELON),
            new ItemStack(Items.CARROT),
            new ItemStack(ModItems.wildroot)
    ));

    addCondition(new ConditionGroveFaith(GroveType.WILD, 0));
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    List<EntityLiving> livingList = Util.getEntitiesWithinRadius(world, EntityLiving.class, pos, 10, 5, 10);
    if (!world.isRemote) {
      for (EntityLiving entity : livingList) {
        ResourceLocation location = EntityLiving_getLootTable(entity);
        LootTable table = world.getLootTableManager().getLootTableFromLocation(location);
        List<ItemStack> droppedItems = table.generateLootForPools(new Random(),
            new LootContext(0, FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0), world.getLootTableManager(), entity, null,
                DamageSource.GENERIC));
        for (ItemStack s : droppedItems) {
          world.spawnEntity(new EntityItem(world, entity.posX, entity.posY, entity.posZ, s));
        }
      }
    } else {
      for (EntityLiving entity : livingList) {
        ParticleUtil.spawnParticleStar(world, (float) entity.posX + 0.5f * (Util.rand.nextFloat() - 0.5f),
            (float) entity.posY + entity.height / 2.5f + (Util.rand.nextFloat() - 0.5f), (float) entity.posZ + 0.5f * (Util.rand.nextFloat() - 0.5f),
            0.125f * (Util.rand.nextFloat() - 0.5f), 0.01875f * (Util.rand.nextFloat()), 0.125f * (Util.rand.nextFloat() - 0.5f), 100, 255, 100, 1.0f,
            1.0f + 2.0f * Util.rand.nextFloat(), 40);

      }
    }
  }

  private static Method m_EntityLiving_getLootTable = ReflectionHelper.findMethod(EntityLiving.class, "getLootTable", "func_184647_J");

  public static ResourceLocation EntityLiving_getLootTable(EntityLiving entity) {
    try {
      return (ResourceLocation) m_EntityLiving_getLootTable.invoke(entity);
    } catch (IllegalAccessException | InvocationTargetException e) {
      return null;
    }
  }
}
