/*package epicsquid.roots.integration.crafttweaker.tweaks;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.integration.crafttweaker.Action;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocArg;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

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
          @ZenDocArg(arg = "name", info = "the name of the recipe being added (must be unique)"),
          @ZenDocArg(arg = "state1", info = "the initial state of the block as defined as a blockstate"),
          @ZenDocArg(arg = "state2", info = "the state that the initial state should be converted into")
      }
  )
  @ZenMethod
  public static void addBlockToBlockRecipe(String name, IBlockState state1, IBlockState state2) {
    CraftTweaker.LATE_ACTIONS.add(new BlockToBlock(name, CraftTweakerMC.getBlockState(state1), CraftTweakerMC.getBlockState(state2)));
  }

  @ZenDocMethod(
      order = 3,
      args = {
          @ZenDocArg(arg = "name", info = "the name of the recipe being added (must be unique)"),
          @ZenDocArg(arg = "state", info = "the initial state that is looked for when converting (as a block state)"),
          @ZenDocArg(arg = "stack", info = "the item stack that replaces the block state")
      }
  )
  @ZenMethod
  public static void addBlockToItemRecipe(String name, IBlockState state, IItemStack stack) {
    CraftTweaker.LATE_ACTIONS.add(new BlockToItem(name, CraftTweakerMC.getBlockState(state), CraftTweakerMC.getItemStack(stack)));
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

  private static class BlockToBlock extends Action {
    private final BlockState state1;
    private final BlockState state2;
    private final String name;

    protected BlockToBlock(String name, BlockState state1, BlockState state2) {
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
    public String describe() {
      return String.format("Recipe to add %s->%s to Transmutation", state1, state2);
    }
  }

  private static class BlockToItem extends Action {
    private final BlockState state;
    private final ItemStack stack;
    private final String name;

    protected BlockToItem(String name, BlockState state1, ItemStack stack) {
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
    public String describe() {
      return String.format("Recipe to turn %s->%s to Transmutation", state, stack);
    }
  }
}*/
