package mysticmods.roots.api.faction;

import mysticmods.roots.api.RootsTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public enum GroveType {
  PRIMAL(RootsTags.Blocks.GROVE_STONE_PRIMAL),
  ELEMENTAL(RootsTags.Blocks.GROVE_STONE_ELEMENTAL),
  FAIRY(RootsTags.Blocks.GROVE_STONE_FAIRY),
  FUNGAL(RootsTags.Blocks.GROVE_STONE_FUNGAL),
  SPROUTING(RootsTags.Blocks.GROVE_STONE_SPROUTING),
  TWILIGHT(RootsTags.Blocks.GROVE_STONE_TWILIGHT),
  WILD(RootsTags.Blocks.GROVE_STONE_WILD);

  private final TagKey<Block> tag;

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
