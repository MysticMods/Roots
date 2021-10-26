package epicsquid.roots.network.fx;

import epicsquid.roots.network.ClientMessageHandler;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellGeas;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.OnlyIn;

public class MessageTargetedGeasFX implements IMessage {
  private Vector3d start;
  private Vector3d stop;

  public MessageTargetedGeasFX() {
    super();
  }

  public MessageTargetedGeasFX(Vector3d start, Vector3d stop) {
    this.start = start;
    this.stop = stop;
  }

  public MessageTargetedGeasFX(PlayerEntity player, Entity target) {
    this.start = player.getPositionVector().add(0, player.getEyeHeight(), 0);
    this.stop = target.getPositionVector().add(0, target.getEyeHeight(), 0);
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.start = new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
    this.stop = new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeDouble(this.start.x);
    buf.writeDouble(this.start.y);
    buf.writeDouble(this.start.z);
    buf.writeDouble(this.stop.x);
    buf.writeDouble(this.stop.y);
    buf.writeDouble(this.stop.z);
  }

  public static class MessageHolder extends ClientMessageHandler<MessageTargetedGeasFX> {
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handleMessage(final MessageTargetedGeasFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      ParticleUtil.renderBeam(world, message.start, message.stop, ParticleUtil::spawnParticleStarNoGravity, SpellGeas.instance);
    }
  }
}