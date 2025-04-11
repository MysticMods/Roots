package mysticmods.roots.item;

import mysticmods.roots.inventory.HerbPouchMenu;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.codehaus.plexus.util.StringUtils;

import java.util.List;

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

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    tooltipComponents.add(Component.empty());
    DyeColor dye = stack.get(DataComponents.BASE_COLOR);
    if (dye != null) {
      tooltipComponents.add(Component.translatable("roots.tooltip.pouch.color", Component.translatable("roots.tooltip.pouch.color_name", StringUtils.capitalise(dye.getName()))
          .setStyle(Style.EMPTY.withColor(dye.getTextColor()).withBold(true))));
    }
  }
}
