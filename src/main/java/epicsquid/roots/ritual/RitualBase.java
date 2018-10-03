package epicsquid.roots.ritual;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import epicsquid.roots.entity.EntityRitualBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class RitualBase {
  public List<ItemStack> ingredients = new ArrayList<>();
  public String name;
  public int duration;
  public boolean updateValidity;

  public RitualBase(String name, int duration, boolean doUpdateValidity) {
    this.name = name;
    this.duration = duration;
    this.updateValidity = doUpdateValidity;
  }

  public RitualBase addIngredient(ItemStack stack) {
    ingredients.add(stack);
    return this;
  }

  public boolean isValidForPos(World world, BlockPos pos) {
    return true;
  }

  public abstract void doEffect(World world, BlockPos pos);

  protected void spawnEntity(World world, BlockPos pos, Class<? extends EntityRitualBase> entity) {
    List<EntityRitualBase> pastRituals = world
        .getEntitiesWithinAABB(entity, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 100, pos.getZ() + 1));
    if (pastRituals.size() == 0 && !world.isRemote) {
      EntityRitualBase ritual = null;
      try {
        Constructor<? extends EntityRitualBase> cons = entity.getDeclaredConstructor(World.class);
        ritual = cons.newInstance(world);
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
        e.printStackTrace();
      }
      if (ritual == null) {
        return;
      }
      ritual.setPosition(pos.getX() + 0.5, pos.getY() + 6.5, pos.getZ() + 0.5);
      world.spawnEntity(ritual);
    } else if (pastRituals.size() > 0) {
      for (EntityRitualBase ritual : pastRituals) {
        ritual.getDataManager().set(ritual.getLifetime(), duration + 20);
        ritual.getDataManager().setDirty(ritual.getLifetime());
      }
    }
  }
}