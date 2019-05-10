package epicsquid.roots.recipe.crafting;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.common.crafting.JsonContext;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

public class ShapedGroveCraftingRecipe extends GroveCraftingRecipe implements IShapedRecipe {
  @Nonnull
  protected ItemStack output;
  protected NonNullList<Ingredient> input;
  protected int width;
  protected int height;
  protected boolean mirrored;

  public ShapedGroveCraftingRecipe(String group, @Nonnull ItemStack result, CraftingHelper.ShapedPrimer primer) {
    super(group);
    output = result.copy();
    this.width = primer.width;
    this.height = primer.height;
    this.input = primer.input;
    this.mirrored = primer.mirrored;
  }

  @Nonnull
  @Override
  public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
    return output.copy();
  }

  @Nonnull
  @Override
  public ItemStack getRecipeOutput() {
    return output;
  }

  @Override
  public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World world) {
    for (int x = 0; x <= inv.getWidth(); x++) {
      for (int y = 0; y <= inv.getHeight(); x++) {
        if (checkMatch(inv, x, y, false)) {
          return findGrove(inv, world);
        }

        if (mirrored && checkMatch(inv, x, y, true)) {
          return findGrove(inv, world);
        }
      }
    }

    return false;
  }

  protected boolean checkMatch(InventoryCrafting inv, int startX, int startY, boolean mirror) {
    for (int x = 0; x < inv.getWidth(); x++) {
      for (int y = 0; y < inv.getHeight(); y++) {
        int subX = x - startX;
        int subY = y - startY;
        Ingredient target = Ingredient.EMPTY;

        if (subX >= 0 && subY >= 0 && subX < width && subY < height) {
          if (mirror) {
            target = input.get(width - subX - 1 + subY * width);
          } else {
            target = input.get(subX + subY * width);
          }
        }

        if (!target.apply(inv.getStackInRowAndColumn(x, y))) {
          return false;
        }
      }
    }

    return true;
  }

  public ShapedGroveCraftingRecipe setMirrored(boolean mirror) {
    mirrored = mirror;
    return this;
  }

  @Nonnull
  @Override
  public NonNullList<Ingredient> getIngredients() {
    return this.input;
  }

  @Override
  public int getRecipeWidth() {
    return width;
  }

  @Override
  public int getRecipeHeight() {
    return height;
  }

  @Override
  public boolean canFit(int width, int height) {
    return width >= this.width && height >= this.height;
  }

  @SuppressWarnings("unused")
  public static class GroveFactory implements IRecipeFactory {
    public ShapedGroveCraftingRecipe parse(JsonContext context, JsonObject json) {
      String group = JsonUtils.getString(json, "group", "");
      //if (!group.isEmpty() && group.indexOf(':') == -1)
      //    group = context.getModId() + ":" + group;

      Map<Character, Ingredient> ingMap = Maps.newHashMap();
      for (Map.Entry<String, JsonElement> entry : JsonUtils.getJsonObject(json, "key").entrySet()) {
        if (entry.getKey().length() != 1)
          throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
        if (" ".equals(entry.getKey()))
          throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");

        ingMap.put(entry.getKey().toCharArray()[0], CraftingHelper.getIngredient(entry.getValue(), context));
      }

      ingMap.put(' ', Ingredient.EMPTY);

      JsonArray patternJ = JsonUtils.getJsonArray(json, "pattern");

      if (patternJ.size() == 0)
        throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");

      String[] pattern = new String[patternJ.size()];
      for (int x = 0; x < pattern.length; ++x) {
        String line = JsonUtils.getString(patternJ.get(x), "pattern[" + x + "]");
        if (x > 0 && pattern[0].length() != line.length())
          throw new JsonSyntaxException("Invalid pattern: each row must  be the same width");
        pattern[x] = line;
      }

      CraftingHelper.ShapedPrimer primer = new CraftingHelper.ShapedPrimer();
      primer.width = pattern[0].length();
      primer.height = pattern.length;
      primer.mirrored = JsonUtils.getBoolean(json, "mirrored", true);
      primer.input = NonNullList.withSize(primer.width * primer.height, Ingredient.EMPTY);

      Set<Character> keys = Sets.newHashSet(ingMap.keySet());
      keys.remove(' ');

      int x = 0;
      for (String line : pattern) {
        for (char chr : line.toCharArray()) {
          Ingredient ing = ingMap.get(chr);
          if (ing == null)
            throw new JsonSyntaxException("Pattern references symbol '" + chr + "' but it's not defined in the key");
          primer.input.set(x++, ing);
          keys.remove(chr);
        }
      }

      if (!keys.isEmpty())
        throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + keys);

      ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
      return new ShapedGroveCraftingRecipe(group, result, primer);
    }
  }
}
