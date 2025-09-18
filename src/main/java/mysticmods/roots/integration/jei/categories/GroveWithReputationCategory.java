package mysticmods.roots.integration.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.action.GroveReputationEntry;
import mysticmods.roots.api.grove.GroveNumber;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.integration.jei.fake.GroveWithReputation;
import mysticmods.roots.integration.jei.ingredient.RootsIngredientHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Supplier;

public class GroveWithReputationCategory implements IRecipeCategory<GroveWithReputation> {
  private final IDrawable background;
  private final IDrawable icon;
  private final Component title;

  private GroveWithReputationCategory(IGuiHelper helper, int width, int height, ResourceLocation background, Supplier<ItemStack> icon, Component title) {
    this.background = helper.createDrawable(background, 0, 0, width, height);
    this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, icon.get());
    this.title = title;
  }

  public GroveWithReputationCategory(IGuiHelper helper) {
    this(helper, 209, 21, RootsAPI.rl("textures/gui/jei/grove_reputation_entry.png"), () -> new ItemStack(ModItems.PRIMAL_GROVE_STONE.get()), Component.translatable("roots.jei.grove_reputation"));
  }


  @Override
  public RecipeType<GroveWithReputation> getRecipeType() {
    return RootsJEIPlugin.GROVE_REPUTATION_ENTRY_TYPE;
  }

  @Override
  public Component getTitle() {
    return title;
  }

  @SuppressWarnings("removal")
  @Override
  public IDrawable getBackground() {
    return background;
  }

  @Override
  public IDrawable getIcon() {
    return icon;
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, GroveWithReputation recipe, IFocusGroup iFocusGroup) {
    builder.addSlot(RecipeIngredientRole.INPUT, 2, 2)
        .addIngredient(RootsJEIPlugin.GROVE_ACTION_TYPE, recipe.groveAction())
        .setCustomRenderer(RootsJEIPlugin.GROVE_TYPE, RootsJEIPlugin.GROVE_RENDERER);

    int o = 42;
    int i = 0;
    for (GroveReputationEntry.SubEntry entry : recipe.entry().entries()) {
      if (i > 7) {
        RootsAPI.LOG.error("Too many entries in GroveReputationEntry {}, truncating to 8", recipe.entry().name());
        break;
      }
      RootsIngredientHelper.subEntrySlot(builder, RecipeIngredientRole.INPUT, o + (i * 18), 2, entry);
      i++;
    }

    var slot = builder.addSlot(RecipeIngredientRole.OUTPUT, 192, 2).setCustomRenderer(RootsJEIPlugin.GROVE_NUMBER_TYPE, RootsJEIPlugin.GROVE_NUMBER_RENDERER);

    if (recipe.entry().unique()) {
      slot.addIngredient(RootsJEIPlugin.GROVE_NUMBER_TYPE, new GroveNumber(recipe.entry().grove(), recipe.entry().reputation().gain1()));
    } else {
      List<GroveNumber> outputs = recipe.entry().reputation().stream().mapToObj(f -> new GroveNumber(recipe.entry().grove(), f)).toList();
      slot.addIngredients(RootsJEIPlugin.GROVE_NUMBER_TYPE, outputs);
    }

  }

  @Override
  public void createRecipeExtras(IRecipeExtrasBuilder builder, GroveWithReputation recipe, IFocusGroup focuses) {
  }
}
