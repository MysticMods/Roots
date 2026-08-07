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
  List<LootPoolEntryContainer> roots$GetEntries();

  @Accessor("conditions")
  List<LootItemCondition> roots$GetConditions();

  @Accessor("functions")
  List<LootItemFunction> roots$GetFunctions();
}
