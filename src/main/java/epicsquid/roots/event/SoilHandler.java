package epicsquid.roots.event;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.block.BlockElementalSoil;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.mechanics.Harvest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Roots.MODID)
@SuppressWarnings("unused")
public class SoilHandler {

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onCropHarvest(BlockEvent.HarvestDropsEvent event) {
    if (event.getHarvester() != null) {
      if (event.getState().getBlock() instanceof IPlantable) {
        IBlockState soil = event.getWorld().getBlockState(event.getPos().offset(EnumFacing.DOWN));
        if (event.getState().getBlock() instanceof IPlantable && soil.getBlock()
            .canSustainPlant(soil, event.getWorld(), event.getPos().offset(EnumFacing.DOWN), EnumFacing.UP, (IPlantable) event.getState().getBlock())) {
          if (soil.getBlock() == ModBlocks.elemental_soil_fire) {
            int cookingMultiplier = soil.getValue(BlockElementalSoil.FIRE_MULTIPLIER);
            if (cookingMultiplier > 0) {
              ItemStack seed = Harvest.getSeed(event.getState());

              List<ItemStack> newDrops = new ArrayList<>();
              Random random = new Random();
              boolean foundSeed = false;
              for (ItemStack stack : event.getDrops()) {
                ItemStack result = FurnaceRecipes.instance().getSmeltingResult(stack);
                if (!foundSeed && !seed.isEmpty() && ItemUtil.equalWithoutSize(seed, stack)) {
                  foundSeed = true;
                  newDrops.add(stack);
                  continue;
                } else if (!foundSeed && seed.isEmpty() && stack.getItem() instanceof IPlantable) {
                  foundSeed = true;
                  seed = stack;
                  newDrops.add(stack);
                  continue;
                }
                if (!result.isEmpty()) {
                  ItemStack copy = result.copy();
                  copy.setCount(cookingMultiplier - 1 > 0 ? random.nextInt(cookingMultiplier - 1) + 1 : 1);
                  newDrops.add(copy);
                } else {
                  newDrops.add(stack);
                }
              }
              World world = event.getWorld();
              BlockPos pos = event.getPos();

              if (Harvest.isGrown(event.getState())) {
                if (Util.rand.nextInt(3) == 0 && !world.isRemote) {
                  world.spawnEntity(new EntityXPOrb(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1));
                }
              }

              event.getDrops().clear();
              event.getDrops().addAll(newDrops);
            }
          }
          if (soil.getBlock() == ModBlocks.elemental_soil_earth) {
            if (Harvest.isGrown(event.getState())) {
              int fertility = soil.getValue(BlockElementalSoil.EARTH_FERTILITY);
              if (fertility > 0) {
                List<ItemStack> newDrops = new ArrayList<>();
                Random random = new Random();
                for (ItemStack stack : event.getDrops()) {
                  newDrops.add(stack);
                  if (random.nextInt(3) < fertility) {
                    ItemStack copy = stack.copy();
                    copy.setCount(fertility > 2 ? 2 : 1);
                    newDrops.add(stack.copy());
                  }
                }
                event.getDrops().clear();
                event.getDrops().addAll(newDrops);
              }
            }
          }
        }
      }
    }
  }

  @SubscribeEvent
  public static void onCropsGrowPost(BlockEvent.CropGrowEvent.Post cropGrowEvent) {

    ((BlockElementalSoil) ModBlocks.elemental_soil_water).doHarvest(cropGrowEvent);
  }

  @SubscribeEvent
  public static void onCropsGrowPre(BlockEvent.CropGrowEvent.Pre cropGrowEvent) {
    IBlockState soil = cropGrowEvent.getWorld().getBlockState(cropGrowEvent.getPos().offset(EnumFacing.DOWN));
    IBlockState plant = cropGrowEvent.getWorld().getBlockState(cropGrowEvent.getPos());
    if (plant.getBlock() instanceof IPlantable && soil.getBlock()
        .canSustainPlant(soil, cropGrowEvent.getWorld(), cropGrowEvent.getPos().offset(EnumFacing.DOWN), EnumFacing.UP, (IPlantable) plant.getBlock())) {
      if (soil.getBlock() == ModBlocks.elemental_soil_air) {
        int speed = soil.getValue(BlockElementalSoil.AIR_SPEED);
        if (speed > 0) {
          cropGrowEvent.setResult(Util.rand.nextInt(3) == 0 ? Event.Result.ALLOW : Event.Result.DEFAULT);
        }
      }
    }
  }
}
