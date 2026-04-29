package mysticmods.roots.integration.jei.transfer;

import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModContainers;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.inventory.fake.GroveContainer;
import mysticmods.roots.recipe.grove.GroveRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;

import java.util.List;
import java.util.Optional;

public class GroveTransferInfo implements IRecipeTransferInfo<GroveContainer, GroveRecipe> {
  @Override
  public Class<? extends GroveContainer> getContainerClass() {
    return GroveContainer.class;
  }

  @Override
  public Optional<MenuType<GroveContainer>> getMenuType() {
    return Optional.of(ModContainers.GROVE.value());
  }

  @Override
  public RecipeType<GroveRecipe> getRecipeType() {
    return RootsJEIPlugin.GROVE_RECIPE_TYPE;
  }

  @Override
  public boolean canHandle(GroveContainer container, GroveRecipe recipe) {
    if (recipe.getResultItem(Minecraft.getInstance().level.registryAccess()).is(RootsTags.Items.RUNESTONE)) {
      return true;
    }
    return true;
  }

  @Override
  public List<Slot> getRecipeSlots(GroveContainer container, GroveRecipe recipe) {
    return container.recipeSlots();
  }

  @Override
  public List<Slot> getInventorySlots(GroveContainer container, GroveRecipe recipe) {
    return container.inventorySlots();
  }
}
