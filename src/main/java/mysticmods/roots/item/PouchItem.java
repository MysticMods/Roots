package mysticmods.roots.item;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.client.RootsClientHooks;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import org.codehaus.plexus.util.StringUtils;

import java.util.EnumMap;
import java.util.List;

public class PouchItem extends Item {
  private final PouchMenuProvider provider;

  public PouchItem(PouchMenuProvider provider, Properties properties) {
    super(properties);
    this.provider = provider;
  }

  public PouchMenuProvider getMenuProvider() {
    return provider;
  }

  public DataComponentType<ItemContainerContents> getComponent() {
    Holder<Item> holder = this.builtInRegistryHolder();
    if (holder.is(ModItems.APOTHECARY_POUCH)) {
      return ModAttachments.APOTHECARY_POUCH_CONTENTS.get();
    } else if (holder.is(ModItems.COMPONENT_POUCH)) {
      return ModAttachments.COMPONENT_POUCH_CONTENTS.get();
    } else if (holder.is(ModItems.HERB_POUCH)) {
      return ModAttachments.HERB_POUCH_CONTENTS.get();
    } else if (holder.is(ModItems.SYLVAN_POUCH)) {
      return ModAttachments.SYLVAN_POUCH_CONTENTS.get();
    } else {
      return null;
    }
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
    ItemStack stack = player.getItemInHand(usedHand);

    if (!level.isClientSide()) {
      player.openMenu(getMenuProvider().createMenu(stack));
    }

    return InteractionResultHolder.success(stack);
  }

  private static final EnumMap<DyeColor, String> COLOR_NAMES = new EnumMap<>(DyeColor.class);

  @Override
  public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    Dyeable dye = stack.get(ModAttachments.DYEABLE);
    if (dye != null && dye != Dyeable.DEFAULT) {
      DyeColor color = dye.color();
      String colorName = COLOR_NAMES.get(color);
      if (colorName == null) {
        colorName = StringUtils.capitalise(color.getName().replace("_", " "));
        COLOR_NAMES.put(color, colorName);
      }

      tooltipComponents.add(Component.empty());
      tooltipComponents.add(Component.translatable("roots.tooltip.pouch.color", Component.translatable("roots.tooltip.pouch.color_name", colorName)
          .setStyle(Style.EMPTY.withColor(color.getTextColor()).withBold(true))));
    }
    if (context.level() != null && context.level().isClientSide() && stack.is(RootsTags.Items.POUCHES)) {
      tooltipComponents.add(Component.empty());
      tooltipComponents.add(Component.translatable("roots.tooltip.pouch.key_binding", RootsClientHooks.getPouchKeyBind()));
    }
  }

  @FunctionalInterface
  public interface PouchMenuProvider {
    MenuProvider createMenu(ItemStack pouch);
  }
}
