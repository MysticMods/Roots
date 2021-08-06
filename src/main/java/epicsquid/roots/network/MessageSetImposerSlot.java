package epicsquid.roots.network;

import epicsquid.roots.tileentity.TileEntityImposer;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSetImposerSlot implements IMessage {
  private int dimension;
  private BlockPos pos;
  private int slot;

  public MessageSetImposerSlot() {
  }

  public MessageSetImposerSlot(int dimension, BlockPos pos, int slot) {
    this.dimension = dimension;
    this.pos = pos;
    this.slot = slot;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.dimension = buf.readInt();
    this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    this.slot = buf.readInt();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(dimension);
    buf.writeInt(pos.getX());
    buf.writeInt(pos.getY());
    buf.writeInt(pos.getZ());
    buf.writeInt(slot);
  }

  public static class MessageHolder extends ServerMessageHandler<MessageSetImposerSlot> {
    @Override
    protected void handleMessage(MessageSetImposerSlot message, MessageContext ctx) {
      WorldServer server = DimensionManager.getWorld(message.dimension);
      TileEntity te = server.getTileEntity(message.pos);
      if (te instanceof TileEntityImposer) {
        TileEntityImposer tei = (TileEntityImposer) te;
        tei.setSlot(message.slot);
      }
    }
  }
}
