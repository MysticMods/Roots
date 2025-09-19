package mysticmods.roots.integration.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mysticmods.roots.api.recipe.WorldRecipe;
import mysticmods.roots.api.test.world.BlockMatchWorldTest;
import mysticmods.roots.api.test.world.PartialBlockStateMatchWorldTest;
import mysticmods.roots.api.test.world.TagMatchWorldTest;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.integration.jei.ingredient.block.BlockStateType;
import mysticmods.roots.integration.jei.ingredient.block.SimpleBlockType;
import mysticmods.roots.recipe.knife.OutputStateMapper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class WorldRecipeUtil {
  public static void setWorldRecipe(IRecipeLayoutBuilder builder, WorldRecipe<?> recipe, IFocusGroup iFocusGroup, int inputX, int inputY, int outputX, int outputY) {
    HolderLookup.Provider provider = Minecraft.getInstance().getConnection().registryAccess();
    if (recipe.getStateMapper() != null) {
      OutputStateMapper mapper = recipe.getStateMapper();
      List<SimpleBlockType> inputs = new ArrayList<>();
      List<SimpleBlockType> outputs = new ArrayList<>();

      mapper.mapBlock().forEach((a, b) -> {
        inputs.add(new SimpleBlockType(a));
        outputs.add(new SimpleBlockType(b));
      });

      builder.addSlot(RecipeIngredientRole.INPUT, inputX, inputY)
          .addIngredients(RootsJEIPlugin.BLOCK_TYPE, inputs)
          .setCustomRenderer(RootsJEIPlugin.BLOCK_TYPE, RootsJEIPlugin.BLOCK_RENDERER);
      builder.addSlot(RecipeIngredientRole.OUTPUT, outputX, outputY)
          .addIngredients(RootsJEIPlugin.BLOCK_TYPE, outputs)
          .setCustomRenderer(RootsJEIPlugin.BLOCK_TYPE, RootsJEIPlugin.BLOCK_RENDERER);
    } else {
      if (recipe.getOutputState() != null) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, outputX, outputY)
            .addIngredient(RootsJEIPlugin.BLOCK_STATE_TYPE, new BlockStateType(recipe.getOutputState()))
            .setCustomRenderer(RootsJEIPlugin.BLOCK_STATE_TYPE, RootsJEIPlugin.BLOCK_STATE_RENDERER);
      }

      var slot = builder.addSlot(RecipeIngredientRole.INPUT, inputX, inputY);
      if (recipe.getTest() != null && recipe.getStateMapper() == null) {
        switch (recipe.getTest()) {
          case BlockMatchWorldTest blockMatchWorldTest ->
              slot.addIngredient(RootsJEIPlugin.BLOCK_TYPE, new SimpleBlockType(blockMatchWorldTest.getBlock()))
                  .setCustomRenderer(RootsJEIPlugin.BLOCK_TYPE, RootsJEIPlugin.BLOCK_RENDERER);
          case PartialBlockStateMatchWorldTest partialBlockStateMatchWorldTest ->
              slot.addIngredient(RootsJEIPlugin.BLOCK_STATE_TYPE, new BlockStateType(partialBlockStateMatchWorldTest.getPartialBlockState()))
                  .setCustomRenderer(RootsJEIPlugin.BLOCK_STATE_TYPE, RootsJEIPlugin.BLOCK_STATE_RENDERER);
          case TagMatchWorldTest tagMatchWorldTest -> {
            List<SimpleBlockType> ingredient = new ArrayList<>();
            BuiltInRegistries.BLOCK.getTag(tagMatchWorldTest.getTag()).ifPresent(tag -> {
              for (Holder<Block> holder : tag) {
                ingredient.add(new SimpleBlockType(holder.value()));
              }
            });
            slot.addIngredients(RootsJEIPlugin.BLOCK_TYPE, ingredient)
                .setCustomRenderer(RootsJEIPlugin.BLOCK_TYPE, RootsJEIPlugin.BLOCK_RENDERER);
          }
          default -> {
            if (recipe.getTest().getIngredient() != null) {
              slot.addIngredients(recipe.getTest().getIngredient());
            } else {
              slot.addIngredient(RootsJEIPlugin.BLOCK_STATE_TYPE, new BlockStateType(recipe.getTest()
                      .getBlockState(provider)))
                  .setCustomRenderer(RootsJEIPlugin.BLOCK_STATE_TYPE, RootsJEIPlugin.BLOCK_STATE_RENDERER);
            }
          }
        }
      }
    }
  }
}
