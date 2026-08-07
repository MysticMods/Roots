package mysticmods.roots.api.content;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public record IngredientContents(List<Ingredient> ingredients) implements ComponentContents {
  public static final MapCodec<IngredientContents> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Ingredient.LIST_CODEC.fieldOf("ingredients")
      .forGetter(IngredientContents::ingredients)).apply(instance, IngredientContents::new));
  public static final ComponentContents.Type<IngredientContents> TYPE = new ComponentContents.Type<>(CODEC, "ingredient");

  @Override
  public Type<?> type() {
    return TYPE;
  }
}
