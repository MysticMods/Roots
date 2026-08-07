package mysticmods.roots.mixin.accessor;

import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LootPoolSingletonContainer.class)
public interface AccessorMixinLootPoolSingletonContainer {
  @Accessor("weight")
  int roots$GetWeight();
}
