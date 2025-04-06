package mysticmods.roots.event.neoforge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.item.TokenItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;


@EventBusSubscriber(modid = RootsAPI.MODID)
public class TooltipHandler {
  @SubscribeEvent(priority= EventPriority.HIGHEST)
  public static void onItemTooltip(ItemTooltipEvent event) {
    ItemStack stack = event.getItemStack();
    if (stack.is(RootsTags.Items.NYI)) {
      event.getToolTip().add(Component.translatable("roots.nyi").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
    } else if (stack.is(RootsTags.Items.WIP)) {
      event.getToolTip()
          .add(Component.translatable("roots.wip").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE)));
    } else {
      if (stack.getItem() instanceof TokenItem.SpellTokenItem spellItem) {
        Spell spell = spellItem.getSpell();
        if (spell.is(RootsTags.Spells.NYI)) {
          event.getToolTip().add(Component.translatable("roots.nyi").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
        } else if (spell.is(RootsTags.Spells.WIP)) {
          event.getToolTip().add(Component.translatable("roots.wip").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE)));
        }
      } else if (stack.getItem() instanceof TokenItem.RitualTokenItem ritualItem) {
        Ritual ritual = ritualItem.getRitual();
        if (ritual.is(RootsTags.Rituals.NYI)) {
          event.getToolTip().add(Component.translatable("roots.nyi").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
        } else if (ritual.is(RootsTags.Rituals.WIP)) {
          event.getToolTip().add(Component.translatable("roots.wip").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE)));
        }
      }
    }
  }
}
