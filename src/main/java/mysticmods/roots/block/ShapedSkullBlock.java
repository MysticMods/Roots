package mysticmods.roots.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.block.ExtraSkullTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ShapedSkullBlock extends SkullBlock {
  public static final MapCodec<ShapedSkullBlock> CODEC = RecordCodecBuilder.mapCodec(
      p_308836_ -> p_308836_.group(SkullBlock.Type.CODEC.fieldOf("kind")
              .forGetter(AbstractSkullBlock::getType), propertiesCodec())
          .apply(p_308836_, ShapedSkullBlock::new)
  );

  public ShapedSkullBlock(Type type, Properties properties) {
    super(type, properties);
  }

  @Override
  protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    if (this.getType() == ExtraSkullTypes.PIGLIN_BRUTE || this.getType() == ExtraSkullTypes.ZOMBIFIED_PIGLIN) {
      return PIGLIN_SHAPE;
    }

    return SHAPE;
  }
}
