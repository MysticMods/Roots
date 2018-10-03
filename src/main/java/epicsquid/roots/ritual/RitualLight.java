package epicsquid.roots.ritual;

import java.util.List;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.entity.EntityRitualLight;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualLight extends RitualBase {
  public RitualLight(String name, int duration, boolean doUpdateValidity) {
    super(name, duration, doUpdateValidity);
    addIngredient(new ItemStack(ModItems.moonglow_leaf, 1));
    addIngredient(new ItemStack(ModItems.moonglow_seed, 1));
    addIngredient(new ItemStack(ModItems.bark_birch, 1));
    addIngredient(new ItemStack(ModItems.bark_acacia, 1));
    addIngredient(new ItemStack(Blocks.LOG, 1, 2));
  }

  @Override
  public boolean isValidForPos(World world, BlockPos pos) {
    return world.getWorldTime() % 24000 < 13000;
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    List<EntityRitualLight> pastRituals = world.getEntitiesWithinAABB(EntityRitualLight.class,
        new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 100, pos.getZ() + 1));
    if (pastRituals.size() == 0 && !world.isRemote) {
      EntityRitualLight ritual = new EntityRitualLight(world);
      ritual.setPosition(pos.getX() + 0.5, pos.getY() + 6.5, pos.getZ() + 0.5);
      world.spawnEntity(ritual);
    } else if (pastRituals.size() > 0) {
      for (EntityRitualLight ritual : pastRituals) {
        ritual.getDataManager().set(EntityRitualLight.getLifetime(), duration + 20);
        ritual.getDataManager().setDirty(EntityRitualLight.getLifetime());
      }
    }
  }
}