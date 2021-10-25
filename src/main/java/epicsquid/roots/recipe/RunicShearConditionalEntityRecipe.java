package epicsquid.roots.recipe;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.Set;
import java.util.function.Function;

/**
 * Transmutation recipe for Runic Shears
 */
public class RunicShearConditionalEntityRecipe extends RunicShearEntityRecipe {
  protected final Function<LivingEntity, ItemStack> functionMap;

  public RunicShearConditionalEntityRecipe(ResourceLocation name, Function<LivingEntity, ItemStack> functionMap, Set<ItemStack> drops, Class<? extends LivingEntity> entity, int cooldown) {
    super(name, ItemStack.EMPTY, entity, cooldown);
    this.functionMap = functionMap;
    this.dropMatch = Ingredient.fromStacks(drops.toArray(new ItemStack[0]));
  }

  @Override
  public ItemStack getDrop(LivingEntity entity) {
    return functionMap.apply(entity);
  }
}
