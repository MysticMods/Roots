package mysticmods.roots.mixin.accessor;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CropBlock.class)
public interface AccessorMixinCropBlock {
  @Invoker("getAgeProperty")
  IntegerProperty roots$CallGetAgeProperty();

  @Invoker("getBaseSeedId")
  ItemLike roots$CallGetBaseSeedId();
}
