package epicsquid.roots.ritual;

import java.util.List;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.entity.EntityRitualWarden;
import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualWarden extends RitualBase {

  public RitualWarden(String name, int duration, boolean doUpdateValidity) {
    super(name, duration, doUpdateValidity);
    addIngredient(new ItemStack(Items.DYE, 1, 15));
    addIngredient(new ItemStack(ModItems.pereskia_bulb, 1));
    addIngredient(new ItemStack(ModItems.spirit_herb, 1));
    addIngredient(new ItemStack(ModItems.bark_oak, 1));
    addIngredient(new ItemStack(Items.IRON_CHESTPLATE, 1));
  }

  @Override
  public boolean isValidForPos(World world, BlockPos pos) {
    int threeHighCount = 0;
    int fourHighCount = 0;
    for (int i = -6; i < 7; i++) {
      for (int j = -6; j < 7; j++) {
        IBlockState state = world.getBlockState(pos.add(i, 3, j));
        if (state.getBlock() == ModBlocks.chiseled_runestone) {
          if (world.getBlockState(pos.add(i, 2, j)).getBlock() == ModBlocks.runestone && world.getBlockState(pos.add(i, 1, j)).getBlock() == ModBlocks.runestone
              && world.getBlockState(pos.add(i, 0, j)).getBlock() == ModBlocks.runestone) {
            fourHighCount++;
          }
        } else {
          state = world.getBlockState(pos.add(i, 2, j));
          if (state.getBlock() == ModBlocks.chiseled_runestone) {
            if (world.getBlockState(pos.add(i, 1, j)).getBlock() == ModBlocks.runestone
                && world.getBlockState(pos.add(i, 0, j)).getBlock() == ModBlocks.runestone) {
              threeHighCount++;
            }
          }
        }
      }
    }
    return threeHighCount == 3 && fourHighCount == 3;
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    List<EntityRitualWarden> pastRituals = world.getEntitiesWithinAABB(EntityRitualWarden.class,
        new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 100, pos.getZ() + 1));
    if (pastRituals.size() == 0 && !world.isRemote) {
      EntityRitualWarden ritual = new EntityRitualWarden(world);
      ritual.setPosition(pos.getX() + 0.5, pos.getY() + 6.5, pos.getZ() + 0.5);
      world.spawnEntity(ritual);
    } else if (pastRituals.size() > 0) {
      for (EntityRitualWarden ritual : pastRituals) {
        ritual.getDataManager().set(EntityRitualWarden.getLifetime(), duration + 20);
        ritual.getDataManager().setDirty(EntityRitualWarden.getLifetime());
      }
    }
  }
}