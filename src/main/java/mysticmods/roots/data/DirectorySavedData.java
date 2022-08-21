package mysticmods.roots.data;

import net.minecraft.world.level.saveddata.SavedData;

import java.io.File;

// TODO: Move to noobutil, remove from this
public abstract class DirectorySavedData extends SavedData {
  @Override
  public void save(File pFile) {
    if (isDirty()) {
      pFile.getParentFile().mkdirs();
    }
    super.save(pFile);
  }
}
