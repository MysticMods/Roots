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

public class MessageLightDrifterFX implements IMessage {
	public static Random random = new Random();
	double posX = 0, posY = 0, posZ = 0;
	
	public MessageLightDrifterFX(){
		super();
	}
	
	public MessageLightDrifterFX(double x, double y, double z){
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

    public static class MessageHolder implements IMessageHandler<MessageLightDrifterFX,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageLightDrifterFX message, final MessageContext ctx) {
	    	World world = Minecraft.getMinecraft().world;
			for (float i = 0; i < 360; i += random.nextInt(12)){
				float x = (float)message.posX+(0.5f*random.nextFloat())*(float)Math.sin(Math.toRadians(i));
				float y = (float)message.posY+(random.nextFloat()-0.5f);
				float z = (float)message.posZ+(0.5f*random.nextFloat())*(float)Math.cos(Math.toRadians(i));
				float vx = 0.0625f*(float)Math.cos(Math.toRadians(i));
				float vz = 0.025f*(float)Math.sin(Math.toRadians(i));
				if (random.nextBoolean()){
					vx *= -1;
					vz *= -1;
				}
				if (random.nextBoolean()){
					ParticleUtil.spawnParticleSmoke(world, x, y, z, vx, 0.125f*(random.nextFloat()-0.5f), vz, SpellRegistry.spell_white_tulip.red1, SpellRegistry.spell_white_tulip.green1, SpellRegistry.spell_white_tulip.blue1, 0.125f, 8f+random.nextFloat()*6f, 80, true);
				}
				else {
					ParticleUtil.spawnParticleSmoke(world, x, y, z, vx, 0.125f*(random.nextFloat()-0.5f), vz, SpellRegistry.spell_white_tulip.red2, SpellRegistry.spell_white_tulip.green2, SpellRegistry.spell_white_tulip.blue2, 0.125f, 8f+random.nextFloat()*6f, 80, true);
				}
			}
    		return null;
        }
    }

}
