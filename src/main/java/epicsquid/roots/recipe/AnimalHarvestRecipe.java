package epicsquid.roots.recipe;

import epicsquid.roots.util.types.RegistryItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

public class AnimalHarvestRecipe extends RegistryItem {
  private Class<? extends LivingEntity> clazz;

  public AnimalHarvestRecipe(ResourceLocation name, Class<? extends LivingEntity> clazz) {
    this.setRegistryName(name);
    this.clazz = clazz;
  }

  public Class<? extends LivingEntity> getHarvestClass() {
    return this.clazz;
  }

  public boolean matches(Entity entity) {
    return entity.getClass().equals(this.clazz);
  }

  public boolean matches(Class clazz) {
    return this.clazz.equals(clazz);
  }
}
