package mysticmods.roots.mixin;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CropBlock.class)
public interface AccessorMixinCropBlock {
  @Invoker("getAgeProperty")
  IntegerProperty rootsCallGetAgeProperty();

  @Invoker("getBaseSeedId")
  ItemLike rootsCallGetBaseSeedId();
}
