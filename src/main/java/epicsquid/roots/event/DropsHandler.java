package epicsquid.roots.event;

import epicsquid.roots.Roots;
import epicsquid.roots.config.SpellConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = Roots.MODID)
public class DropsHandler {
  @SubscribeEvent
  public static void onLivingDrop(LivingDropsEvent event) {
    DamageSource source = event.getSource();
    Entity entity = source.getTrueSource();
    if (entity instanceof EntityPlayer) {
      EntityLivingBase dying = event.getEntityLiving();
      NBTTagCompound data = dying.getEntityData();
      if (data.hasKey("magnetic_ticks", Constants.NBT.TAG_INT)) {
        int ticks = data.getInteger("magnetic_ticks");
        if (ticks < dying.ticksExisted || (dying.ticksExisted - ticks) < 3 * 20) {
          if (data.hasUniqueId("magnetic")) {
            UUID id = data.getUniqueId("magnetic");
            if (id != null && id.equals(entity.getUniqueID())) {
              for (EntityItem item : event.getDrops()) {
                item.setPosition(entity.posX, entity.posY, entity.posZ);
                entity.world.spawnEntity(item);
              }
              event.setCanceled(true);
            }
          }
        }
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public static void onExperienceDrop(LivingExperienceDropEvent event) {
    if (SpellConfig.spellFeaturesCategory.shouldMagnetismAttractXP) {
      EntityLivingBase dying = event.getEntityLiving();
      NBTTagCompound data = dying.getEntityData();
      if (data.hasKey("magnetic_ticks", Constants.NBT.TAG_INT)) {
        int ticks = data.getInteger("magnetic_ticks");
        if (ticks < dying.ticksExisted || (dying.ticksExisted - ticks) < 3 * 20) {
          if (data.hasUniqueId("magnetic")) {
            UUID id = data.getUniqueId("magnetic");
            EntityPlayer player = event.getAttackingPlayer();
            if (id != null && id.equals(player.getUniqueID())) {
              int i = event.getDroppedExperience();
              World world = event.getAttackingPlayer().world;
              while (i > 0) {
                int j = EntityXPOrb.getXPSplit(i);
                i -= j;
                world.spawnEntity(new EntityXPOrb(world, player.posX, player.posY, player.posZ, j));
              }
              event.setCanceled(true);
            }
          }
        }
      }
    }
  }
}
