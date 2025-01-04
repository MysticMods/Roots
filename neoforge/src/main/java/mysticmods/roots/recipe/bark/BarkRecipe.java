package mysticmods.roots.recipe.bark;

import mysticmods.roots.api.recipe.WorldRecipe;
import mysticmods.roots.api.reference.Identifiers;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.recipe.SimpleWorldCrafting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.intellij.lang.annotations.Identifier;

public class BarkRecipe extends WorldRecipe<SimpleWorldCrafting> {
  public BarkRecipe() {
    super();
  }

  @Override
  public BlockState modifyState(SimpleWorldCrafting pContainer, BlockState currentState, HolderLookup.Provider provider) {
    BlockState newState = outputState;

    if (currentState.getBlock() instanceof RotatedPillarBlock && outputState.getBlock() instanceof RotatedPillarBlock) {
      newState = outputState.setValue(RotatedPillarBlock.AXIS, currentState.getValue(RotatedPillarBlock.AXIS));
    }

    return super.modifyState(pContainer, newState, provider);
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return null;
    //return ModSerializers.BARK.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.BARK.get();
  }

  @Override
  public String getGroup() {
    return Identifiers.BARK_RECIPE_GROUP;
  }
}
