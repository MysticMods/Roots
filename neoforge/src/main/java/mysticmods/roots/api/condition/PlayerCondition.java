package mysticmods.roots.api.condition;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.registry.DescribedEntry;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class PlayerCondition extends DescribedEntry {
  public static final Codec<PlayerCondition> CODEC = RootsRegistries.PLAYER_CONDITIONS.byNameCodec();
  public static final Codec<List<PlayerCondition>> LIST_CODEC = CODEC.listOf();
  public static final StreamCodec<RegistryFriendlyByteBuf, PlayerCondition> STREAM_CODEC = ByteBufCodecs.registry(RootsRegistries.Keys.PLAYER_CONDITIONS);
  public static final StreamCodec<RegistryFriendlyByteBuf, List<PlayerCondition>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

  public PlayerCondition() {
  }

  @Override
  protected String getDescriptor() {
    return "player_condition";
  }

  public abstract boolean test(Level level, @Nullable Player player);
}
