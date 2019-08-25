package epicsquid.roots.integration.crafttweaker;

import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocArg;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenDocClass("mods.roots.Bark")
@ZenDocAppend({"docs/include/bark.example.md"})
@ZenRegister
@ZenClass("mods." + Roots.MODID + ".Bark")
public class BarkTweaker {

  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg="name", info="the name of the recipe"),
          @ZenDocArg(arg="woodLog", info="the itemstack equivalent of the wood log being broken"),
          @ZenDocArg(arg="bark", info="the itemstack of the type of bark this log produces")
      }
  )
  @ZenMethod
  public static void addRecipe(String name, IItemStack woodLog, IItemStack bark) {
    ItemStack log = CraftTweakerMC.getItemStack(woodLog);
    if (!(log.getItem() instanceof ItemBlock)) {
      CraftTweakerAPI.logError("Provided log " + woodLog + " is not an item block!");
      return;
    }
    CraftTweaker.LATE_ACTIONS.add(new Add(name, CraftTweakerMC.getItemStack(bark), log));
  }

  @ZenDocMethod
      (

      )
  @ZenMethod
  public static void removeRecipe (IItemStack bark) {
    CraftTweaker.LATE_ACTIONS.add(new Remove(CraftTweakerMC.getItemStack(bark)));
  }

  private static class Remove extends BaseAction {
    private final ItemStack bark;

    public Remove(ItemStack bark) {
      super("remove_bark_recipe");
      this.bark = bark;
    }

    @Override
    public void apply() {
      ModRecipes.removeBarkRecipe(bark);
    }

    @Override
    protected String getRecipeInfo() {
      return String.format("Recipe to remove %s from Bark Recipes", bark.toString());
    }
  }

  private static class Add extends BaseAction {
    private final ItemStack woodLog;
    private final ItemStack bark;
    private final String name;

    public Add(String name, ItemStack bark, ItemStack woodLog) {
      super("add_bark_recipe");
      this.woodLog = woodLog;
      this.bark = bark;
      this.name = name;
    }

    @Override
    public void apply() {
      ModRecipes.addModdedBarkRecipe(name, bark, woodLog);
    }

    @Override
    protected String getRecipeInfo() {
      return String.format("Recipe to add %s->%s to Bark Recipes", woodLog, bark);
    }
  }
}
