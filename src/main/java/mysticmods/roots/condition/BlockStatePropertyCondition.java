package mysticmods.roots.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.condition.CanonicalRepresentation;
import mysticmods.roots.api.condition.ILevelCondition;
import mysticmods.roots.api.condition.ILevelConditionType;
import mysticmods.roots.api.test.world.PartialBlockStateMatchWorldTest;
import mysticmods.roots.init.ModConditions;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Collections;
import java.util.Set;

public record BlockStatePropertyCondition (String name, PartialBlockStateMatchWorldTest test) implements ILevelCondition {
  public static final MapCodec<BlockStatePropertyCondition> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.STRING.fieldOf("name").forGetter(BlockStatePropertyCondition::name), PartialBlockStateMatchWorldTest.CODEC.fieldOf("test").forGetter(BlockStatePropertyCondition::test)).apply(instance, BlockStatePropertyCondition::new));
  public static final Codec<BlockStatePropertyCondition> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<RegistryFriendlyByteBuf, BlockStatePropertyCondition> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, BlockStatePropertyCondition::name, PartialBlockStateMatchWorldTest.STREAM_CODEC, BlockStatePropertyCondition::test, BlockStatePropertyCondition::new);

  @Override
  public Set<BlockPos> test(BlockPos pos, Level level, @javax.annotation.Nullable Player player) {
    if (test.test(level.getBlockState(pos), level.getRandom())) {
      return Collections.singleton(pos.immutable());
    }

    return Collections.emptySet();
  }

  @Override
  public CanonicalRepresentation getRepresentation() {
    if (ModConditions.SPECIAL_REPRESENTATIONS.containsKey(getName().intern())) {
      return ModConditions.SPECIAL_REPRESENTATIONS.get(getName().intern()).get();
    }
    return new CanonicalRepresentation(test.getPartialBlockState());
  }

  @Override
  public ILevelConditionType<?> type() {
    return ModConditions.BLOCK_STATE_CONDITION_TYPE.get();
  }

  @Override
  public String getName() {
    return name;
  }

  public static class Type implements ILevelConditionType<BlockStatePropertyCondition> {

    @Override
    public Codec<BlockStatePropertyCondition> codec() {
      return CODEC;
    }

    @Override
    public MapCodec<BlockStatePropertyCondition> mapCodec() {
      return MAP_CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, BlockStatePropertyCondition> streamCodec() {
      return STREAM_CODEC;
    }
  }
}
