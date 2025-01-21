package mysticmods.roots.block.crop;

import net.minecraft.util.StringRepresentable;

public enum ElementalType implements StringRepresentable {
  FIRE,
  WATER,
  EARTH,
  AIR,
  NONE;

  private final String name;

  ElementalType() {
    this.name = this.name().toLowerCase();
  }

  @Override
  public String getSerializedName() {
    return name;
  }
}
