package mysticmods.roots.client.gui.toasts;

import mysticmods.roots.api.item.IKeybindItem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class KeybindItemToast implements Toast {
  private static final ResourceLocation BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("toast/recipe");

  private final ItemStack itemstack;
  private final IKeybindItem item;

  public KeybindItemToast(Item item) {
    this.itemstack = new ItemStack(item);
    if (!(item instanceof IKeybindItem k)) {
      throw new IllegalStateException("Item " + item + " isn't a keybind item!?");
    }
    this.item = k;
  }

  @Override
  public Visibility render(GuiGraphics guiGraphics, ToastComponent toastComponent, long timeSinceLastVisible) {
    guiGraphics.blitSprite(BACKGROUND_SPRITE, 0, 0, this.width(), this.height());
    // TODO: Test for whether or not it's bound?
    if (item.isBound()) {
      guiGraphics.drawString(toastComponent.getMinecraft().font, item.activate(), 23, 6, -11534256, false);
      guiGraphics.drawString(toastComponent.getMinecraft().font, item.describe(), 23, 16, -11534256, false);
    } else {
      guiGraphics.drawString(toastComponent.getMinecraft().font, item.unbound(), 23, 6, -11534256, false);
      guiGraphics.drawString(toastComponent.getMinecraft().font, item.describe(), 23, 16, -11534256, false);
    }
    guiGraphics.renderFakeItem(itemstack, 5, 5);
    return (double) timeSinceLastVisible >= 33200.0 * toastComponent.getNotificationDisplayTimeMultiplier() ? Visibility.HIDE : Visibility.SHOW;
  }
}
