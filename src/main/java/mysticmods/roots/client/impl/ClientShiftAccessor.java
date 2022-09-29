package mysticmods.roots.client.impl;

import mysticmods.roots.api.access.IShiftAccessor;
import net.minecraft.client.gui.screens.Screen;

public class ClientShiftAccessor implements IShiftAccessor {
  @Override
  public boolean isShiftKeyDown() {
    return Screen.hasShiftDown();
  }
}
