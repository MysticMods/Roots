package epicsquid.roots.capability;

import java.util.HashMap;
import java.util.Map;

import epicsquid.roots.grove.GroveType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PlayerGroveCapability implements IPlayerGroveCapability {

  private Map<GroveType, Integer> groveTrust = new HashMap<>();

  public boolean dirty = true;

  @Override
  public NBTTagCompound getData() {
    NBTTagCompound nbtTagCompound = new NBTTagCompound();

    NBTTagList groveTagList = new NBTTagList();
    int count = 0;
    for(Map.Entry<GroveType, Integer> entry : groveTrust.entrySet()){
      NBTTagCompound groveVariable = new NBTTagCompound();
      groveVariable.setString("grove" + count, entry.getKey().toString());
      groveVariable.setInteger("trust" + count, entry.getValue());
      groveTagList.appendTag(groveVariable);
      count++;
    }

    nbtTagCompound.setTag("groveList", groveTagList);

    return nbtTagCompound;
  }

  @Override
  public void setData(NBTTagCompound tag) {
    NBTTagList groveTagList = (NBTTagList) tag.getTag("groveList");

    for (int i = 0; i < groveTagList.tagCount(); i++) {
      NBTTagCompound groveTag = groveTagList.getCompoundTagAt(i);
      String groveName = groveTag.getString("grove" + i);
      int trust = groveTag.getInteger("trust"+i);
      this.groveTrust.put(GroveType.valueOf(groveName), trust);
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
