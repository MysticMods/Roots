package mysticmods.roots.api.attachment;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.spell.SpellModifier;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface Unlock<T> {
  BiMap<ResourceLocation, UnlockType> TYPES = ImmutableBiMap.of(
      RootsAPI.rl("spell"), SpellUnlock.TYPE,
      RootsAPI.rl("spell_modifier"), ModifierUnlock.TYPE
  );
  Codec<Unlock<?>> CODEC = ResourceLocation.CODEC.xmap(TYPES::get, TYPES.inverse()::get).dispatch(Unlock::unlockType, UnlockType::codec);
  Codec<List<Unlock<?>>> LIST_CODEC = CODEC.listOf();
  StreamCodec<RegistryFriendlyByteBuf, Unlock<?>> STREAM_CODEC = StreamCodec.of(RegistryFriendlyByteBuf::writeResourceLocation, RegistryFriendlyByteBuf::readResourceLocation).map(TYPES::get, TYPES.inverse()::get).dispatch(Unlock::unlockType, UnlockType::streamCodec);
  StreamCodec<RegistryFriendlyByteBuf, List<Unlock<?>>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

  default boolean is(UnlockType type) {
    return unlockType().equals(type);
  }

  UnlockType unlockType();

  record SpellUnlock(Holder<Spell> value) implements Unlock<Spell> {

    public static final MapCodec<SpellUnlock> CODEC = RootsRegistries.SPELLS.holderByNameCodec().fieldOf("value").xmap(SpellUnlock::new, SpellUnlock::value);
    public static final StreamCodec<RegistryFriendlyByteBuf, SpellUnlock> STREAM_CODEC = ByteBufCodecs.holderRegistry(RootsRegistries.Keys.SPELLS).map(SpellUnlock::new, SpellUnlock::value);
    public static final UnlockType TYPE = new UnlockType(CODEC, STREAM_CODEC);

    @Override
    public UnlockType unlockType() {
      return TYPE;
    }
  }

  record ModifierUnlock(Holder<SpellModifier> value) implements Unlock<SpellModifier> {

    public static final MapCodec<ModifierUnlock> CODEC = RootsRegistries.SPELL_MODIFIERS.holderByNameCodec().fieldOf("defaultValue").xmap(ModifierUnlock::new, ModifierUnlock::value);
    public static final StreamCodec<RegistryFriendlyByteBuf, ModifierUnlock> STREAM_CODEC = ByteBufCodecs.holderRegistry(RootsRegistries.Keys.SPELL_MODIFIERS).map(ModifierUnlock::new, ModifierUnlock::value);
    public static final UnlockType TYPE = new UnlockType(CODEC, STREAM_CODEC);

    @Override
    public UnlockType unlockType() {
      return TYPE;
    }
  }

  record UnlockType(MapCodec<? extends Unlock<?>> codec,
                    StreamCodec<? super RegistryFriendlyByteBuf, ? extends Unlock<?>> streamCodec) {

  }
}
