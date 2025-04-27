package mysticmods.roots.api.condition;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public interface ILevelCondition {
  Codec<ILevelCondition> CODEC = RootsRegistries.LEVEL_CONDITIONS.byNameCodec()
      .dispatch(ILevelCondition::type, ILevelConditionType::mapCodec);
  StreamCodec<RegistryFriendlyByteBuf, ILevelCondition> STREAM_CODEC = ByteBufCodecs.registry(RootsRegistries.Keys.LEVEL_CONDITIONS)
      .dispatch(ILevelCondition::type, ILevelConditionType::streamCodec);
  Codec<List<ILevelCondition>> LIST_CODEC = CODEC.listOf();
  StreamCodec<RegistryFriendlyByteBuf, List<ILevelCondition>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

  Set<BlockPos> test(BlockPos pos, Level level, @javax.annotation.Nullable Player player);

  default Set<BlockPos> test(Level level, @Nullable Player player, BoundingBox bounds, BlockPos pos, Set<BlockPos> exclusions) {
    BoundingBox newBounds = bounds.moved(pos.getX(), pos.getY(), pos.getZ());
    for (int x = newBounds.minX(); x < newBounds.maxX(); x++) {
      for (int y = newBounds.minY(); y < newBounds.maxY(); y++) {
        for (int z = newBounds.minZ(); z < newBounds.maxZ(); z++) {
          pos = new BlockPos(x, y, z);
          if (exclusions.contains(pos)) {
            continue;
          }
          Set<BlockPos> result = test(pos, level, player);
          if (!result.isEmpty()) {
            return result;
          }
        }
      }
    }

    return Collections.emptySet();
  }

  CanonicalRepresentation getRepresentation();

  ILevelConditionType<?> type();

  String getName ();

  default Component getNameComponent() {
    return Component.translatable("level_condition.roots." + getName());
  }

  default Component getDescriptionComponent() {
    return Component.translatable("level_condition.roots." + getName() + ".description");
  }
}
