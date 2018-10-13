package epicsquid.roots.entity.grove;

import epicsquid.roots.grove.GroveType;
import net.minecraft.world.World;

public class EntityWildGrove extends EntityGrove {

  public EntityWildGrove(World worldIn) {
    super(worldIn, 165, 42, 42, GroveType.WILD);
    this.setInvisible(true);
  }

}
