package mysticmods.roots.mixin.accessor;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.GrowingPlantBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GrowingPlantBlock.class)
public interface AccessorMixinGrowingPlantBlock {
  @Accessor("growthDirection")
  Direction rootsGetGrowthDirection();
}
