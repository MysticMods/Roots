package epicsquid.roots.network.fx;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.spell.FakeSpellRunicDust;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.tileentity.TileEntityGroveCrafter;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageGrowthCrafterVisualFX implements IMessage {
  private BlockPos pos;
  private int dimension;

  public MessageGrowthCrafterVisualFX() {
    super();
  }

  public MessageGrowthCrafterVisualFX(BlockPos pos, int dimension) {
    super();
    this.pos = pos;
    this.dimension = dimension;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.pos = BlockPos.fromLong(buf.readLong());
    this.dimension = buf.readInt();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeLong(this.pos.toLong());
    buf.writeInt(this.dimension);
  }

  public static class MessageHolder implements IMessageHandler<MessageGrowthCrafterVisualFX, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(final MessageGrowthCrafterVisualFX message, final MessageContext ctx) {
      Minecraft mc = Minecraft.getMinecraft();
      World world = mc.world;
      if (world.provider.getDimension() != message.dimension) return null;

      TileEntity te = world.getTileEntity(message.pos);
      if (te instanceof TileEntityGroveCrafter) {
        ((TileEntityGroveCrafter) te).doVisual();
      }

      return null;
    }
  }
}