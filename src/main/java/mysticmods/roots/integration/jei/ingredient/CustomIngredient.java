package mysticmods.roots.integration.jei.ingredient;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mysticmods.roots.api.condition.ConditionIngredient;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.integration.jei.categories.defaulted.IPositioned;
import mysticmods.roots.util.Pos;
import mysticmods.roots.integration.jei.ingredient.condition.RootsConditionRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public record CustomIngredient<I>(
    RecipeIngredientRole role,
    IIngredientType<I> type,
    List<I> ingredients,
    @Nullable
    IIngredientRenderer<I> renderer,
    @Nullable
    Consumer<IRecipeSlotBuilder> singleBuilder,
    @Nullable
    BiConsumer<List<I>, IRecipeSlotBuilder> multiBuilder) {

  public static final BiConsumer<List<ConditionIngredient>, IRecipeSlotBuilder> CONDITION_ITEM_STACK_BUILDER = (conditions, builder) -> {
    List<ItemStack> items = new ArrayList<>();
    for (int k = 0; k < conditions.size(); k++) {
      items.add(conditions.get(k).getVisual().copyWithCount(k + 1));
    }
    builder.addItemStacks(items)
        .setCustomRenderer(VanillaTypes.ITEM_STACK, new RootsConditionRenderer(conditions));
  };

  public static ChanceOutputTooltip chance (ChanceOutput chanceOutput) {
    return new ChanceOutputTooltip(chanceOutput);
  }

  public record ChanceOutputTooltip (ItemStack stack, float chance) implements IRecipeSlotRichTooltipCallback, Consumer<IRecipeSlotBuilder> {
    public ChanceOutputTooltip(ChanceOutput chanceOutput) {
      this(chanceOutput.output(), chanceOutput.chance());
    }

    @Override
    public void onRichTooltip(IRecipeSlotView recipeSlotView, ITooltipBuilder tooltip) {
      tooltip.add(Component.translatable("roots.tooltip.chance", String.format("%.2f", chance * 100)));
    }

    @Override
    public void accept(IRecipeSlotBuilder builder) {
      builder.addItemStack(stack)
          .addRichTooltipCallback(this);
    }
  }

  public CustomIngredient(RecipeIngredientRole role, IIngredientType<I> type, List<I> ingredients) {
    this(role, type, ingredients, null, null, null);
  }

  public CustomIngredient(RecipeIngredientRole role, IIngredientType<I> type, List<I> ingredients, @Nullable IIngredientRenderer<I> renderer) {
    this(role, type, ingredients, renderer, null, null);
  }

  public CustomIngredient(RecipeIngredientRole role, IIngredientType<I> type, List<I> ingredients, @Nullable IIngredientRenderer<I> renderer, @Nullable BiConsumer<List<I>, IRecipeSlotBuilder> multiBuilder) {
    this(role, type, ingredients, renderer, null, multiBuilder);
  }

  public CustomIngredient(RecipeIngredientRole role, IIngredientType<I> type, List<I> ingredients, @Nullable IIngredientRenderer<I> renderer, @Nullable Consumer<IRecipeSlotBuilder> singleBuilder) {
    this(role, type, ingredients, renderer, singleBuilder, null);
  }

  public void fill(Pos positionInfo, IRecipeLayoutBuilder builder, IPositioned recipeInfo) {
    var slot = role == RecipeIngredientRole.INPUT ? builder.addSlot(role, recipeInfo.getIngredientX(positionInfo), recipeInfo.getIngredientY(positionInfo)) : builder.addSlot(role, recipeInfo.getOutputX(positionInfo), recipeInfo.getOutputY(positionInfo));
    if (this.multiBuilder != null) {
      this.multiBuilder.accept(ingredients, slot);
    } else {
      fill(slot);
    }
  }

  public void fill(IRecipeSlotBuilder slotBuilder) {
    var x = slotBuilder.addIngredients(type, ingredients);
    if (this.renderer != null) {
      x.setCustomRenderer(type, renderer);
    }
  }
}
