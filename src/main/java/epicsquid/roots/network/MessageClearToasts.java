package epicsquid.roots.network;

import epicsquid.roots.event.ClientTickHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageClearToasts implements IMessage {
	public MessageClearToasts() {
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
	}
	
	public static class MessageHolder extends ClientMessageHandler<MessageClearToasts> {
		@SideOnly(Side.CLIENT)
		@Override
		public void handleMessage(final MessageClearToasts message, final MessageContext ctx) {
			ClientTickHandler.addRunnable(() -> Minecraft.getMinecraft().getToastGui().clear(), 20);
		}
	}
}