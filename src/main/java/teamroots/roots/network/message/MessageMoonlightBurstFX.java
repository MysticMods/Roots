package teamroots.roots.network.message;

import java.util.Random;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.roots.entity.EntityAuspiciousPoint;
import teamroots.roots.entity.RenderAuspiciousPoint;
import teamroots.roots.particle.ParticleUtil;

public class MessageMoonlightBurstFX implements IMessage {
	public static Random random = new Random();
	double posX = 0, posY = 0, posZ = 0;
	
	public MessageMoonlightBurstFX(){
		super();
	}
	
	public MessageMoonlightBurstFX(double x, double y, double z){
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

	public static float getColorCycle(float ticks){
		return (MathHelper.sin((float)Math.toRadians(ticks))+1.0f)/2.0f;
	}

    public static class MessageHolder implements IMessageHandler<MessageMoonlightBurstFX,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageMoonlightBurstFX message, final MessageContext ctx) {
    		World world = Minecraft.getMinecraft().world;
			for (int k = 0; k < 80; k ++){
				float colorRand = random.nextFloat();
				ParticleUtil.spawnParticleGlow(world, (float)message.posX, (float)message.posY, (float)message.posZ, 0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()-0.5f), getColorCycle(colorRand*360.0f)*255.0f, getColorCycle(colorRand*360.0f)*255.0f, 255.0f, 0.5f, 10.0f, 80);
			}
    		return null;
        }
    }

}
