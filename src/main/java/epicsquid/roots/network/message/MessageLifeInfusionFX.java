package epicsquid.roots.network.message;

import java.util.Random;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.SpellRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageLifeInfusionFX implements IMessage {
  public static Random random = new Random();
  double posX = 0, posY = 0, posZ = 0;

  public MessageLifeInfusionFX(){
    super();
  }

  public MessageLifeInfusionFX(double x, double y, double z){
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

  public static class MessageHolder implements IMessageHandler<MessageLifeInfusionFX,IMessage>
  {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(final MessageLifeInfusionFX message, final MessageContext ctx) {
      World world = Minecraft.getMinecraft().world;
      BlockPos pos = new BlockPos(message.posX,message.posY,message.posZ);
      IBlockState state = world.getBlockState(pos);
      state.getBlock().randomDisplayTick(state, world, pos, random);
      for (int k = 0; k < 10; k ++){
        if (random.nextBoolean()){
          ParticleUtil.spawnParticleStar(world, (float)message.posX+random.nextFloat(), (float)message.posY+random.nextFloat(), (float)message.posZ+random.nextFloat(), 0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()-0.5f), SpellRegistry.spell_growth_infusion.red1*255.0f,SpellRegistry.spell_growth_infusion.green1*255.0f,SpellRegistry.spell_growth_infusion.blue1*255.0f, 0.5f, 5f, 14);
        }
        else {
          ParticleUtil.spawnParticleStar(world, (float)message.posX+random.nextFloat(), (float)message.posY+random.nextFloat(), (float)message.posZ+random.nextFloat(), 0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()-0.5f), 0.125f*(random.nextFloat()-0.5f), SpellRegistry.spell_growth_infusion.red2*255.0f,SpellRegistry.spell_growth_infusion.green2*255.0f,SpellRegistry.spell_growth_infusion.blue2*255.0f, 0.5f, 5f, 14);
        }
      }
      return null;
    }
  }

}