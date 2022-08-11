package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.network.ClientMessageHandler;
import epicsquid.roots.particle.ParticleUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessagePyreBigFlameFX implements IMessage {
	
	private BlockPos pos;
	private boolean isCold;
	
	public MessagePyreBigFlameFX() {
	}
	
	public MessagePyreBigFlameFX(BlockPos pos, boolean isCold) {
		this.pos = pos;
		this.isCold = isCold;
	}
	
	@Override
	public void fromBytes(ByteBuf byteBuf) {
		int x = byteBuf.readInt();
		int y = byteBuf.readInt();
		int z = byteBuf.readInt();
		this.pos = new BlockPos(x, y, z);
		this.isCold = byteBuf.readBoolean();
	}
	
	@Override
	public void toBytes(ByteBuf byteBuf) {
		byteBuf.writeInt(pos.getX());
		byteBuf.writeInt(pos.getY());
		byteBuf.writeInt(pos.getZ());
		byteBuf.writeBoolean(isCold);
	}
	
	public static class Handler extends ClientMessageHandler<MessagePyreBigFlameFX> {
		@Override
		@SideOnly(Side.CLIENT)
		protected void handleMessage(MessagePyreBigFlameFX message, MessageContext context) {
			World world = Minecraft.getMinecraft().world;
			if (message.isCold) {
				for (int i = 0; i < 40; i++) {
					ParticleUtil.spawnParticleFiery(world, message.pos.getX() + 0.125f + 0.75f * Util.rand.nextFloat(), message.pos.getY() + 0.75f + 0.5f * Util.rand.nextFloat(), message.pos.getZ() + 0.125f + 0.75f * Util.rand.nextFloat(), 0.03125f * (Util.rand.nextFloat() - 0.5f), 0.125f * Util.rand.nextFloat(), 0.03125f * (Util.rand.nextFloat() - 0.5f), 63.0f, 119.0f, 209.0f, 0.75f, 9.0f + 9.0f * Util.rand.nextFloat(), 40);
				}
			} else {
				for (int i = 0; i < 40; i++) {
					ParticleUtil.spawnParticleFiery(world, message.pos.getX() + 0.125f + 0.75f * Util.rand.nextFloat(), message.pos.getY() + 0.75f + 0.5f * Util.rand.nextFloat(), message.pos.getZ() + 0.125f + 0.75f * Util.rand.nextFloat(), 0.03125f * (Util.rand.nextFloat() - 0.5f), 0.125f * Util.rand.nextFloat(), 0.03125f * (Util.rand.nextFloat() - 0.5f), 255.0f, 224.0f, 32.0f, 0.75f, 9.0f + 9.0f * Util.rand.nextFloat(), 40);
				}
			}
		}
	}
}
