package mysticmods.roots.block;

import mysticmods.roots.api.reference.Shapes;
import mysticmods.roots.blockentity.SylvanLightBlockEntity;
import mysticmods.roots.blockentity.template.BaseBlockEntity;
import mysticmods.roots.init.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;


public class SylvanLightBlock extends WaterloggedBlock implements EntityBlock {
  public static BooleanProperty WATERLOGGED = WaterloggedBlock.WATERLOGGED;
  public static BooleanProperty DECAYING = BooleanProperty.create("decaying");
  public static IntegerProperty DECAY = IntegerProperty.create("decay", 0, 10);
  public static BooleanProperty COLORED = BooleanProperty.create("colored");
  public static EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

  public SylvanLightBlock(Properties builder) {
    super(builder);
    this.registerDefaultState(this.defaultBlockState().setValue(DECAYING, false).setValue(DECAY, 0)
        .setValue(COLORED, false).setValue(COLOR, DyeColor.WHITE).setValue(WATERLOGGED, false));
  }


  @Override
  public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
    return Shapes.SYLVAN_LIGHT;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    super.createBlockStateDefinition(pBuilder);
    pBuilder.add(DECAYING, DECAY, COLORED, COLOR);
  }

  @Override
  public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRand) {
    super.animateTick(pState, pLevel, pPos, pRand);
  }

  @Override
  public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new SylvanLightBlockEntity(pos, state);
  }

  @Override
  public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
    if (level.isClientSide()) {
      return BaseBlockEntity::clientTick;
    } else {
      return null;
    }
  }
}