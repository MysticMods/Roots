package epicsquid.roots.integration.jei;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.integration.jei.carving.RunicCarvingCategory;
import epicsquid.roots.integration.jei.carving.RunicCarvingWrapper;
import epicsquid.roots.integration.jei.ritual.RitualCraftingCategory;
import epicsquid.roots.integration.jei.ritual.RitualCraftingWrapper;
import epicsquid.roots.integration.jei.shears.RunicShearsCategory;
import epicsquid.roots.integration.jei.shears.RunicShearsWrapper;
import epicsquid.roots.recipe.PyreCraftingRecipe;
import epicsquid.roots.recipe.RunicCarvingRecipe;
import epicsquid.roots.recipe.RunicShearRecipe;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JEIRootsPlugin implements IModPlugin {

  public static final String RUNIC_SHEARS = Roots.MODID + ".runic_shears";
  public static final String RUNIC_CARVING = Roots.MODID + ".runic_carving";
  public static final String RITUAL_CRAFTING = Roots.MODID + ".ritual_crafting";

  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {
    IGuiHelper helper = registry.getJeiHelpers().getGuiHelper();
    registry.addRecipeCategories(
        new RunicShearsCategory(helper),
        new RunicCarvingCategory(helper),
        new RitualCraftingCategory(helper)
    );
  }

  @Override
  public void register(IModRegistry registry) {
    registry.handleRecipes(RunicShearRecipe.class, RunicShearsWrapper::new, RUNIC_SHEARS);
    registry.handleRecipes(RunicCarvingRecipe.class, RunicCarvingWrapper::new, RUNIC_CARVING);
    registry.handleRecipes(PyreCraftingRecipe.class, RitualCraftingWrapper::new, RITUAL_CRAFTING);

    registry.addRecipes(ModRecipes.getRunicShearRecipes(), RUNIC_SHEARS);
    registry.addRecipes(ModRecipes.getRunicCarvingRecipes(), RUNIC_CARVING);
    registry.addRecipes(ModRecipes.getPyreCraftingRecipes().values(), RITUAL_CRAFTING);

    registry.addRecipeCatalyst(new ItemStack(ModItems.runic_shears), RUNIC_SHEARS);
    registry.addRecipeCatalyst(new ItemStack(ModItems.wood_knife), RUNIC_CARVING);
    registry.addRecipeCatalyst(new ItemStack(ModItems.stone_knife), RUNIC_CARVING);
    registry.addRecipeCatalyst(new ItemStack(ModItems.iron_knife), RUNIC_CARVING);
    registry.addRecipeCatalyst(new ItemStack(ModItems.gold_knife), RUNIC_CARVING);
    registry.addRecipeCatalyst(new ItemStack(ModItems.diamond_knife), RUNIC_CARVING);
    registry.addRecipeCatalyst(new ItemStack(ModItems.copper_knife), RUNIC_CARVING);
    registry.addRecipeCatalyst(new ItemStack(ModItems.silver_knife), RUNIC_CARVING);
    registry.addRecipeCatalyst(new ItemStack(ModBlocks.bonfire), RITUAL_CRAFTING);
  }
}
