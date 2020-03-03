package epicsquid.roots.recipe.ingredient;

import com.google.gson.JsonObject;
import epicsquid.roots.init.ModItems;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

public class ArrowBuilder {
  private static Ingredient ARROW = null;

  public static Ingredient get() {
    if (ARROW == null) {
      NonNullList<ItemStack> matchingStacks = NonNullList.create();
      for (Item item : ForgeRegistries.ITEMS) {
        if (item == ModItems.wildwood_quiver) {
          continue;
        }
        if (item instanceof ItemArrow) {
          item.getSubItems(CreativeTabs.SEARCH, matchingStacks);
        }
      }
      ARROW = Ingredient.fromStacks(matchingStacks.toArray(new ItemStack[0]));
    }

    return ARROW;
  }

  @SuppressWarnings("unused")
  public static class Factory implements IIngredientFactory {
    @Nonnull
    @Override
    public Ingredient parse(JsonContext context, JsonObject json) {
      return get();
    }
  }
}
