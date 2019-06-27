package epicsquid.roots.recipe;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class FlowerRecipe {
  private final IBlockState flower;
  private final ResourceLocation registryName;
  private final int meta;
  private final Block block;

  public FlowerRecipe(ResourceLocation name, IBlockState flower) {
    this.flower = flower;
    this.registryName = name;
    this.meta = -1;
    this.block = null;
  }

  public FlowerRecipe(ResourceLocation name, int meta, Block block) {
    this.registryName = name;
    this.flower = null;
    this.meta = meta;
    this.block = block;
  }

  @Nullable
  @SuppressWarnings("deprecation")
  public IBlockState getFlower() {
    if (flower == null && block != null && meta != -1) {
      return block.getStateFromMeta(meta);
    }
    return flower;
  }

  public ResourceLocation getRegistryName() {
    return registryName;
  }
}
