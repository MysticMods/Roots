package mysticmods.roots.test.decompose;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ILootPoolEntryContainerDecomposer<T extends LootPoolEntryContainer> extends PriorityAssignable, Decomposers.Decomposer<T, ILootPoolEntryContainerDecomposer.ItemRecord> {
  ILootPoolEntryContainerDecomposer<LootPoolEntryContainer> NONE = new ILootPoolEntryContainerDecomposer<>() {
    @Override
    public List<ItemRecord> perform(LootPoolEntryContainer lootPoolEntryContainer, @Nullable List<ItemRecord> result) {
      return List.of();
    }

    @Override
    public Class<?> getAssignableClass() {
      return Object.class;
    }
  };

  record ItemRecord (Item item, ItemStack stack, TagKey<Item> tag) {
    public ItemRecord (Item item) {
      this(item, null, null);
    }

    public ItemRecord (ItemStack stack) {
      this(null, stack, null);
    }

    public ItemRecord (TagKey<Item> tag) {
      this(null, null, tag);
    }
  }
}
