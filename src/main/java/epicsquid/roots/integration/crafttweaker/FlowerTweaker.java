package epicsquid.roots.integration.crafttweaker;

import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods." + Roots.MODID + ".FlowerGrowth")
public class FlowerTweaker {
  @ZenMethod
  public static void removeRecipe(String name) {
    CraftTweaker.LATE_ACTIONS.add(new Remove(name));
  }

  @ZenMethod
  public static void addRecipeBlockState(String name, IBlockState state) {
    CraftTweaker.LATE_ACTIONS.add(new FlowerBlockState(name, CraftTweakerMC.getBlockState(state)));
  }

  @ZenMethod
  public static void addRecipeBlock(String name, IBlock block, int meta) {
    CraftTweaker.LATE_ACTIONS.add(new FlowerBlockMeta(name, CraftTweakerMC.getBlock(block), meta));
  }

  private static class Remove extends BaseAction {
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
    protected String getRecipeInfo() {
      return String.format("Recipe to remove %s from FlowerGrowth", name);
    }
  }

  private static class FlowerBlockState extends BaseAction {
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
    protected String getRecipeInfo() {
      return String.format("Recipe to add %s to FlowerGrowth", state);
    }
  }

  private static class FlowerBlockMeta extends BaseAction {
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
    protected String getRecipeInfo() {
      return String.format("Recipe to produce %s:%s with FlowerGrowth", block, meta);
    }
  }
}
