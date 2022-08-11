package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.network.ClientMessageHandler;
import epicsquid.roots.particle.ParticleUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageLightDrifterFX implements IMessage {
	private static float[] set1 = new float[]{196f / 255f, 240f / 255f, 255f / 255f, 0.125f};
	private static float[] set2 = new float[]{32f / 255f, 64f / 255f, 96f / 255f, 0.125f};
	private double posX = 0, posY = 0, posZ = 0;
	
	public MessageLightDrifterFX() {
		super();
	}
	
	public MessageLightDrifterFX(double x, double y, double z) {
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
	
	public static float getColorCycle(float ticks) {
		return (MathHelper.sin((float) Math.toRadians(ticks)) + 1.0f) / 2.0f;
	}
	
	public static class MessageHolder extends ClientMessageHandler<MessageLightDrifterFX> {
		@SideOnly(Side.CLIENT)
		@Override
		protected void handleMessage(final MessageLightDrifterFX message, final MessageContext ctx) {
			World world = Minecraft.getMinecraft().world;
			for (float i = 0; i < 360; i += Util.rand.nextInt(12)) {
				double yaw = Math.toRadians(i);
				float x = (float) message.posX + (0.5f * Util.rand.nextFloat()) * (float) Math.sin(yaw);
				float y = (float) message.posY + (Util.rand.nextFloat() - 0.5f);
				float z = (float) message.posZ + (0.5f * Util.rand.nextFloat()) * (float) Math.cos(yaw);
				float vx = 0.0625f * (float) Math.cos(yaw);
				float vz = 0.025f * (float) Math.sin(yaw);
				if (Util.rand.nextBoolean()) {
					vx *= -1;
					vz *= -1;
				}
				if (Util.rand.nextBoolean()) {
					ParticleUtil.spawnParticleSmoke(world, x, y, z, vx, 0.125f * (Util.rand.nextFloat() - 0.5f), vz, set1, 8f + Util.rand.nextFloat() * 6f, 80, true);
				} else {
					ParticleUtil.spawnParticleSmoke(world, x, y, z, vx, 0.125f * (Util.rand.nextFloat() - 0.5f), vz, set2, 8f + Util.rand.nextFloat() * 6f, 80, true);
				}
			}
		}
	}
}
