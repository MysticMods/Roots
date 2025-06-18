package mysticmods.roots.api.condition;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;

public interface IPlayerCondition {
  Codec<IPlayerCondition> CODEC = RootsRegistries.PLAYER_CONDITIONS.byNameCodec()
      .dispatch(IPlayerCondition::type, IPlayerConditionType::mapCodec);
  StreamCodec<RegistryFriendlyByteBuf, IPlayerCondition> STREAM_CODEC = ByteBufCodecs.registry(RootsRegistries.Keys.PLAYER_CONDITIONS)
      .dispatch(IPlayerCondition::type, IPlayerConditionType::streamCodec);
  Codec<List<IPlayerCondition>> LIST_CODEC = CODEC.listOf();
  StreamCodec<RegistryFriendlyByteBuf, List<IPlayerCondition>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

  boolean test(Level level, @Nonnull Player player);

  IPlayerConditionType<?> type();

  String getName ();

  default Component getNameComponent() {
    return Component.translatable("player_condition.roots." + getName());
  }

  default Component getDescriptionComponent () {
    return Component.translatable("player_condition.roots." + getName() + ".description");
  }
}
