package epicsquid.roots.potion;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.modifiers.instance.staff.ModifierSnapshot;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageStormCloudGasFX;
import epicsquid.roots.network.fx.MessageStormCloudStormFX;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellStormCloud;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

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
          } else if (state.getBlock() == Blocks.WATER && mods.has(SpellStormCloud.ICE)) {
            world.setBlockState(pos, Blocks.ICE.getDefaultState());
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
          PacketHandler.sendToAllTracking(new MessageStormCloudStormFX(entity), entity);
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
}
