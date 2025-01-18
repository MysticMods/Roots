package mysticmods.roots.api.recipe;

import mysticmods.roots.api.attachment.Unlock;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.recipe.crafting.IRootsCrafting;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class RootsRecipe<H extends IItemHandler, W extends IRootsCrafting<H>> implements IRootsRecipe<W> {
  protected final BaseRecipeData data = new BaseRecipeData();

  public RootsRecipe() {
  }

  public RootsRecipe(BaseRecipeData data) {
    this.data.updateFrom(data);
  }

  protected BaseRecipeData getData() {
    return data;
  }

  @Override
  public List<LevelCondition> getLevelConditions() {
    if (this.data.levelConditions == null) {
      return Collections.emptyList();
    }
    return this.data.levelConditions;
  }

  @Override
  public List<PlayerCondition> getPlayerConditions() {
    if (this.data.playerConditions == null) {
      return Collections.emptyList();
    }
    return this.data.playerConditions;
  }

  @Override
  public List<ChanceOutput> getChanceOutputs() {
    if (this.data.chanceOutputs == null) {
      return Collections.emptyList();
    }
    return this.data.chanceOutputs;
  }

  @Override
  public List<Unlock<?>> getUnlocks() {
    if (this.data.unlocks == null) {
      return Collections.emptyList();
    }
    return this.data.unlocks;
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    if (this.data.ingredients == null) {
      return NonNullList.create();
    }
    return this.data.ingredients;
  }

  @Override
  public boolean matches(W arg, Level arg2) {
    List<ItemStack> inputs = new ArrayList<>();
    for (int i = 0; i < arg.size(); i++) {
      ItemStack stack = arg.getItem(i);
      if (!stack.isEmpty()) {
        inputs.add(stack);
      }
    }
    return RecipeMatcher.findMatches(inputs, getIngredients()) != null;
  }

  @Override
  public ItemStack assemble(W arg, HolderLookup.Provider arg2) {
    Player player = arg.getPlayer();
    if (player != null) {
      for (Unlock<?> unlock : getUnlocks()) {
        // TODO: Do unlocks
      }
    }

    return getResultItem(arg2).copy();
  }

  @Override
  public boolean canCraftInDimensions(int i, int j) {
    return true;
  }

  @Override
  public ItemStack getResultItem(HolderLookup.Provider arg) {
    if (data.result == null) {
      return ItemStack.EMPTY;
    }
    return data.result;
  }
}
