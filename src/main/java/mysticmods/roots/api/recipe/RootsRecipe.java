package mysticmods.roots.api.recipe;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.Unlock;
import mysticmods.roots.api.condition.ILevelCondition;
import mysticmods.roots.api.condition.IPlayerCondition;
import mysticmods.roots.api.grove.GroveNumber;
import mysticmods.roots.api.recipe.crafting.IRootsCrafting;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class RootsRecipe<H extends IItemHandler, W extends IRootsCrafting<H>> implements IRootsRecipe<W> {
  protected final BaseRecipeData data = new BaseRecipeData();
  protected final List<ChanceOutput> cachedChanceOutputs = new ArrayList<>();

  public RootsRecipe(BaseRecipeData data) {
    this.data.updateFrom(data);
  }

  protected BaseRecipeData getData() {
    return data;
  }

  @Override
  public List<ILevelCondition> getLevelConditions() {
    if (this.data.levelConditions == null) {
      return Collections.emptyList();
    }
    return this.data.levelConditions;
  }

  @Override
  public List<IPlayerCondition> getPlayerConditions() {
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
  public List<GroveNumber> getPowerRequirements() {
    if (this.data.powerRequirements == null) {
      return Collections.emptyList();
    }
    return this.data.powerRequirements;
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    if (this.data.ingredients == null) {
      return NonNullList.create();
    }
    return this.data.ingredients;
  }

  @Override
  public List<ChanceOutput> getCachedOutputs() {
    return cachedChanceOutputs;
  }

  @Override
  public void buildCachedOutputs(HolderLookup.Provider provider) {
    if (cachedChanceOutputs.isEmpty()) {
      buildCachedOutputs(cachedChanceOutputs, provider);
    }
  }

  @Override
  public boolean matches(W arg, Level arg2) {
    return RecipeUtil.matchesIngredients(this, arg, arg2);
  }

  @Override
  public ItemStack assemble(W arg, HolderLookup.Provider arg2) {
    Player player = arg.getPlayer();
    if (player instanceof ServerPlayer sPlayer) {
      for (Unlock<?> unlock : getUnlocks()) {
        if (RootsAPI.getInstance().canUnlock(sPlayer, unlock)) {
          RootsAPI.getInstance().unlock(sPlayer, unlock);
        }
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

  @Override
  public int getPriority() {
    return data.priority;
  }
}
