package epicsquid.roots.integration.jei;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.integration.jei.runicshears.RunicShearsCategory;
import epicsquid.roots.integration.jei.runicshears.RunicShearsWrapper;
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

  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {
    IGuiHelper helper = registry.getJeiHelpers().getGuiHelper();
    registry.addRecipeCategories(
        new RunicShearsCategory(helper)
    );
  }

  @Override
  public void register(IModRegistry registry) {
    registry.handleRecipes(RunicShearRecipe.class, RunicShearsWrapper::new, RUNIC_SHEARS);

    registry.addRecipes(ModRecipes.getRunicShearRecipes(), RUNIC_SHEARS);

    registry.addRecipeCatalyst(new ItemStack(ModItems.runic_shears), RUNIC_SHEARS);
  }
}
