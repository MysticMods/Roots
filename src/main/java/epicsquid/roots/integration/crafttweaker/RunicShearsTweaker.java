package epicsquid.roots.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityDefinition;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.RunicShearEntityRecipe;
import epicsquid.roots.recipe.RunicShearRecipe;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocArg;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Objects;

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
          @ZenDocArg(arg = "replacementBlock", info = "the block (as an itemstack) that replaces the block being interacted with upon shearing"),
          @ZenDocArg(arg = "inputBlock", info = "the block that is to be sheared"),
          @ZenDocArg(arg = "jeiDisplayItem", info = "the item that should be displayed in JEI for this recipe")
      }
  )
  @ZenMethod
  public static void addRecipe(String name, IItemStack outputDrop, IItemStack replacementBlock, IItemStack inputBlock, IItemStack jeiDisplayItem) {
    if (!(CraftTweakerMC.getItemStack(inputBlock).getItem() instanceof ItemBlock) || (replacementBlock != null && !(CraftTweakerMC.getItemStack(replacementBlock).getItem() instanceof ItemBlock))) {
      CraftTweakerAPI.logError("Runic Shears require input and replacement to be blocks. Recipe: " + name);
      return;
    }
    CraftTweaker.LATE_ACTIONS.add(new Add(name, CraftTweakerMC.getItemStack(outputDrop), CraftTweakerMC.getBlock(Objects.requireNonNull(replacementBlock).asBlock()), CraftTweakerMC.getBlock(inputBlock.asBlock()), CraftTweakerMC.getItemStack(jeiDisplayItem)));
  }

  @ZenDocMethod(
      order = 2,
      args = {
          @ZenDocArg(arg = "name", info = "the name of the recipe for the shearing"),
          @ZenDocArg(arg = "outputDrop", info = "the item that is dropped upon shearing the specified entity"),
          @ZenDocArg(arg = "entity", info = "the entity that is to be sheared to obtain the drop"),
          @ZenDocArg(arg = "cooldown", info = "the number of ticks (seconds multiplied by 20) it takes until the entity can be sheared again")
      }
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
      order = 3,
      args = {
          @ZenDocArg(arg = "output", info = "the itemstack output that you wish to remove")
      }
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
      RunicShearRecipe recipe = ModRecipes.getRunicShearRecipe(output);
      boolean removed = false;
      if (recipe != null) {
        ModRecipes.getRunicShearRecipes().remove(recipe.getRegistryName());
        removed = true;
      }
      RunicShearEntityRecipe eRecipe = ModRecipes.getRunicShearEntityRecipe(output);
      if (eRecipe != null) {
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
      RunicShearRecipe recipe = new RunicShearRecipe(name, inputBlock, outputBlock, outputItem, displayItem);
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
      RunicShearEntityRecipe recipe = new RunicShearEntityRecipe(rl, outputItem, entity, cooldown);
      ModRecipes.addRunicShearRecipe(recipe);
    }
  }
}
