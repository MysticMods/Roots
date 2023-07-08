package mysticmods.roots.api;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import noobanidus.libs.noobutil.block.BaseBlocks;

public class StateProperties {
  public static class GroveStone {
    public static final DirectionProperty FACING = BaseBlocks.HorizontalBlock.FACING;
    public static final EnumProperty<Part> PART = EnumProperty.create("part", Part.class);
    public static final BooleanProperty VALID = BooleanProperty.create("valid");
  }

  public enum Part implements StringRepresentable {
    TOP("top"),
    MIDDLE("middle"),
    BOTTOM("bottom");

    private final String partName;

    Part(String partName) {
      this.partName = partName;
    }

    @Override
    public String getSerializedName() {
      return this.partName;
    }
  }
}
