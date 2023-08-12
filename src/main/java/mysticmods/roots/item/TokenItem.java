package mysticmods.roots.item;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.capability.Capabilities;
import mysticmods.roots.api.capability.GrantCapability;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellInstance;
import mysticmods.roots.init.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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

import java.util.ArrayList;
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
          Modifier modifier = getSingleModifier(pStack);
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

    if (!pPlayer.isCreative()) {
      stack.shrink(1);
      if (stack.isEmpty()) {
        pPlayer.setItemInHand(pUsedHand, ItemStack.EMPTY);
      }
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
        Modifier modifier = getSingleModifier(stack);
        if (modifier != null) {
          cap.grantModifier(modifier);
          result = InteractionResultHolder.success(stack);
        }
      }
    }
    if (result != null) {
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
        Modifier modifier = getSingleModifier(pStack);
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
          pTooltipComponents.add(Component.translatable("roots.tooltip.token.spell", spell.getStyledName()));
          for (Modifier enabled : spell.getEnabledModifiers()) {
            pTooltipComponents.add(Component.translatable("roots.tooltip.token.modifier", enabled.getName()));
          }
        }
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

  protected ItemStack t() {
    return new ItemStack(this);
  }

  @Override
  public void fillItemCategory(CreativeModeTab pCategory, NonNullList<ItemStack> pItems) {
    if (this.allowedIn(pCategory)) {
      Registries.SPELL_REGISTRY.get().forEach(o -> pItems.add(setSpell(t(), o)));
      Registries.MODIFIER_REGISTRY.get().forEach(o -> pItems.add(setSingleModifier(t(), o)));
      Registries.RITUAL_REGISTRY.get().forEach(o -> pItems.add(setRitual(t(), o)));
    }
  }

  @Nullable
  public static Type getType(ItemStack stack) {
    CompoundTag tag = stack.getTag();
    if (tag == null) {
      return null;
    }

    return EnumUtil.fromString(Type.class, tag.getString("type"));
  }

  @Nullable
  public static Spell getSpell(ItemStack stack) {
    CompoundTag tag = stack.getTag();
    if (tag == null) {
      return null;
    }

    return Registries.SPELL_REGISTRY.get().getValue(new ResourceLocation(tag.getString("spell")));
  }

  public static ItemStack setSpell(ItemStack stack, Spell spell) {
    CompoundTag tag = stack.getOrCreateTag();
    tag.putString("type", Type.SPELL.name().toLowerCase(Locale.ROOT));
    tag.putString("spell", Registries.SPELL_REGISTRY.get().getKey(spell).toString());
    return stack;
  }

  @Nullable
  public static Modifier getSingleModifier(ItemStack stack) {
    CompoundTag tag = stack.getTag();
    if (tag == null) {
      return null;
    }

    return Registries.MODIFIER_REGISTRY.get().getValue(new ResourceLocation(tag.getString("modifier")));
  }

  @Nullable
  public static Set<Modifier> getModifiers(ItemStack stack) {
    SpellInstance spellInstance = getSpellInstance(stack);
    if (spellInstance == null) {
      return null;
    }

    return spellInstance.getEnabledModifiers();
  }

  public static ItemStack setSingleModifier(ItemStack stack, Modifier modifier) {
    CompoundTag tag = stack.getOrCreateTag();
    tag.putString("type", Type.MODIFIER.name().toLowerCase(Locale.ROOT));
    tag.putString("modifier", Registries.MODIFIER_REGISTRY.get().getKey(modifier).toString());
    return stack;
  }

  public static boolean enableModifier(ItemStack stack, Modifier modifier) {
    SpellInstance spellInstance = getSpellInstance(stack);
    if (spellInstance == null) {
      return false;
    }
    Spell spell = spellInstance.getSpell();
    if (modifier.getSpell() != spell) {
      return false;
    }
    spellInstance.addModifier(modifier);
    setSpellInstance(stack, spellInstance);
    return true;
  }

  public static boolean disableModifier(ItemStack stack, Modifier modifier) {
    SpellInstance spellInstance = getSpellInstance(stack);
    if (spellInstance == null) {
      return false;
    }
    Spell spell = spellInstance.getSpell();
    if (modifier.getSpell() != spell) {
      return false;
    }
    spellInstance.removeModifier(modifier);
    setSpellInstance(stack, spellInstance);
    return true;
  }


  @Nullable
  public static SpellInstance getSpellInstance(ItemStack stack) {
    CompoundTag tag = stack.getTag();
    if (tag == null) {
      return null;
    }

    return SpellInstance.fromNBT(tag);
  }

  public static ItemStack setSpellInstance(ItemStack stack, SpellInstance instance) {
    CompoundTag tag = stack.getOrCreateTag();
    instance.toNBT(tag);
    tag.putString("type", Type.STAFF.name().toLowerCase(Locale.ROOT));
    return stack;
  }

  public static ItemStack setRitual(ItemStack stack, Ritual ritual) {
    CompoundTag tag = stack.getOrCreateTag();
    tag.putString("type", Type.RITUAL.name().toLowerCase(Locale.ROOT));
    tag.putString("ritual", Registries.RITUAL_REGISTRY.get().getKey(ritual).toString());
    return stack;
  }

  @Nullable
  public static Ritual getRitual(ItemStack stack) {
    CompoundTag tag = stack.getTag();
    if (tag == null) {
      return null;
    }

    return Registries.RITUAL_REGISTRY.get().getValue(new ResourceLocation(tag.getString("ritual")));
  }

  public enum Type {
    SPELL(true), // Spell
    MODIFIER(true), // Modifier
    STAFF(false), // SpellInstance
    RITUAL(false); // Spell + Modifier and a boolean

    private final boolean grantable;

    Type(boolean grantable) {
      this.grantable = grantable;
    }

    public boolean isGrantable() {
      return grantable;
    }
  }

  public static boolean isGranted(Player player, ItemStack stack) {
    CompoundTag tag = stack.getTag();
    if (tag == null || !tag.contains("type", Tag.TAG_STRING)) {
      return false;
    }
    Type type = EnumUtil.fromString(Type.class, tag.getString("type"));
    if (type.isGrantable()) {
      return false;
    }
    GrantCapability cap = player.getCapability(Capabilities.GRANT_CAPABILITY).orElseThrow(NullPointerException::new);
    if (type == Type.SPELL) {
      return cap.hasSpell(Registries.SPELL_REGISTRY.get().getValue(new ResourceLocation(tag.getString("spell"))));
    } else if (type == Type.MODIFIER) {
      return cap.hasModifier(Registries.MODIFIER_REGISTRY.get().getValue(new ResourceLocation(tag.getString("modifier"))));
    } else {
      // TODO: Throw here?
      return false;
    }
  }

  public static final ResourceLocation INVALID_MODEL = RootsAPI.rl("invalid");

  public static ResourceLocation getModelLocation(ItemStack stack) {
    CompoundTag tag = stack.getOrCreateTag();
    Type type = EnumUtil.fromString(Type.class, tag.getString("type"));
    if (type == null) {
      return INVALID_MODEL;
    }

    switch (type) {
      case RITUAL -> {
        ResourceLocation ritual = new ResourceLocation(tag.getString("ritual"));
        return new ResourceLocation(ritual.getNamespace(), "item/ritual_" + ritual.getPath());
      }
      case SPELL -> {
        ResourceLocation spell = new ResourceLocation(tag.getString("spell"));
        return new ResourceLocation(spell.getNamespace(), "item/spell_" + spell.getPath());
      }
      case MODIFIER -> {
        ResourceLocation modifier = new ResourceLocation(tag.getString("modifier"));
        return new ResourceLocation(modifier.getNamespace(), "item/modifier_" + modifier.getPath());
      }
      default -> {
        return INVALID_MODEL;
      }
    }
  }

  protected static ItemStack T() {
    return new ItemStack(ModItems.TOKEN.get());
  }

  public static ItemStack getSpellToken(Spell spell) {
    return setSpell(T(), spell);
  }

  public static ItemStack getModifierToken(Modifier modifier) {
    return setSingleModifier(T(), modifier);
  }

  public static ItemStack getRitualToken(Ritual ritual) {
    return setRitual(T(), ritual);
  }

  public static ItemStack getSpellInstanceToken (SpellInstance spell) {
    return setSpellInstance(T(), spell);
  }

  public static List<ItemStack> getSpells() {
    List<ItemStack> stack = new ArrayList<>();
    for (Spell spell : Registries.SPELL_REGISTRY.get().getValues()) {
      stack.add(getSpellToken(spell));
    }
    return stack;
  }

  public static List<ItemStack> getModifiers(Spell spell) {
    List<ItemStack> stack = new ArrayList<>();
    for (Modifier modifier : Registries.MODIFIER_REGISTRY.get().getValues()) {
      if (modifier.getSpell() == spell) {
        stack.add(getModifierToken(modifier));
      }
    }
    return stack;
  }
}
