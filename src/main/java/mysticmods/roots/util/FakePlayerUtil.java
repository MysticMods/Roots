package mysticmods.roots.util;

import com.mojang.authlib.GameProfile;
import mysticmods.roots.api.RootsAPI;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;

import java.util.UUID;

public class FakePlayerUtil {
  public static final ResourceKey<EnchantmentProvider> LOOTING_I = ResourceKey.create(Registries.ENCHANTMENT_PROVIDER, RootsAPI.rl("looting_i"));
  public static final ResourceKey<EnchantmentProvider> LOOTING_II = ResourceKey.create(Registries.ENCHANTMENT_PROVIDER, RootsAPI.rl("looting_ii"));
  public static final ResourceKey<EnchantmentProvider> LOOTING_III = ResourceKey.create(Registries.ENCHANTMENT_PROVIDER, RootsAPI.rl("looting_iii"));

  public static final UUID ROOTS_UUID = UUID.fromString("d16d208c-3636-4341-ae0b-bc89e8866e95");
  public static final GameProfile ROOTS = new GameProfile(ROOTS_UUID, "[roots]");
}
