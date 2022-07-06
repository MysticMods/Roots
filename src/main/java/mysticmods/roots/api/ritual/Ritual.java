package mysticmods.roots.api.ritual;

import mysticmods.roots.api.KeyedRegistryEntry;
import mysticmods.roots.blockentity.PyreBlockEntity;
import net.minecraft.resources.ResourceKey;

public abstract class Ritual extends KeyedRegistryEntry<Ritual> {
  @Override
  public abstract ResourceKey<Ritual> getKey();

  protected int duration;
  protected int radiusX;
  protected int radiusY;
  protected int radiusZ;
  protected int interval;

  public abstract void ritualTick(PyreBlockEntity blockEntity);

  // Still executed on the server
  public abstract void animateTick(PyreBlockEntity blockEntity);

  public abstract void initialize();

  public int getDuration() {
    return duration;
  }

  public int getRadiusX() {
    return radiusX;
  }

  public int getRadiusY() {
    return radiusY;
  }

  public int getRadiusZ() {
    return radiusZ;
  }

  public int getInterval() {
    return interval;
  }
}
