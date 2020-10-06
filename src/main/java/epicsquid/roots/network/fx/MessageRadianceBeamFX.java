package epicsquid.roots.network.fx;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellRadiance;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageRadianceBeamFX implements IMessage {
  private double posX = 0, posY = 0, posZ = 0;
  private UUID id = null;

  public MessageRadianceBeamFX() {
    super();
  }

  public MessageRadianceBeamFX(UUID id, double x, double y, double z) {
    super();
    this.posX = x;
    this.posY = y;
    this.posZ = z;
    this.id = id;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    posX = buf.readDouble();
    posY = buf.readDouble();
    posZ = buf.readDouble();
    id = new UUID(buf.readLong(), buf.readLong());
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeDouble(posX);
    buf.writeDouble(posY);
    buf.writeDouble(posZ);
    buf.writeLong(id.getMostSignificantBits());
    buf.writeLong(id.getLeastSignificantBits());
  }

  public static float getColorCycle(float ticks) {
    return (MathHelper.sin((float) Math.toRadians(ticks)) + 1.0f) / 2.0f;
  }

  private static double coeff(int a) {
    return a == 0 ? -1 : 1;
  }

  public static class MessageHolder implements IMessageHandler<MessageRadianceBeamFX, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(final MessageRadianceBeamFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      EntityPlayer player = world.getPlayerEntityByUUID(message.id);
      if (player != null) {
        float distance = SpellRadiance.instance.distance;
        float eye = player.getEyeHeight();
        Vec3d pos = player.getPositionVector();
        Vec3d start = pos.add(0, eye, 0);
        Vec3d lookVec = player.getLookVec();
        RayTraceResult result = player.world.rayTraceBlocks(start, start.add(lookVec).scale(distance), false, true, true);
        Vec3d direction = lookVec;
        List<Vec3d> positions = new ArrayList<>();
        double yaw = Math.toRadians(-90 - player.rotationYaw);
        positions.add(new Vec3d(player.posX + 0.5 * Math.sin(yaw), player.posY + eye, player.posZ + 0.5 * Math.cos(yaw)));
        PacketHandler.sendToAllTracking(new MessageRadianceBeamFX(player.getUniqueID(), player.posX, player.posY + eye, player.posZ), player);
        if (result != null) {
          positions.add(result.hitVec);
          if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
            Vec3d hitVec = result.hitVec;
            Vec3i hitSide = result.sideHit.getDirectionVec();
            direction = new Vec3d(direction.x * coeff(hitSide.getX()), direction.y * coeff(hitSide.getY()), direction.z * coeff(hitSide.getZ()));
            distance -= result.hitVec.subtract(pos).length();
            if (distance > 0) {
              result = player.world.rayTraceBlocks(hitVec, hitVec.add(direction.scale(distance)));
              if (result != null) {
                positions.add(hitVec = result.hitVec);
                if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
                  hitSide = result.sideHit.getDirectionVec();
                  direction = new Vec3d(direction.x * coeff(hitSide.getX()), direction.y * coeff(hitSide.getY()), direction.z * coeff(hitSide.getZ()));
                  distance -= hitVec.subtract(pos).length();
                  if (distance > 0) {
                    result = player.world.rayTraceBlocks(hitVec, hitVec.add(direction.scale(distance)));
                    if (result != null) {
                      positions.add(result.hitVec);
                    } else {
                      positions.add(hitVec.add(direction.scale(distance)));
                    }
                  }
                }
              } else {
                positions.add(hitVec.add(direction.scale(distance)));
              }
            }
          }
        } else {
          positions.add(start.add(lookVec.scale(distance)));
        }
        if (positions.size() > 1) {
          double totalDist = 0;
          for (int i = 0; i < positions.size() - 1; i++) {
            totalDist += positions.get(i).subtract(positions.get(i + 1)).length();
          }
          double alphaDist = 0;
          for (int i = 0; i < positions.size() - 1; i++) {
            double dist = positions.get(i).subtract(positions.get(i + 1)).length();
            for (double j = 0; j < dist; j += 0.15) {
              double x = positions.get(i).x * (1.0 - j / dist) + positions.get(i + 1).x * (j / dist);
              double y = positions.get(i).y * (1.0 - j / dist) + positions.get(i + 1).y * (j / dist);
              double z = positions.get(i).z * (1.0 - j / dist) + positions.get(i + 1).z * (j / dist);
              alphaDist += 0.15;

              if (Util.rand.nextBoolean()) {
                ParticleUtil.spawnParticleStarNoGravity(world, (float) x, (float) y, (float) z, 0, 0, 0, SpellRadiance.instance.getRed1() * 255.0f, SpellRadiance.instance.getGreen1() * 255.0f, SpellRadiance.instance.getBlue1() * 255.0f, 0.75f * (float) (1.0f - alphaDist / totalDist), 3f + 3f * Util.rand.nextFloat(), 14);
              } else {
                ParticleUtil.spawnParticleStarNoGravity(world, (float) x, (float) y, (float) z, 0, 0, 0, SpellRadiance.instance.getRed2() * 255.0f, SpellRadiance.instance.getGreen2() * 255.0f, SpellRadiance.instance.getBlue2() * 255.0f, 0.75f * (float) (1.0f - alphaDist / totalDist), 3f + 3f * Util.rand.nextFloat(), 14);
              }
            }
          }
        }
      }
      return null;
    }
  }

}