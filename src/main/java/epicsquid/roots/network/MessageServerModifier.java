package epicsquid.roots.network;

import epicsquid.roots.container.IModifierContainer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageServerModifier implements IMessage {
  private boolean shiftDown;
  private boolean ctrlDown;
  private boolean altDown;

  public MessageServerModifier() {
  }

  public MessageServerModifier(boolean shiftDown, boolean ctrlDown, boolean altDown) {
    this.shiftDown = shiftDown;
    this.ctrlDown = ctrlDown;
    this.altDown = altDown;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.shiftDown = buf.readBoolean();
    this.ctrlDown = buf.readBoolean();
    this.altDown = buf.readBoolean();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeBoolean(this.shiftDown);
    buf.writeBoolean(this.ctrlDown);
    buf.writeBoolean(this.altDown);
  }

  public static class MessageHolder extends ServerMessageHandler<MessageServerModifier> {
    @Override
    protected void handleMessage(MessageServerModifier message, MessageContext ctx) {
      EntityPlayerMP player = ctx.getServerHandler().player;
      if (player.openContainer instanceof IModifierContainer) {
        ((IModifierContainer) player.openContainer).setModifierStatus(0, message.shiftDown);
        ((IModifierContainer) player.openContainer).setModifierStatus(1, message.ctrlDown);
        ((IModifierContainer) player.openContainer).setModifierStatus(2, message.altDown);
      }
    }
  }
}
