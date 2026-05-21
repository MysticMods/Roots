package mysticmods.roots.item;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.Unlock;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.client.RootsClientHooks;
import mysticmods.roots.util.TooltipUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
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

public abstract class TokenItem extends Item {
  public TokenItem(Properties pProperties) {
    super(pProperties);
  }

  @Override
  public abstract String getDescriptionId(ItemStack pStack);

  @Nullable
  protected abstract Unlock<?> getUnlock();

  @Override
  public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
    ItemStack stack = pPlayer.getItemInHand(pUsedHand);
    Unlock<?> unlock = getUnlock();
    if (unlock == null) {
      return InteractionResultHolder.fail(stack);
    }

    if (pLevel.isClientSide()) {
      return InteractionResultHolder.consume(stack);
    }

    if (!RootsAPI.getInstance().canUnlock((ServerPlayer) pPlayer, unlock)) {
      return InteractionResultHolder.fail(stack);
    }

    RootsAPI.getInstance().unlock((ServerPlayer) pPlayer, unlock);
    if (!pPlayer.isCreative()) {
      stack.shrink(1);
    }
    return InteractionResultHolder.success(stack);
  }

  public static class SpellTokenItem extends TokenItem {
    private final ResourceKey<Spell> spell;

    public SpellTokenItem(ResourceKey<Spell> spell, Properties properties) {
      super(properties);
      this.spell = spell;
    }

    public Spell getSpell() {
      return RootsRegistries.SPELLS.get(spell);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
      return getSpell().getDescriptionId();
    }

    @Override
    public Component getName(ItemStack stack) {
      return getSpell().getStyledName();
    }

    @Override
    protected @Nullable Unlock<?> getUnlock() {
      return new Unlock.SpellUnlock(getSpell().builtInRegistryHolder());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
      super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
      tooltipComponents.add(Component.empty());
      TooltipUtil.baseSpellCostTooltip(context, tooltipComponents, getSpell(), tooltipFlag);
      if (context.level() != null && context.level().isClientSide()) {
        RootsClientHooks.appendTokenHoverText(this, stack, context, tooltipComponents, tooltipFlag);
      }
    }
  }

  public static class RitualTokenItem extends TokenItem {
    private final ResourceKey<Ritual> ritual;

    public RitualTokenItem(ResourceKey<Ritual> ritual, Properties properties) {
      super(properties);
      this.ritual = ritual;
    }

    public Ritual getRitual() {
      return RootsRegistries.RITUALS.get(ritual);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
      return getRitual().getDescriptionId();
    }

    @Override
    protected @Nullable Unlock<?> getUnlock() {
      return null;
    }
  }

  public static class GroveTokenItem extends TokenItem {
    private final ResourceKey<Grove> grove;

    public GroveTokenItem(ResourceKey<Grove> grove, Properties properties) {
      super(properties);
      this.grove = grove;
    }

    public Grove getGrove() {
      return RootsRegistries.GROVES.get(grove);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
      return getGrove().getDescriptionId();
    }

    @Override
    public @Nullable Unlock<?> getUnlock() {
      return null;
    }
  }

  public enum TokenType implements StringRepresentable {
    SPELL, MODIFIER, RITUAL, GROVE;

    public static final Codec<TokenType> CODEC = StringRepresentable.fromEnum(TokenType::values);
    public static final StreamCodec<ByteBuf, TokenType> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(TokenType::fromOrdinal, TokenType::ordinal);

    private final String name;

    TokenType() {
      this.name = name().toLowerCase(Locale.ROOT);
    }

    @Override
    public String getSerializedName() {
      return name;
    }

    public static TokenType fromOrdinal(int ordinal) {
      return TokenType.values()[ordinal];
    }
  }
}
