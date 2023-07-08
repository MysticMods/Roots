package mysticmods.roots.api.faction;

import mysticmods.roots.api.RootsTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public enum GroveType {
  PRIMAL(RootsTags.Blocks.GROVE_STONE_PRIMAL),
  ELEMENTAL,
  FAIRY,
  FUNGAL,
  SPROUT,
  TWILIGHT,
  WILD;

  protected final TagKey<Block> tag;

  GroveType(TagKey<Block> tag) {
    this.tag = tag;
  }

  GroveType() {
    this.tag = null;
  }

  public TagKey<Block> getTag() {
    return tag;
  }
}
