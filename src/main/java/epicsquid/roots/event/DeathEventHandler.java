package epicsquid.roots.event;

import epicsquid.roots.Roots;
import epicsquid.roots.advancements.Advancements;
import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.PacifistEntry;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = Roots.MODID)
@SuppressWarnings("unused")
public class DeathEventHandler {
  public static AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(-0.75, -0.75, -0.75, 1.75, 1.75, 1.75);

  @SubscribeEvent
  public static void onDeath(LivingDeathEvent event) {
    if (!GeneralConfig.UntruePacifist) {
      return;
    }
    LivingEntity entity = event.getEntityLiving();
    PacifistEntry entry = ModRecipes.getPacifistEntry(entity);
    if (entry == null || !entity.isServerWorld()) return;

    DamageSource source = event.getSource();
    if (!(source instanceof EntityDamageSource)) return;
    Entity trueSource = source.getTrueSource();
    if (!(trueSource instanceof ServerPlayerEntity)) return;

    if (!entry.matches(entity, (PlayerEntity) trueSource)) return;

    if (entity.getControllingPassenger() != null) return;

    List<Entity> entities = entity.getEntityWorld().getEntitiesInAABBexcluding(entity, BOUNDING_BOX.offset(entity.getPosition()), o -> EntityUtil.isHostileTo(entity, (PlayerEntity) trueSource));
    if (!entities.isEmpty()) {
      return;
    }

    Advancements.PACIFIST_TRIGGER.trigger((ServerPlayerEntity) trueSource, event);
  }
}
