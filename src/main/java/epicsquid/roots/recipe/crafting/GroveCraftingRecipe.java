package epicsquid.roots.recipe.crafting;

import com.sun.istack.internal.Nullable;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.config.GroveCraftingConfig;
import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public interface GroveCraftingRecipe extends IRecipe {
  BlockPos getGrovePos();

  void setGrovePos(BlockPos pos);

  default boolean testPos(World world, BlockPos pos) {
    IBlockState state = world.getBlockState(pos);
    return state.getBlock() == ModBlocks.grove_stone;
  }

  default boolean findGrove(InventoryCrafting inv, World world) {
    if (getGrovePos() != null && testPos(world, getGrovePos())) {
      return true;
    }

    Container cont = inv.eventHandler;
    if (cont instanceof ContainerWorkbench) {

      ContainerWorkbench bench = (ContainerWorkbench) cont;

      setGrovePos(findGroveStone(world, bench.pos));
      return getGrovePos() != null;
    } else {
      Map<Class<?>, Field> map = GroveCraftingConfig.getClasses();
      Class clazz = cont.getClass();
      Field f = map.get(clazz);
      if (f == null) {
        for (Map.Entry<Class<?>, Field> c : map.entrySet()) {
          if (c.getKey().isAssignableFrom(clazz)) {
            f = c.getValue();
            break;
          }
        }
        if (f == null) return false;
      }

      BlockPos pos;

      try {
        pos = (BlockPos) f.get(cont);
      } catch (IllegalAccessException e) {
        return false;
      }

      setGrovePos(findGroveStone(world, pos));
      return getGrovePos() != null;
    }
  }

  int GROVE_STONE_RADIUS = 5;

  @Nullable
  default BlockPos findGroveStone(World world, BlockPos near) {
    List<BlockPos> positions = Util.getBlocksWithinRadius(world, near, GROVE_STONE_RADIUS, GROVE_STONE_RADIUS, GROVE_STONE_RADIUS, ModBlocks.grove_stone);
    if (positions.isEmpty()) return null;
    return positions.get(0);
  }
}
