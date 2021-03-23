package epicsquid.roots.integration.jei.soil;

import epicsquid.roots.init.ModBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.List;

public class SoilRecipe {
  public static SoilRecipe FIRE = new SoilRecipe(new ItemStack(ModBlocks.elemental_soil_fire), RecipeType.FIRE);
  public static SoilRecipe WATER = new SoilRecipe(new ItemStack(ModBlocks.elemental_soil_water), RecipeType.WATER);
  public static SoilRecipe EARTH = new SoilRecipe(new ItemStack(ModBlocks.elemental_soil_earth), RecipeType.EARTH);
  public static SoilRecipe AIR = new SoilRecipe(new ItemStack(ModBlocks.elemental_soil_air), RecipeType.AIR);
  public static List<SoilRecipe> recipes = Arrays.asList(EARTH, AIR, WATER, FIRE);

  private static ItemStack SOIL = new ItemStack(ModBlocks.elemental_soil);
  private ItemStack output;
  private RecipeType type;

  public SoilRecipe(ItemStack output, RecipeType type) {
    this.output = output;
    this.type = type;
  }

  public ItemStack getOutput() {
    return output;
  }

  public RecipeType getType() {
    return type;
  }

  public ItemStack getSoil () {
    return SOIL;
  }

  enum RecipeType {
    EARTH, AIR, FIRE, WATER
  }
}
