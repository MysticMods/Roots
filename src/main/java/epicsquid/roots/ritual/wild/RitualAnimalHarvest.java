package epicsquid.roots.ritual.wild;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.recipe.conditions.ConditionItems;
import epicsquid.roots.ritual.RitualBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class RitualAnimalHarvest extends RitualBase {

  private static Method dropLoot = null;

  public RitualAnimalHarvest(String name, int duration) {
    super(name, duration);

    addCondition(new ConditionItems(
            new ItemStack(ModItems.wildewheet),
            new ItemStack(Item.getItemFromBlock(Blocks.WOOL)),
            new ItemStack(Items.MELON),
            new ItemStack(Items.CARROT),
            new ItemStack(ModItems.wildroot)
    ));

//    addCondition(new ConditionGroveFaith(GroveType.WILD, 0));
  }

  public void dropLoot(EntityLiving entity) {
    if (dropLoot == null) {
      dropLoot = ReflectionHelper.findMethod(EntityLivingBase.class, "dropLoot", "func_184610_a", boolean.class, int.class, DamageSource.class);
    }

    try {
      dropLoot.invoke(entity, false, 0, DamageSource.GENERIC);
    } catch (IllegalAccessException | InvocationTargetException e) {
      return;
    }
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    List<EntityLiving> livingList = Util.getEntitiesWithinRadius(world, EntityLiving.class, pos, 10, 5, 10);
    if (!world.isRemote) {
      for (EntityLiving entity : livingList) {
        if (entity.isChild() || entity instanceof IMob) continue; // Children don't drop loot; skip mobs
        entity.captureDrops = true;
        entity.capturedDrops.clear();
        dropLoot(entity);
        entity.captureDrops = false;
        if (!ForgeHooks.onLivingDrops(entity, DamageSource.GENERIC, entity.capturedDrops, 0, false)) {
          for (EntityItem item : entity.capturedDrops) {
            world.spawnEntity(item);
          }
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
}
