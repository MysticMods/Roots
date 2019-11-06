package epicsquid.roots.integration.jei.ritual;

import epicsquid.roots.Roots;
import epicsquid.roots.integration.patchouli.Util;
import epicsquid.roots.ritual.RitualBase;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RitualWrapper implements IRecipeWrapper {

  public final RitualBase recipe;

  private final GuiButton button;

  public RitualWrapper(RitualBase recipe) {
    this.recipe = recipe;

    button = new GuiButton(2020, 75, 55, 60, 20,  I18n.format("jei.roots.open_book_button_text"));
  }

  @Override
  public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
    if (this.button.mousePressed(minecraft, mouseX, mouseY)) {
      this.button.playPressSound(minecraft.getSoundHandler());

      ResourceLocation bookPath = new ResourceLocation(Roots.MODID, "roots_guide");
      ResourceLocation entryPath = new ResourceLocation(Roots.MODID, "rituals/" + this.recipe.getName());
      //Old Attempt
      //ResourceLocation entryPath = new ResourceLocation(Roots.MODID, "roots_guide/" + minecraft.getLanguageManager().getCurrentLanguage().getLanguageCode() + "/entries/rituals/" + this.recipe.getName() + ".json");

      Util.openBook(minecraft.world, minecraft.player, bookPath, entryPath, 0);
      return true;
    }

    return false;
  }

  @Override
  public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    this.button.drawButton(minecraft, mouseX, mouseY, 0F);
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    List<Ingredient> ingreds = recipe.getIngredients();
    List<List<ItemStack>> inputs = new ArrayList<>();
    for (Ingredient ingredient : ingreds) {
      inputs.add(Arrays.asList(ingredient.getMatchingStacks()));
    }
    ingredients.setInputLists(VanillaTypes.ITEM, inputs);
    ingredients.setOutput(VanillaTypes.ITEM, new ItemStack(recipe.getIcon()));
  }
}
