package epicsquid.roots.effect;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.network.fx.MessageFrostTouchFX;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EffectFreeze extends Effect {

  public EffectFreeze(String name, boolean hasIcon) {
    super(name, hasIcon);
    setIcon(new ResourceLocation(Roots.MODID, "textures/gui/potion_frost_touch"));
  }

  @Override
  public void onTick(EntityLivingBase entity, int remainingDuration, NBTTagCompound data) {
    super.onTick(entity, remainingDuration, data);
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
}
