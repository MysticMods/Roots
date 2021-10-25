package epicsquid.roots.world.data;

import net.minecraft.entity.player.PlayerEntity;

import java.util.UUID;

public class SpellLibraryRegistry extends UUIDRegistry<SpellLibraryData> {
  public static SpellLibraryRegistry INSTANCE = new SpellLibraryRegistry();

  private SpellLibraryRegistry() {
    super(SpellLibraryData.class, SpellLibraryData::name, SpellLibraryData::new);
  }

  public static SpellLibraryData getData(UUID id) {
    return INSTANCE.getDataInternal(id);
  }

  public static SpellLibraryData getData(PlayerEntity player) {
    return getData(player.getUniqueID());
  }

  public static SpellLibraryData clearData (UUID id) {
    return INSTANCE.clearDataInternal(id);
  }

  public static SpellLibraryData clearData (PlayerEntity player) {
    return clearData(player.getUniqueID());
  }
}
