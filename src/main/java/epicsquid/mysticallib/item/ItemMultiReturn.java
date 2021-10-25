package epicsquid.mysticallib.item;

import epicsquid.mysticallib.util.ItemUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class ItemMultiReturn extends Item {

  public ItemMultiReturn() {
    super();
  }

  @Override
  public abstract UseAction getItemUseAction(ItemStack stack);

  protected Item getReturnItem(ItemStack stack) {
    return getContainerItem(stack).getItem();
  }

  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entity) {
    ItemStack returned = new ItemStack(getReturnItem(stack));
    ItemStack result = super.onItemUseFinish(stack, world, entity);
    if (result.isEmpty()) {
      return returned;
    } else if (entity instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) entity;
      if (!player.addItemStackToInventory(returned)) {
        ItemUtil.spawnItem(world, player.getPosition(), returned);
      }
    }
    return result;
  }
}
