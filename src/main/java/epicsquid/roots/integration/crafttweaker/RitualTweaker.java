package epicsquid.roots.integration.crafttweaker;

import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.PyreCraftingRecipe;
import epicsquid.roots.recipe.conditions.Condition;
import epicsquid.roots.recipe.conditions.ConditionItems;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Stream;

@ZenRegister
@ZenClass("mods." + Roots.MODID + ".Ritual")
public class RitualTweaker {

  @ZenMethod
  public static void modifyRitual(String name, IIngredient[] inputs) throws IllegalArgumentException {
    if (inputs.length != 5) {
      throw new IllegalArgumentException("Rituals must have 5 items: " + name);
    }
    CraftTweaker.LATE_ACTIONS.add(new Modify(name, Stream.of(inputs).map(CraftTweakerMC::getIngredient).toArray(Ingredient[]::new)));
  }

  private static class Modify extends BaseAction {
    private String name;
    private Ingredient[] inputs;

    private Modify(String name, Ingredient[] inputs) {
      super("Ritual Modification");

      this.name = name;
      this.inputs = inputs;
    }

    @Override
    public String describe() {
      return "Modifying Ritual named: " + name;
    }

    @Override
    public void apply() {
      RitualBase ritual = RitualRegistry.getRitual(name);
      if (ritual == null) {
        CraftTweakerAPI.logError("Invalid ritual or no ritual by the name of \"" + name + "\" exists.");
        return;
      }
      ConditionItems newRecipe = new ConditionItems((Object[]) inputs);
      List<Condition> conditions = ritual.getConditions();
      ListIterator<Condition> iterator = conditions.listIterator();
      while (iterator.hasNext()) {
        Condition cond = iterator.next();
        if (cond instanceof ConditionItems) {
          iterator.remove();
          iterator.add(newRecipe);
          return;
        }
      }
      // If we haven't returned already then, for some reason, the above code failed to find it.
      ritual.addCondition(newRecipe);
    }
  }
}
