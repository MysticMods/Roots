package epicsquid.roots.network.message;

import java.util.Random;

import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageAcidCloudFX implements IMessage {
  public static Random random = new Random();
  double posX = 0, posY = 0, posZ = 0;

  public MessageAcidCloudFX(){
    super();
  }

  public MessageAcidCloudFX(double x, double y, double z){
    super();
    this.posX = x;
    this.posY = y;
    this.posZ = z;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    posX = buf.readDouble();
    posY = buf.readDouble();
    posZ = buf.readDouble();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeDouble(posX);
    buf.writeDouble(posY);
    buf.writeDouble(posZ);
  }

  public static float getColorCycle(float ticks){
    return (MathHelper.sin((float)Math.toRadians(ticks))+1.0f)/2.0f;
  }

  public static class MessageHolder implements IMessageHandler<MessageAcidCloudFX,IMessage>
  {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(final MessageAcidCloudFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      for (float i = 0; i < 360; i += random.nextInt(40)){
        float x = (float)message.posX+(1.5f*random.nextFloat()+2.0f)*(float)Math.sin(Math.toRadians(i));
        float y = (float)message.posY+(random.nextFloat()-1.5f);
        float z = (float)message.posZ+(1.5f*random.nextFloat()+2.0f)*(float)Math.cos(Math.toRadians(i));
        float vx = 0.0625f*(float)Math.cos(Math.toRadians(i));
        float vz = 0.025f*(float)Math.sin(Math.toRadians(i));
        if (random.nextBoolean()){
          vx *= -1;
          vz *= -1;
        }
        if (random.nextBoolean()){
          ParticleUtil.spawnParticleSmoke(world, x, y, z, vx, 0.125f*(random.nextFloat()-0.5f), vz, SpellRegistry.spell_acid_cloud.red1, SpellRegistry.spell_acid_cloud.green1, SpellRegistry.spell_acid_cloud.blue1, 0.125f, 10f+random.nextFloat()*6f, 120, false);
        }
        else {
          ParticleUtil.spawnParticleSmoke(world, x, y, z, vx, 0.125f*(random.nextFloat()-0.5f), vz, SpellRegistry.spell_acid_cloud.red2, SpellRegistry.spell_acid_cloud.green2, SpellRegistry.spell_acid_cloud.blue2, 0.125f, 10f+random.nextFloat()*6f, 120, false);
        }
      }
      return null;
    }
  }

}