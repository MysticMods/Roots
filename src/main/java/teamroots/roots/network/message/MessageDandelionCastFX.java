package teamroots.roots.network.message;

import java.util.Random;
import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
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

public class MessageDandelionCastFX implements IMessage {
	public static Random random = new Random();
	double posX = 0, posY = 0, posZ = 0;
	UUID id = null;
	
	public MessageDandelionCastFX(){
		super();
	}
	
	public MessageDandelionCastFX(UUID id, double x, double y, double z){
		super();
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.id = id;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		posX = buf.readDouble();
		posY = buf.readDouble();
		posZ = buf.readDouble();
		id = new UUID(buf.readLong(),buf.readLong());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(posX);
		buf.writeDouble(posY);
		buf.writeDouble(posZ);
		buf.writeLong(id.getMostSignificantBits());
		buf.writeLong(id.getLeastSignificantBits());
	}

	public static float getColorCycle(float ticks){
		return (MathHelper.sin((float)Math.toRadians(ticks))+1.0f)/2.0f;
	}

    public static class MessageHolder implements IMessageHandler<MessageDandelionCastFX,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageDandelionCastFX message, final MessageContext ctx) {
    		World world = Minecraft.getMinecraft().world;
			EntityPlayer player = world.getPlayerEntityByUUID(message.id);
    		if (player != null){
	    		for (int k = 0; k < 40; k ++){
					ParticleUtil.spawnParticleSmoke(world, (float)player.posX, (float)player.posY+player.getEyeHeight(), (float)player.posZ, (float)player.getLookVec().x+(random.nextFloat()-0.5f), (float)player.getLookVec().y+(random.nextFloat()-0.5f), (float)player.getLookVec().z+(random.nextFloat()-0.5f), 0.65f, 0.65f, 0.65f, 0.15f, 12f, 40, false);
				}
    		}
    		return null;
        }
    }

}
