package epicsquid.roots.api;

import java.util.ArrayList;
import java.util.List;

import epicsquid.roots.init.ModItems;
import epicsquid.roots.item.ItemPetalDust;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.common.util.ItemStackUtil;

public class SpellRecipeProcessor implements IComponentProcessor {

  private List<ItemStack> ingredients = new ArrayList<>();
  private String spellName = null;

  @Override
  public void setup(IVariableProvider<String> iVariableProvider) {
    spellName = iVariableProvider.get("spell");
    SpellBase spellBase = SpellRegistry.spellRegistry.get(spellName);
    List<Ingredient> spellIngredients = spellBase.getIngredients();
    for (Ingredient ingredient : spellIngredients) {
      ingredients.add(ingredient.getMatchingStacks()[0]);
    }
  }

  @Override
  public String process(String s) {
    if(s.startsWith("item")) {
      int index = Integer.parseInt(s.substring(4)) - 1;
      ItemStack ingredient = ingredients.get(index);

      return ItemStackUtil.serializeStack(ingredient);
    }
    if (s.equalsIgnoreCase("result")) {
      ItemStack dust = new ItemStack(ModItems.petal_dust);
      ItemPetalDust.createData(dust, SpellRegistry.getSpell(spellName));

      return ItemStackUtil.serializeStack(dust);
    }
    return null;
  }

}
