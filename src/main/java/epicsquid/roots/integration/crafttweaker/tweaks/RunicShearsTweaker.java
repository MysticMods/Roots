package epicsquid.roots.integration.crafttweaker.tweaks;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityDefinition;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.integration.crafttweaker.Action;
import epicsquid.roots.integration.crafttweaker.tweaks.predicates.Predicates.IPredicate;
import epicsquid.roots.recipe.RunicShearEntityRecipe;
import epicsquid.roots.recipe.RunicShearRecipe;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocArg;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Objects;
import java.util.Set;

@SuppressWarnings("ALL")
@ZenDocClass("mods.roots.RunicShears")
@ZenDocAppend({"docs/include/runic_shears.example.md"})
@ZenRegister
@ZenClass("mods." + Roots.MODID + ".RunicShears")
public class RunicShearsTweaker {

  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "name", info = "the name of the recipe being created"),
          @ZenDocArg(arg = "outputDrop", info = "the item output obtained by performing the shearing"),
          @ZenDocArg(arg = "inputState", info = "a predicate describing the input state (see Predicates)"),
          @ZenDocArg(arg = "replacementState", info = "the replacement blockstate described as a block state"),
          @ZenDocArg(arg = "displayItem", info = "the item that should be displayed in integration for this recipe")
      },
      description = "Creates a recipe with the defined name that creats the specified itemstack whenever runic shears are used on the specified input state, as well as the state that will replace the input state. Additionally, an optional item that can be displayed in integration."
  )
  @ZenMethod
  public static void addRecipe(String name, IItemStack outputDrop, IPredicate inputState, crafttweaker.api.block.IBlockState replacementState, IItemStack displayItem) {
    CraftTweaker.LATE_ACTIONS.add(new AddState(name, CraftTweakerMC.getItemStack(outputDrop), CraftTweakerMC.getBlockState(replacementState), inputState, CraftTweakerMC.getItemStack(displayItem)));
  }

  @ZenDocMethod(
      order = 2,
      args = {
          @ZenDocArg(arg = "name", info = "the name of the recipe being created"),
          @ZenDocArg(arg = "outputDrop", info = "the item output obtained by performing the shearing"),
          @ZenDocArg(arg = "inputBlock", info = "the block that is to be sheared"),
          @ZenDocArg(arg = "replacementBlock", info = "the block (as an itemstack) that replaces the block being interacted with upon shearing"),
          @ZenDocArg(arg = "displayItem", info = "the item that should be displayed in integration for this recipe")
      },
      description = "As above, but using ItemStacks that describe ItemBlocks to determine blockstates."
  )
  @ZenMethod
  public static void addRecipeViaItem(String name, IItemStack outputDrop, IItemStack inputBlock, IItemStack replacementBlock, IItemStack displayItem) {
    if (!(CraftTweakerMC.getItemStack(inputBlock).getItem() instanceof ItemBlock) || (replacementBlock != null && !(CraftTweakerMC.getItemStack(replacementBlock).getItem() instanceof ItemBlock))) {
      CraftTweakerAPI.logError("Runic Shears require input and replacement to be blocks. Recipe: " + name);
      return;
    }
    CraftTweaker.LATE_ACTIONS.add(new Add(name, CraftTweakerMC.getItemStack(outputDrop), CraftTweakerMC.getBlock(Objects.requireNonNull(replacementBlock).asBlock()), CraftTweakerMC.getBlock(inputBlock.asBlock()), CraftTweakerMC.getItemStack(displayItem)));
  }

  @ZenDocMethod(
      order = 3,
      args = {
          @ZenDocArg(arg = "name", info = "the name of the recipe for the shearing"),
          @ZenDocArg(arg = "outputDrop", info = "the item that is dropped upon shearing the specified entity"),
          @ZenDocArg(arg = "entity", info = "the entity that is to be sheared to obtain the drop"),
          @ZenDocArg(arg = "cooldown", info = "the number of ticks (seconds multiplied by 20) it takes until the entity can be sheared again")
      },
      description = "Create a Runic Shears recipe that provides the outputDrop whenever the specified entity is interacted with using runic shears. The drop will only be created once every specified cooldown period. The entity specified must derive from EntityLivingBase."
  )
  @ZenMethod
  public static void addEntityRecipe(String name, IItemStack outputDrop, IEntityDefinition entity, int cooldown) {
    EntityEntry internal = (EntityEntry) entity.getInternal();
    if (EntityLivingBase.class.isAssignableFrom(internal.getEntityClass())) {
      CraftTweaker.LATE_ACTIONS.add(new AddEntity(name, CraftTweakerMC.getItemStack(outputDrop), (Class<? extends EntityLivingBase>) internal.getEntityClass(), cooldown));
    } else {
      CraftTweakerAPI.logError("Invalid class: " + internal.getEntityClass().getSimpleName() + " does not derive from EntityLivingBase; could not add Runic Shears recipe");
    }
  }

  @ZenDocMethod(
      order = 4,
      args = {
          @ZenDocArg(arg = "output", info = "the itemstack output that you wish to remove")
      },
      description = "Removes any/all recipes that have the output item specified."
  )
  @ZenMethod
  public static void removeRecipe(IItemStack output) {
    CraftTweaker.LATE_ACTIONS.add(new Remove(CraftTweakerMC.getItemStack(output)));
  }

  private static class Remove extends Action {
    private ItemStack output;

    private Remove(ItemStack output) {
      super("Runic Shears recipe removal");
      this.output = output;
    }

    @Override
    public String describe() {
      return "Removing all Runic Shears recipes involving " + output + " as its output";
    }

    @Override
    public void apply() {
      Set<RunicShearRecipe> recipes = ModRecipes.getRunicShearRecipe(output);
      boolean removed = false;
      for (RunicShearRecipe recipe : recipes) {
        ModRecipes.getRunicShearRecipes().remove(recipe.getRegistryName());
        removed = true;
      }
      Set<RunicShearEntityRecipe> entityRecipes = ModRecipes.getRunicShearEntityRecipe(output);
      for (RunicShearEntityRecipe eRecipe : entityRecipes) {
        ModRecipes.getRunicShearEntityRecipes().remove(eRecipe.getRegistryName());
        ModRecipes.getGeneratedEntityRecipes().remove(eRecipe.getClazz());
        removed = true;
      }
      if (!removed) {
        CraftTweakerAPI.logError("No runic shear recipe found for " + output);
      }
    }
  }

  private static class Add extends Action {
    private String name;
    private ItemStack displayItem;
    private ItemStack outputItem;
    private Block inputBlock;
    private Block outputBlock;

    private Add(String name, ItemStack outputItem, Block replacementBlock, Block inputBlock, ItemStack displayItem) {
      super("Runic Shears recipe add");

      this.name = name;
      this.outputItem = outputItem;
      this.outputBlock = replacementBlock;
      this.inputBlock = inputBlock;
      this.displayItem = displayItem;
    }

    @Override
    public String describe() {
      return "Adding a recipe to create " + outputItem;
    }

    @Override
    public void apply() {
      ResourceLocation name = new ResourceLocation(Roots.MODID, this.name);
      if (ModRecipes.getRunicShearRecipe(this.name) != null) {
        CraftTweakerAPI.logError("Couldn't add Runic Shear recipe for " + name.toString() + ": already exists!");
        return;
      }
      RunicShearRecipe recipe = new RunicShearRecipe(name, inputBlock, outputBlock, outputItem, displayItem);
      ModRecipes.addRunicShearRecipe(recipe);
    }
  }

  private static class AddState extends Action {
    private String name;
    private ItemStack displayItem;
    private ItemStack outputItem;
    private IPredicate input;
    private IBlockState outputState;

    private AddState(String name, ItemStack outputItem, IBlockState replacementState, IPredicate input, ItemStack displayItem) {
      super("Runic Shears recipe add");

      this.name = name;
      this.outputItem = outputItem;
      this.outputState = replacementState;
      this.input = input;
      this.displayItem = displayItem;
    }

    @Override
    public String describe() {
      return "Adding a recipe to create " + outputItem;
    }

    @Override
    public void apply() {
      ResourceLocation name = new ResourceLocation(Roots.MODID, this.name);
      if (ModRecipes.getRunicShearRecipe(this.name) != null) {
        CraftTweakerAPI.logError("Couldn't add Runic Shear recipe for " + name.toString() + ": already exists!");
        return;
      }
      RunicShearRecipe recipe = new RunicShearRecipe(name, input.get(), outputState, outputItem, displayItem);
      ModRecipes.addRunicShearRecipe(recipe);
    }
  }

  private static class AddEntity extends Action {
    private String name;
    private Class<? extends EntityLivingBase> entity;
    private ItemStack outputItem;
    private int cooldown;

    private AddEntity(String name, ItemStack outputItem, Class<? extends EntityLivingBase> entity, int cooldown) {
      super("Runic Shears entity recipe add");

      this.name = name;
      this.outputItem = outputItem;
      this.entity = entity;
      this.cooldown = cooldown;
    }

    @Override
    public String describe() {
      return "Adding a recipe to create " + outputItem + " from entity " + name;
    }

    @Override
    public void apply() {
      ResourceLocation rl = new ResourceLocation(Roots.MODID, name);
      if (ModRecipes.getRunicShearRecipe(this.name) != null) {
        CraftTweakerAPI.logError("Couldn't add Runic Shear recipe for " + name.toString() + ": already exists!");
        return;
      }
      RunicShearEntityRecipe recipe = new RunicShearEntityRecipe(rl, outputItem, entity, cooldown);
      ModRecipes.addRunicShearRecipe(recipe);
    }
  }
}
