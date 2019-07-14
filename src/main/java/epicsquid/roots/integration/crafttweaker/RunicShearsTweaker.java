package epicsquid.roots.integration.crafttweaker;

import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityDefinition;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.RunicShearRecipe;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.EntityEntry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Objects;

@ZenRegister
@ZenClass("mods." + Roots.MODID + ".RunicShears")
public class RunicShearsTweaker {

  @ZenMethod
  public static void addRecipe(String name, IItemStack outputDrop, IItemStack replacementBlock, IItemStack inputBlock, IItemStack jeiDisplayItem) {
    if (!InputHelper.isABlock(inputBlock) || (replacementBlock != null && !InputHelper.isABlock(replacementBlock))) {
      CraftTweakerAPI.logError("Runic Shears require input and replacement to be blocks. Recipe: " + name);
      return;
    }
    CraftTweaker.LATE_ACTIONS.add(new Add(name, CraftTweakerMC.getItemStack(outputDrop), CraftTweakerMC.getBlock(Objects.requireNonNull(replacementBlock).asBlock()), CraftTweakerMC.getBlock(inputBlock.asBlock()), CraftTweakerMC.getItemStack(jeiDisplayItem)));
  }

  @ZenMethod
  public static void addEntityRecipe (String name, IItemStack outputDrop, IEntityDefinition entity, int cooldown) {
    Class<? extends Entity> clz = ((EntityEntry) entity).getEntityClass();
    if (!clz.isInstance(EntityLivingBase.class)) {
      CraftTweakerAPI.logError("Entity must be a living entity.");
      return;
    }
    CraftTweaker.LATE_ACTIONS.add(new AddEntity(name, CraftTweakerMC.getItemStack(outputDrop), (Class<? extends EntityLivingBase>) clz, cooldown));
  }

  @ZenMethod
  public static void removeRecipe(IItemStack output) {
    CraftTweaker.LATE_ACTIONS.add(new Remove(CraftTweakerMC.getItemStack(output)));
  }

  private static class Remove extends BaseAction {
    private ItemStack output;

    private Remove(ItemStack output) {
      super("Runic Shears recipe removal");
      this.output = output;
    }

    @Override
    public String describe() {
      return "Removing all Runic Shears recipes involving " + LogHelper.getStackDescription(output) + " as its output";
    }

    @Override
    public void apply() {
      RunicShearRecipe recipe = ModRecipes.getRunicShearRecipe(output);
      if (recipe == null) {
        recipe = ModRecipes.getRunicShearEntityRecipe(output);
        if (recipe != null) {
          ModRecipes.getRunicShearRecipes().remove(recipe.getName());
          return;
        }
        CraftTweakerAPI.logError("No runic shear recipe found for " + LogHelper.getStackDescription(output));
      } else {
        ModRecipes.getRunicShearRecipes().remove(recipe.getName());
      }
    }
  }

  private static class Add extends BaseAction {
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
      return "Adding a recipe to create " + LogHelper.getStackDescription(outputItem);
    }

    @Override
    public void apply() {
      RunicShearRecipe recipe = new RunicShearRecipe(inputBlock, outputBlock, outputItem, name, displayItem);
      ModRecipes.addRunicShearRecipe(recipe);
    }
  }

  private static class AddEntity extends BaseAction {
    private String name;
    private Class<? extends EntityLivingBase> entity;
    private ItemStack displayItem;
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
      return "Adding a recipe to create " + LogHelper.getStackDescription(outputItem) + " from entity " + name;
    }

    @Override
    public void apply() {
      RunicShearRecipe recipe = new RunicShearRecipe(outputItem, entity, cooldown, name);
      ModRecipes.addRunicShearRecipe(recipe);
    }
  }
}
