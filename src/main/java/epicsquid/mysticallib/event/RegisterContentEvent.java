package epicsquid.mysticallib.event;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RegisterContentEvent extends Event {
  private List<Item> items;
  private List<Block> blocks;
  private List<SoundEvent> sounds;

  public RegisterContentEvent(List<Item> items, List<Block> blocks, List<SoundEvent> sounds) {
    super();
    this.items = items;
    this.blocks = blocks;
    this.sounds = sounds;
  }

  public Item addItem(Item item) {
    items.add(item);
    return item;
  }

  public Block addBlock(Block block) {
    blocks.add(block);
    return block;
  }

  public SoundEvent addSound(SoundEvent sound) {
    sounds.add(sound);
    return sound;
  }
}
