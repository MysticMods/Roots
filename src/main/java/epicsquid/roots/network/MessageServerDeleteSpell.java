package epicsquid.roots.network;

import epicsquid.roots.container.ContainerLibrary;
import epicsquid.roots.spell.info.storage.StaffSpellStorage;
import epicsquid.roots.util.PlayerSyncUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageServerDeleteSpell implements IMessage {
	private int slot;
	
	public MessageServerDeleteSpell() {
	}
	
	public MessageServerDeleteSpell(int slot) {
		this.slot = slot;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		slot = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(slot);
	}
	
	public static class MessageHolder extends ServerMessageHandler<MessageServerDeleteSpell> {
		@Override
		protected void handleMessage(MessageServerDeleteSpell message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			if (player.openContainer instanceof ContainerLibrary) {
				ContainerLibrary container = (ContainerLibrary) player.openContainer;
				StaffSpellStorage storage = container.getSpellStorage();
				if (storage != null) {
					storage.clearSlot(message.slot);
					PlayerSyncUtil.syncPlayer(player);
				} else {
					player.sendStatusMessage(new TextComponentTranslation("roots.message.no_storage"), true);
				}
			} else {
				player.sendStatusMessage(new TextComponentTranslation("roots.message.no_library"), true);
			}
		}
	}
}
