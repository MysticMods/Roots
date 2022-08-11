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

import java.util.ArrayList;
import java.util.List;

public class MessageTreeCompleteFX implements IMessage {
	public List<BlockPos> affectedBlocks = new ArrayList<>();
	
	public MessageTreeCompleteFX() {
	}
	
	public MessageTreeCompleteFX(List<BlockPos> affectedBlocks) {
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
	
	public static class MessageHolder extends ClientMessageHandler<MessageTreeCompleteFX> {
		@SideOnly(Side.CLIENT)
		@Override
		protected void handleMessage(final MessageTreeCompleteFX message, final MessageContext ctx) {
			World world = Minecraft.getMinecraft().world;
			for (BlockPos pos : message.affectedBlocks) {
				for (int k = 0; k < 2 + Util.rand.nextInt(2); k++) {
					ParticleUtil.spawnParticleGlow(world, (float) pos.getX(), (float) pos.getY(), (float) pos.getZ(), 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), 0.125f * (Util.rand.nextFloat() - 0.5f), 204f, 196f, 35f, 0.5f, 2.5f, 48);
				}
			}
		}
	}
}
