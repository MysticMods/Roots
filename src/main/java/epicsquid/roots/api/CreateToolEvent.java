package epicsquid.roots.api;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.function.Supplier;

public class CreateToolEvent extends Event {
  protected Item result = null;
  protected Supplier<Item> itemSupplier;
  protected final String registry_name;

  public CreateToolEvent(String registry_name, Supplier<Item> itemSupplier) {
    this.itemSupplier = itemSupplier;
    this.registry_name = registry_name;
  }

  @Override
  public boolean isCancelable() {
    return true;
  }

  public String getRegistryName() {
    return registry_name;
  }

  public Item getItemResult() {
    if (result == null) {
      result = itemSupplier.get();
    }
    return result;
  }

  public void setItemResult(Item item) {
    result = item;
  }

  public void setItemSupplier(Supplier<Item> supplier) {
    this.itemSupplier = supplier;
  }

  public static Item createTool(String registryName, Supplier<Item> itemSupplier) {
    CreateToolEvent event = new CreateToolEvent(registryName, itemSupplier);
    MinecraftForge.EVENT_BUS.post(event);
    return event.getItemResult();
  }
}
