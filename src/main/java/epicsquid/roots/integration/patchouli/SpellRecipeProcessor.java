package epicsquid.roots.integration.patchouli;

import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.common.util.ItemStackUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class SpellRecipeProcessor implements IComponentProcessor {

  private List<Ingredient> ingredients = new ArrayList<>();
  private final List<ItemStack> herbs = new ArrayList<>();
  private final List<String> costs = new ArrayList<>();
  private String spellName = null;

  @Override
  public void setup(IVariableProvider<String> iVariableProvider) {
    spellName = iVariableProvider.get("spell");
    SpellBase spell = SpellRegistry.spellRegistry.get(new ResourceLocation(Roots.MODID, spellName));
    if (spell != null) {
      ingredients = spell.getIngredients();
      for (Map.Entry<Herb, Double> cost : spell.getCosts().entrySet()) {
        herbs.add(new ItemStack(cost.getKey().getItem()));
        costs.add(String.format("x %.3f", cost.getValue()));
      }
    }
  }

  @Override
  public String process(String s) {
    if (ingredients.isEmpty() || herbs.isEmpty() || costs.isEmpty()) {
      return ItemStackUtil.serializeStack(ItemStack.EMPTY);
    }
    if (spellName == null) {
      return "null";
    }
    if (s.startsWith("item")) {
      int index = Integer.parseInt(s.substring(4)) - 1;
      Ingredient ingredient = ingredients.get(index);

      return ItemStackUtil.serializeIngredient(ingredient);
    }
    if (s.equalsIgnoreCase("result")) {
      ItemStack dust = SpellRegistry.getSpell(spellName).getIcon();

      return ItemStackUtil.serializeStack(dust);
    }
    if (s.equals("cost1")) {
      return ItemStackUtil.serializeStack(herbs.get(0));
    }
    if (s.equals("cost2")) {
      if (herbs.size() < 2) return ItemStackUtil.serializeStack(ItemStack.EMPTY);
      return ItemStackUtil.serializeStack(herbs.get(1));
    }
    if (s.equals("cost3")) {
      if (herbs.size() < 3) return ItemStackUtil.serializeStack(ItemStack.EMPTY);
      return ItemStackUtil.serializeStack(herbs.get(2));
    }
    if (s.equals("cost1_text")) {
      return costs.get(0);
    }
    if (s.equals("cost2_text")) {
      if (costs.size() < 2) return "";
      return costs.get(1);
    }
    if (s.equals("cost3_text")) {
      if (costs.size() < 3) return "";
      return costs.get(2);
    }
    return null;
  }
}
