package epicsquid.roots.network.fx;

import epicsquid.roots.particle.ParticleUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*************************************************
 * Author: Davoleo
 * Date / Hour: 04/07/2019 / 15:53
 * Class: MessageIcedTouchFX
 * Project: Mystic Mods
 * Copyright - © - Davoleo - 2019
 **************************************************/

public class MessageIcedTouchFX implements IMessage {

  private float x;
  private float y;
  private float z;

  public MessageIcedTouchFX() {
  }

  public MessageIcedTouchFX(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override
  public void fromBytes(ByteBuf byteBuf) {
    x = byteBuf.readFloat();
    y = byteBuf.readFloat();
    z = byteBuf.readFloat();
  }

  @Override
  public void toBytes(ByteBuf byteBuf) {
    byteBuf.writeFloat(x);
    byteBuf.writeFloat(y);
    byteBuf.writeFloat(z);
  }

  public static class Handler implements IMessageHandler<MessageIcedTouchFX, IMessage> {

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(MessageIcedTouchFX message, MessageContext context) {
      World world = Minecraft.getMinecraft().world;
      ParticleUtil.spawnParticleSmoke(world, message.x + 0.5F, message.y + 0.9F, message.z + 0.5F, 0, 0.01F, 0, 180, 255, 255, 0.30F, 15F, 40, true);
      return null;
    }
  }
}
