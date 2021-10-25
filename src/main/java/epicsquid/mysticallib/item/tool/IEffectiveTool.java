package epicsquid.mysticallib.item.tool;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import java.util.Set;

public interface IEffectiveTool extends ISizedTool {
  Set<Material> getEffectiveMaterials();

  default boolean displayBreak () {
    return true;
  }
}
