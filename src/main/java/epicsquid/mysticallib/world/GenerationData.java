package epicsquid.mysticallib.world;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.MysticalLib;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

public class GenerationData extends WorldSavedData {
  private Set<GenerationNode> nodes = new HashSet<>();

  public GenerationData(@Nonnull String name) {
    super(name);
  }

  public GenerationData() {
    this(MysticalLib.MODID + "_generation_data");
  }

  public void addNode(@Nonnull GenerationNode node) {
    nodes.add(node);
    markDirty();
  }

  @Override
  public void readFromNBT(@Nonnull NBTTagCompound nbt) {
    NBTTagList list = nbt.getTagList("gen_data_nodes", Constants.NBT.TAG_COMPOUND);
    for (int i = 0; i < list.tagCount(); i++) {
      nodes.add(new GenerationNode(list.getCompoundTagAt(i)));
    }
  }

  @Override
  @Nonnull
  public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
    NBTTagList list = new NBTTagList();
    for (GenerationNode g : nodes) {
      list.appendTag(g.writeToNBT());
    }
    compound.setTag("gen_data_nodes", list);
    return compound;
  }

  public static GenerationData get(@Nonnull World w) {
    MapStorage s = w.getPerWorldStorage();
    GenerationData d = (GenerationData) s.getOrLoadData(GenerationData.class, MysticalLib.MODID + "_generation_data");

    if (d == null) {
      d = new GenerationData();
      s.setData(MysticalLib.MODID + "_generation_data", d);
    }

    return d;
  }

  public void update(@Nonnull World world) {
    Set<GenerationNode> toDelete = new HashSet<>();
    for (GenerationNode n : nodes) {
      if (n != null) {
        n.update(world);
      }
      if (n == null || !n.isAlive) {
        toDelete.add(n);
      }
    }
    for (GenerationNode n : toDelete) {
      nodes.remove(n);
    }
    toDelete.clear();
  }

}
