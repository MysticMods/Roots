package epicsquid.roots.init;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nullable;
import java.util.UUID;

public class ModDamage {
  private static final String FAKE_PLAYER_NAME = "Fey Fire";
  private static final UUID FAKE_PLAYER_UUID = UUID.fromString("fd3bc208-e351-48ac-afc4-1b612dc36aff");
  private static final GameProfile FAKE_PLAYER = new GameProfile(FAKE_PLAYER_UUID, FAKE_PLAYER_NAME);

  public static final String FEY_FIRE = "fey_fire";

  @SuppressWarnings("unused")
  public static DamageSource RADIANT_DAMAGE = (new DamageSource("holy_damage")).setDamageBypassesArmor().setMagicDamage();

  public static DamageSource PSYCHIC_DAMAGE = (new DamageSource("psychic_damage")).setDamageBypassesArmor().setMagicDamage();

  public static DamageSource WATER_DAMAGE = (new DamageSource("water_damage")).setDamageBypassesArmor().setMagicDamage();

  public static DamageSource BLEED_DAMAGE = (new DamageSource("bleed_damage")).setDamageBypassesArmor();

  public static DamageSource ROSE_DAMAGE = (new DamageSource("rose_thorns")).setDamageBypassesArmor();

  public static DamageSource roseDamageFrom (@Nullable EntityPlayer player) {
    DamageSource source;
    if (player == null) {
      return ROSE_DAMAGE;
    } else {
      source = new EntityDamageSource("rose_thorns", player).setDamageBypassesArmor();
    }
    return source;
  }

  public static DamageSource radiantDamageFrom(@Nullable EntityPlayer player) {
    DamageSource source;
    if (player == null) {
      source = RADIANT_DAMAGE;
    } else {
      source = new EntityDamageSource("holy_damage", player).setDamageBypassesArmor().setMagicDamage();
    }
    if (Loader.isModLoaded("consecration")) {
      source.setFireDamage();
    }
    return source;
  }

  public static DamageSource fireDamageFrom(@Nullable EntityPlayer player) {
    DamageSource source;
    if (player == null) {
      source = DamageSource.IN_FIRE;
    } else {
      source = new EntityDamageSource("fire_damage", player).setDamageBypassesArmor().setMagicDamage().setFireDamage();
    }
    return source;
  }

  public static DamageSource waterDamageFrom(@Nullable EntityPlayer player) {
    if (player == null) {
      return WATER_DAMAGE;
    }

    return new EntityDamageSource("water_damage", player).setDamageBypassesArmor().setMagicDamage();
  }

  @Nullable
  public static DamageSource wildfireDamage(World world) {
    if (world.isRemote) return null;

    WorldServer server = (WorldServer) world;
    FakePlayer player = FakePlayerFactory.get(server, FAKE_PLAYER);

    return new EntityDamageSource(FEY_FIRE, player).setDamageBypassesArmor().setMagicDamage().setFireDamage();
  }

  public static void init() {
  }
}
