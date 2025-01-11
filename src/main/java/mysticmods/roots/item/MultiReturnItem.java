package mysticmods.roots.item;

import mysticmods.roots.util.ItemUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

// TODO: There's a property for it now
@Deprecated
public abstract class MultiReturnItem extends Item {
  public MultiReturnItem(Properties properties) {
    super(properties);
  }

  @Override
  public abstract UseAnim getUseAnimation(ItemStack stack);

  protected abstract Item getReturnItem(ItemStack stack);

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
    ItemStack returned = new ItemStack(getReturnItem(stack));
    ItemStack result = super.finishUsingItem(stack, world, entity);
    if (result.isEmpty()) {
      return returned;
    } else if (entity instanceof Player) {
      Player player = (Player) entity;
      if (!player.addItem(returned)) {
        ItemUtil.Spawn.spawnItem(world, player.blockPosition(), returned);
      }
    }
    return result;
  }
}
