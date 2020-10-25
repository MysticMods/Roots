package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.modifiers.instance.staff.ISnapshot;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellWildfire;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageWildfireFX extends ModifierPacket implements IMessage {
  private double posX;
  private double posY;
  private double posZ;
  private double motionX;
  private double motionY;
  private double motionZ;
  private float rotationYaw;

  public MessageWildfireFX() {
    super();
  }

  public MessageWildfireFX(double posX, double posY, double posZ, double motionX, double motionY, double motionZ, float rotationYaw, ISnapshot modifierInstances) {
    super(modifierInstances);
    this.posX = posX;
    this.posY = posY;
    this.posZ = posZ;
    this.motionX = motionX;
    this.motionY = motionY;
    this.motionZ = motionZ;
    this.rotationYaw = rotationYaw;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    super.fromBytes(buf);
    this.posX = buf.readDouble();
    this.posY = buf.readDouble();
    this.posZ = buf.readDouble();
    this.motionX = buf.readDouble();
    this.motionY = buf.readDouble();
    this.motionZ = buf.readDouble();
    this.rotationYaw = buf.readFloat();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    super.toBytes(buf);
    buf.writeDouble(this.posX);
    buf.writeDouble(this.posY);
    buf.writeDouble(this.posZ);
    buf.writeDouble(this.motionX);
    buf.writeDouble(this.motionY);
    buf.writeDouble(this.motionZ);
    buf.writeFloat(this.rotationYaw);
  }

  private static final float[] redColor = new float[]{255.0f / 255.0f, 96.0f / 255.0f, 32.0f / 255.0f, 0.5f};
  private static final float[] purpleColor = new float[]{212 / 255.0f, 11 / 255.0f, 74 / 255.0f, 0.5f};
  private static final float[] greenColor = new float[]{148 / 255.0f, 212 / 255.0f, 11 / 255.0f, 0.5f};

  public static class MessageHolder implements IMessageHandler<MessageWildfireFX, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(final MessageWildfireFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      float[] color = redColor;
      if (message.has(SpellWildfire.GREEN)) {
        color = greenColor;
      } else if (message.has(SpellWildfire.PURPLE)) {
        color = purpleColor;
      }
      for (int i = 0; i < 8; i++) {
        float offX = 0.5f * (float) Math.sin(Math.toRadians(message.rotationYaw));
        float offZ = 0.5f * (float) Math.cos(Math.toRadians(message.rotationYaw));
        ParticleUtil.spawnParticleFiery(world, (float) message.posX + (float) message.motionX * 2.5f + offX, (float) message.posY + 1.62F + (float) message.motionY * 2.5f, (float) message.posZ + (float) message.motionZ * 2.5f + offZ, (float) message.motionX + 0.125f * (Util.rand.nextFloat() - 0.5f), (float) message.motionY + 0.125f * (Util.rand.nextFloat() - 0.5f), (float) message.motionZ + 0.125f * (Util.rand.nextFloat() - 0.5f), color, 7.5f, 24);
      }
      return null;
    }
  }
}
