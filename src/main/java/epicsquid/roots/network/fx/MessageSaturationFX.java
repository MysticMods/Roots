package epicsquid.roots.network.fx;

import epicsquid.roots.network.ClientMessageHandler;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellSaturate;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageSaturationFX implements IMessage {
	
	private int entityId;
	
	@SuppressWarnings("unused")
	public MessageSaturationFX() {
	}
	
	public MessageSaturationFX(int id) {
		this.entityId = id;
	}
	
	public MessageSaturationFX(EntityPlayer player) {
		this.entityId = player.getEntityId();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityId);
	}
	
	@SuppressWarnings("Duplicates")
	public static class Handler extends ClientMessageHandler<MessageSaturationFX> {
		@SideOnly(Side.CLIENT)
		@Override
		protected void handleMessage(MessageSaturationFX message, MessageContext ctx) {
			World world = Minecraft.getMinecraft().world;
			// TODO: CHECK THIS?
			Entity entity = world.getEntityByID(message.entityId);
			if (entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				for (int i = 0; i <= 360; i += 16) {
					float tx = (float) entity.posX + 0.1f * (float) Math.sin(Math.toRadians(i));
					float ty = (float) entity.posY + (player.height / 2) + 0.5f;
					float tz = (float) entity.posZ + 0.1f * (float) Math.cos(Math.toRadians(i));
					ParticleUtil.spawnParticlePetal(world, tx, ty, tz, (float) Math.sin(Math.toRadians(i)) * 0.1f, -0.01f, (float) Math.cos(Math.toRadians(i)) * 0.1f, SpellSaturate.instance.getRed1(), SpellSaturate.instance.getGreen1(), SpellSaturate.instance.getBlue1(), 1.0f, 3.0f, 90);
				}
			}
		}
	}
}
