package mysticmods.roots.api.herbs;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IHerb extends IForgeRegistryEntry<IHerb> {
  ItemLike getItem();

  ItemLike getSeed();

  Block getCrop();
}
