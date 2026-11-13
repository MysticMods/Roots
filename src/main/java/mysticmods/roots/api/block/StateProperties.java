package mysticmods.roots.api.block;

import mysticmods.roots.api.grove.Rank;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.List;

public class StateProperties {
  public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
  public static final BooleanProperty CRAFTING = BooleanProperty.create("crafting");

  public static class GroveStone {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final PartProperty PART = PartProperty.create("part");
    public static final IntegerProperty OLD_RANK = IntegerProperty.create("rank", 0, 4);
    public static final RankProperty RANK = RankProperty.create("grove_rank");
  }

  public static class PartProperty extends EnumProperty<Part> {
    protected PartProperty(String name) {
      super(name, Part.class, List.of(Part.values()));
    }

    public static PartProperty create(String name) {
      return new PartProperty(name);
    }
  }

  public static class RankProperty extends EnumProperty<Rank> {
    protected RankProperty (String name) {
      super(name, Rank.class, List.of(Rank.values()));
    }

    public static RankProperty create(String name) {
      return new RankProperty(name);
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
