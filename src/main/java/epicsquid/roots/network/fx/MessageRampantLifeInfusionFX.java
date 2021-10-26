package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.network.ClientMessageHandler;
import epicsquid.roots.particle.ParticleUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.OnlyIn;

public class MessageRampantLifeInfusionFX implements IMessage {
  private double posX = 0, posY = 0, posZ = 0;
  private static float[] colors1 = new float[]{224f / 255f, 135f / 255f, 40f / 255f, 0.5f};
  private static float[] colors2 = new float[]{46f / 255f, 94f / 255f, 93f / 255f, 0.5f};

  public MessageRampantLifeInfusionFX() {
    super();
  }

  public MessageRampantLifeInfusionFX(double x, double y, double z) {
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

  public static class MessageHolder extends ClientMessageHandler<MessageRampantLifeInfusionFX> {
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handleMessage(final MessageRampantLifeInfusionFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      BlockPos pos = new BlockPos(message.posX, message.posY, message.posZ);
      BlockState state = world.getBlockState(pos);
      state.getBlock().randomDisplayTick(state, world, pos, Util.rand);
      for (int k = 0; k < 10; k++) {
        if (Util.rand.nextBoolean()) {
          ParticleUtil.spawnParticlePetal(world, (float) message.posX + Util.rand.nextFloat(), (float) message.posY + Util.rand.nextFloat(), (float) message.posZ + Util.rand.nextFloat(), 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), colors1, 5f, 14);
        } else {
          ParticleUtil.spawnParticlePetal(world, (float) message.posX + Util.rand.nextFloat(), (float) message.posY + Util.rand.nextFloat(), (float) message.posZ + Util.rand.nextFloat(), 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), colors2, 5f, 14);
        }
      }
    }
  }
}
