package epicsquid.roots;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.capability.grove.IPlayerGroveCapability;
import epicsquid.roots.capability.grove.PlayerGroveCapabilityProvider;
import epicsquid.roots.capability.playerdata.IPlayerDataCapability;
import epicsquid.roots.capability.playerdata.PlayerDataCapabilityProvider;
import epicsquid.roots.capability.runic_shears.RunicShearsCapabilityProvider;
import epicsquid.roots.effect.EffectManager;
import epicsquid.roots.entity.spell.EntityPetalShell;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.integration.baubles.pouch.BaubleBeltCapabilityHandler;
import epicsquid.roots.item.ItemPouch;
import epicsquid.roots.network.MessagePlayerDataUpdate;
import epicsquid.roots.network.MessagePlayerGroveUpdate;
import epicsquid.roots.network.fx.*;
import epicsquid.roots.recipe.BarkRecipe;
import epicsquid.roots.util.Constants;
import epicsquid.roots.util.ItemUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.GameType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

public class EventManager {
  public static long ticks = 0;

  @SubscribeEvent
  public static void barkHarvested(HarvestDropsEvent event) {
    if (event.getHarvester() != null) {
      ItemStack tool = event.getHarvester().getHeldItem(EnumHand.MAIN_HAND);
      if (tool.getItem().getToolClasses(tool).contains("druidKnife")) {
        event.getDrops().clear();
        IBlockState blockstate = event.getState();
        Block block = blockstate.getBlock();
        EnumType type = (block == Blocks.LOG) ?
            blockstate.getValue(BlockOldLog.VARIANT) :
            (block == Blocks.LOG2) ? blockstate.getValue(BlockNewLog.VARIANT) : null;
        BarkRecipe bark;
        if (type == null) {
          bark = ModRecipes.getModdedBarkRecipe(blockstate);
        } else {
          bark = ModRecipes.getVanillaBarkRecipe(type);
        }
        if (bark != null) {
          ItemStack barkStack = bark.getBarkStack(4 + Util.rand.nextInt(getAdditionalBarkAmount(tool)) + 1);
          if (!event.getWorld().isRemote) {
            ItemUtil.spawnItem(event.getWorld(), event.getPos(), barkStack);
          }
        }
      }
    }
  }

  private static int getAdditionalBarkAmount(ItemStack stack) {
    return Math.max(EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack) + 1, EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack) + 1);
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onTick(TickEvent.ClientTickEvent event) {
    if (event.side == Side.CLIENT) {
      ClientProxy.particleRenderer.updateParticles();
      ticks++;
    }
  }

  // TODO: Handle Player Teleport

  @SubscribeEvent
  public static void copyCapabilities(PlayerEvent.Clone event) {
    if (event.isWasDeath()) {
      if (event.getOriginal().hasCapability(PlayerGroveCapabilityProvider.PLAYER_GROVE_CAPABILITY, null)) {
        event.getEntityPlayer().getCapability(PlayerGroveCapabilityProvider.PLAYER_GROVE_CAPABILITY, null)
            .setData(event.getOriginal().getCapability(PlayerGroveCapabilityProvider.PLAYER_GROVE_CAPABILITY, null).getData());
      }
      if (event.getOriginal().hasCapability(PlayerDataCapabilityProvider.PLAYER_DATA_CAPABILITY, null)) {
        event.getEntityPlayer().getCapability(PlayerDataCapabilityProvider.PLAYER_DATA_CAPABILITY, null)
            .setData(event.getOriginal().getCapability(PlayerDataCapabilityProvider.PLAYER_DATA_CAPABILITY, null).getData());
      }
    }
  }

  @SubscribeEvent
  public static void addCapabilities(AttachCapabilitiesEvent<Entity> event) {
    if (ModRecipes.getRunicShearEntities().contains(event.getObject().getClass())) {
      event.addCapability(RunicShearsCapabilityProvider.IDENTIFIER, new RunicShearsCapabilityProvider());
    }
    if (event.getObject() instanceof EntityPlayer) {
      event.addCapability(new ResourceLocation(Roots.MODID, "player_grove_capability"), new PlayerGroveCapabilityProvider());
      event.addCapability(new ResourceLocation(Roots.MODID, "player_data_capability"), new PlayerDataCapabilityProvider());
    }
  }

  @SubscribeEvent
  @Optional.Method(modid = "baubles")
  public static void addBaublesCapability(AttachCapabilitiesEvent<ItemStack> event) {
    if (event.getObject().getItem() instanceof ItemPouch) {
      event.addCapability(new ResourceLocation(Roots.MODID, "baubles_pouch"), BaubleBeltCapabilityHandler.instance);
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
    if (EffectManager.hasEffect(event.getEntityLiving(), EffectManager.effect_time_stop.getName())) {
      event.setAmount(event.getAmount() * 0.1f);
    }
    if (EffectManager.hasEffect(event.getEntityLiving(), EffectManager.effect_invulnerability.getName())) {
      event.setCanceled(true);
    }
    if (event.getEntityLiving().getEntityData().hasKey(Constants.EFFECT_TAG)) {
      NBTTagCompound tag = event.getEntityLiving().getEntityData().getCompoundTag(Constants.EFFECT_TAG);
      if (tag.hasKey(EffectManager.effect_invulnerability.getName())) {
        event.setCanceled(true);
      }
    }
    if (event.getEntityLiving() instanceof EntityPlayer && !event.getEntityLiving().getEntityWorld().isRemote) {
      EntityPlayer player = ((EntityPlayer) event.getEntityLiving());
      List<EntityPetalShell> shells = player.getEntityWorld().getEntitiesWithinAABB(EntityPetalShell.class,
          new AxisAlignedBB(player.posX - 0.5, player.posY, player.posZ - 0.5, player.posX + 0.5, player.posY + 2.0, player.posZ + 0.5));
      if (shells.size() > 0) {
        for (EntityPetalShell shell : shells) {
          if (shell.getPlayerId().compareTo(player.getUniqueID()) == 0) {
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
    if (event.getEntity().getEntityData().hasKey(Constants.GEAS_TAG)) {
      if (event.getSource().getTrueSource() instanceof EntityLivingBase) {
        if (event.getSource().getTrueSource().getUniqueID().compareTo(event.getEntity().getUniqueID()) != 0) {
          event.getEntity().getEntityData().removeTag(Constants.GEAS_TAG);
        }
      }
    }
    if (event.getSource().getTrueSource() instanceof EntityLivingBase) {
      if (event.getSource().getTrueSource().getEntityData().hasKey(Constants.GEAS_TAG)) {
        EntityLivingBase entity = (EntityLivingBase) event.getSource().getTrueSource();
        entity.attackEntityFrom(DamageSource.WITHER, event.getAmount() * 2.0f);
        event.setAmount(0);
        PacketHandler.sendToAllTracking(new MessageGeasRingFX(entity.posX, entity.posY + 1.0, entity.posZ), entity);
      }
    }
  }

  @SubscribeEvent
  public static void onEntityTick(LivingUpdateEvent event) {
    EffectManager.tickEffects(event.getEntityLiving());
    if (EffectManager.hasEffect(event.getEntityLiving(), EffectManager.effect_time_stop.getName())) {
      event.setCanceled(true);
    }
    if (event.getEntity().getEntityData().hasKey(Constants.GEAS_TAG) && !event.getEntity().getEntityWorld().isRemote) {
      event.getEntity().getEntityData().setInteger(Constants.GEAS_TAG, event.getEntity().getEntityData().getInteger(Constants.GEAS_TAG) - 1);
      if (event.getEntity().getEntityData().getInteger(Constants.GEAS_TAG) <= 0) {
        event.getEntity().getEntityData().removeTag(Constants.GEAS_TAG);
      }
      PacketHandler.sendToAllTracking(
          new MessageGeasFX(event.getEntity().posX, event.getEntity().posY + event.getEntity().getEyeHeight() + 0.75f, event.getEntity().posZ),
          event.getEntity());
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
        PacketHandler
            .sendToAllTracking(new MessageLightDrifterFX(event.getEntity().posX, event.getEntity().posY + 1.0f, event.getEntity().posZ), event.getEntity());
        event.getEntity().getEntityData().removeTag(Constants.LIGHT_DRIFTER_TAG);
        event.getEntity().getEntityData().removeTag(Constants.LIGHT_DRIFTER_X);
        event.getEntity().getEntityData().removeTag(Constants.LIGHT_DRIFTER_Y);
        event.getEntity().getEntityData().removeTag(Constants.LIGHT_DRIFTER_Z);
        event.getEntity().getEntityData().removeTag(Constants.LIGHT_DRIFTER_MODE);
      }
    }
  }

}
