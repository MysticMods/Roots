package mysticmods.roots.api.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

// canEnable: isn't currently enabled, none of its conflicts are enabled, and either all of its parents are enabled or can be enabled
// isEnabled: is currently enabled
// isUnlocked: is unlocked for the player
// isRestricted: is globally restricted
public record ModifierInfo(boolean canEnable, boolean isEnabled, boolean isUnlocked, boolean isRestricted) {
  public static final Codec<ModifierInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      Codec.BOOL.fieldOf("canEnable").forGetter(ModifierInfo::canEnable),
      Codec.BOOL.fieldOf("isEnabled").forGetter(ModifierInfo::isEnabled),
      Codec.BOOL.fieldOf("isUnlocked").forGetter(ModifierInfo::isUnlocked),
      Codec.BOOL.fieldOf("isRestricted").forGetter(ModifierInfo::isRestricted)
  ).apply(instance, ModifierInfo::new));
  public static final StreamCodec<RegistryFriendlyByteBuf, ModifierInfo> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.BOOL, ModifierInfo::canEnable, ByteBufCodecs.BOOL, ModifierInfo::isEnabled, ByteBufCodecs.BOOL, ModifierInfo::isUnlocked, ByteBufCodecs.BOOL, ModifierInfo::isRestricted, ModifierInfo::new
  );
}
