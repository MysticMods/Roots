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
import java.util.function.Function;

public class LootTableUtil {
  public static List<Either<Item, TagKey<Item>>> recursivelyGetItems(LootTable table, HolderGetter.Provider provider) {
    List<Either<Item, TagKey<Item>>> result = new ArrayList<>();

    List<LootPool> pools = ((AccessorMixinLootTable) table).roots$GetPools();

    for (LootPool pool : pools) {
      result.addAll(recursivelyGetItems(pool, provider));
    }

    return result;
  }

  public static List<Either<Item, TagKey<Item>>> recursivelyGetItems(LootPool pool, HolderGetter.Provider provider) {
    List<Either<Item, TagKey<Item>>> result = new ArrayList<>();

    List<LootPoolEntryContainer> entries = ((AccessorMixinLootPool) pool).roots$GetEntries();
    result.addAll(recursivelyGetItems(entries, provider));

    return result;
  }

  public static List<Either<Item, TagKey<Item>>> recursivelyGetItems(List<LootPoolEntryContainer> entries, HolderGetter.Provider provider) {
    List<Either<Item, TagKey<Item>>> result = new ArrayList<>();

    for (LootPoolEntryContainer entry : entries) {
      if (entry instanceof EmptyLootItem) {
        continue;
      }
      if (entry instanceof LootItem) {
        result.add(Either.left(((AccessorMixinLootItem) entry).roots$GetItem().value()));
        continue;
      }
      if (entry instanceof TagEntry) {
        result.add(Either.right(((AccessorMixinTagEntry) entry).roots$GetTag()));
        continue;
      }
      if (entry instanceof NestedLootTable) {
        var key = ((AccessorMixinNestedLootTable) entry).roots$GetContents();
        LootTable table = key.map(o -> provider.lookup(Registries.LOOT_TABLE).orElseThrow().get(o).orElseThrow()
            .value(), Function.identity());
        result.addAll(recursivelyGetItems(table, provider));
        continue;
      }
      if (entry instanceof CompositeEntryBase) {
        result.addAll(recursivelyGetItems(((AccessorMixinCompositeEntryBase) entry).roots$getChildren(), provider));
      }
    }
    return result;
  }

  public static List<ChanceOutput> parseLootTable(LootTable table, HolderGetter.Provider provider) {
    List<ChanceOutput> outputs = new ArrayList<>();

    List<LootPool> pools = ((AccessorMixinLootTable) table).roots$GetPools();

    for (LootPool pool : pools) {
      float totalWeight = 0;
      List<LootPoolEntryContainer> entries = ((AccessorMixinLootPool) pool).roots$GetEntries();
      for (LootPoolEntryContainer entry : entries) {
        if (entry instanceof LootPoolSingletonContainer singletonContainer) {
          if (singletonContainer instanceof TagEntry tagEntry && ((AccessorMixinTagEntry) tagEntry).roots$GetExpand()) {
            TagKey<Item> tag = ((AccessorMixinTagEntry) tagEntry).roots$GetTag();
            for (Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
              totalWeight += ((AccessorMixinLootPoolSingletonContainer) singletonContainer).roots$GetWeight();
            }
          } else {
            totalWeight += ((AccessorMixinLootPoolSingletonContainer) singletonContainer).roots$GetWeight();
          }
        }
      }

      final float finalTotalWeight = totalWeight;

      for (LootPoolEntryContainer entry : entries) {
        if (entry instanceof LootItem lootItem) {
          float weight = ((AccessorMixinLootPoolSingletonContainer) lootItem).roots$GetWeight();
          float chance = weight / totalWeight;
          ChanceOutput output = new ChanceOutput(new ItemStack(((AccessorMixinLootItem) lootItem).roots$GetItem()
              .value()), chance);
          outputs.add(output);
        } else if (entry instanceof TagEntry tagEntry) {
          TagKey<Item> tag = ((AccessorMixinTagEntry) tagEntry).roots$GetTag();
          var result = BuiltInRegistries.ITEM.getTag(tag);
          if (result.isEmpty()) {
            continue;
          }
          HolderSet.Named<Item> actualTag = result.get();
          if (actualTag.size() > 0) {
            if (((AccessorMixinTagEntry) tagEntry).roots$GetExpand()) {
              actualTag.forEach(item -> {
                ;
                float weight = ((AccessorMixinLootPoolSingletonContainer) entry).roots$GetWeight();
                float chance = weight / finalTotalWeight;
                ChanceOutput output = new ChanceOutput(new ItemStack(item.value()), chance);
                outputs.add(output);
              });
            } else {
              float weight = ((AccessorMixinLootPoolSingletonContainer) entry).roots$GetWeight();
              float chance = weight / totalWeight;
              ChanceOutput output = new ChanceOutput(new ItemStack(actualTag.get(0)), chance);
              outputs.add(output);
            }
          }
        }
      }

      for (LootPoolEntryContainer entry : entries) {
        if (entry instanceof NestedLootTable nested) {
          Either<ResourceKey<LootTable>, LootTable> contents = ((AccessorMixinNestedLootTable) nested).roots$GetContents();
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
