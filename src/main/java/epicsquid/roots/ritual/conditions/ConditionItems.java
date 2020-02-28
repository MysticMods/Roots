package epicsquid.roots.ritual.conditions;

import epicsquid.mysticallib.util.ListUtil;
import epicsquid.roots.tileentity.TileEntityPyre;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ConditionItems implements ICondition {

  final private List<Ingredient> ingredients;

  public ConditionItems(Object... stacks) {
    ingredients = new ArrayList<>();
    for (Object stack : stacks) {
      if (stack instanceof Ingredient) {
        ingredients.add((Ingredient) stack);
      } else if (stack instanceof ItemStack) {
        ingredients.add(Ingredient.fromStacks((ItemStack) stack));
      }
    }
  }

  @Override
  public boolean checkCondition(TileEntityPyre tile, @Nullable EntityPlayer player) {
    List<ItemStack> stacks = new ArrayList<>();
    for (int i = 0; i < tile.inventory.getSlots(); i++) {
      stacks.add(tile.inventory.extractItem(i, 1, true));
    }
    return ListUtil.matchesIngredients(stacks, ingredients);
  }

  @Override
  public ITextComponent failMessage() {
    return new TextComponentTranslation("roots.ritual.condition.items");
  }

  public List<Ingredient> getIngredients() {
    return ingredients;
  }
}
