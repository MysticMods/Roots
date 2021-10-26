package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.VecUtil;
import epicsquid.roots.network.ClientMessageHandler;
import epicsquid.roots.particle.ParticleUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.OnlyIn;

public class MessageSenseHomeFX implements IMessage {
  private BlockPos home;

  public MessageSenseHomeFX() {
    super();
  }

  public MessageSenseHomeFX(BlockPos home) {
    this.home = home;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.home = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(home.getX());
    buf.writeInt(home.getY());
    buf.writeInt(home.getZ());
  }

  private static final float[] color = new float[]{182 / 255.0f, 109 / 255.0f, 191 / 255.0f, 0.5f};

  public static class MessageHolder extends ClientMessageHandler<MessageSenseHomeFX> {
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handleMessage(final MessageSenseHomeFX message, final MessageContext ctx) {
      Minecraft mc = Minecraft.getMinecraft();
      //noinspection ConstantConditions
      if (mc == null || mc.player == null) {
        return;
      }
      World world = mc.world;
      PlayerEntity player = mc.player;
      Vec3d playerPos = player.getPositionVector().add(0, 1, 0);
      Vec3d line = new Vec3d(message.home.getX(), player.posY, message.home.getZ()).add(0.5, 0.5, 0.5);
      for (Vec3d vec : VecUtil.pointsBetween(playerPos, line, 15)) {
        ParticleUtil.spawnParticlePetal(world, (float) vec.x, (float) vec.y, (float) vec.z, 0, 0, 0, color, 15, 19 * 20);
      }
    }
  }

}