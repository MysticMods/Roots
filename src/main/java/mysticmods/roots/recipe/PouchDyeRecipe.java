package mysticmods.roots.recipe;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModSerializers;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class PouchDyeRecipe extends CustomRecipe {

  public PouchDyeRecipe(CraftingBookCategory category) {
    super(category);
  }

  public boolean matches(CraftingInput input, Level level) {
    ItemStack itemstack = ItemStack.EMPTY;
    ItemStack list = ItemStack.EMPTY;

    for (int i = 0; i < input.size(); i++) {
      ItemStack itemstack1 = input.getItem(i);
      if (!itemstack1.isEmpty()) {
        if (itemstack1.is(RootsTags.Items.DYEABLE)) {
          if (!itemstack.isEmpty()) {
            return false;
          }

          itemstack = itemstack1;
        } else {
          if (!(itemstack1.getItem() instanceof DyeItem)) {
            return false;
          }

          list = itemstack1;
          break;
        }
      }
    }

    return !itemstack.isEmpty() && !list.isEmpty();
  }

  public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
    DyeItem list = null;
    ItemStack itemstack = ItemStack.EMPTY;

    for (int i = 0; i < input.size(); i++) {
      ItemStack itemstack1 = input.getItem(i);
      if (!itemstack1.isEmpty()) {
        if (itemstack1.is(RootsTags.Items.DYEABLE)) {
          if (!itemstack.isEmpty()) {
            return ItemStack.EMPTY;
          }

          itemstack = itemstack1.copy();
        } else {
          if (!(itemstack1.getItem() instanceof DyeItem dyeitem)) {
            return ItemStack.EMPTY;
          }

          list = dyeitem;
          break;
        }
      }
    }

    if (itemstack.isEmpty() || list == null) {
      return ItemStack.EMPTY;
    }

    itemstack.set(DataComponents.BASE_COLOR, list.getDyeColor());
    return itemstack;
  }

  /**
   * Used to determine if this recipe can fit in a grid of the given width/height
   */
  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width * height >= 2;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModSerializers.DYE_POUCH.get();
  }
}
