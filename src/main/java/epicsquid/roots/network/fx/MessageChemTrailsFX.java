package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellNaturesScythe;
import epicsquid.roots.spell.SpellSkySoarer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageChemTrailsFX implements IMessage {
  private double[] pos;
  private double[] motion;

  @SuppressWarnings("unused")
  public MessageChemTrailsFX() {
  }

  public MessageChemTrailsFX(double x, double y, double z, double motionX, double motionY, double motionZ) {
    this.pos = new double[]{x, y, z};
    this.motion = new double[]{motionX, motionY, motionZ};
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.pos = new double[]{buf.readDouble(), buf.readDouble(), buf.readDouble()};
    this.motion = new double[]{buf.readDouble(), buf.readDouble(), buf.readDouble()};
  }

  @Override
  public void toBytes(ByteBuf buf) {
    for (double val : pos) {
      buf.writeDouble(val);
    }
    for (double val : motion) {
      buf.writeDouble(val);
    }
  }

  private static final float[] smoke = new float[]{87/255.0f, 79/255.0f, 75/255.0f, 1.0f};

  public static class Handler implements IMessageHandler<MessageChemTrailsFX, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(MessageChemTrailsFX message, MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;

      for (int i = 0; i < 4; i++) {
        ParticleUtil.spawnParticleSmoke(world, (float) message.pos[0] + (Util.rand.nextFloat()) - 0.5f, (float) (message.pos[1] - 1f) + (Util.rand.nextFloat()) + 0.5f, (float) message.pos[2] + (Util.rand.nextFloat()) - 0.5f, -0.125f * (float) message.motion[0], -0.125f * (float) message.motion[1], -0.125f * (float) message.motion[2], smoke, 5.0f * Util.rand.nextFloat() + 5.0f, 190, false);
      }
      return null;
    }
  }
}
