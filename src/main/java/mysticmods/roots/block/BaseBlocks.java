package mysticmods.roots.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class BaseBlocks {
  public static class CropsBlock extends net.minecraft.world.level.block.CropBlock {
    public CropsBlock(Properties builder) {
      super(builder);
    }

    @Override
    @Nonnull
    public VoxelShape getShape(BlockState state, BlockGetter blockReader, BlockPos pos, CollisionContext selectionContext) {
      return Block.box(0, 0, 0, 16.0D, 2.0D * (state.getValue(AGE) + 1), 16.0D);
    }
  }

  public static class SeededCropsBlock extends CropsBlock {
    private final Supplier<Supplier<? extends ItemLike>> seedProvider;

    public SeededCropsBlock(Properties builder, Supplier<Supplier<? extends ItemLike>> seedProvider) {
      super(builder);
      this.seedProvider = seedProvider;
    }

    @Override
    protected ItemLike getBaseSeedId() {
      return seedProvider.get().get();
    }
  }

  public static class WildCropBlock extends BushBlock {
    public static final MapCodec<WildCropBlock> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(propertiesCodec(), TagKey.codec(Registries.BLOCK).fieldOf("tag").forGetter(WildCropBlock::getSupporterTag)).apply(builder, WildCropBlock::new));

    private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private final TagKey<Block> supporter;

    public WildCropBlock(Properties builder, TagKey<Block> tag) {
      super(builder);
      this.supporter = tag;
    }

    public TagKey<Block> getSupporterTag () {
      return supporter;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
      return SHAPE;
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
      return CODEC;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos) {
      return state.is(this.supporter);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
      return false;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
      return this.mayPlaceOn(worldIn.getBlockState(pos.below()), worldIn, pos.below());
    }
  }
}
