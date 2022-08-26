package epicsquid.roots;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.roots.capability.grove.IPlayerGroveCapability;
import epicsquid.roots.capability.grove.PlayerGroveCapabilityProvider;
import epicsquid.roots.capability.playerdata.IPlayerDataCapability;
import epicsquid.roots.capability.playerdata.PlayerDataCapabilityProvider;
import epicsquid.roots.capability.runic_shears.RunicShearsCapabilityProvider;
import epicsquid.roots.entity.spell.EntityPetalShell;
import epicsquid.roots.init.ModDamage;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.integration.baubles.pouch.BaubleBeltCapabilityHandler;
import epicsquid.roots.item.ItemPouch;
import epicsquid.roots.network.MessagePlayerDataUpdate;
import epicsquid.roots.network.MessagePlayerGroveUpdate;
import epicsquid.roots.network.fx.*;
import epicsquid.roots.util.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

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
  public static void copyCapabilities(PlayerEvent.Clone event) {
    if (event.isWasDeath()) {
      EntityPlayer player = event.getOriginal();
      EntityPlayer newPlayer = event.getEntityPlayer();
      IPlayerGroveCapability groveOrig = player.getCapability(PlayerGroveCapabilityProvider.PLAYER_GROVE_CAPABILITY, null);
      IPlayerGroveCapability groveNew = newPlayer.getCapability(PlayerGroveCapabilityProvider.PLAYER_GROVE_CAPABILITY, null);
      if (groveOrig != null && groveNew != null) {
        groveNew.setData(groveOrig.getData());
      }

      IPlayerDataCapability dataOrig = player.getCapability(PlayerDataCapabilityProvider.PLAYER_DATA_CAPABILITY, null);
      IPlayerDataCapability dataNew = newPlayer.getCapability(PlayerDataCapabilityProvider.PLAYER_DATA_CAPABILITY, null);
      if (dataOrig != null && dataNew != null) {
        dataNew.setData(dataOrig.getData());
      }
    }
  }

  @SubscribeEvent
  public static void addCapabilities(AttachCapabilitiesEvent<Entity> event) {
    if (ModRecipes.getRunicShearEntities().contains(event.getObject().getClass())) {
      event.addCapability(RunicShearsCapabilityProvider.IDENTIFIER, new RunicShearsCapabilityProvider());
    }
    if (event.getObject() instanceof EntityPlayer) {
      event.addCapability(PlayerGroveCapabilityProvider.IDENTIFIER, new PlayerGroveCapabilityProvider());
      event.addCapability(PlayerDataCapabilityProvider.IDENTIFIER, new PlayerDataCapabilityProvider());
    }
  }

  @SubscribeEvent
  @Optional.Method(modid = "baubles")
  public static void addBaublesCapability(AttachCapabilitiesEvent<ItemStack> event) {
    if (event.getObject().getItem() instanceof ItemPouch) {
      event.addCapability(BaubleBeltCapabilityHandler.IDENTIFIER, BaubleBeltCapabilityHandler.instance);
    }
  }

  @SubscribeEvent
  public static void livingUpdate(LivingUpdateEvent event) {
    if (event.getEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) event.getEntity();
      if (player.hasCapability(PlayerGroveCapabilityProvider.PLAYER_GROVE_CAPABILITY, null)) {
        IPlayerGroveCapability cap = player.getCapability(PlayerGroveCapabilityProvider.PLAYER_GROVE_CAPABILITY, null);
        if (cap != null && !player.world.isRemote && cap.isDirty()) {
          PacketHandler.INSTANCE.sendTo(new MessagePlayerGroveUpdate(player.getUniqueID(), cap.getData()), (EntityPlayerMP) player);
          cap.clean();
        }
      }
      if (player.hasCapability(PlayerDataCapabilityProvider.PLAYER_DATA_CAPABILITY, null)) {
        IPlayerDataCapability cap = player.getCapability(PlayerDataCapabilityProvider.PLAYER_DATA_CAPABILITY, null);
        if (cap != null && !player.world.isRemote && cap.isDirty()) {
          PacketHandler.INSTANCE.sendToAllTracking(new MessagePlayerDataUpdate(player.getUniqueID(), cap.getData()),
              new NetworkRegistry.TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 0));
          cap.clean();
        }
      }
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
      List<EntityPetalShell> shells = player.getEntityWorld().getEntitiesWithinAABB(EntityPetalShell.class,
          new AxisAlignedBB(player.posX - 0.5, player.posY, player.posZ - 0.5, player.posX + 0.5, player.posY + 2.0, player.posZ + 0.5));
      if (shells.size() > 0) {
        for (EntityPetalShell shell : shells) {
          if (shell.getPlayerId() == player.getUniqueID()) {
            if (shell.getDataManager().get(shell.getCharge()) > 0) {
              event.setAmount(0);
              event.setCanceled(true);
              shell.getDataManager().set(shell.getCharge(), shell.getDataManager().get(shell.getCharge()) - 1);
              shell.getDataManager().setDirty(shell.getCharge());
              PacketHandler.sendToAllTracking(new MessagePetalShellBurstFX(player.posX, player.posY + 1.0f, player.posZ), player);
              if (shell.getDataManager().get(shell.getCharge()) <= 0) {
                player.world.removeEntity(shell);
              }
            }
          }
        }
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
