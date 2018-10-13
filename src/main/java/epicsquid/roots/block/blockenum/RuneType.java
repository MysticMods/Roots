package epicsquid.roots.block.blockenum;

import net.minecraft.util.IStringSerializable;

public enum RuneType implements IStringSerializable {
  NORMAL(0, "normal"), WILD(1, "wild"), NATURAL(2, "natural"), MYSTIC(3, "mystic"), FUNGAL(4, "fungal"), FORBIDDEN(5, "forbidden"), FAIRY(6, "fairy");

  private final String name;
  private final int meta;

  RuneType(final int meta, final String name) {
    this.meta = meta;
    this.name = name;
  }

  public int getMeta() {
    return meta;
  }

  @Override
  public String getName() {
    return name;
  }

  public static RuneType intOf(int meta) {
    for (RuneType type : RuneType.values()) {
      if (type.getMeta() == meta) {
        return type;
      }
    }
    return WILD;
  }
}
