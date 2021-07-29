package epicsquid.roots.integration.crafttweaker.tweaks;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.oredict.IOreDictEntry;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import crafttweaker.mc1120.item.MCItemStack;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.integration.crafttweaker.Action;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocArg;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ZenDocClass("mods.roots.FlowerGrowth")
@ZenDocAppend({"docs/include/flower_growth.example.md"})
@ZenRegister
@ZenClass("mods." + Roots.MODID + ".FlowerGrowth")
public class FlowerTweaker {
  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "name", info = "The name of the recipe you wish to remove")
      },
      description = "Removes a flower growth recipe by name."
  )
  @ZenMethod
  public static void removeRecipe(String name) {
    CraftTweaker.LATE_ACTIONS.add(new Remove(name));
  }

  @ZenDocMethod(
      order = 2,
      args = {
          @ZenDocArg(arg = "name", info = "The name of the recipe that you're adding"),
          @ZenDocArg(arg = "state", info = "The state of the block of the flower")
      },
      description = "Adds a recipe to create the specific block state during the flower growth ritual."
  )
  @ZenMethod
  public static void addRecipeBlockState(String name, IBlockState state) {
    CraftTweaker.LATE_ACTIONS.add(new FlowerBlockState(name, CraftTweakerMC.getBlockState(state)));
  }

  @ZenDocMethod(
      order = 3,
      args = {
          @ZenDocArg(arg = "name", info = "The name of the recipe that you're adding"),
          @ZenDocArg(arg = "block", info = "The block of the flower to be placed"),
          @ZenDocArg(arg = "meta", info = "The meta of the state of the flower block")
      },
      description = "Adds a recipe by creating a blockstate from a block along with the meta value from an itemblock to be grown during the flower growth ritual."
  )
  @ZenMethod
  public static void addRecipeBlock(String name, IBlock block, int meta) {
    CraftTweaker.LATE_ACTIONS.add(new FlowerBlockMeta(name, CraftTweakerMC.getBlock(block), meta, null));
  }

  @ZenDocMethod(
      order = 3,
      args = {
          @ZenDocArg(arg = "name", info = "The name of the recipe that you're adding"),
          @ZenDocArg(arg = "stack", info = "The itemstack describing an itemblock to be placed")
      },
      description = "Adds a recipe by creating a blockstate from an itemstack containing an itemblock and metadata to be grown during the Flower Growth ritual."
  )
  @ZenMethod
  public static void addRecipeItem(String name, IItemStack stack) {
    ItemStack converted = CraftTweakerMC.getItemStack(stack);
    if (!(converted.getItem() instanceof ItemBlock)) {
      CraftTweakerAPI.logError("Cannot set " + stack.toString() + " as a Flower Growth ritual item as it is not an ItemBlock.");
      return;
    }
    Block block = ((ItemBlock) converted.getItem()).getBlock();
    CraftTweaker.LATE_ACTIONS.add(new FlowerBlockMeta(name, block, converted.getMetadata(), null));
  }

  @ZenDocMethod(
      order = 4,
      args = {
          @ZenDocArg(arg = "name", info = "The name of the recipe that you're adding"),
          @ZenDocArg(arg = "stack", info = "The itemstack describing an itemblock to be placed"),
          @ZenDocArg(arg = "allowedSoils", info = "A list of blocks that this flower can be placed on")
      },
      description = "Adds a recipe by creating a blockstate from an itemstack containing an itemblock and metadata to be grown during the Flower Growth ritual. The flower will only be grown on a soil mentioned. Accepts oredict entries."
  )
  @ZenMethod
  public static void addRecipeItemOnSoils(String name, IItemStack stack, List<IIngredient> allowedSoils) {
    ItemStack converted = CraftTweakerMC.getItemStack(stack);
    if (!(converted.getItem() instanceof ItemBlock)) {
      CraftTweakerAPI.logError("Cannot set " + stack.toString() + " as a Flower Growth ritual item as it is not an ItemBlock.");
      return;
    }
    List<Ingredient> allowedSoilsMC = new ArrayList<>(allowedSoils.size());
    for (IIngredient soil : allowedSoils) {
      allowedSoilsMC.add(CraftTweakerMC.getIngredient(soil));
    }
    Block block = ((ItemBlock) converted.getItem()).getBlock();
    CraftTweaker.LATE_ACTIONS.add(new FlowerBlockMeta(name, block, converted.getMetadata(), allowedSoilsMC));
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
    public String describe() {
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
    public String describe() {
      return String.format("Recipe to add %s to FlowerGrowth", state);
    }
  }

  private static class FlowerBlockMeta extends Action {
    private final Block block;
    private final int meta;
    private final String name;
    private final List<Ingredient> allowedSoils;

    protected FlowerBlockMeta(String name, Block block, int meta, List<Ingredient> allowedSoils) {
      super("add_block_meta_flower");
      this.name = name;
      this.block = block;
      this.meta = meta;
      this.allowedSoils = allowedSoils == null ? Collections.emptyList() : allowedSoils;
    }

    @Override
    public void apply() {
      ModRecipes.addFlowerRecipe(name, block, meta, allowedSoils);
    }

    @Override
    public String describe() {
      return String.format("Recipe to produce %s:%s with FlowerGrowth", block, meta);
    }
  }
}
