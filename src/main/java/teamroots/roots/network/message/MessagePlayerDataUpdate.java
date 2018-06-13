package teamroots.roots.network.message;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.roots.capability.PlayerDataProvider;

public class MessagePlayerDataUpdate implements IMessage {
	public UUID id = null;
	public NBTTagCompound tag = new NBTTagCompound();
	
	public MessagePlayerDataUpdate(){
		//
	}
	
	public MessagePlayerDataUpdate(UUID id, NBTTagCompound tag){
		this.tag = tag;
		this.id = id;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		id = new UUID(buf.readLong(),buf.readLong());
		tag = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(id.getMostSignificantBits());
		buf.writeLong(id.getLeastSignificantBits());
		ByteBufUtils.writeTag(buf, tag);
	}

    public static class MessageHolder implements IMessageHandler<MessagePlayerDataUpdate,IMessage>
    {
    	@SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessagePlayerDataUpdate message, final MessageContext ctx) {
    		if (message != null){
	    		World w = Minecraft.getMinecraft().world;
	    		if (w != null){
		    		if (w.getPlayerEntityByUUID(message.id) != null){
		    			EntityPlayer player = w.getPlayerEntityByUUID(message.id);
		    			if (player.hasCapability(PlayerDataProvider.playerDataCapability, null)){
		    				player.getCapability(PlayerDataProvider.playerDataCapability, null).setData(message.tag);
		    			}
		    		}
	    		}
    		}
    		return null;
	    }
    }
}
