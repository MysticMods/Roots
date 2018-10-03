package epicsquid.roots.ritual;

import java.util.List;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.entity.EntityRitualLight;
import epicsquid.roots.entity.EntityRitualPurity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualPurity extends RitualBase {

  public RitualPurity(String name, int duration, boolean doUpdateValidity) {
    super(name, duration, doUpdateValidity);
    addIngredient(new ItemStack(ModItems.spirit_herb, 1));
    addIngredient(new ItemStack(ModItems.spirit_herb, 1));
    addIngredient(new ItemStack(Items.BONE, 1));
    addIngredient(new ItemStack(Items.BONE, 1));
    addIngredient(new ItemStack(Items.BONE, 1));
  }

  @Override
  public void doEffect(World world, BlockPos pos) {
    List<EntityRitualPurity> pastRituals = world.getEntitiesWithinAABB(EntityRitualPurity.class,
        new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 100, pos.getZ() + 1));
    if (pastRituals.size() == 0 && !world.isRemote) {
      EntityRitualPurity ritual = new EntityRitualPurity(world);
      ritual.setPosition(pos.getX() + 0.5, pos.getY() + 6.5, pos.getZ() + 0.5);
      world.spawnEntity(ritual);
    } else if (pastRituals.size() > 0) {
      for (EntityRitualPurity ritual : pastRituals) {
        ritual.getDataManager().set(EntityRitualLight.lifetime, duration + 20);
        ritual.getDataManager().setDirty(EntityRitualLight.lifetime);
      }
    }
  }
}
