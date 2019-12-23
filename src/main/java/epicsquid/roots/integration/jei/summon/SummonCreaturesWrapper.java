package epicsquid.roots.integration.jei.summon;

import epicsquid.roots.recipe.SummonCreatureRecipe;
import epicsquid.roots.util.EntityRenderHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

public class SummonCreaturesWrapper implements IRecipeWrapper {
  public EntityLivingBase entity = null;
  public final SummonCreatureRecipe recipe;

  public SummonCreaturesWrapper(SummonCreatureRecipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    //ingredients.setOutput(VanillaTypes.ITEM, this.recipe.getDrop());
  }

  @Override
  public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    if (entity == null) {
      //entity = recipe.getEntity(minecraft.world);
    }
    float scale = getScale(entity);
    EntityRenderHelper.drawEntityOnScreen(30, 70, scale, 38 - mouseX, 70 - mouseY, entity);
  }

  private float getScale(EntityLivingBase entityLivingBase) {
    float width = entityLivingBase.width;
    float height = entityLivingBase.height;
    if (width <= height) {
      if (height < 0.9) return 40.0F;
      else if (height < 1) return 25.0F;
      else if (height < 1.8) return 23.0F;
      else if (height < 2) return 22.0F;
      else if (height < 3) return 14.0F;
      else if (height < 4) return 10.0F;
      else return 0.0F;
    } else {
      if (width < 1) return 28.0F;
      else if (width < 2) return 17.0F;
      else if (width < 3) return 3.0F;
      else return -1.0F;
    }
  }
}
