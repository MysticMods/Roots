package mysticmods.roots.api.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public interface ICondition {
  Codec<ICondition> DIRECT_CODEC = RootsRegistries.CONDITION_TYPES.byNameCodec()
      .dispatch(ICondition::codec, Function.identity());

  Codec<Holder<ICondition>> CODEC = RegistryFixedCodec.create(RootsRegistries.Keys.CONDITIONS);
  StreamCodec<RegistryFriendlyByteBuf, Holder<ICondition>> STREAM_CODEC = ByteBufCodecs.holderRegistry(RootsRegistries.Keys.CONDITIONS);
  Codec<List<Holder<ICondition>>> LIST_CODEC = CODEC.listOf();
  StreamCodec<RegistryFriendlyByteBuf, List<Holder<ICondition>>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

  MapCodec<? extends ICondition> codec();

  @Nullable
  default ConditionIngredient getIngredient() {
    return null;
  }

  static String getDescriptionId(Holder<ICondition> condition) {
    return Util.makeDescriptionId("condition", condition.getKey().location());
  }

  static Component getName(Holder<ICondition> condition) {
    return Component.translatable(getDescriptionId(condition));
  }

  static Component getDescription(Holder<ICondition> condition) {
    return Component.translatable(getDescriptionId(condition) + ".description");
  }
}
