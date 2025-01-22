package mysticmods.roots.block.crop;

import mysticmods.roots.api.RootsTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import javax.annotation.Nullable;

public enum ElementalType implements StringRepresentable {
  FIRE(RootsTags.Blocks.FIRE_SOIL),
  WATER(RootsTags.Blocks.WATER_SOIL),
  EARTH(RootsTags.Blocks.EARTH_SOIL),
  AIR(RootsTags.Blocks.AIR_SOIL),
  DEFAULT(RootsTags.Blocks.BASE_ELEMENTAL_SOIL),
  NONE(null);

  public static final EnumProperty<ElementalType> ELEMENTAL_TYPE = EnumProperty.create("elemental_type", ElementalType.class);
  public static final EnumProperty<ElementalType> SOIL_TYPE = EnumProperty.create("soil_type", ElementalType.class);
  private final TagKey<Block> tag;
  private final String name;

  ElementalType(TagKey<Block> tag) {
    this.tag = tag;
    this.name = this.name().toLowerCase();
  }

  @Nullable
  public TagKey<Block> getTag() {
    return tag;
  }

  @Override
  public String getSerializedName() {
    return name;
  }
}
