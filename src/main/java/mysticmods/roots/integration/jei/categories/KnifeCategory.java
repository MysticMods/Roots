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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class KnifeCategory extends RootsRecipeBaseCategory<KnifeRecipe> {
  public KnifeCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.KNIFE_RECIPE_TYPE, helper, 92, 47, RootsAPI.rl("textures/gui/jei/bark_carving.png"), () -> new ItemStack(ModItems.SILVER_KNIFE.get()), Component.translatable("roots.jei.knife_crafting"));
  }

  // TODO: State mappers are just block -> block so these can be blocks
  // TODO: The dynamic recipe could just have a canonical representation of a block tag
  // but it would also need to know what blocks aren't being included
  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, KnifeRecipe recipe, IFocusGroup iFocusGroup) {
    HolderLookup.Provider provider = Minecraft.getInstance().getConnection().registryAccess();
    if (recipe != DynamicBarkRecipe.INSTANCE) {
      if (recipe.getStateMapper() != null) {
        OutputStateMapper mapper = recipe.getStateMapper();
        List<ItemLike> inputs = new ArrayList<>();
        List<ItemLike> outputs = new ArrayList<>();

        mapper.mapBlock().forEach((a, b) -> {
          inputs.add(a);
          outputs.add(b);
        });

        builder.addSlot(RecipeIngredientRole.INPUT, 5, 5)
            .addIngredients(Ingredient.of(inputs.toArray(new ItemLike[0])));
        builder.addSlot(RecipeIngredientRole.INPUT, 71, 5)
            .addIngredients(Ingredient.of(outputs.toArray(new ItemLike[0])));
      }
    }

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
      } else if (recipe.getOutputState() != null) {
        BlockState output = recipe.getOutputState().build();
        BlockState input = recipe.getConditions().getFirst().test().getBlockState(provider);
        RenderUtil.renderBlock(guiGraphics, input, 12.5f, 9, 0, 45, 12f);
        RenderUtil.renderBlock(guiGraphics, output, 79.5f, 9, 0, 45, 12f);
      }
    }
  }
}
