package mysticmods.roots.util;

import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.block.entity.PyreBlockEntity;
import mysticmods.roots.entity.RitualEntity;
import net.minecraft.world.entity.Entity;

public class RitualUtil {
  public static void validatePyre (PyreBlockEntity pyre) {

  }

  public static RitualEntity validateRitualEntity (RitualEntity entity, Ritual ritual) {
    if (entity.isRemoved()) {
      return null;
    }
    if (ritual == null || !entity.sameRitual(ritual)) {
      entity.remove(Entity.RemovalReason.DISCARDED);
      return null;
    }

    return entity;
  }
}
