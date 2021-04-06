package epicsquid.roots.world.data;

import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.UUID;
import java.util.function.Function;

@SuppressWarnings("WeakerAccess")
public abstract class UUIDRegistry<T extends WorldSavedData> {
  private Class<? extends T> clazz;
  private Function<UUID, String> nameConverter;
  private Function<UUID, T> builder;

  public UUIDRegistry(Class<? extends T> clazz, Function<UUID, String> nameConverter, Function<UUID, T> builder) {
    this.clazz = clazz;
    this.nameConverter = nameConverter;
    this.builder = builder;
  }

  @SuppressWarnings("unchecked")
  public T getDataInternal(UUID id) {
    WorldServer server = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
    MapStorage storage = server.getMapStorage();
    if (storage == null) {
      throw new NullPointerException("Map storage is null");
    }
    T data = (T) storage.getOrLoadData(clazz, nameConverter.apply(id));

    if (data == null) {
      data = builder.apply(id);
      server.getMapStorage().setData(nameConverter.apply(id), data);
    }

    return data;
  }

  public T clearDataInternal(UUID id) {
    WorldServer server = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
    MapStorage storage = server.getMapStorage();
    if (storage == null) {
      throw new NullPointerException("Map storage is null");
    }

    T data = builder.apply(id);
    server.getMapStorage().setData(nameConverter.apply(id), data);
    data.markDirty();
    server.getMapStorage().saveAllData();

    return data;
  }
}
