package mysticmods.roots.api.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class BaseRecipeData {
  public List<Ingredient> ingredients;
  public List<LevelCondition> levelConditions;
  public List<PlayerCondition> playerConditions;
  public ItemStack result;
  public List<ChanceOutput> chanceOutputs;
  public List<Grant> grants;

  public BaseRecipeData () {
  }

  public BaseRecipeData(List<Ingredient> ingredients, List<LevelCondition> levelConditions, List<PlayerCondition> playerConditions, ItemStack result, List<ChanceOutput> chanceOutputs, List<Grant> grants) {
    this.ingredients = ingredients;
    this.levelConditions = levelConditions;
    this.playerConditions = playerConditions;
    this.result = result;
    this.chanceOutputs = chanceOutputs;
    this.grants = grants;
  }

  public void updateFrom (BaseRecipeData data) {
    this.ingredients = data.ingredients;
    this.levelConditions = data.levelConditions;
    this.playerConditions = data.playerConditions;
    this.result = data.result;
    this.chanceOutputs = data.chanceOutputs;
    this.grants = data.grants;
  }

  public static final MapCodec<BaseRecipeData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").forGetter(o -> o.ingredients),
      LevelCondition.CODEC.listOf().fieldOf("levelConditions").forGetter(o -> o.levelConditions),
      PlayerCondition.CODEC.listOf().fieldOf("playerConditions").forGetter(o -> o.playerConditions),
      ItemStack.CODEC.fieldOf("result").forGetter(o -> o.result),
      ChanceOutput.CODEC.listOf().fieldOf("chanceOutputs").forGetter(o -> o.chanceOutputs),
      Grant.CODEC.listOf().fieldOf("grants").forGetter(o -> o.grants)
  ).apply(instance, BaseRecipeData::new));
}
