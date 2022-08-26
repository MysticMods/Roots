package epicsquid.roots;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.roots.capability.life_essence.LifeEssenceCapabilityProvider;
import epicsquid.roots.capability.runic_shears.RunicShearsCapabilityProvider;
import epicsquid.roots.init.ModDamage;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.integration.baubles.pouch.BaubleBeltCapabilityHandler;
import epicsquid.roots.item.IItemPouch;
import epicsquid.roots.network.MessageLightDrifterSync;
import epicsquid.roots.network.fx.MessageGeasFX;
import epicsquid.roots.network.fx.MessageGeasRingFX;
import epicsquid.roots.network.fx.MessagePetalShellBurstFX;
import epicsquid.roots.spell.SpellAquaBubble;
import epicsquid.roots.util.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
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
    if (event.getObject() instanceof EntityLivingBase) {
      if (ModRecipes.isLifeEssenceAllowed((EntityLivingBase) event.getObject())) {
        event.addCapability(LifeEssenceCapabilityProvider.IDENTIFIER, new LifeEssenceCapabilityProvider());
      }
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
    DamageSource damage = event.getSource();

    if (entity.getActivePotionEffect(ModPotions.aqua_bubble) != null) {
      if (damage.isFireDamage()) {
        event.setAmount(event.getAmount() * SpellAquaBubble.instance.fire_reduction);
      }
      if (damage == DamageSource.LAVA) {
        event.setAmount(event.getAmount() * SpellAquaBubble.instance.lava_reduction);
      }
    }
    if (entity.getActivePotionEffect(ModPotions.time_stop) != null) {
      event.setAmount(event.getAmount() * 0.1f);
    }
    if (entity.getActivePotionEffect(ModPotions.invulnerability) != null) {
      event.setCanceled(true);
    }

    World world = entity.getEntityWorld();

    PotionEffect effect = entity.getActivePotionEffect(ModPotions.petal_shell);
    if (entity instanceof EntityPlayer && !world.isRemote) {
      if (effect != null) {
        int newCount = effect.getAmplifier() - 1;
        entity.removePotionEffect(ModPotions.petal_shell);
        if (newCount > 0) {
          entity.addPotionEffect(new PotionEffect(ModPotions.petal_shell, 60 * 20, newCount, false, false));
        }
        event.setAmount(0);
        event.setCanceled(true);
        PacketHandler.sendToAllTracking(new MessagePetalShellBurstFX(entity.posX, entity.posY + 1.0f, entity.posZ), entity);
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
    if (entity instanceof EntityPlayer && event.getEntity().getEntityData().hasKey(Constants.LIGHT_DRIFTER_TAG) && !event.getEntity().getEntityWorld().isRemote) {
      event.getEntity().getEntityData().setInteger(Constants.LIGHT_DRIFTER_TAG, event.getEntity().getEntityData().getInteger(Constants.LIGHT_DRIFTER_TAG) - 1);
      if (event.getEntity().getEntityData().getInteger(Constants.LIGHT_DRIFTER_TAG) <= 0) {
        EntityPlayer player = ((EntityPlayer) event.getEntity());
        player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 40, 10, false, false));
        player.posX = event.getEntity().getEntityData().getDouble(Constants.LIGHT_DRIFTER_X);
        player.posY = event.getEntity().getEntityData().getDouble(Constants.LIGHT_DRIFTER_Y);
        player.posZ = event.getEntity().getEntityData().getDouble(Constants.LIGHT_DRIFTER_Z);
        PacketHandler.sendToAllTracking(new MessageLightDrifterSync(event.getEntity().getUniqueID(), player.posX, player.posY, player.posZ, false, event.getEntity().getEntityData().getInteger(Constants.LIGHT_DRIFTER_MODE)), player);
        player.capabilities.allowFlying = false;
        player.capabilities.disableDamage = false;
        player.noClip = false;
        player.capabilities.isFlying = false;
        player.setPositionAndUpdate(player.posX, player.posY, player.posZ);
        player.setGameType(GameType.getByID(event.getEntity().getEntityData().getInteger(Constants.LIGHT_DRIFTER_MODE)));
        player.extinguish();
        //PacketHandler.sendToAllTracking(new MessageLightDrifterFX(event.getEntity().posX, event.getEntity().posY + 1.0f, event.getEntity().posZ), event.getEntity());
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
