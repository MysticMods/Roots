package epicsquid.roots.network;

import epicsquid.roots.api.Herb;
import epicsquid.roots.event.ClientTickHandler;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.client.hud.RenderHerbHUD;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageUpdateHerb implements IMessage {
  private Herb herb = null;
  private double amount = 0;

  public MessageUpdateHerb() {
  }

  public MessageUpdateHerb(Herb herb, double amount) {
    this.herb = herb;
    this.amount = amount;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.herb = HerbRegistry.getHerb(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
    this.amount = buf.readDouble();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    ByteBufUtils.writeUTF8String(buf, herb.getRegistryName().toString());
    buf.writeDouble(amount);
  }

  public static class MessageHolder implements IMessageHandler<MessageUpdateHerb, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(final MessageUpdateHerb message, final MessageContext ctx) {
      ClientTickHandler.addRunnable(() -> RenderHerbHUD.INSTANCE.resolveSlots(Minecraft.getMinecraft().player, message.herb, message.amount));
      return null;
    }
  }
}