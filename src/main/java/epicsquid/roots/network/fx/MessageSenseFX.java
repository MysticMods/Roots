package epicsquid.roots.network.fx;

import epicsquid.roots.network.ClientMessageHandler;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellExtension;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.OnlyIn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageSenseFX implements IMessage {
  public static class SensePosition {
    private SpellExtension.SenseType type;
    private BlockPos pos;

    public SensePosition(SpellExtension.SenseType type, BlockPos pos) {
      this.type = type;
      this.pos = pos;
    }

    public SensePosition(SpellExtension.SenseType type, int x, int y, int z) {
      this.type = type;
      this.pos = new BlockPos(x, y, z);
    }

    public SpellExtension.SenseType getType() {
      return type;
    }

    public BlockPos getPos() {
      return pos;
    }
  }

  private HashMap<SpellExtension.SenseType, List<BlockPos>> senses;

  public MessageSenseFX() {
  }

  public MessageSenseFX(HashMap<SpellExtension.SenseType, List<BlockPos>> senses) {
    this.senses = senses;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.senses = new HashMap<>();
    for (SpellExtension.SenseType type : SpellExtension.SenseType.values()) {
      List<BlockPos> positions = senses.computeIfAbsent(type, (t) -> new ArrayList<>());
      int count = buf.readInt();
      for (int i = 0; i < count; i++) {
        positions.add(new BlockPos(buf.readInt(), buf.readInt(), buf.readInt()));
      }
    }
  }

  @Override
  public void toBytes(ByteBuf buf) {
    for (SpellExtension.SenseType type : SpellExtension.SenseType.values()) {
      List<BlockPos> positions = senses.get(type);
      if (positions == null) {
        buf.writeInt(0);
      } else {
        buf.writeInt(positions.size());
        for (BlockPos pos : positions) {
          buf.writeInt(pos.getX());
          buf.writeInt(pos.getY());
          buf.writeInt(pos.getZ());
        }
      }
    }
  }

  public static class Handler extends ClientMessageHandler<MessageSenseFX> {
    @Override
    @OnlyIn(Dist.CLIENT)
    protected void handleMessage(MessageSenseFX message, MessageContext ctx) {
      Minecraft minecraft = Minecraft.getMinecraft();
      World world = minecraft.world;
      for (SpellExtension.SenseType type : SpellExtension.SenseType.values()) {
        List<BlockPos> positions = message.senses.get(type);
        if (positions != null) {
          for (BlockPos pos : positions) {
            for (int i = 0; i < 2; ++i) {
              ParticleUtil.spawnParticlePetal(world,
                  pos.getX() + (0.5f - world.rand.nextFloat()) + (world.rand.nextInt(3) == 0 ? (world.rand.nextBoolean() ? -1 : 1) : 0),
                  pos.getY() + (0.5f - world.rand.nextFloat()) + (world.rand.nextInt(3) == 0 ? (world.rand.nextBoolean() ? -1 : 1) : 0),
                  pos.getZ() + (0.5f - world.rand.nextFloat()) + (world.rand.nextInt(3) == 0 ? (world.rand.nextBoolean() ? -1 : 1) : 0),
                  0, 0, 0, type.getColor(), 10f, 20 * 30, true);
            }
          }
        }
      }
    }
  }
}
