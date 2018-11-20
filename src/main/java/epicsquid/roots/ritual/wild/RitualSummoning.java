package epicsquid.roots.ritual.wild;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import epicsquid.mysticallib.util.ListUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.mysticalworld.entity.EntityBeetle;
import epicsquid.mysticalworld.entity.EntityDeer;
import epicsquid.mysticalworld.entity.EntityFox;
import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.tileentity.TileEntityOffertoryPlate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualSummoning extends RitualBase {

  private static Map<Class<? extends Entity>, List<ItemStack>> entityItem = new HashMap<>();

  public RitualSummoning(String name, int duration) {
    super(name, duration);
    addIngredients(new ItemStack(Items.WHEAT));
    addIngredients(new ItemStack(Items.EGG));
    addIngredients(new ItemStack(Items.ENDER_PEARL));
    addIngredients(new ItemStack(ModItems.wildroot));
    addIngredients(new ItemStack(ModItems.fungus_cap));

    entityItem.put(EntityChicken.class, Lists.newArrayList(new ItemStack(Items.CHICKEN)));
    entityItem.put(EntityCow.class, Lists.newArrayList(new ItemStack(Items.BEEF)));
    entityItem.put(EntityPig.class, Lists.newArrayList(new ItemStack(Items.PORKCHOP)));

    entityItem.put(EntityFox.class, Lists.newArrayList(new ItemStack(ModItems.pelt)));
    entityItem.put(EntityBeetle.class, Lists.newArrayList(new ItemStack(ModItems.carapace)));
    entityItem.put(EntityDeer.class, Lists.newArrayList(new ItemStack(Items.LEATHER)));
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    List<TileEntityOffertoryPlate> plateList = Util.getTileEntitiesWithin(world, TileEntityOffertoryPlate.class, pos, OFFERTORY_RADIUS);
    if (plateList.size() <= 0) {
      return;
    }
    List<ItemStack> offertoryItems = new ArrayList<>();
    for (TileEntityOffertoryPlate plate : plateList) {
      if (plate.getHeldItem() != ItemStack.EMPTY) {
        offertoryItems.add(plate.getHeldItem());
      }
    }

    for (Map.Entry<Class<? extends Entity>, List<ItemStack>> entry : entityItem.entrySet()) {
      if (ListUtil.stackListsMatch(entry.getValue(), offertoryItems)) {

        Entity entity = null;
        try {
          Constructor<? extends Entity> cons = entry.getKey().getDeclaredConstructor(World.class);
          entity = cons.newInstance(world);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
          e.printStackTrace();
        }
        if (entity == null) {
          return;
        }
        entity.setPosition(pos.getX() + 1.5, pos.getY(), pos.getZ());
        if (!world.isRemote) {
          world.spawnEntity(entity);

          for (TileEntityOffertoryPlate plate : plateList) {
            plate.removeItem();
          }
        } else {
          for(int i = 0; i < 10; i++){
            ParticleUtil.spawnParticleStar(world, (float) entity.posX + 0.5f * (Util.rand.nextFloat() - 0.5f),
                (float) (entity.posY + entity.height / 2.5f + (Util.rand.nextFloat())), (float) entity.posZ + 0.5f * (Util.rand.nextFloat() - 0.5f),
                0.125f * (Util.rand.nextFloat() - 0.5f), 0.01875f * (Util.rand.nextFloat()), 0.125f * (Util.rand.nextFloat() - 0.5f), 100, 255, 100, 1.0f,
                1.0f + 2.0f * Util.rand.nextFloat(), 40);
          }
        }
      }
    }
  }

  @Override
  public boolean canFire(World world, BlockPos pos, @Nullable EntityPlayer player) {
    //todo: add wild grove trust needed
    return getStandingStones(world, pos, 3, ModBlocks.runestone_wild) >= 3;
  }
}
