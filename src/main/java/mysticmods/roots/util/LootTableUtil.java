package mysticmods.roots.util;

import com.mojang.datafixers.util.Either;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.mixin.accessor.*;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.*;

import java.util.ArrayList;
import java.util.List;

public class LootTableUtil {
  public static List<ChanceOutput> parseLootTable(LootTable table, HolderGetter.Provider provider) {
    List<ChanceOutput> outputs = new ArrayList<>();

    List<LootPool> pools = ((AccessorMixinLootTable) table).rootsGetPools();

    for (LootPool pool : pools) {
      float totalWeight = 0;
      List<LootPoolEntryContainer> entries = ((AccessorMixinLootPool) pool).rootsGetEntries();
      for (LootPoolEntryContainer entry : entries) {
        if (entry instanceof LootPoolSingletonContainer singletonContainer) {
          if (singletonContainer instanceof TagEntry tagEntry && ((AccessorMixinTagEntry) tagEntry).rootsGetExpand()) {
            TagKey<Item> tag = ((AccessorMixinTagEntry) tagEntry).rootsGetTag();
            for (Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
              totalWeight += ((AccessorMixinLootPoolSingletonContainer) singletonContainer).rootsGetWeight();
            }
          } else {
            totalWeight += ((AccessorMixinLootPoolSingletonContainer) singletonContainer).rootsGetWeight();
          }
        }
      }

      final float finalTotalWeight = totalWeight;

      for (LootPoolEntryContainer entry : entries) {
        if (entry instanceof LootItem lootItem) {
          float weight = ((AccessorMixinLootPoolSingletonContainer) lootItem).rootsGetWeight();
          float chance = weight / totalWeight;
          ChanceOutput output = new ChanceOutput(new ItemStack(((AccessorMixinLootItem) lootItem).rootsGetItem()
              .value()), chance);
          outputs.add(output);
        } else if (entry instanceof TagEntry tagEntry) {
          TagKey<Item> tag = ((AccessorMixinTagEntry) tagEntry).rootsGetTag();
          var result = BuiltInRegistries.ITEM.getTag(tag);
          if (result.isEmpty()) {
            continue;
          }
          HolderSet.Named<Item> actualTag = result.get();
          if (actualTag.size() > 0) {
            if (((AccessorMixinTagEntry) tagEntry).rootsGetExpand()) {
              actualTag.forEach(item -> {
                ;
                float weight = ((AccessorMixinLootPoolSingletonContainer) entry).rootsGetWeight();
                float chance = weight / finalTotalWeight;
                ChanceOutput output = new ChanceOutput(new ItemStack(item.value()), chance);
                outputs.add(output);
              });
            } else {
              float weight = ((AccessorMixinLootPoolSingletonContainer) entry).rootsGetWeight();
              float chance = weight / totalWeight;
              ChanceOutput output = new ChanceOutput(new ItemStack(actualTag.get(0)), chance);
              outputs.add(output);
            }
          }
        }
      }

      for (LootPoolEntryContainer entry : entries) {
        if (entry instanceof NestedLootTable nested) {
          Either<ResourceKey<LootTable>, LootTable> contents = ((AccessorMixinNestedLootTable) nested).rootsGetContents();
          contents.ifLeft(o -> {
            provider.lookup(Registries.LOOT_TABLE).orElseThrow().get(o).ifPresent(b -> {
              outputs.addAll(parseLootTable(b.value(), provider));
            });
          }).ifRight(o -> {
            outputs.addAll(parseLootTable(o, provider));
          });
        }
      }
    }

    return outputs;
  }
}
