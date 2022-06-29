package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.EntityEntry;
import mysticmods.roots.Roots;
import mysticmods.roots.entity.RitualEntity;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {
  public static EntityEntry<RitualEntity> RITUAL_ENTITY = Roots.REGISTRATE.<RitualEntity>entity("ritual_entity", RitualEntity::new, MobCategory.MISC)
      .properties(o -> o.sized(1f, 1f).setTrackingRange(16).setShouldReceiveVelocityUpdates(false).setUpdateInterval(3))
      .register();

  public static void load () {
  }
}
