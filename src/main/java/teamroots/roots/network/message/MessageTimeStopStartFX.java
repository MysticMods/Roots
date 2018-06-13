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

public class MessageTimeStopStartFX implements IMessage {
	public static Random random = new Random();
	double posX = 0, posY = 0, posZ = 0;
	
	public MessageTimeStopStartFX(){
		super();
	}
	
	public MessageTimeStopStartFX(double x, double y, double z){
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

    public static class MessageHolder implements IMessageHandler<MessageTimeStopStartFX,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageTimeStopStartFX message, final MessageContext ctx) {
	    	World world = Minecraft.getMinecraft().world;
			for (float k = 0; k < 360; k += random.nextInt(4)){
				if (random.nextBoolean()){
					if (random.nextBoolean()){
						ParticleUtil.spawnParticleGlow(world, (float)message.posX+0.5f*(float)Math.sin(Math.toRadians(k)), (float)message.posY, (float)message.posZ+0.5f*(float)Math.cos(Math.toRadians(k)), random.nextFloat()*0.1875f*(float)Math.sin(Math.toRadians(k)), random.nextFloat()*-0.0625f, random.nextFloat()*0.1875f*(float)Math.cos(Math.toRadians(k)), SpellRegistry.spell_oxeye_daisy.red1, SpellRegistry.spell_oxeye_daisy.green1, SpellRegistry.spell_oxeye_daisy.blue1, 0.5f, 2.5f+7.0f*random.nextFloat(), 40);
					}
					else {
						ParticleUtil.spawnParticleGlow(world, (float)message.posX+0.5f*(float)Math.sin(Math.toRadians(k)), (float)message.posY, (float)message.posZ+0.5f*(float)Math.cos(Math.toRadians(k)), random.nextFloat()*0.1875f*(float)Math.sin(Math.toRadians(k)), random.nextFloat()*-0.0625f, random.nextFloat()*0.1875f*(float)Math.cos(Math.toRadians(k)), SpellRegistry.spell_oxeye_daisy.red2, SpellRegistry.spell_oxeye_daisy.green2, SpellRegistry.spell_oxeye_daisy.blue2, 0.5f, 2.5f+7.0f*random.nextFloat(), 40);
					}
				}
			}
    		return null;
        }
    }

}
