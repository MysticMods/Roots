package epicsquid.roots.event.handlers;

import epicsquid.roots.Roots;
import epicsquid.roots.advancements.Advancements;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.PacifistEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
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
    PacifistEntry entry = ModRecipes.getPacifistEntry(entity);
    if (entry == null || !entity.isServerWorld()) return;

    DamageSource source = event.getSource();
    if (!(source instanceof EntityDamageSource)) return;
    Entity trueSource = source.getTrueSource();
    if (!(trueSource instanceof EntityPlayerMP)) return;

    if (!entry.matches(entity, (EntityPlayer) trueSource)) return;

    Advancements.PACIFIST_TRIGGER.trigger((EntityPlayerMP) trueSource, event);
  }
}
