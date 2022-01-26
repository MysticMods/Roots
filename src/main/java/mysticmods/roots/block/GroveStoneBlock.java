package mysticmods.roots.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.IStringSerializable;
import noobanidus.libs.noobutil.block.BaseBlocks;

public class GroveStoneBlock extends BaseBlocks.HorizontalBlock {
  public static final DirectionProperty FACING = BaseBlocks.HorizontalBlock.FACING;
  public static final EnumProperty<Part> PART = EnumProperty.create("part", Part.class);
  public static final BooleanProperty VALID = BooleanProperty.create("valid");

  public GroveStoneBlock(Properties builder) {
    super(builder);
    this.registerDefaultState(defaultBlockState().setValue(VALID, false).setValue(PART, Part.TOP));
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
    super.createBlockStateDefinition(pBuilder);
    pBuilder.add(PART, VALID);
  }

  public enum Part implements IStringSerializable {
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
