package mysticmods.roots.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.block.ExtraSkullTypes;
import mysticmods.roots.api.block.SkullShapes;
import mysticmods.roots.blockentity.ExtraSkullBlockEntity;
import mysticmods.roots.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class ShapedSkullBlock extends SkullBlock {
  public static final MapCodec<ShapedSkullBlock> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(SkullBlock.Type.CODEC.fieldOf("kind")
              .forGetter(AbstractSkullBlock::getType), propertiesCodec())
          .apply(instance, ShapedSkullBlock::new)
  );

  public ShapedSkullBlock(Type type, Properties properties) {
    super(type, properties);
  }

  @Override
  public MapCodec<ShapedSkullBlock> codec() {
    return CODEC;
  }

  @Override
  protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    if (this.getType() == ExtraSkullTypes.PIGLIN_BRUTE || this.getType() == ExtraSkullTypes.ZOMBIFIED_PIGLIN) {
      return PIGLIN_SHAPE;
    }

    VoxelShape shape = SkullShapes.floor(this.getType(), state.getValue(ROTATION), state.getValue(POWERED));
    return shape != null ? shape : SHAPE;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    if (getType() == ExtraSkullTypes.ENDERMAN || getType() == ExtraSkullTypes.DROWNED || getType() == ExtraSkullTypes.STRAY || getType() == ExtraSkullTypes.BOGGED) {
      return new ExtraSkullBlockEntity(pos, state);
    } else {
      return new SkullBlockEntity(pos, state);
    }
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
    if (level.isClientSide) {
      if (getType() == ExtraSkullTypes.ENDERMAN) {
        return createTickerHelper(blockEntityType, ModBlockEntities.EXTRA_SKULL.get(), ExtraSkullBlockEntity::animation);
      }
    }

    return null;
  }
}
