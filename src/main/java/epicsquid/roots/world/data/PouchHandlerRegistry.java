package epicsquid.roots.world.data;

import java.util.UUID;

public class PouchHandlerRegistry extends UUIDRegistry<PouchHandlerData> {
  public static PouchHandlerRegistry INSTANCE = new PouchHandlerRegistry();

  private PouchHandlerRegistry() {
    super(PouchHandlerData.class, PouchHandlerData::name, PouchHandlerData::new);
  }

  public static PouchHandlerData getNewData() {
    return getData(UUID.randomUUID());
  }

  public static PouchHandlerData getData(UUID id) {
    return INSTANCE.getDataInternal(id);
  }
}
