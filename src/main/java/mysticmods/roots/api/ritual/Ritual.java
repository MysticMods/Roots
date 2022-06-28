package mysticmods.roots.api.ritual;

import mysticmods.roots.entity.RitualEntity;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class Ritual extends ForgeRegistryEntry<Ritual> {
  public void ritualTick (RitualEntity entity) {
  }

  // Still executed on the server
  public void animateTick (RitualEntity entity) {
  }

  public void initialize () {
  }
}
