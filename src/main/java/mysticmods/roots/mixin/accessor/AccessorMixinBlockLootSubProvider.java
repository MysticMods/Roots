package mysticmods.roots.mixin.accessor;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockLootSubProvider.class)
public interface AccessorMixinBlockLootSubProvider {
  @Accessor("NORMAL_LEAVES_STICK_CHANCES")
  float[] roots$GetNORMAL_LEAVES_STICK_CHANCES();

  @Invoker("doesNotHaveShearsOrSilkTouch")
  LootItemCondition.Builder roots$CallDoesNotHaveShearsOrSilkTouch();
}
