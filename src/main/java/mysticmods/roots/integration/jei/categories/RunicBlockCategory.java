package mysticmods.roots.integration.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.condition.CanonicalRepresentation;
import mysticmods.roots.api.condition.ILevelCondition;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.integration.jei.categories.widget.LevelConditionWidget;
import mysticmods.roots.integration.jei.categories.widget.DurabilityWidget;
import mysticmods.roots.integration.jei.categories.widget.WorldTestWidget;
import mysticmods.roots.recipe.knife.OutputStateMapper;
import mysticmods.roots.recipe.runic.RunicBlockRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

// TODO: Durability cost
public class RunicBlockCategory extends RootsRecipeBaseCategory<RunicBlockRecipe> {
  public RunicBlockCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.RUNIC_RECIPE_TYPE, helper, 166, 124, RootsAPI.rl("textures/gui/jei/runic_block.png"), () -> new ItemStack(ModItems.RUNIC_SHEARS.get()), Component.translatable("roots.jei.runic_block"));
  }

  // TODO: State mappers are just block -> block so these can be blocks
  // TODO: The dynamic recipe could just have a canonical representation of a block tag
  // but it would also need to know what blocks aren't being included
  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, RunicBlockRecipe recipe, IFocusGroup iFocusGroup) {
    super.setRecipe(builder, recipe, iFocusGroup);

    HolderLookup.Provider provider = Minecraft.getInstance().getConnection().registryAccess();
    if (recipe.getStateMapper() != null) {
      OutputStateMapper mapper = recipe.getStateMapper();
      List<ItemLike> inputs = new ArrayList<>();
      List<ItemLike> outputs = new ArrayList<>();

      mapper.mapBlock().forEach((a, b) -> {
        inputs.add(a);
        outputs.add(b);
      });

      // 35 -> 39

      builder.addSlot(RecipeIngredientRole.INPUT, 7, 26)
          .addIngredients(Ingredient.of(inputs.toArray(new ItemLike[0])));
      builder.addSlot(RecipeIngredientRole.OUTPUT, 73, 26)
          .addIngredients(Ingredient.of(outputs.toArray(new ItemLike[0])));
    } else {
      var acceptor = builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT);

      if (recipe.getOutputState() != null) {
        // TODO: Create a recipe that uses this
        BlockState output = recipe.getOutputState().build();
        acceptor.addIngredients(Ingredient.of(output.getBlock()));
      }

      acceptor = builder.addInvisibleIngredients(RecipeIngredientRole.INPUT);

      if (recipe.getTest() != null && recipe.getStateMapper() == null) {
        if (recipe.getTest().getIngredient() != null) {
          acceptor.addIngredients(recipe.getTest().getIngredient());
        } else {
          BlockState output = recipe.getTest().getBlockState(provider);
          acceptor.addIngredients(Ingredient.of(output.getBlock()));
        }
      }
    }

    List<ChanceOutput> outputs = recipe.getCachedOutputs();

    int row = 0;
    int column = 0;

    for (int i = 0; i < outputs.size(); i++) {
      if (i % 4 == 0 && i != 0) {
        row++;
        column = 0;
      }
      builder.addSlot(RecipeIngredientRole.OUTPUT, 97 + column * 17, 2 + row * 17)
          .addItemStack(outputs.get(i).getOutput()).setSlotName(String.valueOf(i))
          .addRichTooltipCallback(this.richestTooltip(recipe));
      column++;
    }
  }

  @Override
  public void createRecipeExtras(IRecipeExtrasBuilder builder, RunicBlockRecipe recipe, IFocusGroup focuses) {
    super.createRecipeExtras(builder, recipe, focuses);

    HolderLookup.Provider provider = Minecraft.getInstance().getConnection().registryAccess();

    if (recipe.getDurabilityCost() != -1) {
      Component durability = Component.translatable("roots.jei.text.durability", recipe.getDurabilityCost());
      builder.addWidget(new DurabilityWidget(recipe.getDurabilityCost(), 40, 44, durability));
    }

    int column = 0;
    for (ILevelCondition condition : recipe.getLevelConditions()) {
      CanonicalRepresentation rep = condition.getRepresentation();
      int count = rep.getStates().size();
      int offset = 71;
      if (count == 4) {
        offset = 76;
      }
      if (count == 3) {
        offset = 79;
      }
      builder.addWidget(new LevelConditionWidget(column * 18, offset, 18, 40, rep.getStates(), condition.getNameComponent(), condition.getDescriptionComponent()));
      column++;
    }


    if (recipe.getOutputState() != null) {
      // TODO: Create a recipe that uses this
      BlockState output = recipe.getOutputState().build();
      builder.addWidget(new WorldTestWidget(69, 21, 24, 24, output, new ItemStack(output.getBlock())));
    }
    if (recipe.getTest() != null && recipe.getStateMapper() == null) {
      BlockState output = recipe.getTest().getBlockState(provider);
      builder.addWidget(new WorldTestWidget(3, 21, 24, 24, output, new ItemStack(output.getBlock())));
    }
  }
}
