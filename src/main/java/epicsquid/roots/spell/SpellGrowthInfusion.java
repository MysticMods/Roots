package epicsquid.roots.spell;

import java.util.Random;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.network.message.fx.MessageLifeInfusionFX;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;

public class SpellGrowthInfusion extends SpellBase {

  public SpellGrowthInfusion(String name) {
    super(name, TextFormatting.GREEN, 48f / 255f, 255f / 255f, 48f / 255f, 192f / 255f, 255f / 255f, 192f / 255f);
    this.castType = SpellBase.EnumCastType.CONTINUOUS;
    this.cooldown = 16;
  }

  @Override
  public void cast(EntityPlayer player) {
    if (!player.world.isRemote) {
      RayTraceResult result = player.world.rayTraceBlocks(player.getPositionVector().addVector(0, player.getEyeHeight(), 0),
          player.getLookVec().scale(8.0f).add(player.getPositionVector().addVector(0, player.getEyeHeight(), 0)));
      if (result != null) {
        if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
          BlockPos pos = result.getBlockPos();
          IBlockState state = player.world.getBlockState(pos);
          for (int i = 0; i < 1; i++) {
            state.getBlock().randomTick(player.world, pos, state, new Random());
          }
          PacketHandler.INSTANCE.sendToAll(new MessageLifeInfusionFX(pos.getX(), pos.getY(), pos.getZ()));
        }
      }
    }
  }

}
