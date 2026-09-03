package mysticmods.roots.mixin.accessor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CropBlock.class)
public interface AccessorMixinCropBlock {
  @Invoker("getAgeProperty")
  IntegerProperty roots$CallGetAgeProperty();

  @Invoker("getBaseSeedId")
  ItemLike roots$CallGetBaseSeedId();

  @Invoker("mayPlaceOn")
  boolean roots$CallMayPlaceOn
      (BlockState state, BlockGetter level, BlockPos pos);
}
