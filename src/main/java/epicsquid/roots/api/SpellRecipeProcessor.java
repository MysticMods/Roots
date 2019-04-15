package epicsquid.roots.api;

import java.util.ArrayList;
import java.util.List;

import epicsquid.roots.init.ModItems;
import epicsquid.roots.item.ItemSpellDust;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.common.util.ItemStackUtil;

public class SpellRecipeProcessor implements IComponentProcessor {

  private List<Ingredient> ingredients = new ArrayList<>();
  private String spellName = null;

  @Override
  public void setup(IVariableProvider<String> iVariableProvider) {
    spellName = iVariableProvider.get("spell");
    SpellBase spellBase = SpellRegistry.spellRegistry.get(spellName);
    ingredients = spellBase.getIngredients();
  }

  @Override
  public String process(String s) {
    if (s.startsWith("item")) {
      int index = Integer.parseInt(s.substring(4)) - 1;
      Ingredient ingredient = ingredients.get(index);

      return ItemStackUtil.serializeIngredient(ingredient);
    }
    if (s.equalsIgnoreCase("result")) {
      ItemStack dust = SpellRegistry.getSpell(spellName).getResult();

      return ItemStackUtil.serializeStack(dust);
    }
    return null;
  }

}
