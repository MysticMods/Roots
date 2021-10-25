package epicsquid.mysticallib.network;

import epicsquid.mysticallib.MysticalLib;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
  public static SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(MysticalLib.MODID);

  private static int id = 0;

  public static void registerMessages() {
  }

  public static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> handler, Class<REQ> message,
      Side side) {
    INSTANCE.registerMessage(handler, message, id++, side);
  }

  public static void sendToAllTracking(IMessage message, int dimension, BlockPos pos) {
    INSTANCE.sendToAllTracking(message, new NetworkRegistry.TargetPoint(dimension, pos.getX(), pos.getY(), pos.getZ(), 0));
  }

  public static void sendToAllTracking(IMessage message, TileEntity tile) {
    sendToAllTracking(message, tile.getWorld(), tile.getPos());
  }

  public static void sendToAllTracking(IMessage message, World world, BlockPos pos) {
    sendToAllTracking(message, world.provider.getDimension(), pos);
  }

  public static void sendToAllTracking(IMessage message, Entity entity) {
    sendToAllTracking(message, entity.dimension, entity.getPosition());
  }
}