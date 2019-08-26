package epicsquid.roots.advancements;

import epicsquid.mysticallib.advancement.GenericTrigger;
import epicsquid.roots.Roots;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class Advancements {
  public static final ResourceLocation PACIFIST_ID = new ResourceLocation(Roots.MODID, "pacifist");
  public static final ResourceLocation ACTIVATE_ID = new ResourceLocation(Roots.MODID, "grove_activate");
  public static GenericTrigger<LivingDeathEvent> PACIFIST_TRIGGER;
  public static GenericTrigger<Void> GROVE_TRIGGER;

  public static void init() {
    PACIFIST_TRIGGER = CriteriaTriggers.register(new GenericTrigger<>(PACIFIST_ID, new KillPredicate()));
    GROVE_TRIGGER = CriteriaTriggers.register(new GenericTrigger<>(ACTIVATE_ID, new ActivatePredicate()));
  }
}
