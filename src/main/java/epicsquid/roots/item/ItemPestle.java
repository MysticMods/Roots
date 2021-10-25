package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.roots.item.dispenser.DispensePestle;
import net.minecraft.block.DispenserBlock;

import javax.annotation.Nonnull;

public class ItemPestle extends ItemBase {
  public ItemPestle(@Nonnull String name) {
    super(name);

    DispenserBlock.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DispensePestle.getInstance());
  }
}
