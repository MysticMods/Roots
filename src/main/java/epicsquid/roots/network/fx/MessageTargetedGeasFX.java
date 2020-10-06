package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.mysticallib.util.VecUtil;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellGeas;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageTargetedGeasFX implements IMessage {
  public MessageTargetedGeasFX() {
    super();
  }

  @Override
  public void fromBytes(ByteBuf buf) {
  }

  @Override
  public void toBytes(ByteBuf buf) {
  }

  public static class MessageHolder implements IMessageHandler<MessageTargetedGeasFX, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(final MessageTargetedGeasFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      EntityPlayer player = Minecraft.getMinecraft().player;
      Vec3d playerPos = player.getPositionVector().add(0, 1, 0);
      Vec3d lookVec = player.getLookVec().scale(SpellGeas.instance.distance);
      for (Vec3d vec : VecUtil.pointsFrom(playerPos, lookVec)) {
        if (Util.rand.nextBoolean()) {
          ParticleUtil.spawnParticleGlow(world, (float) vec.x, (float) vec.y, (float) vec.z, 0, 0.125f * (Util.rand.nextFloat() - 0.5f), 0, SpellGeas.instance.getFirstColours(0.75f), 2f + Util.rand.nextFloat() * 2f, 20);
        } else {
          ParticleUtil.spawnParticleGlow(world, (float) vec.x, (float) vec.y, (float) vec.z, 0, 0.125f * (Util.rand.nextFloat() - 0.5f), 0, SpellGeas.instance.getSecondColours(0.75f), 2f + Util.rand.nextFloat() * 2f, 20);
        }
      }
      return null;
    }
  }

}