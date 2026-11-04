package mysticmods.roots.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mysticmods.roots.api.RootsTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FungusBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FungusBlock.class)
public class MixinFungusBlock$ElementalSoil {
  @WrapMethod(method = "isValidBonemealTarget")
  private boolean roots$isValidBoneMealTarget(LevelReader level, BlockPos pos, BlockState state, Operation<Boolean> original) {
    if (state.is(Blocks.WARPED_FUNGUS) || state.is(Blocks.CRIMSON_FUNGUS)) {
      BlockState stateBelow = level.getBlockState(pos.below());
      if (stateBelow.is(RootsTags.Blocks.ELEMENTAL_SOIL)) {
        return true;
      }
    }

    return original.call(level, pos, state);
  }
}
