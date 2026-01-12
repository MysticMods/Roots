package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
  private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, RootsAPI.MODID);

  // Sprout
  public static final DeferredHolder<SoundEvent, SoundEvent> SPROUT_AMBIENT = SOUNDS.register("mob.sprout.ambient", () -> SoundEvent.createVariableRangeEvent(RootsAPI.rl("mob.sprout.ambient")));

  // Fennec
  public static final DeferredHolder<SoundEvent, SoundEvent> FENNEC_AGGRO = SOUNDS.register("mob.fennec.aggro", () -> SoundEvent.createVariableRangeEvent(RootsAPI.rl("mob.fennec.aggro")));
  public static final DeferredHolder<SoundEvent, SoundEvent> FENNEC_BARK = SOUNDS.register("mob.fennec.bark", () -> SoundEvent.createVariableRangeEvent(RootsAPI.rl("mob.fennec.bark")));
  public static final DeferredHolder<SoundEvent, SoundEvent> FENNEC_BITE = SOUNDS.register("mob.fennec.bite", () -> SoundEvent.createVariableRangeEvent(RootsAPI.rl("mob.fennec.bite")));
  public static final DeferredHolder<SoundEvent, SoundEvent> FENNEC_DEATH = SOUNDS.register("mob.fennec.death", () -> SoundEvent.createVariableRangeEvent(RootsAPI.rl("mob.fennec.death")));
  public static final DeferredHolder<SoundEvent, SoundEvent> FENNEC_EAT = SOUNDS.register("mob.fennec.eat", () -> SoundEvent.createVariableRangeEvent(RootsAPI.rl("mob.fennec.eat")));
  public static final DeferredHolder<SoundEvent, SoundEvent> FENNEC_IDLE = SOUNDS.register("mob.fennec.idle", () -> SoundEvent.createVariableRangeEvent(RootsAPI.rl("mob.fennec.idle")));
  public static final DeferredHolder<SoundEvent, SoundEvent> FENNEC_SLEEP = SOUNDS.register("mob.fennec.sleep", () -> SoundEvent.createVariableRangeEvent(RootsAPI.rl("mob.fennec.sleep")));
  public static final DeferredHolder<SoundEvent, SoundEvent> FENNEC_SNIFF = SOUNDS.register("mob.fennec.sniff", () -> SoundEvent.createVariableRangeEvent(RootsAPI.rl("mob.fennec.sniff")));
  public static final DeferredHolder<SoundEvent, SoundEvent> FENNEC_SPIT = SOUNDS.register("mob.fennec.spit", () -> SoundEvent.createVariableRangeEvent(RootsAPI.rl("mob.fennec.spit")));

  // Squid
  public static final DeferredHolder<SoundEvent, SoundEvent> SQUID_MILK = SOUNDS.register("mob.squid.milk", () -> SoundEvent.createVariableRangeEvent(RootsAPI.rl("mob.squid.milk")));

  // Duck
  public static final DeferredHolder<SoundEvent, SoundEvent> DUCK_AMBIENT = SOUNDS.register("mob.duck.ambient", () -> SoundEvent.createVariableRangeEvent(RootsAPI.rl("mob.duck.ambient")));
  public static final DeferredHolder<SoundEvent, SoundEvent> DUCK_SWIM = SOUNDS.register("mob.duck.swim", () -> SoundEvent.createVariableRangeEvent(RootsAPI.rl("mob.duck.swim")));

  // Deer
  public static final DeferredHolder<SoundEvent, SoundEvent> DEER_AMBIENT = SOUNDS.register("mob.deer.ambient", () -> SoundEvent.createVariableRangeEvent(RootsAPI.rl("mob.deer.ambient")));

  // Pyre
  public static final DeferredHolder<SoundEvent, SoundEvent> PYRE_CRACKLES = SOUNDS.register("block.pyre.crackle", () -> SoundEvent.createVariableRangeEvent(RootsAPI.rl("block.pyre.crackle")));

  // Alertness
  public static final DeferredHolder<SoundEvent, SoundEvent> ALERTNESS = SOUNDS.register("roots.alert", () -> SoundEvent.createVariableRangeEvent(RootsAPI.rl("roots.alert")));

  // Knife
  public static final DeferredHolder<SoundEvent, SoundEvent> KNIFE_STRIP = SOUNDS.register("item.knife.strip", () -> SoundEvent.createVariableRangeEvent(RootsAPI.rl("item.knife.strip")));

  // Herb pickup
  public static final DeferredHolder<SoundEvent, SoundEvent> HERB_PICKUP = SOUNDS.register("item.pouch.pickup_herb", () -> SoundEvent.createVariableRangeEvent(RootsAPI.rl("item.pouch.pickup_herb")));

  public static void register(IEventBus bus) {
    SOUNDS.register(bus);
  }
}
