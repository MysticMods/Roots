package epicsquid.roots.init;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
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
  private static final String WILD_FIRE_FAKE_PLAYER_NAME = "Wild Fire";
  private static final UUID WILD_FIRE_FAKE_PLAYER_UUID = UUID.fromString("fd3bc208-e351-48ac-afc4-1b612dc36aff");
  public static final GameProfile WILD_FIRE_FAKE_PLAYER = new GameProfile(WILD_FIRE_FAKE_PLAYER_UUID, WILD_FIRE_FAKE_PLAYER_NAME);

  private static final String FEY_CRAFTER_FAKE_PLAYER_NAME = "Fey Crafter";
  private static final UUID FEY_CRAFTER_FAKE_PLAYER_UUID = UUID.fromString("f8f5968b-da9f-430d-851b-cf232728f141");
  public static final GameProfile FEY_CRAFTER_FAKE_PLAYER = new GameProfile(FEY_CRAFTER_FAKE_PLAYER_UUID, FEY_CRAFTER_FAKE_PLAYER_NAME);

  public static final String WILD_FIRE = "wild_fire";

  @SuppressWarnings("unused")
  public static DamageSource RADIANT_DAMAGE = (new DamageSource("holy_damage")).setDamageBypassesArmor().setMagicDamage();

  public static DamageSource PHYSICAL_DAMAGE = (new DamageSource("physical_damage")).setDamageBypassesArmor();

  public static DamageSource PSYCHIC_DAMAGE = (new DamageSource("psychic_damage")).setDamageBypassesArmor().setMagicDamage();

  public static DamageSource WATER_DAMAGE = (new DamageSource("water_damage")).setDamageBypassesArmor().setMagicDamage();

  public static DamageSource COLD_DAMAGE = (new DamageSource("cold_damage")).setDamageBypassesArmor().setMagicDamage();

  public static DamageSource BLEED_DAMAGE = (new DamageSource("bleed_damage")).setDamageBypassesArmor();

  public static DamageSource ROSE_DAMAGE = (new DamageSource("rose_thorns")).setDamageBypassesArmor();

  public static DamageSource HARVEST_RITUAL_DAMAGE = (new DamageSource("harvest_damage_ritual"));

  public static DamageSource physicalDamageFrom(@Nullable Entity player) {
    DamageSource source;
    if (player == null) {
      source = PHYSICAL_DAMAGE;
    } else {
      source = new EntityDamageSource("physical", player).setDamageBypassesArmor();
    }
    return source;
  }

  public static DamageSource magicDamageFrom(@Nullable Entity player) {
    DamageSource source;
    if (player == null) {
      source = DamageSource.MAGIC;
    } else {
      source = new EntityDamageSource("magic", player).setDamageBypassesArmor().setMagicDamage();
    }
    return source;
  }

  public static DamageSource coldDamageFrom(@Nullable Entity player) {
    DamageSource source;
    if (player == null) {
      source = DamageSource.MAGIC;
    } else {
      source = new EntityDamageSource("cold_damage", player).setDamageBypassesArmor().setMagicDamage();
    }
    return source;
  }

  public static DamageSource roseDamageFrom(@Nullable Entity player) {
    DamageSource source;
    if (player == null) {
      return ROSE_DAMAGE;
    } else {
      source = new EntityDamageSource("rose_thorns", player).setDamageBypassesArmor();
    }
    return source;
  }

  public static DamageSource radiantDamageFrom(@Nullable Entity player) {
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

  public static DamageSource fireDamageFrom(@Nullable Entity player) {
    DamageSource source;
    if (player == null) {
      source = DamageSource.IN_FIRE;
    } else {
      source = new EntityDamageSource("fire_damage", player).setDamageBypassesArmor().setMagicDamage().setFireDamage();
    }
    return source;
  }

  public static DamageSource waterDamageFrom(@Nullable Entity player) {
    if (player == null) {
      return WATER_DAMAGE;
    }

    return new EntityDamageSource("water_damage", player).setDamageBypassesArmor().setMagicDamage();
  }

  @Nullable
  public static DamageSource wildfireDamage(World world) {
    if (world.isRemote) return null;

    FakePlayer player = getFakePlayer(world);

    return new EntityDamageSource(WILD_FIRE, player).setDamageBypassesArmor().setMagicDamage().setFireDamage();
  }

  public static FakePlayer getFakePlayer(World world) {
    return getFakePlayer(world, WILD_FIRE_FAKE_PLAYER);
  }

  public static FakePlayer getFakePlayer(World world, GameProfile profile) {
    if (world.isRemote) {
      throw new IllegalStateException("can't get a fake player on a client side");
    }

    WorldServer server = (WorldServer) world;
    return FakePlayerFactory.get(server, profile);
  }

  public static void init() {
  }
}
