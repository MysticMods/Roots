package epicsquid.roots.network.fx;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemDye;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Random;

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
            Random random = new Random();

            for (int i = 0; i < 50; i++) {
                world.spawnParticle(EnumParticleTypes.WATER_SPLASH,
                        pos.getX() + random.nextDouble(), pos.getY() + 2D, pos.getZ() + random.nextDouble(),
                        0, 0,0);
            }

            ItemDye.spawnBonemealParticles(world, pos.up(), 0);

            return null;
        }
    }
}
