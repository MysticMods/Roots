/*
package mysticmods.roots.recipe.summon;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModSerializers;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;



import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

// TODO: However this is going to work
public class SummonCreaturesRecipe implements IBoundlessRecipe<SummonCreaturesCrafting>, net.minecraft.world.item.crafting.Recipe<SummonCreaturesCrafting> {
  protected final NonNullList<Ingredient> ingredients;
  protected final EntityType<?> result;
  protected final ResourceLocation recipeId;

  public SummonCreaturesRecipe(NonNullList<Ingredient> ingredients, EntityType<?> result, ResourceLocation recipeId) {
    this.ingredients = ingredients;
    this.result = result;
    this.recipeId = recipeId;
  }

  public EntityType<?> getResultEntity() {
    return result;
  }

  // TODO:
  @Override
  public boolean matches(SummonCreaturesCrafting pInv, Level pLevel) {
    return false;
  }

  @Override
  public ItemStack assemble(SummonCreaturesCrafting pInv) {
    return ItemStack.EMPTY;
  }

  @Override
  public ItemStack getResultItem() {
    return ItemStack.EMPTY;
  }

  @Override
  public ResourceLocation getId() {
    return recipeId;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModSerializers.SUMMON_CREATURES.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.SUMMON_CREATURES.get();
  }
}*/
