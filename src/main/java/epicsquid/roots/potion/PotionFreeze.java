/*package epicsquid.roots.potion;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.network.fx.MessageFrostTouchFX;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.OnlyIn;

import javax.annotation.Nonnull;

public class PotionFreeze extends Effect {

  private ResourceLocation texture = new ResourceLocation(Roots.MODID, "textures/gui/potions.png");

  public PotionFreeze() {
    super(false, 0xffffff);
    setPotionName("Freeze");
    setBeneficial();
    setIconIndex(0, 0);
  }

  @Override
  public boolean isReady(int duration, int amplifier) {
    return true;
  }

  @Override
  public void performEffect(@Nonnull LivingEntity entity, int amplifier) {
    World world = entity.getEntityWorld();
    BlockPos posDown = entity.getPosition().down();

    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        BlockPos pos = posDown.add(i, 0, j);
        BlockState state = world.getBlockState(pos);
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
  public boolean shouldRender(EffectInstance effect) {
    return true;
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public int getStatusIconIndex() {
    Minecraft.getInstance().getTextureManager().bindTexture(texture);
    return super.getStatusIconIndex();
  }
}*/
