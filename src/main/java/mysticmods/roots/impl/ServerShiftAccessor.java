package mysticmods.roots.impl;

import mysticmods.roots.api.access.IShiftAccessor;
import org.apache.commons.lang3.NotImplementedException;

public class ServerShiftAccessor implements IShiftAccessor {
  @Override
  public boolean isShiftKeyDown() {
    throw new NotImplementedException("isShiftKeyDown is not implemented server side and should not be used.");
  }
}
