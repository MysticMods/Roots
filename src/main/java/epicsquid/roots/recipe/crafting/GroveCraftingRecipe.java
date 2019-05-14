package epicsquid.roots.recipe.crafting;

import epicsquid.roots.Roots;
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

  public boolean findGrove (InventoryCrafting inv, World world) {
    Container cont = inv.eventHandler;
    if (!(cont instanceof ContainerWorkbench)) return false;

    ContainerWorkbench bench = (ContainerWorkbench) cont;

    if (grove_pos != null) {
      IBlockState state = world.getBlockState(grove_pos);
      if (state.getBlock() == ModBlocks.grove_stone && grove_pos.getDistance(bench.pos.getX(), bench.pos.getY(), bench.pos.getZ()) <= 5) return true;
    }

    grove_pos = GroveStoneUtil.findGroveStone(world, bench.pos);
    if (grove_pos != null) return true;

    return false;
  }
}
