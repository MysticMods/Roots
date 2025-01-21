package mysticmods.roots.block;

import mysticmods.roots.block.crop.ElementalType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.neoforged.neoforge.common.util.TriState;


public class ElementalSoilBlock extends Block {

  public ElementalSoilBlock(ElementalType soilType, Properties pProperties) {
    super(pProperties);
    this.registerDefaultState(this.defaultBlockState().setValue(ElementalType.SOIL_TYPE, soilType));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(ElementalType.SOIL_TYPE);
  }

  @Override
  public TriState canSustainPlant(BlockState state, BlockGetter level, BlockPos soilPosition, Direction facing, BlockState plant) {
    return super.canSustainPlant(state, level, soilPosition, facing, plant);
  }
}
