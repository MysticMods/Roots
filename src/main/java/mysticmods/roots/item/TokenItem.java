package mysticmods.roots.item;

import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.spells.Spell;
import mysticmods.roots.data.Grants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class TokenItem extends Item {
  public TokenItem(Properties pProperties) {
    super(pProperties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
    ItemStack stack = pPlayer.getItemInHand(pUsedHand);
    if (pLevel.isClientSide()) {
      return InteractionResultHolder.consume(stack);
    }
    CompoundTag tag = stack.getOrCreateTag();
    // TODO:
    if (tag.contains("type", Tag.TAG_STRING)) {
      switch (tag.getString("type").toLowerCase(Locale.ROOT)) {
        case "spell" -> {
          Spell spell = Registries.SPELL_REGISTRY.get().getValue(new ResourceLocation(tag.getString("id")));
          if (spell != null) {
            Grants.getGrants().addSpell(pPlayer, spell);
            return InteractionResultHolder.success(stack);
          }
        }
        case "modifier" -> {
          Modifier modifier = Registries.MODIFIER_REGISTRY.get().getValue(new ResourceLocation(tag.getString("id")));
          if (modifier != null) {
            Grants.getGrants().addModifier(pPlayer, modifier);
            return InteractionResultHolder.success(stack);
          }
        }
      }
    }

    return InteractionResultHolder.fail(stack);
  }

  @Override
  public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
    super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

    CompoundTag tag = pStack.getOrCreateTag();
    if (tag.contains("type", Tag.TAG_STRING)) {
      String type = tag.getString("type").toLowerCase(Locale.ROOT);
      if (type.equals("spell")) {
        Spell spell = Registries.SPELL_REGISTRY.get().getValue(new ResourceLocation(tag.getString("id")));
        if (spell != null) {
          pTooltipComponents.add(new TranslatableComponent("roots.tooltip.token.spell", spell.getName()));
        }
      } else if (type.equals("modifier")) {
        Modifier modifier = Registries.MODIFIER_REGISTRY.get().getValue(new ResourceLocation(tag.getString("id")));
        if (modifier != null) {
          pTooltipComponents.add(new TranslatableComponent("roots.tooltip.token.modifier", modifier.getName()));
        }
      }
    }
  }
}
