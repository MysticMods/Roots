package teamroots.roots.network.message;

import java.util.ArrayList;
import java.util.List;
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
import teamroots.roots.util.Misc;

public class MessageFairyTameFX implements IMessage {
	public static Random random = new Random();
	static List<Float> reds = new ArrayList<Float>();
	static List<Float> greens = new ArrayList<Float>();
	static List<Float> blues = new ArrayList<Float>();
	static {
		reds.add(177f);
		reds.add(255f);
		reds.add(255f);
		reds.add(219f);
		reds.add(122f);
		greens.add(255f);
		greens.add(223f);
		greens.add(163f);
		greens.add(179f);
		greens.add(144f);
		blues.add(117f);
		blues.add(163f);
		blues.add(255f);
		blues.add(255f);
		blues.add(255f);
	}
	double posX = 0, posY = 0, posZ = 0;
	
	public MessageFairyTameFX(){
		super();
	}
	
	public MessageFairyTameFX(double x, double y, double z){
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

    public static class MessageHolder implements IMessageHandler<MessageFairyTameFX,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageFairyTameFX message, final MessageContext ctx) {
    		World world = Minecraft.getMinecraft().world;
    		for (int k = 0; k < 40; k ++){
        		int ind = Misc.random.nextInt(5);
        		float r = reds.get(ind);
        		float g = greens.get(ind);
        		float b = blues.get(ind);
				float colorRand = random.nextFloat();
				ParticleUtil.spawnParticleGlow(world, (float)message.posX, (float)message.posY, (float)message.posZ, 0.0625f*(random.nextFloat()-0.5f), 0.0625f*(random.nextFloat()-0.5f), 0.0625f*(random.nextFloat()-0.5f), r, g, b, 0.5f, 3.0f+Misc.random.nextFloat()*3.0f, 30);
			}
    		for (float k = 0; k < 360; k += Misc.random.nextFloat()*24.0f){
        		int ind = Misc.random.nextInt(5);
        		float r = reds.get(ind);
        		float g = greens.get(ind);
        		float b = blues.get(ind);
				float colorRand = random.nextFloat();
				double dx = Math.sin(Math.toRadians(k));
				double dz = Math.cos(Math.toRadians(k));
				ParticleUtil.spawnParticleStar(world, (float)(message.posX+dx), (float)message.posY, (float)(message.posZ+dz), 0.03125f*(random.nextFloat()-0.5f), 0.0625f*(random.nextFloat()), 0.03225f*(random.nextFloat()-0.5f), r, g, b, 0.5f, 2.0f+Misc.random.nextFloat()*2.0f, 80);
			}
    		return null;
        }
    }

}
