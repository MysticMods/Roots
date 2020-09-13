package epicsquid.roots.network.fx;

import epicsquid.roots.client.particle.ParticlePosition;
import epicsquid.roots.spell.SpellExtension;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class MessageSenseFX implements IMessage {
  public static class SensePosition {
    private SpellExtension.SenseType type;
    private BlockPos pos;
    private int dimension;

    public SensePosition(SpellExtension.SenseType type, BlockPos pos, int dimension) {
      this.type = type;
      this.pos = pos;
      this.dimension = dimension;
    }

    public SensePosition(SpellExtension.SenseType type, int x, int y, int z, int dimension) {
      this.type = type;
      this.pos = new BlockPos(x, y, z);
      this.dimension = dimension;
    }

    public SpellExtension.SenseType getType() {
      return type;
    }

    public BlockPos getPos() {
      return pos;
    }

    public int getDimension() {
      return dimension;
    }
  }

  private List<SensePosition> senses;

  public MessageSenseFX() {
  }

  public MessageSenseFX(List<SensePosition> senses) {
    this.senses = senses;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.senses = new ArrayList<>();
    int count = buf.readInt();
    for (int i = 0; i < count; i++) {
      SpellExtension.SenseType sense = SpellExtension.SenseType.fromOrdinal(buf.readInt());
      int x = buf.readInt();
      int y = buf.readInt();
      int z = buf.readInt();
      int dimension = buf.readInt();
      this.senses.add(new SensePosition(sense, x, y, z, dimension));
    }
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(this.senses.size());
    for (SensePosition pos : this.senses) {
      buf.writeInt(pos.type.ordinal());
      buf.writeInt(pos.pos.getX());
      buf.writeInt(pos.pos.getY());
      buf.writeInt(pos.pos.getZ());
      buf.writeInt(pos.dimension);
    }
  }

  public static class Handler implements IMessageHandler<MessageSenseFX, IMessage> {

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(MessageSenseFX message, MessageContext ctx) {
      Minecraft minecraft = Minecraft.getMinecraft();
      EntityPlayer player = minecraft.player;
      World world = minecraft.world;
      ParticleManager manager = minecraft.effectRenderer;
      for (SensePosition sense : message.senses) {
        for (int i = 0; i < 2; ++i) {
          manager.addEffect(new ParticlePosition(player.world, sense.pos.getX() + 0.75 - world.rand.nextDouble() / 2d, sense.pos.getY() + 0.75 - world.rand.nextDouble() / 2d, sense.pos.getZ() + 0.75 - world.rand.nextDouble() / 2d, sense.type));
        }
      }
      return null;
    }
  }
}
