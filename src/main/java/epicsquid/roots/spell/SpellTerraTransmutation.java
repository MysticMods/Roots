package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.network.fx.MessageShatterBurstFX;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;

public class SpellTerraTransmutation extends SpellBase{

  public SpellTerraTransmutation(String name) {
    super(name, TextFormatting.GRAY, 0, 0, 0, 211 / 255f,211 / 255f,211 / 255f);
    this.castType = SpellBase.EnumCastType.INSTANTANEOUS;
    this.cooldown = 5;
    addCost(ModItems.wildroot, 0.0625f);
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
          if(state.getBlock() != Blocks.STONE){
            return;
          }
          player.world.setBlockState(pos, ModBlocks.runestone.getDefaultState());
          float offX = 0.5f * (float) Math.sin(Math.toRadians(-90.0f - player.rotationYaw));
          float offZ = 0.5f * (float) Math.cos(Math.toRadians(-90.0f - player.rotationYaw));
          PacketHandler.INSTANCE.sendToAll(
              new MessageShatterBurstFX(player.posX + offX, player.posY + player.getEyeHeight(), player.posZ + offZ, result.hitVec.x, result.hitVec.y,
                  result.hitVec.z));
        }
      }
    }
  }

}
