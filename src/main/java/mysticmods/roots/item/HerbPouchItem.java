package mysticmods.roots.item;

import mysticmods.roots.inventory.HerbPouchMenu;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HerbPouchItem extends Item {
  public HerbPouchItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
    ItemStack stack = player.getItemInHand(usedHand);

    if (!level.isClientSide()) {
      player.openMenu(new HerbPouchMenu(stack));
    }

    return InteractionResultHolder.success(stack);
  }
}
