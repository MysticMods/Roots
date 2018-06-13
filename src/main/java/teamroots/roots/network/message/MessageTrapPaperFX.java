package teamroots.roots.network.message;

import java.util.Random;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.roots.entity.EntityAuspiciousPoint;
import teamroots.roots.entity.RenderAuspiciousPoint;
import teamroots.roots.particle.ParticleUtil;
import teamroots.roots.spell.SpellBase;
import teamroots.roots.spell.SpellRegistry;
import teamroots.roots.util.Misc;

public class MessageTrapPaperFX implements IMessage {
	public static Random random = new Random();
	double posX = 0, posY = 0, posZ = 0;
	
	public MessageTrapPaperFX(){
		super();
	}
	
	public MessageTrapPaperFX(double x, double y, double z){
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

    public static class MessageHolder implements IMessageHandler<MessageTrapPaperFX,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageTrapPaperFX message, final MessageContext ctx) {
	    	World world = Minecraft.getMinecraft().world;
			for (int i = 0; i < 8; i ++){
				float pitch = Misc.random.nextFloat()*360.0f;
				float yaw = Misc.random.nextFloat()*360.0f;
				for (float j = 0; j < 1.0; j += 0.1f){
					float tx = (float)message.posX + j*(float)Math.sin(Math.toRadians(yaw))*(float)Math.sin(Math.toRadians(pitch));
					float ty = (float)message.posY + j*(float)Math.cos(Math.toRadians(pitch));
					float tz = (float)message.posZ + j*(float)Math.cos(Math.toRadians(yaw))*(float)Math.sin(Math.toRadians(pitch));
					float coeff = j;
					ParticleUtil.spawnParticleGlow(world, tx, ty, tz, 0, 0, 0, 137, 186, 127, 0.5f*(1.0f-coeff), 10.0f*(1.0f-coeff), 20);
				}
			}
    		return null;
        }
    }

}
