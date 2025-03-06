package mysticmods.roots.integration.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.RenderUtil;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.recipe.knife.DynamicBarkRecipe;
import mysticmods.roots.recipe.knife.KnifeRecipe;
import mysticmods.roots.recipe.knife.OutputStateMapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class KnifeCategory extends RootsRecipeBaseCategory<KnifeRecipe> {
  public KnifeCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.KNIFE_RECIPE_TYPE, helper, 92, 47, RootsAPI.rl("textures/gui/jei/bark_carving.png"), () -> new ItemStack(ModItems.SILVER_KNIFE.get()), Component.translatable("roots.jei.knife_crafting"));
  }

  // TODO: State mappers are just block -> block so these can be blocks
  // TODO: The dynamic recipe could just have a canonical representation of a block tag
  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, KnifeRecipe recipe, IFocusGroup iFocusGroup) {
    HolderLookup.Provider provider = Minecraft.getInstance().getConnection().registryAccess();
    builder.addSlot(RecipeIngredientRole.OUTPUT, 71, 28)
        .addItemStack(recipe.getResultItem(provider));
  }

  @Override
  public void draw(KnifeRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
    super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
    HolderLookup.Provider provider = Minecraft.getInstance().getConnection().registryAccess();

    if (recipe == DynamicBarkRecipe.INSTANCE) {

    } else {

      if (recipe.getStateMapper() != null) {
        OutputStateMapper mapper = recipe.getStateMapper();
        mapper.mapBlock().entrySet().stream().findFirst().ifPresent(o -> {
          RenderUtil.renderBlock(guiGraphics, o.getKey().defaultBlockState(), 12.5f, 9, 0, 45, 12f);
          RenderUtil.renderBlock(guiGraphics, o.getValue().defaultBlockState(), 79.5f, 9, 0, 45, 12f);
        });
      } else if (recipe.getOutputState() != null) {
        BlockState output = recipe.getOutputState().build();
        BlockState input = recipe.getConditions().getFirst().test().getBlockState(provider);
        RenderUtil.renderBlock(guiGraphics, input, 12.5f, 9, 0, 45, 12f);
        RenderUtil.renderBlock(guiGraphics, output, 79.5f, 9, 0, 45, 12f);
      }
    }
  }
}
