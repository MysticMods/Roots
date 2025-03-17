package mysticmods.roots.api.condition;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.registry.IDescribed;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class PlayerCondition implements IDescribed {
  public static final Codec<PlayerCondition> CODEC = RootsRegistries.PLAYER_CONDITIONS.byNameCodec();
  public static final Codec<List<PlayerCondition>> LIST_CODEC = CODEC.listOf();
  public static final StreamCodec<RegistryFriendlyByteBuf, PlayerCondition> STREAM_CODEC = ByteBufCodecs.registry(RootsRegistries.Keys.PLAYER_CONDITIONS);
  public static final StreamCodec<RegistryFriendlyByteBuf, List<PlayerCondition>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());
  private String descriptionId;

  public PlayerCondition() {
  }

  public Holder<PlayerCondition> builtInRegistryHolder() {
    return RootsRegistries.PLAYER_CONDITIONS.wrapAsHolder(this);
  }

  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId("player_condition", builtInRegistryHolder().getKey().location());
    }

    return this.descriptionId;
  }

  public abstract boolean test(Level level, @Nullable Player player);

  public static class PlayerOffHandTaggedItemCondition extends PlayerCondition {
    private final TagKey<Item> tag;

    public PlayerOffHandTaggedItemCondition(TagKey<Item> tag) {
      this.tag = tag;
    }

    @Override
    public boolean test(Level level, @Nullable Player player) {
      if (player == null) {
        return false;
      }

      return player.getOffhandItem().is(tag);
    }
  }
}
