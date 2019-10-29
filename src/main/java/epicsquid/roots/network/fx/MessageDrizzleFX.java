package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellDrizzle;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemDye;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageDrizzleFX implements IMessage {

    private double x;
    private double y;
    private double z;

    public MessageDrizzleFX() { }

    public MessageDrizzleFX(double x, double y, double z) {
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

    public static class Handler implements IMessageHandler<MessageDrizzleFX, IMessage> {
        @Override
        public IMessage onMessage(MessageDrizzleFX message, MessageContext ctx) {
            World world = Minecraft.getMinecraft().world;
            BlockPos pos = new BlockPos(message.x, message.y, message.z);
            int y = pos.getY() + 10;

            if (Util.rand.nextBoolean()) {
                for (int i = 0; i < 100; i++) {
                    ParticleUtil.spawnParticleGlow(world, pos.getX(), y - (i / 10F), pos.getZ(),
                            0, 0.025F, 0, SpellDrizzle.instance.getRed1(), SpellDrizzle.instance.getGreen1(), SpellDrizzle.instance.getBlue1(),
                            0.75F, 5F, 20);
                }
            } else {
                for (int i = 0; i < 100; i++) {
                    ParticleUtil.spawnParticleGlow(world, pos.getX(), y - (i / 10F), pos.getZ(),
                            0, 0.025F, 0, SpellDrizzle.instance.getRed2(), SpellDrizzle.instance.getGreen2(), SpellDrizzle.instance.getBlue2(),
                            0.75F, 5F, 20);
                }
            }

            ItemDye.spawnBonemealParticles(world, pos, 0);

            return null;
        }
    }
}
