package epicsquid.roots.capability;

import java.util.HashMap;
import java.util.Map;

import epicsquid.roots.grove.GroveType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PlayerGroveCapability implements IPlayerGroveCapability {

  private Map<GroveType, Float> groveTrust = new HashMap<>();

  private boolean dirty = true;

  @Override
  public void addTrust(GroveType type, float amount) {
    if(groveTrust.get(type) == null ||groveTrust.get(type) == 0f ){
      groveTrust.put(type, + amount);
    }
    else{
      groveTrust.put(type, groveTrust.get(type) + amount);
    }
    markDirty();
  }

  @Override
  public NBTTagCompound getData() {
    NBTTagCompound nbtTagCompound = new NBTTagCompound();

    NBTTagList groveTagList = new NBTTagList();
    int count = 0;
    for(Map.Entry<GroveType, Float> entry : groveTrust.entrySet()){
      NBTTagCompound groveVariable = new NBTTagCompound();
      groveVariable.setString("grove" + count, entry.getKey().toString());
      groveVariable.setFloat("trust" + count, entry.getValue());
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
      float trust = groveTag.getFloat("trust"+i);
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
