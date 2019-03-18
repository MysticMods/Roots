package epicsquid.roots.event.handlers;

import epicsquid.roots.Roots;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Roots.MODID)
@SuppressWarnings("unused")
public class DeathEventHandler {
  @SubscribeEvent
  public static void onDeath(LivingDeathEvent event) {
    EntityLivingBase entity = event.getEntityLiving();
    if (!entity.isServerWorld() || entity instanceof IMob) return;

    DamageSource source = event.getSource();
    if (!(source instanceof EntityDamageSource)) return;
    Entity trueSource = source.getTrueSource();
    if (!(trueSource instanceof EntityPlayerMP)) return;

    Roots.PACIFIST_TRIGGER.trigger((EntityPlayerMP) trueSource, event);
  }
}
