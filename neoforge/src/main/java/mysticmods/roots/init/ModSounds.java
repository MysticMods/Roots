package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.sounds.SoundEvent;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModSounds {
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

  // Squid
  public static final RegistryEntry<SoundEvent> SQUID_MILK = REGISTRATE.soundEvent("mob.squid.milk").register();

  // Duck
/*  public static final RegistryEntry<SoundEvent> DUCK_AMBIENT = REGISTRATE.soundEvent("mob.duck.quack").register();*/
  public static final RegistryEntry<SoundEvent> DUCK_SWIM = REGISTRATE.soundEvent("mob.duck.swim").register();

  // Deer
/*  public static final RegistryEntry<SoundEvent> DEER_AMBIENT = REGISTRATE.soundEvent("mob.deer.ambient").register();*/

  public static void load() {
  }
}
