package epicsquid.roots.init;

import epicsquid.roots.Roots;
import epicsquid.roots.potion.PotionFreeze;
import epicsquid.roots.potion.PotionGeas;
import epicsquid.roots.potion.PotionInvulnerability;
import epicsquid.roots.potion.PotionTimeStop;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;

public class ModPotions {
  public static Potion freeze;
  public static Potion geas;
  public static Potion time_stop;
  public static Potion invulnerability;

  public static void registerPotions(RegistryEvent.Register<Potion> event) {
    event.getRegistry().register(freeze = new PotionFreeze(0xFFFFFF).setRegistryName(Roots.MODID, "freeze"));
    event.getRegistry().register(geas = new PotionGeas().setRegistryName(Roots.MODID, "geas"));
    event.getRegistry().register(time_stop = new PotionTimeStop().setRegistryName(Roots.MODID, "time_stop"));
    event.getRegistry().register(invulnerability = new PotionInvulnerability().setRegistryName(Roots.MODID, "invulnerability"));
  }
}
