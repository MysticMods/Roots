package epicsquid.roots.entity.grove;

import epicsquid.roots.grove.GroveType;
import net.minecraft.world.World;

public class EntityWildGrove extends EntityGrove {

  public EntityWildGrove(World worldIn) {
    super(worldIn, 100, 255, 100, GroveType.WILD);
    this.setInvisible(true);
  }

}
