package mysticmods.roots.item;

import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.spells.Spell;
import mysticmods.roots.data.Grants;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
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
  public String getDescriptionId(ItemStack pStack) {
    CompoundTag tag = pStack.getTag();
    if (tag != null) {
      if (tag.getString("type").equals("spell")) {
        return Registries.SPELL_REGISTRY.get().getValue(new ResourceLocation(tag.getString("id"))).getDescriptionId();
      } else if (tag.getString("type").equals("modifier")) {
        return Registries.MODIFIER_REGISTRY.get().getValue(new ResourceLocation(tag.getString("id"))).getDescriptionId();
      }
    }
    return super.getDescriptionId(pStack);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
    ItemStack stack = pPlayer.getItemInHand(pUsedHand);
    if (pLevel.isClientSide()) {
      return InteractionResultHolder.consume(stack);
    }
    CompoundTag tag = stack.getOrCreateTag();
    stack.shrink(1);
    if (stack.isEmpty()) {
      pPlayer.setItemInHand(pUsedHand, ItemStack.EMPTY);
    }
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

  @Override
  public void fillItemCategory(CreativeModeTab pCategory, NonNullList<ItemStack> pItems) {
    if (this.allowdedIn(pCategory)) {
      for (Spell spell : Registries.SPELL_REGISTRY.get().getValues()) {
        ItemStack thisStack = new ItemStack(this);
        CompoundTag tag = thisStack.getOrCreateTag();
        tag.putString("type", "spell");
        tag.putString("id", spell.getKey().toString());
        pItems.add(thisStack);
      }
      for (Modifier modifier : Registries.MODIFIER_REGISTRY.get().getValues()) {
        ItemStack thisStack = new ItemStack(this);
        CompoundTag tag = thisStack.getOrCreateTag();
        tag.putString("type", "modifier");
        tag.putString("id", modifier.getKey().toString());
        pItems.add(thisStack);
      }
    }
  }
}
