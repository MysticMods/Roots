package epicsquid.roots.network.fx;

import epicsquid.roots.network.ClientMessageHandler;
import epicsquid.roots.tileentity.TileEntityFeyCrafter;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageGrowthCrafterVisualFX implements IMessage {
	private BlockPos pos;
	private int dimension;
	
	public MessageGrowthCrafterVisualFX() {
		super();
	}
	
	public MessageGrowthCrafterVisualFX(BlockPos pos, int dimension) {
		super();
		this.pos = pos;
		this.dimension = dimension;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = BlockPos.fromLong(buf.readLong());
		this.dimension = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(this.pos.toLong());
		buf.writeInt(this.dimension);
	}
	
	public static class MessageHolder extends ClientMessageHandler<MessageGrowthCrafterVisualFX> {
		@SideOnly(Side.CLIENT)
		@Override
		protected void handleMessage(final MessageGrowthCrafterVisualFX message, final MessageContext ctx) {
			Minecraft mc = Minecraft.getMinecraft();
			if (mc == null || mc.world == null) {
				return;
			}
			// This is now sent only to those tracking the tile entity.
			World world = mc.world;
			TileEntity te = world.getTileEntity(message.pos);
			if (te instanceof TileEntityFeyCrafter) {
				((TileEntityFeyCrafter) te).doVisual();
			}
		}
	}
}