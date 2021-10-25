package epicsquid.roots.integration.jei.shears;

import com.google.common.collect.Lists;
import epicsquid.roots.recipe.SummonCreatureIntermediate;
import epicsquid.roots.util.RenderUtil;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;

public class RunicShearsSummonEntityWrapper implements IRecipeWrapper {
  public LivingEntity entity = null;
  public final SummonCreatureIntermediate recipe;

  public RunicShearsSummonEntityWrapper(SummonCreatureIntermediate recipe) {
    this.recipe = recipe;
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    ingredients.setOutputs(VanillaTypes.ITEM, Lists.newArrayList(this.recipe.getEssenceStack()));
  }

  public int getCooldown() {
    return 360;
  }

  @Override
  public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    if (entity == null) {
      entity = recipe.getEntity(minecraft.world);
    }
    float scale = getScale(entity);
    RenderUtil.drawEntityOnScreen(30, 70, scale, 38 - mouseX, 70 - mouseY, entity);
  }

  public static float getScale(LivingEntity entityLivingBase) {
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
