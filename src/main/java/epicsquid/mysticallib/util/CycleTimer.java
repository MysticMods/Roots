package epicsquid.mysticallib.util;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * CycleTime originally from JEI by Mezz et al
 * Licensed under the terms of the MIT License.
 * Sourced from: https://github.com/mezz/JustEnoughItems/blob/f520f423d9e518eed0a5a1067999b430d519fd70/src/main/java/mezz/jei/gui/ingredients/CycleTimer.java
 */

@SideOnly(Side.CLIENT)
public class CycleTimer {
  private static final int cycleTime = 1000;
  private long startTime;
  private long drawTime;
  private long pausedDuration = 0;

  public CycleTimer(int offset) {
    if (offset == -1) {
      offset = (int) (Math.random() * 10000);
    }
    long time = System.currentTimeMillis();
    this.startTime = time - (offset * cycleTime);
    this.drawTime = time;
  }

  @Nullable
  public <T> T getCycledItem(Collection<T> list) {
    return getCycledItem(new ArrayList<>(list));
  }

  @Nullable
  public <T> T getCycledItem(List<T> list) {
    if (list.isEmpty()) {
      return null;
    }
    Long index = ((drawTime - startTime) / cycleTime) % list.size();
    return list.get(index.intValue());
  }

  public void onDraw() {
    if (!GuiScreen.isShiftKeyDown()) {
      if (pausedDuration > 0) {
        startTime += pausedDuration;
        pausedDuration = 0;
      }
      drawTime = System.currentTimeMillis();
    } else {
      pausedDuration = System.currentTimeMillis() - drawTime;
    }
  }
}
