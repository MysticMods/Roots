package mysticmods.roots.block;

import mysticmods.roots.api.reference.Shapes;
import mysticmods.roots.init.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;


public class FeyLightBlock extends WaterloggedBlock {
  public static BooleanProperty WATERLOGGED = WaterloggedBlock.WATERLOGGED;
  public static BooleanProperty DECAYING = BooleanProperty.create("decaying");
  public static IntegerProperty DECAY = IntegerProperty.create("decay", 0, 10);
  public static BooleanProperty COLORED = BooleanProperty.create("colored");
  public static EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

  public FeyLightBlock(Properties builder) {
    super(builder);
    this.registerDefaultState(this.defaultBlockState().setValue(DECAYING, false).setValue(DECAY, 0)
        .setValue(COLORED, false).setValue(COLOR, DyeColor.WHITE).setValue(WATERLOGGED, false));
  }



  @Override
  public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
    return Shapes.FEY_LIGHT;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    super.createBlockStateDefinition(pBuilder);
    pBuilder.add(DECAYING, DECAY, COLORED, COLOR);
  }

  @Override
  public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRand) {
    super.animateTick(pState, pLevel, pPos, pRand);
    if (pRand.nextInt(2) == 0) {
      pLevel.addParticle(
          ModParticles.FEY_LIGHT_EMITTER.get(),
          pPos.getX() + 0.5,
          pPos.getY() + 0.5,
          pPos.getZ() + 0.5,
          0,
          0,
          0
      );
    }
  }
}