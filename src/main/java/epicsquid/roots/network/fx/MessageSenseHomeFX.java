package epicsquid.roots.network.fx;

import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellGeas;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

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

  private static final float[] color = new float[]{182/255.0f, 109/255.0f, 191/255.0f, 0.5f};

  public static class MessageHolder implements IMessageHandler<MessageSenseHomeFX, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(final MessageSenseHomeFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      EntityPlayer player = Minecraft.getMinecraft().player;
      Vec3d playerPos = player.getPositionVector().add(0, 1, 0);
      Vec3d line = playerPos.add(new Vec3d(message.home).add(0.5, 0.5, 0.5)).normalize();
      Vec3d particlePoint = playerPos.add(line);
      for (int i = 0; i < 50; i++) {
        ParticleUtil.spawnParticlePetal(world, (float) particlePoint.x, (float) particlePoint.y, (float) particlePoint.z, 0, 0, 0, color, 15, 10 * 20);
        particlePoint = particlePoint.add(line);
      }
      return null;
    }
  }

}