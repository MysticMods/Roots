package mysticmods.roots.recipe.pyre;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.api.reference.Identifiers;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModSerializers;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static net.minecraft.data.recipes.RecipeBuilder.ROOT_RECIPE_ADVANCEMENT;

public class PyreRecipe extends RootsTileRecipe<PyreInventory, PyreBlockEntity, PyreCrafting> {
  private Ritual ritual;

  public PyreRecipe() {
    super();
  }

  public Ritual getRitual() {
    return ritual;
  }

  public void setRitual(Ritual ritual) {
    this.ritual = ritual;
  }


  @Override
  public RecipeSerializer<?> getSerializer() {
    return null;
    //return ModSerializers.PYRE.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.PYRE.get();
  }

  @Override
  public String getGroup() {
    return Identifiers.PYRE_RECIPE_GROUP;
  }
}
