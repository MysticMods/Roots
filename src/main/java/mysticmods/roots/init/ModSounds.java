package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.sounds.SoundEvent;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModSounds {
  // Endermini
  public static final RegistryEntry<SoundEvent> ENDERMINI_DEATH = REGISTRATE.soundEvent("mob.endermini.death").register();
  public static final RegistryEntry<SoundEvent> ENDERMINI_HIT = REGISTRATE.soundEvent("mob.endermini.hit").register();
  public static final RegistryEntry<SoundEvent> ENDERMINI_IDLE = REGISTRATE.soundEvent("mob.endermini.idle").register();
  public static final RegistryEntry<SoundEvent> ENDERMINI_PORTAL = REGISTRATE.soundEvent("mob.endermini.portal").register();
  public static final RegistryEntry<SoundEvent> ENDERMINI_SCREAM = REGISTRATE.soundEvent("mob.endermini.scream").register();
  public static final RegistryEntry<SoundEvent> ENDERMINI_STARE = REGISTRATE.soundEvent("mob.endermini.stare").register();

  // Sprout
  public static final RegistryEntry<SoundEvent> SPROUT_AMBIENT = REGISTRATE.soundEvent("mob.sprout.ambient").register();

  // Fennec
  public static final RegistryEntry<SoundEvent> FENNEC_AGGRO = REGISTRATE.soundEvent("mob.fennec.aggro").register();
  public static final RegistryEntry<SoundEvent> FENNEC_BARK = REGISTRATE.soundEvent("mob.fennec.bark").register();
  public static final RegistryEntry<SoundEvent> FENNEC_BITE = REGISTRATE.soundEvent("mob.fennec.bite").register();
  public static final RegistryEntry<SoundEvent> FENNEC_DEATH = REGISTRATE.soundEvent("mob.fennec.death").register();
  public static final RegistryEntry<SoundEvent> FENNEC_EAT = REGISTRATE.soundEvent("mob.fennec.eat").register();
  public static final RegistryEntry<SoundEvent> FENNEC_IDLE = REGISTRATE.soundEvent("mob.fennec.idle").register();
  public static final RegistryEntry<SoundEvent> FENNEC_SLEEP = REGISTRATE.soundEvent("mob.fennec.sleep").register();
  public static final RegistryEntry<SoundEvent> FENNEC_SNIFF = REGISTRATE.soundEvent("mob.fennec.sniff").register();
  public static final RegistryEntry<SoundEvent> FENNEC_SPIT = REGISTRATE.soundEvent("mob.fennec.spit").register();

  // Silkworm
  public static final RegistryEntry<SoundEvent> SILKWORM_EGG_USE = REGISTRATE.soundEvent("mob.silkworm.egg.use").register();
  public static final RegistryEntry<SoundEvent> SILKWORM_PLOP = REGISTRATE.soundEvent("mob.silkworm.plop").register();
  public static final RegistryEntry<SoundEvent> SILKWORM_AMBIENT = REGISTRATE.soundEvent("mob.silkworm.ambient").register();
  public static final RegistryEntry<SoundEvent> SILKWORM_DEATH = REGISTRATE.soundEvent("mob.silkworm.death").register();
  public static final RegistryEntry<SoundEvent> SILKWORM_HURT = REGISTRATE.soundEvent("mob.silkworm.hurt").register();
  public static final RegistryEntry<SoundEvent> SILKWORM_STEP = REGISTRATE.soundEvent("mob.silkworm.step").register();
  public static final RegistryEntry<SoundEvent> SILKWORM_EAT = REGISTRATE.soundEvent("mob.silkworm.eat").register();

  // Lava cat
  public static final RegistryEntry<SoundEvent> LAVA_CAT_SIZZLE = REGISTRATE.soundEvent("mob.lava_cat.sizzle").register();
  public static final RegistryEntry<SoundEvent> LAVA_CAT_AMBIENT = REGISTRATE.soundEvent("mob.lava_cat.ambient").register();
  public static final RegistryEntry<SoundEvent> LAVA_CAT_DEATH = REGISTRATE.soundEvent("mob.lava_cat.death").register();
  public static final RegistryEntry<SoundEvent> LAVA_CAT_HURT = REGISTRATE.soundEvent("mob.lava_cat.hurt").register();
  public static final RegistryEntry<SoundEvent> LAVA_CAT_PURR = REGISTRATE.soundEvent("mob.lava_cat.purr").register();
  public static final RegistryEntry<SoundEvent> LAVA_CAT_PURREOW = REGISTRATE.soundEvent("mob.lava_cat.purreow").register();

  // Frog
/*  public static final RegistryEntry<SoundEvent> FROG_SLIME = REGISTRATE.soundEvent("mob.frog.slime").register();
  public static final RegistryEntry<SoundEvent> FROG_AMBIENT = REGISTRATE.soundEvent("mob.frog.croak").register();*/

  // Squid
  public static final RegistryEntry<SoundEvent> SQUID_MILK = REGISTRATE.soundEvent("mob.squid.milk").register();

  // Unrip pearl
  public static final RegistryEntry<SoundEvent> UNRIPE_PEARL_USE = REGISTRATE.soundEvent("item.unripe_pearl.use").register();
  public static final RegistryEntry<SoundEvent> PEARLEPORTER_USE = REGISTRATE.soundEvent("item.pearleporter.use").register();

  // Duck
/*  public static final RegistryEntry<SoundEvent> DUCK_AMBIENT = REGISTRATE.soundEvent("mob.duck.quack").register();*/
  public static final RegistryEntry<SoundEvent> DUCK_SWIM = REGISTRATE.soundEvent("mob.duck.swim").register();

  // Deer
/*  public static final RegistryEntry<SoundEvent> DEER_AMBIENT = REGISTRATE.soundEvent("mob.deer.ambient").register();*/

  public static void load() {
  }
}
