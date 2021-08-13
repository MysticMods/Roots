package epicsquid.roots.potion;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.entity.spell.EntityIcicle;
import epicsquid.roots.init.ModSounds;
import epicsquid.roots.modifiers.instance.staff.ModifierSnapshot;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageStormCloudGasFX;
import epicsquid.roots.network.fx.MessageStormCloudStormFX;
import epicsquid.roots.spell.SpellStormCloud;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class PotionStormCloud extends Potion {

  private ResourceLocation texture = new ResourceLocation(Roots.MODID, "textures/gui/potions.png");

  public PotionStormCloud() {
    super(false, 0xffffff);
    setPotionName("Storm Cloud");
    setBeneficial();
    setIconIndex(0, 0);
  }

  @Override
  public boolean isReady(int duration, int amplifier) {
    return true;
  }

  @Override
  public void performEffect(@Nonnull EntityLivingBase entity, int amplifier) {
    World world = entity.getEntityWorld();
    BlockPos posDown = entity.getPosition().down();
    ModifierSnapshot mods = StaffModifierInstanceList.fromSnapshot(entity.getEntityData(), SpellStormCloud.instance);

    if (!world.isRemote) {
      for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
          BlockPos pos = posDown.add(i, 0, j);
          IBlockState state = world.getBlockState(pos);

          if (state.getBlock() == Blocks.FIRE) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
          } else if (state.getBlock() == Blocks.LAVA && mods.has(SpellStormCloud.OBSIDIAN)) {
            world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
          } else if (GeneralConfig.getWaterBlocks().contains(state.getBlock()) && mods.has(SpellStormCloud.ICE)) {
            if (state.getPropertyKeys().contains(BlockLiquid.LEVEL)) {
              if (state.getValue(BlockLiquid.LEVEL) == 0) {
                world.setBlockState(pos, Blocks.ICE.getDefaultState());
              }
            }
          }
        }
      }

      int radius = SpellStormCloud.instance.radius;
      if (mods.has(SpellStormCloud.RADIUS)) {
        radius += SpellStormCloud.instance.radius_extended;
      }

      List<EntityLivingBase> entities = Util.getEntitiesWithinRadius(world, EntityLivingBase.class, entity.getPosition(), radius, radius, radius);
      for (EntityLivingBase e : entities) {
        if (EntityUtil.isFriendly(e, SpellStormCloud.instance) || e == entity) {
          if (entity.ticksExisted % SpellStormCloud.instance.heal_interval == 0) {
            if (mods.has(SpellStormCloud.HEALING)) {
              e.heal(SpellStormCloud.instance.heal_amount);
            }
          }
          e.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, SpellStormCloud.instance.duration, SpellStormCloud.instance.fire_resistance, false, false));
          e.extinguish();
          if (mods.has(SpellStormCloud.PEACEFUL) || e == entity) {
            continue;
          }
        }

        if (mods.has(SpellStormCloud.LIGHTNING) && e.ticksExisted % SpellStormCloud.instance.lightning_interval == 0) {
          if (Util.rand.nextFloat() < SpellStormCloud.instance.lightning_chance) {
            world.addWeatherEffect(new EntityLightningBolt(world, e.posX, e.posY, e.posZ, false));
          }
        }

        if (mods.has(SpellStormCloud.POISON)) {
          e.addPotionEffect(new PotionEffect(MobEffects.POISON, SpellStormCloud.instance.poison_duration, SpellStormCloud.instance.poison_amplifier));
        }

        if (mods.has(SpellStormCloud.ICICLES)) {
          if (e.ticksExisted % SpellStormCloud.instance.icicle_interval == 0) {
            if (Util.rand.nextFloat() < SpellStormCloud.instance.icicle_chance) {
              Vec3d pos = e.getPositionVector();
              Vec3d playerPos = entity.getPositionVector().add(0, entity.getEyeHeight(), 0);
              Vec3d accel = pos.subtract(playerPos);
              EntityIcicle icicle = new EntityIcicle(world, entity, accel.x, accel.y, accel.z, EntityIcicle.SpellType.STORM_CLOUD);
              icicle.posX = playerPos.x;
              icicle.posY = playerPos.y + Util.rand.nextDouble() - 0.5;
              icicle.posZ = playerPos.z;
              icicle.setModifiers(mods);
              world.spawnEntity(icicle);
            }
          }
        }
      }

      if (mods.has(SpellStormCloud.OBSIDIAN) || mods.has(SpellStormCloud.ICE)) {
        if (entity.ticksExisted % 4 == 0) {
          PacketHandler.sendToAllTracking(new MessageStormCloudGasFX(entity), entity);
        }
      }
    }

    if (entity.ticksExisted % 2 == 0) {
      PacketHandler.sendToAllTracking(new MessageStormCloudStormFX(entity, mods), entity);
    }
  }

  @Override
  public boolean shouldRender(PotionEffect effect) {
    return true;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public int getStatusIconIndex() {
    Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
    return super.getStatusIconIndex();
  }


  @Override
  public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) {
    super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
    if (SpellStormCloud.instance.shouldPlaySound()) {
      entityLivingBaseIn.playSound(ModSounds.Spells.STORM_CLOUD_END, SpellStormCloud.instance.getSoundVolume(), 1);
    }
  }
}
