package epicsquid.roots.init;

import epicsquid.roots.Roots;
import epicsquid.roots.potion.*;
import net.minecraft.potion.Effect;
import net.minecraftforge.event.RegistryEvent;

public class ModPotions {
  public static Effect storm_cloud;
  public static Effect geas;
  public static Effect time_stop;
  public static Effect nondetection;
  public static Effect petal_shell;
  public static Effect danger_sense;
  public static Effect animal_sense;
  public static PotionReach reach;
  public static PotionBleeding bleeding;
  public static PotionAquaBubble aqua_bubble;
  public static PotionSlowFall slow_fall;

  public static void registerPotions(RegistryEvent.Register<Effect> event) {
    event.getRegistry().register(storm_cloud = new PotionStormCloud().setRegistryName(Roots.MODID, "storm_cloud"));
    event.getRegistry().register(geas = new PotionGeas().setRegistryName(Roots.MODID, "geas"));
    event.getRegistry().register(time_stop = new PotionTimeStop().setRegistryName(Roots.MODID, "time_stop"));
    event.getRegistry().register(nondetection = new PotionNondetection().setRegistryName(Roots.MODID, "nondetection"));
    event.getRegistry().register(petal_shell = new PotionPetalShell().setRegistryName(Roots.MODID, "petal_shell"));
    event.getRegistry().register(danger_sense = new PotionDangerSense().setRegistryName(Roots.MODID, "danger_sense"));
    event.getRegistry().register(animal_sense = new PotionAnimalSense().setRegistryName(Roots.MODID, "animal_sense"));
    event.getRegistry().register(reach = (PotionReach) new PotionReach().setRegistryName(Roots.MODID, "reach"));
    event.getRegistry().register(bleeding = (PotionBleeding) new PotionBleeding().setRegistryName(Roots.MODID, "bleeding"));
    event.getRegistry().register(aqua_bubble = (PotionAquaBubble) new PotionAquaBubble().setRegistryName(Roots.MODID, "aqua_bubble"));
    event.getRegistry().register(slow_fall = (PotionSlowFall) new PotionSlowFall().setRegistryName(Roots.MODID, "slow_fall"));
  }
}
