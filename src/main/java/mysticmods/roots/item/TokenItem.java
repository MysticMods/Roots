package mysticmods.roots.item;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Locale;

public class TokenItem extends Item {
  public TokenItem(Properties pProperties) {
    super(pProperties);
  }

  @Override
  public String getDescriptionId(ItemStack pStack) {
    // TODO:
    return super.getDescriptionId(pStack);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
    ItemStack stack = pPlayer.getItemInHand(pUsedHand);
    if (pLevel.isClientSide()) {
      return InteractionResultHolder.consume(stack);
    }

    return InteractionResultHolder.fail(stack);
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
