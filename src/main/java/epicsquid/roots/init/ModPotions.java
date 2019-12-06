package epicsquid.roots.init;

import net.minecraft.potion.Effect;
import net.minecraftforge.event.RegistryEvent;

public class ModPotions {
  public static Effect freeze;
  public static Effect geas;
  public static Effect time_stop;
  public static Effect invulnerability;
  public static Effect petal_shell;
  public static Effect danger_sense;
  public static Effect animal_sense;

  public static void registerPotions(RegistryEvent.Register<Effect> event) {
/*    event.getRegistry().register(freeze = new PotionFreeze().setRegistryName(Roots.MODID, "freeze"));
    event.getRegistry().register(geas = new PotionGeas().setRegistryName(Roots.MODID, "geas"));
    event.getRegistry().register(time_stop = new PotionTimeStop().setRegistryName(Roots.MODID, "time_stop"));
    event.getRegistry().register(invulnerability = new PotionInvulnerability().setRegistryName(Roots.MODID, "invulnerability"));
    event.getRegistry().register(petal_shell = new PotionPetalShell().setRegistryName(Roots.MODID, "petal_shell"));
    event.getRegistry().register(danger_sense = new PotionDangerSense().setRegistryName(Roots.MODID, "danger_sense"));
    event.getRegistry().register(animal_sense = new PotionAnimalSense().setRegistryName(Roots.MODID, "animal_sense"));*/
  }
}
