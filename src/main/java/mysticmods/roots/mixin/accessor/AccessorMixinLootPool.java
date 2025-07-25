package mysticmods.roots.mixin.accessor;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(LootPool.class)
public interface AccessorMixinLootPool {
  @Accessor("entries")
  List<LootPoolEntryContainer> rootsGetEntries();

  @Accessor("conditions")
  List<LootItemCondition> rootsGetConditions();

  @Accessor("functions")
  List<LootItemFunction> rootsGetFunctions();
}
