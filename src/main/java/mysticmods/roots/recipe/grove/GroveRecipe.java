package mysticmods.roots.recipe.grove;

import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.block.entity.GroveCrafterBlockEntity;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.recipe.mortar.MortarInventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.ArrayList;
import java.util.List;

public class GroveRecipe extends RootsTileRecipe<GroveInventoryWrapper, GroveCrafterBlockEntity, GroveCrafting> {
  public GroveRecipe(ResourceLocation recipeId) {
    super(recipeId);
  }

  @Override
  public boolean matches(GroveCrafting pInv, Level pLevel) {
    List<ItemStack> inputs = new ArrayList<>();
    GroveInventoryWrapper inv = pInv.getHandler();
    for (int i = 0; i < inv.getSlots(); i++) {
      ItemStack stack = inv.getStackInSlot(i);
      if (!stack.isEmpty()) {
        inputs.add(stack);
      }
    }

    return RecipeMatcher.findMatches(inputs, ingredients) != null;
  }

  @Override
  public ItemStack assemble(GroveCrafting pContainer) {
    return super.assemble(pContainer);
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModRecipes.Serializers.GROVE_CRAFTING.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.Types.GROVE;
  }

  public static class Serializer extends RootsRecipe.Serializer<GroveInventoryWrapper, GroveCrafting, GroveRecipe> {
    public Serializer() {
      super(GroveRecipe::new);
    }
  }

  public static class Builder extends RootsRecipe.Builder {

    protected Builder(ItemStack result) {
      super(result);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
      return ModRecipes.Serializers.GROVE_CRAFTING.get();
    }
  }

  public static Builder builder (ItemStack stack) {
    return new Builder(stack);
  }
}
