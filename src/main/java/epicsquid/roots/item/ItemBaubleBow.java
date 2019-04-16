package epicsquid.roots.item;

import epicsquid.roots.integration.baubles.quiver.BaubleQuiverInventoryUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;

public class ItemBaubleBow extends ItemBow {
  @Override
  public ItemStack findAmmo(EntityPlayer player) {
    ItemStack ammo = super.findAmmo(player);
    if (!ammo.isEmpty()) return ammo;

    ammo = BaubleQuiverInventoryUtil.getQuiver(player);
    return ammo;
  }
}
