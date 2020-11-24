package epicsquid.roots.recipe;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

public class FlowerRecipe {
  private final IBlockState flower;
  private final ResourceLocation registryName;
  private final int meta;
  private final Block block;
  private final ItemStack stack;
  private final List<IIngredient> allowedSoils;

  public FlowerRecipe(ResourceLocation name, IBlockState flower, List<IIngredient> allowedSoils) {
    this.flower = flower;
    this.registryName = name;
    this.block = flower.getBlock();
    this.meta = this.block.getMetaFromState(flower);
    this.stack = new ItemStack(this.block, 1, this.meta);
    this.allowedSoils = allowedSoils;
  }

  public FlowerRecipe(ResourceLocation name, IBlockState flower) {
    this.flower = flower;
    this.registryName = name;
    this.block = flower.getBlock();
    this.meta = this.block.getMetaFromState(flower);
    this.stack = new ItemStack(this.block, 1, this.meta);
    this.allowedSoils = Collections.emptyList();
  }

  @SuppressWarnings("deprecation")
  public FlowerRecipe(ResourceLocation name, int meta, Block block) {
    this.registryName = name;
    this.meta = meta;
    this.flower = block.getStateFromMeta(this.meta);
    this.block = block;
    this.stack = new ItemStack(this.block, 1, this.meta);
    this.allowedSoils = Collections.emptyList();
  }

  public FlowerRecipe(ResourceLocation name, int meta, Block block, List<IIngredient> allowedSoils) {
    this.registryName = name;
    this.meta = meta;
    this.flower = block.getStateFromMeta(this.meta);
    this.block = block;
    this.stack = new ItemStack(this.block, 1, this.meta);
    this.allowedSoils = allowedSoils;
  }

  public ItemStack getStack() {
    return this.stack.copy();
  }

  public boolean matches(IBlockState state) {
    Block block = state.getBlock();
    int meta = block.getMetaFromState(state);
    return matches(block, meta);
  }

  public boolean matches(Block block, int meta) {
    return this.block == block && this.meta == meta;
  }

  @Nullable
  public IBlockState getFlower() {
    return flower;
  }

  public List<IIngredient> getAllowedSoils() {
    return allowedSoils;
  }

  public ResourceLocation getRegistryName() {
    return registryName;
  }
}
