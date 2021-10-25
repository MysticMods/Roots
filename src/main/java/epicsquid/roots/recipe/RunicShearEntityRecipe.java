package epicsquid.roots.recipe;

import epicsquid.roots.recipe.transmutation.BlockStatePredicate;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Transmutation recipe for Runic Shears
 */
public class RunicShearEntityRecipe extends RunicShearRecipe {

  protected Class<? extends LivingEntity> clazz;
  protected int cooldown;

  public RunicShearEntityRecipe(ResourceLocation name, ItemStack drop, Class<? extends LivingEntity> entity, int cooldown) {
    super(name, BlockStatePredicate.TRUE, Blocks.AIR.getDefaultState(), drop, drop);
    this.clazz = entity;
    this.cooldown = cooldown;
  }

  public Class<? extends LivingEntity> getClazz() {
    return clazz;
  }

  public ItemStack getDrop(LivingEntity entity) {
    return getDrop();
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
