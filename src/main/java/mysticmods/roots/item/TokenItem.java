package mysticmods.roots.item;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Locale;

public abstract class TokenItem extends Item {
  public TokenItem(Properties pProperties) {
    super(pProperties);
  }

  @Override
  public abstract String getDescriptionId(ItemStack pStack);

  @Override
  public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
    ItemStack stack = pPlayer.getItemInHand(pUsedHand);
    if (pLevel.isClientSide()) {
      return InteractionResultHolder.consume(stack);
    }

    return InteractionResultHolder.fail(stack);
  }

  public static class SpellTokenItem extends Item {
    private final ResourceKey<Spell> spell;
    public SpellTokenItem(ResourceKey<Spell> spell, Properties properties) {
      super(properties);
      this.spell = spell;
    }

    public Spell getSpell (HolderLookup.Provider provider) {
      return provider.lookupOrThrow(RootsRegistries.Keys.SPELLS).getOrThrow(spell).value();
    }

    @Override
    public String getDescriptionId (ItemStack stack) {
      return Util.makeDescriptionId("spell", RootsAPI.rl(builtInRegistryHolder().getKey().location().getPath().replace("spell_", "")));
    }
  }

  public static class RitualTokenItem extends Item {
    private final ResourceKey<Ritual> ritual;
    public RitualTokenItem(ResourceKey<Ritual> ritual, Properties properties) {
      super(properties);
      this.ritual = ritual;
    }

    public Ritual getRitual (HolderLookup.Provider provider) {
      return provider.lookupOrThrow(RootsRegistries.Keys.RITUALS).getOrThrow(ritual).value();
    }

    @Override
    public String getDescriptionId (ItemStack stack) {
      return Util.makeDescriptionId("ritual", RootsAPI.rl(builtInRegistryHolder().getKey().location().getPath().replace("ritual_", "")));
    }
  }

  public enum TokenType implements StringRepresentable {
    SPELL, MODIFIER, RITUAL;

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
