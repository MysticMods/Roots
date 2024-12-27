package mysticmods.roots.api.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

// TODO: Move to NoobUtil
public interface IBoundlessRecipe<C extends Container> extends Recipe<C> {
  @Override
  default boolean canCraftInDimensions(int pWidth, int pHeight) {
    return true;
  }
}
