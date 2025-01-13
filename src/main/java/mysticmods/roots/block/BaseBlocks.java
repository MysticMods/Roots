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
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class BaseBlocks {
  public static class SeededCropsBlock extends CropBlock {
    private final Supplier<? extends ItemLike> seedProvider;

    public SeededCropsBlock(Supplier<? extends ItemLike> seedProvider, Properties builder) {
      super(builder);
      this.seedProvider = seedProvider;
    }

    @Override
    protected ItemLike getBaseSeedId() {
      return seedProvider.get();
    }
  }

  public static class WildCropBlock extends BushBlock {
    public static final MapCodec<WildCropBlock> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(propertiesCodec(), TagKey.codec(Registries.BLOCK).fieldOf("tag").forGetter(WildCropBlock::getSupporterTag)).apply(builder, (builder1, tag) -> new WildCropBlock(tag, builder1)));

    private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private final TagKey<Block> supporter;

    public WildCropBlock(TagKey<Block> tag, Properties builder) {
      super(builder);
      this.supporter = tag;
    }

    public TagKey<Block> getSupporterTag () {
      return supporter;
    }

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
