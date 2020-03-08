package epicsquid.roots.integration.crafttweaker.tweaks;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.integration.crafttweaker.Action;
import epicsquid.roots.integration.crafttweaker.tweaks.transmutation.Predicate;
import epicsquid.roots.integration.crafttweaker.tweaks.transmutation.WorldPredicate;
import epicsquid.roots.recipe.TransmutationRecipe;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocArg;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@ZenDocClass("mods.roots.Transmutation")
@ZenDocAppend({"docs/include/transmutation.example.md"})
@ZenRegister
@ZenClass("mods." + Roots.MODID + ".Transmutation")
public class TransmutationTweaker {
  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "name", info = "the name of the recipe being removed")
      }
  )
  @ZenMethod
  public static void removeRecipe(String name) {
    CraftTweaker.LATE_ACTIONS.add(new Remove(name));
  }

  @ZenDocMethod(
      order = 2,
      args = {
          @ZenDocArg(arg = "name", info = "the name of the recipe being created"),
          @ZenDocArg(arg = "start", info = "the predicate describing the starting state being converted"),
          @ZenDocArg(arg = "result", info = "the block state to convert to"),
          @ZenDocArg(arg = "condition", info = "the condition of this transition (can be null)")
      }
  )
  @ZenMethod
  public static void addStateToStateRecipe(String name, Predicate<?> start, IBlockState result, @Nullable WorldPredicate<?> condition) {
    CraftTweaker.LATE_ACTIONS.add(new AddStateToState(name, start, result, condition));
  }

  @ZenDocMethod(
      order = 3,
      args = {
          @ZenDocArg(arg = "name", info = "the name of the recipe being created"),
          @ZenDocArg(arg = "start", info = "the predicate describing the starting state being converted"),
          @ZenDocArg(arg = "result", info = "the item stack to convert to"),
          @ZenDocArg(arg = "condition", info = "the condition of this transition (can be null)")
      }
  )
  @ZenMethod
  public static void addStateToItemRecipe(String name, Predicate<?> start, IItemStack result, @Nullable WorldPredicate<?> condition) {
    CraftTweaker.LATE_ACTIONS.add(new AddStateToItem(name, start, result, condition));
  }

  private static class AddStateToState extends Action {
    private final ResourceLocation name;
    private Predicate<?> start;
    private IBlockState result;
    private WorldPredicate<?> condition;

    public AddStateToState(String name, Predicate<?> start, IBlockState result, WorldPredicate<?> condition) {
      super("AddStateToState");
      this.name = new ResourceLocation(Roots.MODID, name);
      this.start = start;
      this.result = result;
      this.condition = condition;
    }

    @Override
    public void apply() {
      TransmutationRecipe recipe = new TransmutationRecipe(start.get()).state(CraftTweakerMC.getBlockState(result)).condition(condition == null ? null : condition.get());
      recipe.setRegistryName(name);
      ModRecipes.addTransmutationRecipe(recipe);
    }

    @Override
    public String describe() {
      return "Add a State-to-State conversion recipe, resulting state: " + result.toString();
    }
  }

  private static class AddStateToItem extends Action {
    private final ResourceLocation name;
    private Predicate<?> start;
    private IItemStack result;
    private WorldPredicate<?> condition;

    public AddStateToItem(String name, Predicate<?> start, IItemStack result, WorldPredicate<?> condition) {
      super("AddStateToItem");
      this.name = new ResourceLocation(Roots.MODID, name);
      this.start = start;
      this.result = result;
      this.condition = condition;
    }

    @Override
    public void apply() {
      TransmutationRecipe recipe = new TransmutationRecipe(start.get()).item(CraftTweakerMC.getItemStack(result)).condition(condition == null ? null : condition.get());
      recipe.setRegistryName(name);
      ModRecipes.addTransmutationRecipe(recipe);
    }

    @Override
    public String describe() {
      return "Add a State-to-Item conversion recipe, resulting state: " + result.toString();
    }
  }

  private static class Remove extends Action {
    private final ResourceLocation name;

    public Remove(String name) {
      super("remove_transmutation");
      if (name.contains(":")) {
        this.name = new ResourceLocation(name);
      } else {
        this.name = new ResourceLocation(Roots.MODID, name);
      }
    }

    @Override
    public void apply() {
      ModRecipes.removeTransmutationRecipe(this.name);
    }

    @Override
    public String describe() {
      return String.format("Recipe to remove %s from Transmutation", name);
    }
  }

}
