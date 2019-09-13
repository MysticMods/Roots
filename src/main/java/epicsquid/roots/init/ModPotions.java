package epicsquid.roots.init;

import epicsquid.roots.Roots;
import epicsquid.roots.potion.PotionFreeze;
import epicsquid.roots.potion.PotionGeas;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;

public class ModPotions {
  public static Potion freeze;
  public static Potion geas;

  public static void registerPotions(RegistryEvent.Register<Potion> event) {
    event.getRegistry().register(freeze = new PotionFreeze(0xFFFFFF).setRegistryName(Roots.MODID, "freeze"));
    event.getRegistry().register(geas = new PotionGeas().setRegistryName(Roots.MODID, "geas"));
  }
}
