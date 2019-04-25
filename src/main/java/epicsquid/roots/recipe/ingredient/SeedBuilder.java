package epicsquid.roots.recipe.ingredient;

import epicsquid.roots.init.ModItems;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

import java.util.regex.Pattern;

public class SeedBuilder {
  private static final Pattern SEED_PATTERN = Pattern.compile("(?:(?:(?:[A-Z-_.:]|^)seed)|(?:(?:[a-z-_.:]|^)Seed))(?:[sA-Z-_.:]|$)");
  private static Ingredient SEEDS = null;

  public static Ingredient get () {
    if (SEEDS == null) {
      NonNullList<ItemStack> matchingStacks = NonNullList.create();
      for (Item item : Item.REGISTRY) {
        if (!SEED_PATTERN.matcher(item.getTranslationKey()).find()) {
          continue;
        }

        if (item instanceof ItemBlock && ((ItemBlock) item).getBlock() instanceof BlockNetherWart) {
          continue;
        }

        if (item instanceof ItemSeeds) {
          item.getSubItems(CreativeTabs.SEARCH, matchingStacks);
        }
      }
      matchingStacks.add(new ItemStack(ModItems.terra_spores));
      SEEDS = Ingredient.fromStacks(matchingStacks.toArray(new ItemStack[0]));
    }

    return SEEDS;
  }
}
