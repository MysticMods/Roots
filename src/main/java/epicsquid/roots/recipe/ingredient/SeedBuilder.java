package epicsquid.roots.recipe.ingredient;

import com.google.gson.JsonObject;
import epicsquid.roots.init.ModItems;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

public class SeedBuilder {
  private static final Pattern SEED_PATTERN = Pattern.compile("(?:(?:(?:[A-Z-_.:]|^)seed)|(?:(?:[a-z-_.:]|^)Seed))(?:[sA-Z-_.:]|$)");
  private static Ingredient SEEDS = null;

  public static Ingredient get() {
    if (SEEDS == null) {
      NonNullList<ItemStack> matchingStacks = NonNullList.create();
      for (Item item : Item.REGISTRY) {
        if (!SEED_PATTERN.matcher(item.getTranslationKey()).find()) {
          continue;
        }

        if (item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof NetherWartBlock) {
          continue;
        }

        if (item instanceof ItemSeeds) {
          item.getSubItems(ItemGroup.SEARCH, matchingStacks);
        }
      }
      matchingStacks.add(new ItemStack(ModItems.terra_spores));
      SEEDS = Ingredient.fromStacks(matchingStacks.toArray(new ItemStack[0]));
    }

    return SEEDS;
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
