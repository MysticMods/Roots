package mysticmods.roots.recipe.runic;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.recipe.WorldCondition;
import mysticmods.roots.api.recipe.WorldRecipe;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.api.reference.Identifiers;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.recipe.SimpleWorldCrafting;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RunicBlockRecipe extends WorldRecipe<SimpleWorldCrafting> {
  protected final List<String> skipProperties = new ArrayList<>();
  protected int durabilityCost = 1;

  public RunicBlockRecipe() {
    super();
  }

  @Override
  public void setIngredients(NonNullList<Ingredient> ingredients) {
  }

  public int getDurabilityCost() {
    return durabilityCost;
  }

  public void setDurabilityCost(int cost) {
    this.durabilityCost = cost;
  }

  public List<String> getSkipProperties() {
    return skipProperties;
  }

  public void setSkipProperties(List<String> skipProperty) {
    this.skipProperties.clear();
    this.skipProperties.addAll(skipProperty);
  }

  @Override
  public BlockState modifyState(SimpleWorldCrafting pContainer, BlockState state, HolderLookup.Provider provider) {
    BlockState newState = outputState;
    for (Property<?> prop : newState.getProperties()) {
      if (!state.hasProperty(prop)) {
        continue;
      }

      if (skipProperties.contains(prop.getName())) {
        continue;
      }

      newState = copyProperty(state, newState, prop);
    }

    return newState;
  }

  private static <T extends Comparable<T>> BlockState copyProperty(BlockState pSourceState, BlockState pTargetState, Property<T> pProperty) {
    return pTargetState.setValue(pProperty, pSourceState.getValue(pProperty));
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return null;
/*    return ModSerializers.RUNIC_BLOCK.get();*/
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.RUNIC_BLOCK.get();
  }

  @Override
  public String getGroup() {
    return Identifiers.RUNIC_BLOCK_RECIPE_GROUP;
  }
}
