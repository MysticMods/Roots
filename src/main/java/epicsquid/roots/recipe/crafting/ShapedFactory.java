package epicsquid.roots.recipe.crafting;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import epicsquid.roots.Roots;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public abstract class ShapedFactory<T extends IRecipe> implements IRecipeFactory {
  public T parse(JsonContext context, JsonObject json) {
    return newRecipeFromPrimer(parseInternal(context, json));
  }

  public abstract T newRecipeFromPrimer (PrimerResult result);

  public PrimerResult parseInternal (JsonContext context, JsonObject json) {
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
    return new PrimerResult(group.isEmpty() ? null : new ResourceLocation(Roots.MODID, group), result, primer);
  }

  public static class PrimerResult {
    public ResourceLocation group;
    public ItemStack result;
    public CraftingHelper.ShapedPrimer primer;

    public PrimerResult(ResourceLocation group, ItemStack result, CraftingHelper.ShapedPrimer primer) {
      this.group = group;
      this.result = result;
      this.primer = primer;
    }
  }
}
