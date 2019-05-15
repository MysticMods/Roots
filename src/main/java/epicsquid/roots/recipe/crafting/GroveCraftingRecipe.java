package epicsquid.roots.recipe.crafting;

import epicsquid.roots.Roots;
import epicsquid.roots.config.GroveCraftingConfig;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.util.GroveStoneUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Map;

public abstract class GroveCraftingRecipe implements IRecipe {
  private ResourceLocation registryName;
  private String group;
  private BlockPos grove_pos = null;

  public GroveCraftingRecipe(String group) {
    this.setRegistryName(new ResourceLocation(Roots.MODID, group));
    this.group = group;
  }

  @Override
  public String getGroup() {
    return group;
  }

  @Override
  public IRecipe setRegistryName(ResourceLocation name) {
    this.registryName = name;
    return this;
  }

  @Nullable
  @Override
  public ResourceLocation getRegistryName() {
    return this.registryName;
  }

  @Override
  public Class<IRecipe> getRegistryType() {
    return IRecipe.class;
  }

  private boolean testPos(World world, BlockPos pos) {
    IBlockState state = world.getBlockState(pos);
    return state.getBlock() == ModBlocks.grove_stone;
  }

  public boolean findGrove(InventoryCrafting inv, World world) {
    if (grove_pos != null && testPos(world, grove_pos)) {
      return true;
    }

    Container cont = inv.eventHandler;
    if (cont instanceof ContainerWorkbench) {

      ContainerWorkbench bench = (ContainerWorkbench) cont;

      grove_pos = GroveStoneUtil.findGroveStone(world, bench.pos);
      return grove_pos != null;
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

      grove_pos = GroveStoneUtil.findGroveStone(world, pos);
      return grove_pos != null;
    }
  }
}
