package mysticmods.roots.api.attachment;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface Unlock<T> {
  BiMap<ResourceLocation, UnlockType> TYPES = ImmutableBiMap.of(
      RootsAPI.rl("spell"), SpellUnlock.TYPE,
      RootsAPI.rl("spell_modifier"), ModifierUnlock.TYPE
  );
  Codec<Unlock<?>> CODEC = ResourceLocation.CODEC.xmap(TYPES::get, TYPES.inverse()::get)
      .dispatch(Unlock::unlockType, UnlockType::codec);
  Codec<List<Unlock<?>>> LIST_CODEC = CODEC.listOf();
  StreamCodec<RegistryFriendlyByteBuf, Unlock<?>> STREAM_CODEC = StreamCodec.of(RegistryFriendlyByteBuf::writeResourceLocation, RegistryFriendlyByteBuf::readResourceLocation)
      .map(TYPES::get, TYPES.inverse()::get).dispatch(Unlock::unlockType, UnlockType::streamCodec);
  StreamCodec<RegistryFriendlyByteBuf, List<Unlock<?>>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

  default boolean is(UnlockType type) {
    return unlockType().equals(type);
  }

  UnlockType unlockType();

  Component getFailed();

  default ItemStack getIcon() {
    return ItemStack.EMPTY;
  }

  static SpellUnlock spell(Holder<Spell> value) {
    return new SpellUnlock(value);
  }

  static SpellUnlock spell(Spell value) {
    return new SpellUnlock(value.builtInRegistryHolder());
  }

  static ModifierUnlock modifier(Holder<Modifier> value) {
    return new ModifierUnlock(value);
  }

  record SpellUnlock(Holder<Spell> value) implements Unlock<Spell> {

    public static final MapCodec<SpellUnlock> CODEC = RootsRegistries.SPELLS.holderByNameCodec().fieldOf("value")
        .xmap(SpellUnlock::new, SpellUnlock::value);
    public static final StreamCodec<RegistryFriendlyByteBuf, SpellUnlock> STREAM_CODEC = ByteBufCodecs.holderRegistry(RootsRegistries.Keys.SPELLS)
        .map(SpellUnlock::new, SpellUnlock::value);
    public static final UnlockType TYPE = new UnlockType(CODEC, STREAM_CODEC);

    @Override
    public UnlockType unlockType() {
      return TYPE;
    }

    @Override
    public ItemStack getIcon() {
      return value.value().getIcon();
    }

    @Override
    public Component getFailed() {
      return Component.translatable("roots.message.spell.already_learned", value.value().getStyledName());
    }
  }

  record ModifierUnlock(Holder<Modifier> value) implements Unlock<Modifier> {

    public static final MapCodec<ModifierUnlock> CODEC = RootsRegistries.SPELL_MODIFIERS.holderByNameCodec()
        .fieldOf("defaultValue").xmap(ModifierUnlock::new, ModifierUnlock::value);
    public static final StreamCodec<RegistryFriendlyByteBuf, ModifierUnlock> STREAM_CODEC = ByteBufCodecs.holderRegistry(RootsRegistries.Keys.SPELL_MODIFIERS)
        .map(ModifierUnlock::new, ModifierUnlock::value);
    public static final UnlockType TYPE = new UnlockType(CODEC, STREAM_CODEC);

    @Override
    public UnlockType unlockType() {
      return TYPE;
    }

    @Override
    public Component getFailed() {
      return Component.translatable("roots.message.spell_modifier.already_learned", value.value().getName());
    }
  }

  record UnlockType(MapCodec<? extends Unlock<?>> codec,
                    StreamCodec<? super RegistryFriendlyByteBuf, ? extends Unlock<?>> streamCodec) {
  }
}
