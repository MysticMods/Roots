package epicsquid.roots.network;

import epicsquid.roots.client.SpectatorHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class MessageLightDrifterSync implements IMessage {
	private UUID id = null;
	private boolean enable = false;
	private double x = 0, y = 0, z = 0;
	private int dimension = 0;
	private int mode = 0;
	
	public MessageLightDrifterSync() {
		super();
	}
	
	public MessageLightDrifterSync(UUID id, double x, double y, double z, boolean enable, int mode, int dimension) {
		super();
		this.id = id;
		this.enable = enable;
		this.x = x;
		this.y = y;
		this.z = z;
		this.mode = mode;
		this.dimension = dimension;
	}
	
	
	
	
	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readDouble();
		y = buf.readDouble();
		z = buf.readDouble();
		enable = buf.readBoolean();
		id = new UUID(buf.readLong(), buf.readLong());
		mode = buf.readInt();
		dimension = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeBoolean(enable);
		buf.writeLong(id.getMostSignificantBits());
		buf.writeLong(id.getLeastSignificantBits());
		buf.writeInt(mode);
		buf.writeInt(dimension);
	}
	
	public static float getColorCycle(float ticks) {
		return (MathHelper.sin((float) Math.toRadians(ticks)) + 1.0f) / 2.0f;
	}
	
	public static class MessageHolder extends ClientMessageHandler<MessageLightDrifterSync> {
		@SideOnly(Side.CLIENT)
		@Override
		protected void handleMessage(final MessageLightDrifterSync message, final MessageContext ctx) {
			Minecraft mc = Minecraft.getMinecraft();
			//noinspection ConstantConditions
			if (mc == null || mc.player == null) {
				return;
			}
			World world = mc.world;
			EntityPlayer player = world.getPlayerEntityByUUID(message.id);
			if (player != null) {
				if (!message.enable) {
					player.setPositionAndUpdate(message.x, message.y, message.z);
//					player.changeDimension(message.dimension);
					if (player == mc.player) {
						SpectatorHandler.setReal();
					}
					player.noClip = message.enable;
				} else {
					player.noClip = true;
					player.setPositionAndUpdate(message.x, message.y, message.z);
//					player.changeDimension(message.dimension);
				}
				
				player.capabilities.isFlying = message.enable;
				GameType type = GameType.getByID(message.mode);
				//noinspection ConstantConditions
				if (type == null) {
					type = GameType.SURVIVAL;
				}
				player.setGameType(type);
				player.capabilities.disableDamage = message.enable;
				player.capabilities.allowFlying = message.enable;
			}
		}
	}
}