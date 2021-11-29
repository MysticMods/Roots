package mysticmods.roots.api.herbs;

import net.minecraft.block.Block;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IHerb extends IForgeRegistryEntry<IHerb> {
  IItemProvider getItem();

  IItemProvider getSeed();

  Block getCrop();
}
