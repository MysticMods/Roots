package mysticmods.roots.integration.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.grove.SimpleGroveValue;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.integration.jei.ingredient.entity.RootsEntityType;
import mysticmods.roots.integration.jei.ingredient.grove.GroveGeneratedPower;
import mysticmods.roots.recipe.fake.GroveEntityPowerRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public class EntityGrovePowerCategory implements IRecipeCategory<GroveEntityPowerRecipe> {
  private final IDrawable background;
  private final IDrawable icon;
  private final Component title;

  // 96 * 21
  private EntityGrovePowerCategory(IGuiHelper helper, int width, int height, ResourceLocation background, Supplier<ItemStack> icon, Component title) {
    this.background = helper.createDrawable(background, 0, 0, width, height);
    this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, icon.get());
    this.title = title;
  }

  public EntityGrovePowerCategory(IGuiHelper helper) {
    this(helper, 96, 21, RootsAPI.rl("textures/gui/jei/grove_power_entity_entry.png"), () -> new ItemStack(ModItems.WILD_GROVE_STONE.get()), Component.translatable("roots.jei.grove_power_entity"));
  }


  @Override
  public RecipeType<GroveEntityPowerRecipe> getRecipeType() {
    return RootsJEIPlugin.GROVE_ENTITY_POWER_RECIPE_TYPE;
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
  public void setRecipe(IRecipeLayoutBuilder builder, GroveEntityPowerRecipe recipe, IFocusGroup iFocusGroup) {
    builder.addSlot(RecipeIngredientRole.INPUT, 2, 2)
        .addIngredients(RootsJEIPlugin.ENTITY_TYPE, RootsEntityType.fromTag(recipe.entityTag()))
        .setCustomRenderer(RootsJEIPlugin.ENTITY_TYPE, RootsJEIPlugin.SMALL_ENTITY_RENDERER);
    builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addIngredients(recipe.itemIngredient());
    builder.addSlot(RecipeIngredientRole.CATALYST, 40, 2)
        .addIngredients(Ingredient.of(RootsTags.Groves.getGroveStoneTag(recipe.groveTag())));
    var power = new GroveGeneratedPower(SimpleGroveValue.generate(recipe.groveTag(), recipe.power()));
    builder.addSlot(RecipeIngredientRole.OUTPUT, 78, 2)
        .addIngredient(RootsJEIPlugin.GROVE_POWER_TYPE, power)
        .setCustomRenderer(RootsJEIPlugin.GROVE_POWER_TYPE, RootsJEIPlugin.GROVE_POWER_RENDERER);
  }
}
