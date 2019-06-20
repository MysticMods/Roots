package epicsquid.roots.recipe.conditions;

import java.util.ArrayList;
import java.util.List;

import epicsquid.mysticallib.util.ListUtil;
import epicsquid.roots.tileentity.TileEntityBonfire;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class ConditionItems implements Condition {

    final private List<Ingredient> ingredients;

    public ConditionItems(Object... stacks){
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
    public boolean checkCondition(TileEntityBonfire tile, EntityPlayer player) {
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < tile.inventory.getSlots(); i++) {
            stacks.add(tile.inventory.extractItem(i, 1, true));
        }
        return ListUtil.matchesIngredients(stacks, ingredients);
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}
