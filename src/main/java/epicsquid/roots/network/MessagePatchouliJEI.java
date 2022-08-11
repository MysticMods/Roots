package epicsquid.roots.network;

import epicsquid.roots.Roots;
import epicsquid.roots.integration.IntegrationUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessagePatchouliJEI implements IMessage {
	private boolean isJEI;
	private String category;
	
	public MessagePatchouliJEI() {
	}
	
	public MessagePatchouliJEI(boolean isJEI, String category) {
		this.category = category;
		this.isJEI = isJEI;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.isJEI = buf.readBoolean();
		this.category = ByteBufUtils.readUTF8String(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(isJEI);
		ByteBufUtils.writeUTF8String(buf, category);
	}
	
	public static class MessageHolder extends ClientMessageHandler<MessagePatchouliJEI> {
		@SideOnly(Side.CLIENT)
		@Override
		protected void handleMessage(final MessagePatchouliJEI message, final MessageContext ctx) {
			if (message.isJEI) {
				// TODO: HANDLE THIS???
			} else {
				ResourceLocation entry = new ResourceLocation(Roots.MODID, message.category.toLowerCase());
				ResourceLocation bookPath = new ResourceLocation(Roots.MODID, "roots_guide");
				IntegrationUtil.openCategory(bookPath, entry);
			}
		}
	}
}