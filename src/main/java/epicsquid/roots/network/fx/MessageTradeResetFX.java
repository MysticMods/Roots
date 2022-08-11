package epicsquid.roots.network.fx;

import epicsquid.mysticallib.particle.particles.ParticleLeaf;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.network.ClientMessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageTradeResetFX implements IMessage {
	private double[] pos;
	private boolean success;
	
	@SuppressWarnings("unused")
	public MessageTradeResetFX() {
	}
	
	public MessageTradeResetFX(double x, double y, double z, boolean success) {
		this.pos = new double[]{x, y, z};
		this.success = success;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = new double[]{buf.readDouble(), buf.readDouble(), buf.readDouble()};
		this.success = buf.readBoolean();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		for (double val : pos) {
			buf.writeDouble(val);
		}
		buf.writeBoolean(success);
	}
	
	private static final double[] failed = new double[]{224 / 255.0, 11 / 255.0, 54 / 255.0};
	private static final double[] succeeded = new double[]{11 / 255.0, 224 / 255.0, 168 / 255.0};
	
	public static class Handler extends ClientMessageHandler<MessageTradeResetFX> {
		@SideOnly(Side.CLIENT)
		@Override
		protected void handleMessage(MessageTradeResetFX message, MessageContext ctx) {
			World world = Minecraft.getMinecraft().world;
			
			for (int i = 0; i < 18; i++) {
				ClientProxy.particleRenderer.spawnParticle(world, ParticleLeaf.class,
						message.pos[0] + (Util.rand.nextDouble() - 0.5) * 0.2,
						message.pos[1] + 0.6D,
						message.pos[2] + (Util.rand.nextDouble() - 0.5) * 0.2,
						-((Util.rand.nextDouble() - 0.5) * 0.009),
						-((Util.rand.nextDouble() * 0.02) * 0.8),
						-((Util.rand.nextDouble() - 0.5) * 0.009),
						290,
						message.success ? succeeded[0] : failed[0],
						message.success ? succeeded[1] : failed[1],
						message.success ? succeeded[2] : failed[2],
						1.0, 1, 1
				);
			}
		}
	}
}
