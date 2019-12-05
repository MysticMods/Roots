package epicsquid.roots.capability.grove;

import java.util.HashMap;
import java.util.Map;

import epicsquid.roots.grove.GroveType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

public class PlayerGroveCapability implements IPlayerGroveCapability {

  private Map<GroveType, Float> groveTrust = new HashMap<>();

  private boolean dirty = true;

  @Override
  public void addTrust(GroveType type, float amount) {
    if (groveTrust.get(type) == null || groveTrust.get(type) == 0f) {
      groveTrust.put(type, +amount);
    } else {
      groveTrust.put(type, groveTrust.get(type) + amount);
    }
    markDirty();
  }

  @Override
  public float getTrust(GroveType type) {
    return groveTrust.getOrDefault(type, 0f);
  }

  @Override
  public CompoundNBT getData() {
    CompoundNBT nbtTagCompound = new CompoundNBT();

    ListNBT groveTagList = new ListNBT();
    int count = 0;
    for (Map.Entry<GroveType, Float> entry : groveTrust.entrySet()) {
      CompoundNBT groveVariable = new CompoundNBT();
      groveVariable.setString("fey" + count, entry.getKey().toString());
      groveVariable.setFloat("trust" + count, entry.getValue());
      groveTagList.appendTag(groveVariable);
      count++;
    }

    nbtTagCompound.setTag("groveList", groveTagList);

    return nbtTagCompound;
  }

  @Override
  public void setData(CompoundNBT tag) {
    ListNBT groveTagList = (ListNBT) tag.getTag("groveList");

    for (int i = 0; i < groveTagList.tagCount(); i++) {
      CompoundNBT groveTag = groveTagList.getCompoundTagAt(i);
      String groveName = groveTag.getString("fey" + i);
      float trust = groveTag.getFloat("trust" + i);
      this.groveTrust.put(GroveType.valueOf(groveName.toUpperCase()), trust);
    }
    this.dirty = true;
  }

  @Override
  public void markDirty() {
    this.dirty = true;
  }

  @Override
  public boolean isDirty() {
    return dirty;
  }

  @Override
  public void clean() {
    this.dirty = false;
  }

}
