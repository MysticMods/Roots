package epicsquid.roots.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocArg;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenDocClass("mods.roots.FlowerGrowth")
@ZenDocAppend({"docs/include/flower_growth.example.md"})
@ZenRegister
@ZenClass("mods." + Roots.MODID + ".FlowerGrowth")
public class FlowerTweaker {
  @ZenDocMethod(
      order=1,
      args = {
          @ZenDocArg(arg="name", info="The name of the recipe you wish to remove")
      }
  )
  @ZenMethod
  public static void removeRecipe(String name) {
    CraftTweaker.LATE_ACTIONS.add(new Remove(name));
  }

  @ZenDocMethod(
      order=2,
      args = {
          @ZenDocArg(arg="name", info="The name of the recipe that you're adding"),
          @ZenDocArg(arg="state", info="The state of the block of the flower")
      }
  )
  @ZenMethod
  public static void addRecipeBlockState(String name, IBlockState state) {
    CraftTweaker.LATE_ACTIONS.add(new FlowerBlockState(name, CraftTweakerMC.getBlockState(state)));
  }

  @ZenDocMethod(
      order=3,
      args = {
          @ZenDocArg(arg="name", info="The name of the recipe that you're adding"),
          @ZenDocArg(arg="block", info="The block of the flower to be placed"),
          @ZenDocArg(arg="meta", info="The meta of the state of the flower block")
      }
  )
  @ZenMethod
  public static void addRecipeBlock(String name, IBlock block, int meta) {
    CraftTweaker.LATE_ACTIONS.add(new FlowerBlockMeta(name, CraftTweakerMC.getBlock(block), meta));
  }

  private static class Remove extends Action {
    private final ResourceLocation name;

    public Remove(String name) {
      super("remove_flower");
      if (name.contains(":")) {
        this.name = new ResourceLocation(name);
      } else {
        this.name = new ResourceLocation(Roots.MODID, name);
      }
    }

    @Override
    public void apply() {
      ModRecipes.removeFlowerRecipe(this.name);
    }

    @Override
    public String describe () {
      return String.format("Recipe to remove %s from FlowerGrowth", name);
    }
  }

  private static class FlowerBlockState extends Action {
    private final net.minecraft.block.state.IBlockState state;
    private final String name;

    protected FlowerBlockState(String name, net.minecraft.block.state.IBlockState state) {
      super("add_flower_block_state");
      this.name = name;
      this.state = state;
    }

    @Override
    public void apply() {
      ModRecipes.addFlowerRecipe(name, state);
    }

    @Override
    public String describe () {
      return String.format("Recipe to add %s to FlowerGrowth", state);
    }
  }

  private static class FlowerBlockMeta extends Action {
    private final Block block;
    private final int meta;
    private final String name;

    protected FlowerBlockMeta(String name, Block block, int meta) {
      super("add_block_meta_flower");
      this.name = name;
      this.block = block;
      this.meta = meta;
    }

    @Override
    public void apply() {
      ModRecipes.addFlowerRecipe(name, block, meta);
    }

    @Override
    public String describe () {
      return String.format("Recipe to produce %s:%s with FlowerGrowth", block, meta);
    }
  }
}
