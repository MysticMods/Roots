package epicsquid.roots.entity.grove;

import epicsquid.roots.grove.GroveType;
import net.minecraft.world.World;

public class EntityNaturalGrove extends EntityGrove {

  public EntityNaturalGrove(World worldIn) {
    super(worldIn, 100, 255, 100, GroveType.NATURAL);
    this.setInvisible(true);
  }

}
