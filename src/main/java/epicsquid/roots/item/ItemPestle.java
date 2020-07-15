package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.roots.item.dispenser.DispensePestle;
import net.minecraft.block.BlockDispenser;

import javax.annotation.Nonnull;

public class ItemPestle extends ItemBase {
  public ItemPestle(@Nonnull String name) {
    super(name);

    BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DispensePestle.getInstance());
  }
}
