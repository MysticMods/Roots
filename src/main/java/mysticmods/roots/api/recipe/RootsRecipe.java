package mysticmods.roots.api.recipe;

import mysticmods.roots.recipe.fey.FeyCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import noobanidus.libs.noobutil.block.entities.IReferentialBlockEntity;
import noobanidus.libs.noobutil.crafting.Crafting;
import noobanidus.libs.noobutil.ingredient.IngredientStack;
import noobanidus.libs.noobutil.processor.Processor;

import java.util.ArrayList;
import java.util.List;

public abstract class RootsRecipe<H extends IItemHandler, T extends TileEntity & IReferentialBlockEntity, W extends Crafting<H, T>> implements IRootsRecipe<H, T, W> {
  protected final NonNullList<IngredientStack> ingredients;
  protected final ItemStack result;
  protected final List<Processor<W>> processors = new ArrayList<>();
  protected final ResourceLocation recipeId;

  public RootsRecipe(NonNullList<IngredientStack> ingredients, ItemStack result, ResourceLocation recipeId) {
    this.ingredients = ingredients;
    this.result = result;
    this.recipeId = recipeId;
  }

  @Override
  public NonNullList<IngredientStack> getIngredientStacks() {
    return ingredients;
  }

  @Override
  public ItemStack getResultItem() {
    return result.copy();
  }

  @Override
  public ResourceLocation getId() {
    return recipeId;
  }

  @Override
  public List<Processor<W>> getProcessors() {
    return processors;
  }

  @Override
  public void addProcessor(Processor<W> processor) {
    this.processors.add(processor);
  }
}
