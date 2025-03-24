package mysticmods.roots.mixin;

import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SaplingBlock.class)
public interface AccessorMixinSaplingBlock {
  @Accessor("treeGrower")
  TreeGrower rootsGetTreeGrower();
}
