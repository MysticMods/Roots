package mysticmods.roots.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mysticmods.roots.block.WildRootsBlock;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BoneMealItem.class)
public class MixinBoneMealItem {
  @WrapOperation(method = "growWaterPlant", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;canSurvive(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z"))
  private static boolean roots$tryGrowWildRoots(BlockState blockstate, LevelReader levelReader, BlockPos blockpos, Operation<Boolean> original, @Local(name = "randomsource") RandomSource randomsource) {
    boolean result = original.call(blockstate, levelReader, blockpos);
    double chance = ConfigManager.UNDERWATER_BONE_MEAL_WILD_ROOTS_CHANCE.getAsDouble();
    if (result && chance > 0 && randomsource.nextFloat() <= chance && levelReader instanceof ServerLevel level) {
      BlockState newRoots = ModBlocks.WILD_ROOTS.get().defaultBlockState().setValue(WildRootsBlock.WATERLOGGED, true)
          .setValue(WildRootsBlock.MOSSY, true).setValue(DirectionalBlock.FACING, Direction.UP);
      if (newRoots.canSurvive(level, blockpos)) {
        BlockState blockstate1 = level.getBlockState(blockpos);
        if (blockstate1.is(Blocks.WATER) && level.getFluidState(blockpos).getAmount() == 8) {
          level.setBlock(blockpos, newRoots, 3);
          return false;
        }
      }
    }

    return result;
  }
}
