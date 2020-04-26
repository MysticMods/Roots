package epicsquid.roots.recipe;

import epicsquid.roots.recipe.transmutation.BlockStatePredicate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.Function;

/**
 * Transmutation recipe for Runic Shears
 */
public class RunicShearConditionalEntityRecipe extends RunicShearEntityRecipe {
  protected final Function<EntityLivingBase, ItemStack> functionMap;

  public RunicShearConditionalEntityRecipe(ResourceLocation name, Function<EntityLivingBase, ItemStack> functionMap, Set<ItemStack> drops, Class<? extends EntityLivingBase> entity, int cooldown) {
    super(name, ItemStack.EMPTY, entity, cooldown);
    this.functionMap = functionMap;
    this.dropMatch = Ingredient.fromStacks(drops.toArray(new ItemStack[0]));
  }

  @Override
  public ItemStack getDrop(EntityLivingBase entity) {
    return functionMap.apply(entity);
  }
}
