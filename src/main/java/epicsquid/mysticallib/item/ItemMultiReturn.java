package epicsquid.mysticallib.item;

import epicsquid.mysticallib.util.ItemUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class ItemMultiReturn extends Item {

  public ItemMultiReturn() {
    super();
  }

  @Override
  public abstract EnumAction getItemUseAction(ItemStack stack);

  protected Item getReturnItem(ItemStack stack) {
    return getContainerItem(stack).getItem();
  }

  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
    ItemStack returned = new ItemStack(getReturnItem(stack));
    ItemStack result = super.onItemUseFinish(stack, world, entity);
    if (result.isEmpty()) {
      return returned;
    } else if (entity instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) entity;
      if (!player.addItemStackToInventory(returned)) {
        ItemUtil.spawnItem(world, player.getPosition(), returned);
      }
    }
    return result;
  }
}
