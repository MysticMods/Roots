/*
package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellRampantGrowth;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageRampantLifeInfusionFX implements IMessage {
  private double posX = 0, posY = 0, posZ = 0;

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

  public static class MessageHolder implements IMessageHandler<MessageRampantLifeInfusionFX, IMessage> {
    @OnlyIn(Dist.CLIENT)
    @Override
    public IMessage onMessage(final MessageRampantLifeInfusionFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      BlockPos pos = new BlockPos(message.posX, message.posY, message.posZ);
      BlockState state = world.getBlockState(pos);
      state.getBlock().randomDisplayTick(state, world, pos, Util.rand);
      for (int k = 0; k < 10; k++) {
        if (Util.rand.nextBoolean()) {
          ParticleUtil.spawnParticlePetal(world, (float) message.posX + Util.rand.nextFloat(), (float) message.posY + Util.rand.nextFloat(),
              (float) message.posZ + Util.rand.nextFloat(), 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f),
              0.125f * (Util.rand.nextFloat() - 0.5f), SpellRampantGrowth.instance.getRed1() * 255.0f, SpellRampantGrowth.instance.getGreen1() * 255.0f,
              SpellRampantGrowth.instance.getBlue1() * 255.0f, 0.5f, 5f, 14);
        } else {
          ParticleUtil.spawnParticlePetal(world, (float) message.posX + Util.rand.nextFloat(), (float) message.posY + Util.rand.nextFloat(),
              (float) message.posZ + Util.rand.nextFloat(), 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f),
              0.125f * (Util.rand.nextFloat() - 0.5f), SpellRampantGrowth.instance.getRed2() * 255.0f, SpellRampantGrowth.instance.getGreen2() * 255.0f,
              SpellRampantGrowth.instance.getBlue2() * 255.0f, 0.5f, 5f, 14);
        }
      }
      return null;
    }
  }

}*/
