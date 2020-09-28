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
    this.block = flower.getBlock();
    this.meta = this.block.getMetaFromState(flower);
  }

  @SuppressWarnings("deprecation")
  public FlowerRecipe(ResourceLocation name, int meta, Block block) {
    this.registryName = name;
    this.meta = meta;
    this.flower = block.getStateFromMeta(this.meta);
    this.block = block;
  }

  @Nullable
  public IBlockState getFlower() {
    return flower;
  }

  public ResourceLocation getRegistryName() {
    return registryName;
  }
}
