package epicsquid.roots.recipe.crafting;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import epicsquid.roots.Roots;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

public class ShapedGroveCraftingRecipe extends ShapedOreRecipe implements GroveCraftingRecipe {
  private BlockPos grove_pos = null;

  public ShapedGroveCraftingRecipe(ResourceLocation group, @Nonnull ItemStack result, Object... recipe) {
    super(group, result, recipe);
  }

  public ShapedGroveCraftingRecipe(ResourceLocation group, @Nonnull ItemStack result, CraftingHelper.ShapedPrimer primer) {
    super(group, result, primer);
  }

  public ShapedGroveCraftingRecipe(ResourceLocation group, Block result, Object... recipe) {
    super(group, result, recipe);
  }

  public ShapedGroveCraftingRecipe(ResourceLocation group, Item result, Object... recipe) {
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

  public static class Factory extends ShapedFactory<ShapedGroveCraftingRecipe> {

    @Override
    public ShapedGroveCraftingRecipe newRecipeFromPrimer(PrimerResult result) {
      return new ShapedGroveCraftingRecipe(result.group, result.result, result.primer);
    }
  }
}
