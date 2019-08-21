package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellFall;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageFallFX implements IMessage {

    private double x;
    private double y;
    private double z;

    public MessageFallFX() { }

    public MessageFallFX(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }

    public static class Handler implements IMessageHandler<MessageFallFX, IMessage>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(MessageFallFX message, MessageContext ctx) {
            World world = Minecraft.getMinecraft().world;

            for (int i = 0; i < 10; i++)
            {

                if (Util.rand.nextBoolean())
                {
                    ParticleUtil.spawnParticlePetal(
                            world, (float) message.x + Util.rand.nextFloat(), (float) message.y + Util.rand.nextFloat(), (float) message.z + Util.rand.nextFloat(),
                            0, -0.05F, 0,
                            SpellFall.instance.getRed1(), SpellFall.instance.getGreen1(), SpellFall.instance.getBlue1(), 1.0f,
                            6.0f + 6.0f * Util.rand.nextFloat(), 120);
                }

                if (Util.rand.nextBoolean())
                {
                    ParticleUtil.spawnParticlePetal(
                            world, (float) message.x + Util.rand.nextFloat(), (float) message.y + Util.rand.nextFloat(), (float) message.z + Util.rand.nextFloat(),
                            0, -0.05F, 0,
                            SpellFall.instance.getRed2(), SpellFall.instance.getGreen2(), SpellFall.instance.getBlue2(), 1.0f,
                            1F, 120);
                }
            }


            return null;
        }

    }
}
