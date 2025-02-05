package mysticmods.roots.api;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import java.util.List;

public class StateProperties {
  public static class GroveStone {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final PartProperty PART = PartProperty.create("part");
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
  }

  public static class PartProperty extends EnumProperty<Part> {
    protected PartProperty(String name) {
      super(name, Part.class, List.of(Part.values()));
    }

    public static PartProperty create(String name) {
      return new PartProperty(name);
    }
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
