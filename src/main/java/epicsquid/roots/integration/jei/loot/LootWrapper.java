package epicsquid.roots.integration.jei.loot;

import epicsquid.roots.recipe.SpiritDrops;
import jeresources.api.drop.LootDrop;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class LootWrapper implements IRecipeWrapper, ITooltipCallback<ItemStack> {

  public final LootRecipe recipe;

  public LootWrapper(LootRecipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    ingredients.setInput(VanillaTypes.ITEM, this.recipe.bagType);
    ingredients.setOutputs(VanillaTypes.ITEM, this.recipe.stacks);
  }

  public ItemStack getType () {
    return this.recipe.bagType;
  }

  public List<ItemStack> getStacks () {
    return this.recipe.stacks;
  }

  @Override
  public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip) {
    if (slotIndex == 0) {
      return;
    }
    SpiritDrops.SpiritItem spirit = this.recipe.drops.get(slotIndex-1);
    float weight = ((float) spirit.itemWeight / this.recipe.totalWeight) * 100;
    tooltip.add("" + ingredient.getCount() + " (" + String.format("%.2f", weight) + "%)");
  }

  public static class LootRecipe {
    private final ItemStack bagType;
    private final List<SpiritDrops.SpiritItem> drops;
    private final List<ItemStack> stacks;
    private int totalWeight = 0;

    public LootRecipe(Item bagType, List<SpiritDrops.SpiritItem> drops) {
      this.bagType = new ItemStack(bagType);
      this.drops = drops;
      this.stacks = drops.stream().peek(o -> totalWeight += o.itemWeight).map(SpiritDrops.SpiritItem::getItem).collect(Collectors.toList());
    }
  }
}
