package mysticmods.roots.recipe;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModSerializers;
import mysticmods.roots.item.Dyeable;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class PouchDyeRecipe extends CustomRecipe {

  public PouchDyeRecipe(CraftingBookCategory category) {
    super(category);
  }

  @Override
  public boolean matches(CraftingInput input, Level level) {
    List<ItemStack> items = new ArrayList<>();
    for (int i = 0; i < input.size(); i++) {
      ItemStack item = input.getItem(i);
      if (!item.isEmpty()) {
        items.add(item);
      }
    }

    if (items.size() != 2) {
      return false;
    }

    ItemStack itemstack1 = items.get(0);
    ItemStack itemstack2 = items.get(1);

    if (itemstack2.getItem() instanceof DyeItem && itemstack1.is(RootsTags.Items.DYEABLE)) {
      return true;
    } else if (itemstack1.getItem() instanceof DyeItem && itemstack2.is(RootsTags.Items.DYEABLE)) {
      return true;
    }

    return false;
  }

  @Override
  public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
    List<ItemStack> items = new ArrayList<>();
    for (int i = 0; i < input.size(); i++) {
      ItemStack item = input.getItem(i);
      if (!item.isEmpty()) {
        items.add(item);
      }
    }

    if (items.size() != 2) {
      return ItemStack.EMPTY;
    }

    ItemStack itemstack1 = items.get(0);
    ItemStack itemstack2 = items.get(1);
    DyeItem dye = null;
    ItemStack toModify = null;

    if (itemstack1.getItem() instanceof DyeItem dyeitem) {
      dye = dyeitem;
      toModify = itemstack2.copy();
    } else if (itemstack2.getItem() instanceof DyeItem dyeItem) {
      dye = dyeItem;
      toModify = itemstack1.copy();
    }

    if (dye == null) {
      return ItemStack.EMPTY;
    }


    toModify.set(ModAttachments.DYEABLE, Dyeable.fromColor(dye.getDyeColor()));
    return toModify;
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width * height >= 2;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModSerializers.DYE_POUCH.get();
  }
}
