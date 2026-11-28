package mysticmods.roots.integration.jei.ingredient.condition;

import mezz.jei.api.ingredients.IIngredientRenderer;
import mysticmods.roots.api.condition.ConditionIngredient;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RootsConditionRenderer implements IIngredientRenderer<ItemStack> {
  private final List<ConditionIngredient> conditions;

  public RootsConditionRenderer(List<ConditionIngredient> conditions) {
    this.conditions = conditions;
  }

  @Nullable
  private ConditionIngredient ingredientFromStack(ItemStack stack) {
    int index = stack.getCount() - 1;
    return index >= 0 && index < conditions.size() ? conditions.get(index) : null;
  }

  @Override
  public void render(GuiGraphics guiGraphics, ItemStack ingredient) {
    ConditionIngredient conditionIngredient = ingredientFromStack(ingredient);
    if (conditionIngredient != null) {
      RootsJEIPlugin.CONDITION_RENDERER.render(guiGraphics, conditionIngredient);
    }
  }

  @Override
  public List<Component> getTooltip(ItemStack ingredient, TooltipFlag tooltipFlag) {
    ConditionIngredient conditionIngredient = ingredientFromStack(ingredient);
    if (conditionIngredient != null) {
      return RootsJEIPlugin.CONDITION_RENDERER.getTooltip(conditionIngredient, tooltipFlag);
    }
    return List.of();
  }
}
