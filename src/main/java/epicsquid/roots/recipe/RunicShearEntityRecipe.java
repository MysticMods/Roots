package epicsquid.roots.recipe;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Transmutation recipe for Runic Shears
 */
public class RunicShearEntityRecipe extends RunicShearRecipe {

  private Class<? extends LivingEntity> clazz;
  private int cooldown;

  public RunicShearEntityRecipe(ResourceLocation name, ItemStack drop, Class<? extends LivingEntity> entity, int cooldown) {
    super(name, null, null, drop, drop);
    this.clazz = entity;
    this.cooldown = cooldown;
  }

  public Class<? extends LivingEntity> getClazz() {
    return clazz;
  }

  public int getCooldown() {
    return cooldown;
  }

  @Nullable
  public LivingEntity getEntity(World world) {
    try {
      return clazz.getConstructor(World.class).newInstance(world);
    } catch (Exception e) {
      return null;
    }
  }
}
