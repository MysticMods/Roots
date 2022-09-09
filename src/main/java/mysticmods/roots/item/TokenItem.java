package mysticmods.roots.item;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.capability.Capabilities;
import mysticmods.roots.api.capability.GrantCapability;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellInstance;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import noobanidus.libs.noobutil.util.EnumUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

// TokenItem performs multiple duties.
// 1. Spell info
// - Allows you to right-click to Grant a spell.
// - Provides tooltip information for the spell.
// - Provides tooltip information for if you have learned the spell or not.
// 2. Modifier info
// - Allows you to right-click to Grant a spell's modifier.
// - Provides tooltip information for the modifier & its spell.
// - Provides tooltip information for if you have learned the modifier or not.
// 3. Spell library info
// - Is clickable in order to select the spell.
// - Provides a list of available modifiers that the player has unlocked.
// 4. Staff/casting item info
// - Is clickable in order to select the spell
// - Provides a list of modifiers that have been enabled for this (SpellInstance)
// 5. Staff modifier info
// - Provides cost and tooltip information for the modifier.
// - Allows you to click to enable/disable a modifier.
// 6. TODO: RITUALS!
public class TokenItem extends Item {
  public TokenItem(Properties pProperties) {
    super(pProperties);
  }

  @Override
  public String getDescriptionId(ItemStack pStack) {
    Type type = getType(pStack);
    if (type != null) {
      switch (type) {
        case MODIFIER -> {
          // Spell modifier
          Modifier modifier = getModifier(pStack);
          if (modifier != null) {
            return modifier.getDescriptionId();
          }
        }
        case STAFF -> {
          // Spell
          SpellInstance spellInstance = getSpellInstance(pStack);
          if (spellInstance != null) {
            return spellInstance.getSpell().getDescriptionId();
          }
        }
        case RITUAL -> {
          Ritual ritual = getRitual(pStack);
          if (ritual != null) {
            return ritual.getDescriptionId();
          }
        }
        default -> {
          // Spell
          Spell spell = getSpell(pStack);
          if (spell != null) {
            return spell.getDescriptionId();
          }
        }
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
    Type type = getType(stack);
    if (type == null || !type.isGrantable()) {
      // Shouldn't really get to this point
      return InteractionResultHolder.fail(stack);
    }

    stack.shrink(1);
    if (stack.isEmpty()) {
      pPlayer.setItemInHand(pUsedHand, ItemStack.EMPTY);
    }
    LazyOptional<GrantCapability> oCap = pPlayer.getCapability(Capabilities.GRANT_CAPABILITY);
    if (!oCap.isPresent()) {
      return InteractionResultHolder.fail(stack);
    }
    GrantCapability cap = oCap.orElseThrow(() -> new IllegalStateException("Grant capability is not present even though it said it was present for '" + pPlayer.getName().getString() + "'"));
    InteractionResultHolder<ItemStack> result = null;
    switch (type) {
      case SPELL -> {
        Spell spell = getSpell(stack);
        if (spell != null) {
          cap.grantSpell(spell);
          result = InteractionResultHolder.success(stack);
        }
      }
      case MODIFIER -> {
        Modifier modifier = getModifier(stack);
        if (modifier != null) {
          cap.grantModifier(modifier);
          result = InteractionResultHolder.success(stack);
        }
      }
    }
    if (result != null) {
      if (cap.isDirty()) {
        RootsAPI.getInstance().synchronizeCapability((ServerPlayer) pPlayer, RootsAPI.GRANT_CAPABILITY_ID);
      }
      return result;
    }
    return InteractionResultHolder.fail(stack);
  }

  @Override
  public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
    super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

    Type type = getType(pStack);
    if (type == null) {
      return;
    }

    boolean unlocked = false;
    GrantCapability cap = null;
    Player player = RootsAPI.getInstance().getPlayer();
    if (player != null) {
      LazyOptional<GrantCapability> oCap = player.getCapability(Capabilities.GRANT_CAPABILITY);
      if (oCap.isPresent()) {
        cap = oCap.orElseThrow(() -> new IllegalStateException("Grant capability is not present even though it said it was present for '" + player.getName().getString() + "'"));
      }
    }

    switch (type) {
      case SPELL -> {
        Spell spell = getSpell(pStack);
        if (spell != null) {
          pTooltipComponents.add(Component.translatable("roots.tooltip.token.spell", spell.getStyledName()));
          if (cap != null && cap.hasSpell(spell)) {
            unlocked = true;
          }
        }
      }
      case MODIFIER -> {
        Modifier modifier = getModifier(pStack);
        if (modifier != null) {
          pTooltipComponents.add(Component.translatable("roots.tooltip.token.modifier", modifier.getName()));
          pTooltipComponents.add(Component.translatable("roots.tooltip.token.spell", modifier.getSpell().getStyledName()));
          if (cap != null && cap.hasModifier(modifier)) {
            unlocked = true;
          }
        }
      }
      case STAFF -> {
        SpellInstance spell = getSpellInstance(pStack);
        if (spell != null) {
          //pTooltipComponents.add(Component.translatable("roots.tooltip.token.staff", spell.getName()));
        }
      }
      case STAFF_MODIFIER -> {
      }
      case LIBRARY -> {
      }
      case RITUAL -> {
        Ritual ritual = getRitual(pStack);
        if (ritual != null) {
          pTooltipComponents.add(Component.translatable("roots.tooltip.token.ritual", ritual.getName()));
        }
      }
    }

    if (type.isGrantable()) {
      pTooltipComponents.add(Component.literal(""));
      if (unlocked) {
        pTooltipComponents.add(Component.translatable("roots.tooltip.token.unlocked"));
      } else {
        pTooltipComponents.add(Component.translatable("roots.tooltip.token.unlock"));
      }
    }
  }

  @Override
  public void fillItemCategory(CreativeModeTab pCategory, NonNullList<ItemStack> pItems) {
    if (this.allowedIn(pCategory)) {
      for (Spell spell : Registries.SPELL_REGISTRY.get().getValues()) {
        ItemStack thisStack = new ItemStack(this);
        setSpell(thisStack, spell);
        pItems.add(thisStack);
      }
      for (Modifier modifier : Registries.MODIFIER_REGISTRY.get().getValues()) {
        ItemStack thisStack = new ItemStack(this);
        setModifier(thisStack, modifier);
        pItems.add(thisStack);
      }
      for (Ritual ritual : Registries.RITUAL_REGISTRY.get().getValues()) {
        ItemStack thisStack = new ItemStack(this);
        setRitual(thisStack, ritual);
        pItems.add(thisStack);
      }
    }
  }

  @Nullable
  public Type getType(ItemStack stack) {
    CompoundTag tag = stack.getTag();
    if (tag == null) {
      return null;
    }

    return EnumUtil.fromString(Type.class, tag.getString("type"));
  }

  @Nullable
  public Spell getSpell(ItemStack stack) {
    CompoundTag tag = stack.getTag();
    if (tag == null) {
      return null;
    }

    return Registries.SPELL_REGISTRY.get().getValue(new ResourceLocation(tag.getString("spell")));
  }

  public void setSpell(ItemStack stack, Spell spell) {
    CompoundTag tag = stack.getOrCreateTag();
    tag.putString("type", Type.SPELL.name().toLowerCase(Locale.ROOT));
    tag.putString("spell", Registries.SPELL_REGISTRY.get().getKey(spell).toString());
  }

  @Nullable
  public Modifier getModifier(ItemStack stack) {
    CompoundTag tag = stack.getTag();
    if (tag == null) {
      return null;
    }

    return Registries.MODIFIER_REGISTRY.get().getValue(new ResourceLocation(tag.getString("modifier")));
  }

  public void setModifier(ItemStack stack, Modifier modifier) {
    CompoundTag tag = stack.getOrCreateTag();
    tag.putString("type", Type.MODIFIER.name().toLowerCase(Locale.ROOT));
    tag.putString("modifier", Registries.MODIFIER_REGISTRY.get().getKey(modifier).toString());
  }

  @Nullable
  public LibraryInfo getLibraryInfo(ItemStack stack) {
    CompoundTag tag = stack.getTag();
    if (tag == null) {
      return null;
    }

    Spell spell = Registries.SPELL_REGISTRY.get().getValue(new ResourceLocation(tag.getString("spell")));
    Set<Modifier> modifiers = new HashSet<>();
    for (Tag iTag : tag.getList("modifiers", Tag.TAG_STRING)) {
      modifiers.add(Registries.MODIFIER_REGISTRY.get().getValue(new ResourceLocation(iTag.getAsString())));
    }

    return new LibraryInfo(spell, modifiers);
  }

  public void setLibraryInfo(ItemStack stack, Spell spell, Set<Modifier> modifiers) {
    CompoundTag tag = stack.getOrCreateTag();
    tag.putString("type", Type.LIBRARY.name().toLowerCase(Locale.ROOT));
    tag.putString("spell", Registries.SPELL_REGISTRY.get().getKey(spell).toString());
    ListTag modifiersTag = new ListTag();
    for (Modifier modifier : modifiers) {
      modifiersTag.add(StringTag.valueOf(Registries.MODIFIER_REGISTRY.get().getKey(modifier).toString()));
    }
    tag.put("modifiers", modifiersTag);
  }

  @Nullable
  public SpellInstance getSpellInstance(ItemStack stack) {
    CompoundTag tag = stack.getTag();
    if (tag == null) {
      return null;
    }

    return SpellInstance.fromNBT(tag);
  }

  public void setSpellInstance(ItemStack stack, SpellInstance instance) {
    CompoundTag tag = stack.getOrCreateTag();
    instance.toNBT(tag);
    tag.putString("type", Type.STAFF.name().toLowerCase(Locale.ROOT));
  }

  @Nullable
  public StaffModifier getStaffModifier(ItemStack stack) {
    CompoundTag tag = stack.getTag();
    if (tag == null) {
      return null;
    }

    Spell spell = getSpell(stack);
    Modifier modifier = getModifier(stack);
    if (spell == null || modifier == null) {
      return null;
    }

    return new StaffModifier(spell, modifier, tag.getBoolean("enabled"));
  }

  public void setStaffModifier(ItemStack stack, Spell spell, Modifier modifier, boolean enabled) {
    CompoundTag tag = stack.getOrCreateTag();
    tag.putString("type", Type.STAFF_MODIFIER.name().toLowerCase(Locale.ROOT));
    tag.putString("spell", Registries.SPELL_REGISTRY.get().getKey(spell).toString());
    tag.putString("modifier", Registries.MODIFIER_REGISTRY.get().getKey(modifier).toString());
    tag.putBoolean("enabled", enabled);
  }

  public void setRitual(ItemStack stack, Ritual ritual) {
    CompoundTag tag = stack.getOrCreateTag();
    tag.putString("type", Type.RITUAL.name().toLowerCase(Locale.ROOT));
    tag.putString("ritual", Registries.RITUAL_REGISTRY.get().getKey(ritual).toString());
  }

  @Nullable
  public Ritual getRitual(ItemStack stack) {
    CompoundTag tag = stack.getTag();
    if (tag == null) {
      return null;
    }

    return Registries.RITUAL_REGISTRY.get().getValue(new ResourceLocation(tag.getString("ritual")));
  }

  public enum Type {
    SPELL(true), // Spell
    MODIFIER(true), // Modifier
    LIBRARY(false), // Spell + all available modifiers
    STAFF(false), // SpellInstance
    STAFF_MODIFIER(false),
    RITUAL(false); // Spell + Modifier and a boolean

    private final boolean grantable;

    Type(boolean grantable) {
      this.grantable = grantable;
    }

    public boolean isGrantable() {
      return grantable;
    }
  }

  public record StaffModifier(Spell spell, Modifier modifier, boolean enabled) {
  }

  public record LibraryInfo(Spell spell, Set<Modifier> modifiers) {
  }
}
