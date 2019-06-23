package epicsquid.roots.integration.crafttweaker;

import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods." + Roots.MODID + ".Transmutation")
public class TransmutationTweaker {
  @ZenMethod
  public static void removeRecipe(String name) {
    CraftTweaker.LATE_ACTIONS.add(new Remove(name));
  }

  @ZenMethod
  public static void addBlockToBlockRecipe(String name, IBlockState state1, IBlockState state2) {
    CraftTweaker.LATE_ACTIONS.add(new BlockToBlock(name, CraftTweakerMC.getBlockState(state1), CraftTweakerMC.getBlockState(state2)));
  }

  @ZenMethod
  public static void addBlockToItemRecipe(String name, IBlockState state, IItemStack stack) {
    CraftTweaker.LATE_ACTIONS.add(new BlockToItem(name, CraftTweakerMC.getBlockState(state), CraftTweakerMC.getItemStack(stack)));
  }

  private static class Remove extends BaseAction {
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
    protected String getRecipeInfo() {
      return String.format("Recipe to remove %s from Transmutation", name);
    }
  }

  private static class BlockToBlock extends BaseAction {
    private final net.minecraft.block.state.IBlockState state1;
    private final net.minecraft.block.state.IBlockState state2;
    private final String name;

    protected BlockToBlock(String name, net.minecraft.block.state.IBlockState state1, net.minecraft.block.state.IBlockState state2) {
      super("add_block_to_block_transmutation");
      this.name = name;
      this.state1 = state1;
      this.state2 = state2;
    }

    @Override
    public void apply() {
      ModRecipes.addTransmutationRecipe(name, state1, state2);
    }

    @Override
    protected String getRecipeInfo() {
      return String.format("Recipe to add %s->%s to Transmutation", state1, state2);
    }
  }

  private static class BlockToItem extends BaseAction {
    private final net.minecraft.block.state.IBlockState state;
    private final ItemStack stack;
    private final String name;

    protected BlockToItem(String name, net.minecraft.block.state.IBlockState state1, ItemStack stack) {
      super("add_block_to_item_transmutation");
      this.state = state1;
      this.stack = stack;
      this.name = name;
    }

    @Override
    public void apply() {
      ModRecipes.addTransmutationRecipe(name, state, stack);
    }

    @Override
    protected String getRecipeInfo() {
      return String.format("Recipe to turn %s->%s to Transmutation", state, stack);
    }
  }
}
