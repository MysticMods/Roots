package mysticmods.roots.mixin.accessor;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TagEntry.class)
public interface AccessorMixinTagEntry {
  @Accessor("tag")
  TagKey<Item> roots$GetTag();

  @Accessor("expand")
  boolean roots$GetExpand();
}
