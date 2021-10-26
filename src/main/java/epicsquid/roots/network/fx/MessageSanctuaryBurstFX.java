package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.network.ClientMessageHandler;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellSanctuary;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.OnlyIn;

public class MessageSanctuaryBurstFX implements IMessage {
  private double posX = 0, posY = 0, posZ = 0;

  public MessageSanctuaryBurstFX() {
    super();
  }

  public MessageSanctuaryBurstFX(double x, double y, double z) {
    super();
    this.posX = x;
    this.posY = y;
    this.posZ = z;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    posX = buf.readDouble();
    posY = buf.readDouble();
    posZ = buf.readDouble();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeDouble(posX);
    buf.writeDouble(posY);
    buf.writeDouble(posZ);
  }

  public static float getColorCycle(float ticks) {
    return (MathHelper.sin((float) Math.toRadians(ticks)) + 1.0f) / 2.0f;
  }

  public static class MessageHolder extends ClientMessageHandler<MessageSanctuaryBurstFX> {
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handleMessage(final MessageSanctuaryBurstFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      for (int k = 0; k < 10; k++) {
        if (Util.rand.nextBoolean()) {
          ParticleUtil.spawnParticlePetal(world, (float) message.posX, (float) message.posY, (float) message.posZ, 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), SpellSanctuary.instance.getFirstColours(0.5f), 5f, 14);
        } else {
          ParticleUtil.spawnParticlePetal(world, (float) message.posX, (float) message.posY, (float) message.posZ, 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), SpellSanctuary.instance.getSecondColours(0.5f), 5f, 14);
        }
      }
    }
  }

}