package epicsquid.roots.world.data;

import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

public class SpellLibraryRegistry extends UUIDRegistry<SpellLibraryData> {
  public static SpellLibraryRegistry INSTANCE = new SpellLibraryRegistry();

  private SpellLibraryRegistry() {
    super(SpellLibraryData.class, SpellLibraryData::name, SpellLibraryData::new);
  }

  public static SpellLibraryData getData(UUID id) {
    return INSTANCE.getDataInternal(id);
  }

  public static SpellLibraryData getData(EntityPlayer player) {
    return INSTANCE.getDataInternal(player.getUniqueID());
  }
}
