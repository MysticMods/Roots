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
    CraftTweaker.LATE_ACTIONS.add(new Add(name, ((ItemBlock) log.getItem()).getBlock(), CraftTweakerMC.getItemStack(bark)));
  }

  private static class Add extends BaseAction {
    private final Block woodLog;
    private final ItemStack bark;
    private final String name;

    public Add(String name, Block woodLog, ItemStack bark) {
      super("add_bark_recipe");
      this.woodLog = woodLog;
      this.bark = bark;
      this.name = name;
    }

    @Override
    public void apply() {
      ModRecipes.addModdedBarkRecipe(name, woodLog, bark);
    }

    @Override
    protected String getRecipeInfo() {
      return String.format("Recipe to add %s->%s to Bark Recipes", woodLog, bark);
    }
  }
}
