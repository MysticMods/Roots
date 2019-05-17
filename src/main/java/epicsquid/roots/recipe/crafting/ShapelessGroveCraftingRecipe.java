package epicsquid.roots.recipe.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import epicsquid.roots.Roots;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;

public class ShapelessGroveCraftingRecipe extends ShapelessOreRecipe implements GroveCraftingRecipe {
  private BlockPos grove_pos = null;

  public ShapelessGroveCraftingRecipe(ResourceLocation group, Block result, Object... recipe) {
    super(group, result, recipe);
  }

  public ShapelessGroveCraftingRecipe(ResourceLocation group, Item result, Object... recipe) {
    super(group, result, recipe);
  }

  public ShapelessGroveCraftingRecipe(ResourceLocation group, NonNullList<Ingredient> input, @Nonnull ItemStack result) {
    super(group, input, result);
  }

  public ShapelessGroveCraftingRecipe(ResourceLocation group, @Nonnull ItemStack result, Object... recipe) {
    super(group, result, recipe);
  }

  @Override
  public BlockPos getGrovePos() {
    return grove_pos;
  }

  @Override
  public void setGrovePos(BlockPos pos) {
    this.grove_pos = pos;
  }

  @Override
  public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World world) {
    return super.matches(inv, world) && findGrove(inv, world);
  }

  @SuppressWarnings("unused")
  public static class Factory implements IRecipeFactory {
    public ShapelessGroveCraftingRecipe parse(JsonContext context, JsonObject json) {
      String group = JsonUtils.getString(json, "group", "");

      NonNullList<Ingredient> ings = NonNullList.create();
      for (JsonElement ele : JsonUtils.getJsonArray(json, "ingredients"))
        ings.add(CraftingHelper.getIngredient(ele, context));

      if (ings.isEmpty())
        throw new JsonParseException("No ingredients for shapeless recipe");

      ItemStack itemstack = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
      return new ShapelessGroveCraftingRecipe(group.isEmpty() ? null : new ResourceLocation(Roots.MODID, group), ings, itemstack);
    }
  }
}
