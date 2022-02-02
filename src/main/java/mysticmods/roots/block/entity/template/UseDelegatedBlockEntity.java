package mysticmods.roots.block.entity.template;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public abstract class UseDelegatedBlockEntity extends BaseBlockEntity {

  public UseDelegatedBlockEntity(TileEntityType<?> blockEntityType) {
    super(blockEntityType);
  }

  public abstract ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult ray);
}
