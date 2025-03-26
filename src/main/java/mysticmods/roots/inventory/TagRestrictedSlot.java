package mysticmods.roots.inventory;

import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;

public class TagRestrictedSlot extends Slot {
  private final TagKey<Item> tag;
  public TagRestrictedSlot(TagKey<Item> tag, Container container, int slot, int x, int y) {
    super(container, slot, x, y);
    this.tag = tag;
  }

  @Override
  public boolean mayPickup(Player player) {
    return this.hasItem() && !this.getItem().is(tag) && super.mayPickup(player);
  }
}
