package epicsquid.mysticallib.recipe.factories;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MultiOreIngredient extends CompoundIngredient {
  public MultiOreIngredient(String... names) {
    this(Arrays.asList(names));
  }

  public MultiOreIngredient(List<String> names) {
    super(names.stream().map(DelayedOreIngredient::new).collect(Collectors.toList()));
  }

  @SuppressWarnings("unused")
  public static class Factory implements IIngredientFactory {
    @Nonnull
    @Override
    public Ingredient parse(JsonContext context, JsonObject json) {
      List<String> names = new ArrayList<>();
      for (JsonElement element : JsonUtils.getJsonArray(json, "ores")) {
        names.add(element.getAsString());
      }
      return new MultiOreIngredient(names);
    }
  }
}
