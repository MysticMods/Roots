package epicsquid.roots.recipe;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class FlowerRecipe {
  private final BlockState flower;
  private final ResourceLocation registryName;
  private final int meta;
  private final Block block;
  private final ItemStack stack;
  private final List<Ingredient> allowedSoils;

  public FlowerRecipe(ResourceLocation name, BlockState flower, List<Ingredient> allowedSoils) {
    this.flower = flower;
    this.registryName = name;
    this.block = flower.getBlock();
    this.meta = this.block.getMetaFromState(flower);
    this.stack = new ItemStack(this.block, 1, this.meta);
    this.allowedSoils = allowedSoils;
  }

  public FlowerRecipe(ResourceLocation name, BlockState flower) {
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

  public FlowerRecipe(ResourceLocation name, int meta, Block block, List<Ingredient> allowedSoils) {
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

  public boolean matches(BlockState state) {
    Block block = state.getBlock();
    int meta = block.getMetaFromState(state);
    return matches(block, meta);
  }

  public boolean matches(Block block, int meta) {
    return this.block == block && this.meta == meta;
  }

  @Nullable
  public BlockState getFlower() {
    return flower;
  }

  public List<Ingredient> getAllowedSoils() {
    return allowedSoils;
  }

  public ResourceLocation getRegistryName() {
    return registryName;
  }
}
