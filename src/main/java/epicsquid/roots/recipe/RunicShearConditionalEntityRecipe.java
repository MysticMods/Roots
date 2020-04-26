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
public class RunicShearConditionalEntityRecipe<T extends EntityLivingBase> extends RunicShearEntityRecipe {
  protected final Function<T, ItemStack> functionMap;

  public RunicShearConditionalEntityRecipe(ResourceLocation name, Function<T, ItemStack> functionMap, Set<ItemStack> drops, Class<T> entity, int cooldown) {
    super(name, ItemStack.EMPTY, entity, cooldown);
    this.functionMap = functionMap;
    this.dropMatch = Ingredient.fromStacks(drops.toArray(new ItemStack[0]));
  }

  public Class<? extends EntityLivingBase> getClazz() {
    return clazz;
  }

  public int getCooldown() {
    return cooldown;
  }

  @Nullable
  public EntityLivingBase getEntity(World world) {
    try {
      return clazz.getConstructor(World.class).newInstance(world);
    } catch (Exception e) {
      return null;
    }
  }
}
