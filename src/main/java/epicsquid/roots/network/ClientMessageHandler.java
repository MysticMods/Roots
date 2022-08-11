package epicsquid.roots.network;

import epicsquid.roots.event.ClientTickHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class ClientMessageHandler<T extends IMessage> extends MessageHandler<T> {
	@Override
	public IMessage onMessage(T message, MessageContext ctx) {
		ClientTickHandler.addRunnable(() -> handleMessageInternal(message, ctx));
		return null;
	}
}
