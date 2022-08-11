package epicsquid.roots.network.fx;

import epicsquid.mysticallib.particle.particles.ParticleGlitter;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.network.ClientMessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class MessageFrostLandsProgressFX implements IMessage {
	public List<BlockPos> affectedBlocks = new ArrayList<>();
	
	public MessageFrostLandsProgressFX() {
	}
	
	public MessageFrostLandsProgressFX(List<BlockPos> affectedBlocks) {
		this.affectedBlocks = affectedBlocks;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		affectedBlocks.clear();
		int posCount = buf.readInt();
		for (int i = 0; i < posCount; i++) {
			affectedBlocks.add(BlockPos.fromLong(buf.readLong()));
		}
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(affectedBlocks.size());
		for (BlockPos pos : affectedBlocks) {
			buf.writeLong(pos.toLong());
		}
	}
	
	public static class MessageHolder extends ClientMessageHandler<MessageFrostLandsProgressFX> {
		@SideOnly(Side.CLIENT)
		@Override
		protected void handleMessage(final MessageFrostLandsProgressFX message, final MessageContext ctx) {
			World world = Minecraft.getMinecraft().world;
			for (BlockPos pos : message.affectedBlocks) {
				for (int k = 0; k < 20 + Util.rand.nextInt(15); k++) {
					ClientProxy.particleRenderer.spawnParticle(world, ParticleGlitter.class, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, Util.rand.nextDouble() * 0.1 * (Util.rand.nextDouble() > 0.5 ? -1 : 1), Util.rand.nextDouble() * 0.1 * (Util.rand.nextDouble() > 0.5 ? -1 : 1), Util.rand.nextDouble() * 0.1 * (Util.rand.nextDouble() > 0.5 ? -1 : 1), 120, 0.619, 0.72, 0.90 + Util.rand.nextDouble() * 0.05, 1, Util.rand.nextDouble() + 0.5, Util.rand.nextDouble() * 2);
				}
			}
		}
	}
}
