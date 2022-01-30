package mysticmods.roots.block;

import mysticmods.roots.api.reference.Shapes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
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

  @Override
  public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
    switch (p_220053_1_.getValue(PART)) {
      case TOP:
        return Shapes.GROVE_STONE_TOP;
      case MIDDLE:
        return Shapes.GROVE_STONE_MIDDLE;
      default:
      case BOTTOM:
        return Shapes.GROVE_STONE_BOTTOM;
    }
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
