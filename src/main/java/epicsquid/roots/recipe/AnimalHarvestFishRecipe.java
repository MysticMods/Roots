package epicsquid.roots.recipe;

import epicsquid.roots.util.types.WeightedRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class AnimalHarvestFishRecipe extends WeightedRegistry.WeightedRegistryItem {
  private ItemStack fish;

  public AnimalHarvestFishRecipe(ResourceLocation name, ItemStack fish, int weight) {
    this.setRegistryName(name);
    this.setWeight(weight);
    this.fish = fish;
  }

  public ItemStack getItemStack() {
    return this.fish;
  }
}
