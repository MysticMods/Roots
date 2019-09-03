package epicsquid.roots.effect;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.network.fx.MessageFrostTouchFX;
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

public class PotionFreeze extends Potion {

  private ResourceLocation texture = new ResourceLocation(Roots.MODID, "textures/gui/potions.png");

  public PotionFreeze(int liquidColorIn) {
    super(false, liquidColorIn);
    setPotionName("Freeze");
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

    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        BlockPos pos = posDown.add(i, 0, j);
        IBlockState state = world.getBlockState(pos);
        if (!world.isRemote) {
          if (state.getBlock() == Blocks.FIRE) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
          } else if (state.getBlock() == Blocks.LAVA) {
            world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
          } else if (state.getBlock() == Blocks.WATER) {
            world.setBlockState(pos, Blocks.ICE.getDefaultState());
          }
        }
      }
    }

    if (entity.ticksExisted % 4 == 0) {
      PacketHandler.sendToAllTracking(new MessageFrostTouchFX(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ), entity);
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
