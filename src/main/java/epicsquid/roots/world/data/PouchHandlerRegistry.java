package epicsquid.roots.world.data;

import epicsquid.roots.item.PouchType;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.UUID;

@SuppressWarnings("WeakerAccess")
public class PouchHandlerRegistry {
	public static PouchHandlerData getNewData(PouchType type) {
		return getData(UUID.randomUUID(), type);
	}
	
	@SuppressWarnings("ConstantConditions")
	public static PouchHandlerData getData(UUID id, PouchType type) {
		WorldServer server = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
		PouchHandlerData data = (PouchHandlerData) server.getMapStorage().getOrLoadData(PouchHandlerData.class, PouchHandlerData.name(id));
		
		// I feel this check could be problematic
		if (data == null || data.getType() != type) {
			data = new PouchHandlerData(id, type);
			server.getMapStorage().setData(PouchHandlerData.name(id), data);
		}
		
		return data;
	}
}
