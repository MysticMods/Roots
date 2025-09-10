package mysticmods.roots.integration.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.condition.CanonicalRepresentation;
import mysticmods.roots.api.condition.ILevelCondition;
import mysticmods.roots.api.condition.IPlayerCondition;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.init.ResolvedRecipes;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.integration.jei.categories.ingredient.block.SimpleBlockType;
import mysticmods.roots.integration.jei.categories.widget.DurabilityWidget;
import mysticmods.roots.integration.jei.categories.widget.LevelConditionWidget;
import mysticmods.roots.integration.jei.categories.widget.PlayerConditionWidget;
import mysticmods.roots.recipe.knife.DynamicBarkRecipe;
import mysticmods.roots.recipe.knife.KnifeOffHandRecipe;
import mysticmods.roots.recipe.knife.KnifeRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ItemAbilities;

import java.util.*;

// TODO: Durability cost
public class KnifeCategory extends RootsRecipeBaseCategory<KnifeRecipe> {
  private static ItemStack knife;

  private static List<SimpleBlockType> dynamicInput;
  private static List<SimpleBlockType> dynamicOutput;
  private static boolean dynamicIngredientsDone = false;

  public KnifeCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.KNIFE_RECIPE_TYPE, helper, 166, 124, RootsAPI.rl("textures/gui/jei/bark_carving.png"), () -> new ItemStack(ModItems.SILVER_KNIFE.get()), Component.translatable("roots.jei.knife_crafting"));
  }

  public static void generateDynamicIngredients() {
    if (knife == null) {
      knife = new ItemStack(ModItems.NETHERITE_KNIFE);
    }

    HolderLookup.Provider provider = Minecraft.getInstance().getConnection().registryAccess();

    Set<Block> skippedInputs = new HashSet<>();
    for (RecipeHolder<KnifeRecipe> recipeHolder : ResolvedRecipes.KNIFE.getRecipes(Minecraft.getInstance().level)) {
      if (recipeHolder.value().equals(DynamicBarkRecipe.INSTANCE)) {
        continue;
      }

      KnifeRecipe recipe = recipeHolder.value();
      ItemStack result = recipe.getResultItem(provider);
      if (!result.is(RootsTags.Items.BARKS)) {
        continue;
      }

      if (recipe.getStateMapper() != null) {
        for (Map.Entry<Block, Block> entry : recipe.getStateMapper().mapBlock().entrySet()) {
          skippedInputs.add(entry.getKey());
        }
      } else if (recipe.getTest() != null && recipe.getOutputState() != null) {
        skippedInputs.add(recipe.getTest().getBlockState(provider).getBlock());
      }
    }

    List<Block> inputs = new ArrayList<>();
    List<Block> outputs = new ArrayList<>();

    // This is the dynamic recipe
    if (BuiltInRegistries.BLOCK.getTag(BlockTags.LOGS).isPresent()) {
      UseOnContext fakeContext = new UseOnContext(Minecraft.getInstance().level, null, InteractionHand.MAIN_HAND, knife, BlockHitResult.miss(Vec3.ZERO, Direction.UP, BlockPos.ZERO));

      for (Holder<Block> holder : BuiltInRegistries.BLOCK.getTag(BlockTags.LOGS).get()) {
        Block block = holder.value();
        if (skippedInputs.contains(block)) {
          continue;
        }

        BlockState result = block.defaultBlockState().getToolModifiedState(fakeContext, ItemAbilities.AXE_STRIP, true);
        if (result == null) {
          result = AxeItem.getAxeStrippingState(block.defaultBlockState());
          if (result == null) {
            continue;
          }
        }

        inputs.add(block);
        outputs.add(result.getBlock());
      }

      dynamicInput = inputs.stream().map(SimpleBlockType::new).toList();
      dynamicOutput = outputs.stream().map(SimpleBlockType::new).toList();
      dynamicIngredientsDone = true;
    }
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, KnifeRecipe recipe, IFocusGroup iFocusGroup) {
    super.setRecipe(builder, recipe, iFocusGroup);

    if (recipe instanceof KnifeOffHandRecipe offHandRecipe) {
      builder.addSlot(RecipeIngredientRole.INPUT, 39, 11)
          .addIngredients(Ingredient.of(offHandRecipe.getOffHandTag()));
    }

    if (recipe == DynamicBarkRecipe.INSTANCE) {
      if (!dynamicIngredientsDone) {
        generateDynamicIngredients();
      }

      builder.addSlot(RecipeIngredientRole.INPUT, 7, 34)
          .addIngredients(RootsJEIPlugin.BLOCK_TYPE, dynamicInput)
          .setCustomRenderer(RootsJEIPlugin.BLOCK_TYPE, RootsJEIPlugin.BLOCK_RENDERER);
      builder.addSlot(RecipeIngredientRole.OUTPUT, 73, 34)
          .addIngredients(RootsJEIPlugin.BLOCK_TYPE, dynamicOutput)
          .setCustomRenderer(RootsJEIPlugin.BLOCK_TYPE, RootsJEIPlugin.BLOCK_RENDERER);
    } else {
      WorldRecipeUtil.setWorldRecipe(builder, recipe, iFocusGroup, 7, 34, 73, 34);
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
          .addItemStack(outputs.get(i).output()).setSlotName(String.valueOf(i))
          .addRichTooltipCallback(this.richestTooltip(recipe));
      column++;
    }
  }

  @Override
  public void createRecipeExtras(IRecipeExtrasBuilder builder, KnifeRecipe recipe, IFocusGroup focuses) {
    super.createRecipeExtras(builder, recipe, focuses);

    if (recipe.getDurabilityCost() != -1) {
      Component durability = Component.translatable("roots.jei.text.durability", recipe.getDurabilityCost());
      builder.addWidget(new DurabilityWidget(recipe.getDurabilityCost(), 44, 55, durability));
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

    int row = 0;
    for (IPlayerCondition condition : recipe.getPlayerConditions()) {
      builder.addWidget(PlayerConditionWidget.create(getWidth(), 78 + row * 18, condition));
      row++;
    }
  }
}
