package epicsquid.roots;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.roots.capability.runic_shears.RunicShearsCapabilityProvider;
import epicsquid.roots.init.ModDamage;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.integration.baubles.pouch.BaubleBeltCapabilityHandler;
import epicsquid.roots.item.IItemPouch;
import epicsquid.roots.network.fx.*;
import epicsquid.roots.util.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = Roots.MODID)
public class EventManager {
  public static long ticks = 0;

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onTick(TickEvent.ClientTickEvent event) {
    if (event.side == Side.CLIENT) {
      ClientProxy.particleRenderer.updateParticles();
      ticks++;
    }
  }

  @SubscribeEvent
  public static void addCapabilities(AttachCapabilitiesEvent<Entity> event) {
    if (ModRecipes.getRunicShearEntities().contains(event.getObject().getClass())) {
      event.addCapability(RunicShearsCapabilityProvider.IDENTIFIER, new RunicShearsCapabilityProvider());
    }
  }

  @SubscribeEvent
  @Optional.Method(modid = "baubles")
  public static void addBaublesCapability(AttachCapabilitiesEvent<ItemStack> event) {
    if (event.getObject().getItem() instanceof IItemPouch) {
      event.addCapability(BaubleBeltCapabilityHandler.IDENTIFIER, BaubleBeltCapabilityHandler.instance);
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onDamage(LivingHurtEvent event) {
    EntityLivingBase entity = event.getEntityLiving();
    Entity trueSource = event.getSource().getTrueSource();

    if (entity.getActivePotionEffect(ModPotions.time_stop) != null) {
      event.setAmount(event.getAmount() * 0.1f);
    }
    if (entity.getActivePotionEffect(ModPotions.invulnerability) != null) {
      event.setCanceled(true);
    }

    World world = entity.getEntityWorld();

    if (entity instanceof EntityPlayer && !world.isRemote) {
      EntityPlayer player = ((EntityPlayer) entity);
      PotionEffect effect = player.getActivePotionEffect(ModPotions.petal_shell);
      if (effect != null) {
        int newCount = effect.getAmplifier() - 1;
        player.removePotionEffect(ModPotions.petal_shell);
        if (newCount > 0) {
          player.addPotionEffect(new PotionEffect(ModPotions.petal_shell, 60 * 20, newCount, false, false));
        }
        event.setAmount(0);
        event.setCanceled(true);
        PacketHandler.sendToAllTracking(new MessagePetalShellBurstFX(player.posX, player.posY + 1.0f, player.posZ), player);
      }
    }
    if (trueSource instanceof EntityLivingBase) {
      EntityLivingBase trueLiving = (EntityLivingBase) trueSource;
      if (trueLiving.getActivePotionEffect(ModPotions.geas) != null) {
        trueLiving.attackEntityFrom(ModDamage.PSYCHIC_DAMAGE, 3);
        event.setAmount(0);
        PacketHandler.sendToAllTracking(new MessageGeasRingFX(trueLiving.posX, trueLiving.posY + 1.0, trueLiving.posZ), trueLiving);
      }
    }
  }

  @SubscribeEvent
  public static void onEntityTarget(LivingSetAttackTargetEvent event) {
    EntityLivingBase entity = event.getEntityLiving();
    if (entity.getActivePotionEffect(ModPotions.geas) != null && entity instanceof EntityLiving) {
      if (((EntityLiving) entity).getAttackTarget() != null) {
        ((EntityLiving) entity).setAttackTarget(null);
      }
    }
  }

  @SubscribeEvent
  public static void onEntityTick(LivingUpdateEvent event) {
    EntityLivingBase entity = event.getEntityLiving();
    if (entity.getActivePotionEffect(ModPotions.time_stop) != null) {
      entity.removePotionEffect(ModPotions.time_stop);
      event.setCanceled(true);
    }
    if (event.getEntity().getEntityData().hasKey(Constants.LIGHT_DRIFTER_TAG) && !event.getEntity().getEntityWorld().isRemote) {
      event.getEntity().getEntityData().setInteger(Constants.LIGHT_DRIFTER_TAG, event.getEntity().getEntityData().getInteger(Constants.LIGHT_DRIFTER_TAG) - 1);
      if (event.getEntity().getEntityData().getInteger(Constants.LIGHT_DRIFTER_TAG) <= 0) {
        EntityPlayer player = ((EntityPlayer) event.getEntity());
        player.posX = event.getEntity().getEntityData().getDouble(Constants.LIGHT_DRIFTER_X);
        player.posY = event.getEntity().getEntityData().getDouble(Constants.LIGHT_DRIFTER_Y);
        player.posZ = event.getEntity().getEntityData().getDouble(Constants.LIGHT_DRIFTER_Z);
        PacketHandler.sendToAllTracking(new MessageLightDrifterSync(event.getEntity().getUniqueID(), player.posX, player.posY, player.posZ, false,
            event.getEntity().getEntityData().getInteger(Constants.LIGHT_DRIFTER_MODE)), player);
        player.capabilities.allowFlying = false;
        player.capabilities.disableDamage = false;
        player.noClip = false;
        player.capabilities.isFlying = false;
        player.extinguish();
        player.setGameType(GameType.getByID(event.getEntity().getEntityData().getInteger(Constants.LIGHT_DRIFTER_MODE)));
        player.setPositionAndUpdate(player.posX, player.posY, player.posZ);
        PacketHandler.sendToAllTracking(new MessageLightDrifterFX(event.getEntity().posX, event.getEntity().posY + 1.0f, event.getEntity().posZ), event.getEntity());
        event.getEntity().getEntityData().removeTag(Constants.LIGHT_DRIFTER_TAG);
        event.getEntity().getEntityData().removeTag(Constants.LIGHT_DRIFTER_X);
        event.getEntity().getEntityData().removeTag(Constants.LIGHT_DRIFTER_Y);
        event.getEntity().getEntityData().removeTag(Constants.LIGHT_DRIFTER_Z);
        event.getEntity().getEntityData().removeTag(Constants.LIGHT_DRIFTER_MODE);
      }
    }
    if (entity.getActivePotionEffect(ModPotions.geas) != null) {
      PacketHandler.sendToAllTracking(new MessageGeasFX(entity.posX, entity.posY + entity.getEyeHeight() + 0.75f, entity.posZ), entity);
    }
  }

  @SubscribeEvent
  public static void onLooting(LootingLevelEvent event) {
    if (event.getDamageSource().damageType.equals(ModDamage.FEY_FIRE)) {
      event.setLootingLevel(event.getLootingLevel() + 2);
    }
  }
}
