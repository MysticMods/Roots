package epicsquid.roots.world.data;

import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.UUID;

@SuppressWarnings("WeakerAccess")
public class PouchHandlerRegistry {
  public static PouchHandlerData getNewData() {
    return getData(UUID.randomUUID());
  }

  @SuppressWarnings("ConstantConditions")
  public static PouchHandlerData getData(UUID id) {
    WorldServer server = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
    PouchHandlerData data = (PouchHandlerData) server.getMapStorage().getOrLoadData(PouchHandlerData.class, PouchHandlerData.name(id));

    if (data == null) {
      data = new PouchHandlerData(id);
      server.getMapStorage().setData(PouchHandlerData.name(id), data);
    }

    return data;
  }
}
