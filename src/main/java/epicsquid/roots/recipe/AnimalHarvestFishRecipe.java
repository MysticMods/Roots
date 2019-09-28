package epicsquid.roots.recipe;

import epicsquid.roots.util.types.RegistryItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class AnimalHarvestFishRecipe extends RegistryItem {
  private ItemStack fish;

  public AnimalHarvestFishRecipe(ResourceLocation name, ItemStack fish) {
    this.setRegistryName(name);
    this.fish = fish;
  }

  public ItemStack getItemStack() {
    return this.fish;
  }
}
