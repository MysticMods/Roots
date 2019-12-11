package epicsquid.roots;

import epicsquid.roots.capability.runic_shears.RunicShearsCapabilityProvider;
import epicsquid.roots.init.ModDamage;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.util.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Roots.MODID)
public class EventManager {
  public static long ticks = 0;

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onTick(TickEvent.ClientTickEvent event) {
    if (event.side == LogicalSide.CLIENT) {
      // ClientProxy.particleRenderer.updateParticles();
      ticks++;
    }
  }

  @SubscribeEvent
  public static void copyCapabilities(PlayerEvent.Clone event) {
    if (event.isWasDeath()) {
      PlayerEntity player = event.getOriginal();
      PlayerEntity newPlayer = event.getPlayer();
    }
  }

  @SubscribeEvent
  public static void addCapabilities(AttachCapabilitiesEvent<Entity> event) {
    if (ModRecipes.getRunicShearEntities().contains(event.getObject().getClass())) {
      event.addCapability(RunicShearsCapabilityProvider.IDENTIFIER, new RunicShearsCapabilityProvider());
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onDamage(LivingHurtEvent event) {
    LivingEntity entity = event.getEntityLiving();
    Entity trueSource = event.getSource().getTrueSource();

    if (entity.getActivePotionEffect(ModPotions.time_stop) != null) {
      event.setAmount(event.getAmount() * 0.1f);
    }
    if (entity.getActivePotionEffect(ModPotions.invulnerability) != null) {
      event.setCanceled(true);
    }

    World world = entity.getEntityWorld();

    if (entity instanceof PlayerEntity && !world.isRemote) {
      PlayerEntity player = ((PlayerEntity) entity);
      EffectInstance effect = player.getActivePotionEffect(ModPotions.petal_shell);
      if (effect != null) {
        int newCount = effect.getAmplifier() - 1;
        player.removePotionEffect(ModPotions.petal_shell);
        if (newCount > 0) {
          player.addPotionEffect(new EffectInstance(ModPotions.petal_shell, 60 * 20, newCount, false, false));
        }
        event.setAmount(0);
        event.setCanceled(true);
        // TODO: Once packets are back in
        /*        PacketHandler.sendToAllTracking(new MessagePetalShellBurstFX(player.posX, player.posY + 1.0f, player.posZ), player);*/
      }
    }
    if (trueSource instanceof LivingEntity) {
      LivingEntity trueLiving = (LivingEntity) trueSource;
      if (trueLiving.getActivePotionEffect(ModPotions.geas) != null) {
        trueLiving.attackEntityFrom(ModDamage.PSYCHIC_DAMAGE, 3);
        event.setAmount(0);
        // TODO: Once packets are back in
        /*        PacketHandler.sendToAllTracking(new MessageGeasRingFX(trueLiving.posX, trueLiving.posY + 1.0, trueLiving.posZ), trueLiving);*/
      }
    }
  }

  @SubscribeEvent
  public static void onEntityTarget(LivingSetAttackTargetEvent event) {
    LivingEntity entity = event.getEntityLiving();
    if (entity.getActivePotionEffect(ModPotions.geas) != null && entity instanceof MobEntity) {
      if (((MobEntity) entity).getAttackTarget() != null) {
        ((MobEntity) entity).setAttackTarget(null);
      }
    }
  }

  @SubscribeEvent
  public static void onEntityTick(LivingUpdateEvent event) {
    LivingEntity entity = event.getEntityLiving();
    if (entity.getActivePotionEffect(ModPotions.time_stop) != null) {
      entity.removePotionEffect(ModPotions.time_stop);
      event.setCanceled(true);
    }
    if (event.getEntity().getPersistentData().contains(Constants.LIGHT_DRIFTER_TAG) && !event.getEntity().getEntityWorld().isRemote) {
      event.getEntity().getPersistentData().putInt(Constants.LIGHT_DRIFTER_TAG, event.getEntity().getPersistentData().getInt(Constants.LIGHT_DRIFTER_TAG) - 1);
      if (event.getEntity().getPersistentData().getInt(Constants.LIGHT_DRIFTER_TAG) <= 0) {
        PlayerEntity player = ((PlayerEntity) event.getEntity());
        player.posX = event.getEntity().getPersistentData().getDouble(Constants.LIGHT_DRIFTER_X);
        player.posY = event.getEntity().getPersistentData().getDouble(Constants.LIGHT_DRIFTER_Y);
        player.posZ = event.getEntity().getPersistentData().getDouble(Constants.LIGHT_DRIFTER_Z);
        // TODO: Packets/particles
        /*        PacketHandler.sendToAllTracking(new MessageLightDrifterSync(event.getEntity().getUniqueID(), player.posX, player.posY, player.posZ, false, event.getEntity().getPersistentData().getInt(Constants.LIGHT_DRIFTER_MODE)), player);*/
        // TODO: Reimplement light drifter
/*        player.capabilities.allowFlying = false;
        player.capabilities.disableDamage = false;*/
        player.noClip = false;
        /*        player.capabilities.isFlying = false;*/
        player.extinguish();
        player.setGameType(GameType.getByID(event.getEntity().getPersistentData().getInt(Constants.LIGHT_DRIFTER_MODE)));
        player.setPositionAndUpdate(player.posX, player.posY, player.posZ);
        // TODO: Particles/packets
        /*        PacketHandler.sendToAllTracking(new MessageLightDrifterFX(event.getEntity().posX, event.getEntity().posY + 1.0f, event.getEntity().posZ), event.getEntity());*/
        event.getEntity().getPersistentData().remove(Constants.LIGHT_DRIFTER_TAG);
        event.getEntity().getPersistentData().remove(Constants.LIGHT_DRIFTER_X);
        event.getEntity().getPersistentData().remove(Constants.LIGHT_DRIFTER_Y);
        event.getEntity().getPersistentData().remove(Constants.LIGHT_DRIFTER_Z);
        event.getEntity().getPersistentData().remove(Constants.LIGHT_DRIFTER_MODE);
      }
    }
    if (entity.getActivePotionEffect(ModPotions.geas) != null) {
      // TODO: Particles/packets
      /*      PacketHandler.sendToAllTracking(new MessageGeasFX(entity.posX, entity.posY + entity.getEyeHeight() + 0.75f, entity.posZ), entity);*/
    }
  }

  @SubscribeEvent
  public static void onLooting(LootingLevelEvent event) {
    if (event.getDamageSource().damageType.equals(ModDamage.FEY_FIRE)) {
      event.setLootingLevel(event.getLootingLevel() + 2);
    }
  }
}
