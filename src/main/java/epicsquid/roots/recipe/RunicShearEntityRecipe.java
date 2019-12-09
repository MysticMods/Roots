package epicsquid.roots.recipe;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Transmutation recipe for Runic Shears
 */
public class RunicShearEntityRecipe extends RunicShearRecipe {

  private Class<? extends EntityLivingBase> clazz;
  private int cooldown;

  public RunicShearEntityRecipe(ResourceLocation name, ItemStack drop, Class<? extends EntityLivingBase> entity, int cooldown) {
    super(name, null, null, drop, drop);
    this.clazz = entity;
    this.cooldown = cooldown;
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
