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

public class MessagePetalShellBurstFX implements IMessage {
  public static Random random = new Random();
  double posX = 0, posY = 0, posZ = 0;

  public MessagePetalShellBurstFX(){
    super();
  }

  public MessagePetalShellBurstFX(double x, double y, double z){
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

  public static class MessageHolder implements IMessageHandler<MessagePetalShellBurstFX,IMessage>
  {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(final MessagePetalShellBurstFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      for (int k = 0; k < 10; k ++){
        if (random.nextBoolean()){
          ParticleUtil.spawnParticlePetal(world, (float)message.posX, (float)message.posY, (float)message.posZ, 0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()-0.5f), SpellRegistry.spell_petal_shell.red1*255.0f,SpellRegistry.spell_petal_shell.green1*255.0f,SpellRegistry.spell_petal_shell.blue1*255.0f, 0.5f, 7.5f, 24);
        }
        else {
          ParticleUtil.spawnParticlePetal(world, (float)message.posX, (float)message.posY, (float)message.posZ, 0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()-0.5f), SpellRegistry.spell_petal_shell.red2*255.0f,SpellRegistry.spell_petal_shell.green2*255.0f,SpellRegistry.spell_petal_shell.blue2*255.0f, 0.5f, 7.5f, 24);
        }
      }
      for (float k = 0; k < 360; k += random.nextInt(12)){
        if (random.nextBoolean()){
          if (random.nextBoolean()){
            ParticleUtil.spawnParticlePetal(world, (float)message.posX+1.35f*(float)Math.sin(Math.toRadians(k)), (float)message.posY, (float)message.posZ+1.35f*(float)Math.cos(Math.toRadians(k)), 0, 0, 0, SpellRegistry.spell_petal_shell.red1, SpellRegistry.spell_petal_shell.green1, SpellRegistry.spell_petal_shell.blue1, 0.5f, 1.25f+5.0f*random.nextFloat(), 40);
          }
          else {
            ParticleUtil.spawnParticlePetal(world, (float)message.posX+1.35f*(float)Math.sin(Math.toRadians(k)), (float)message.posY, (float)message.posZ+1.35f*(float)Math.cos(Math.toRadians(k)), 0, 0, 0, SpellRegistry.spell_petal_shell.red2, SpellRegistry.spell_petal_shell.green2, SpellRegistry.spell_petal_shell.blue2, 0.5f, 1.25f+5.0f*random.nextFloat(), 40);
          }
        }
      }
      return null;
    }
  }

}