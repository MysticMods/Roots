package epicsquid.mysticallib.world.books;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class BookRegistry extends WorldSavedData {
  public static String id = "BookRegistry-";

  public static BookRegistry getBookRegistry(String modName, PlayerEntity player) {
    String thisId = id + modName + "-" + player.getCachedUniqueIdString();
    ServerWorld server = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
    BookRegistry registry = (BookRegistry) server.getMapStorage().getOrLoadData(BookRegistry.class, thisId);

    if (registry == null) {
      registry = new BookRegistry(thisId);
      server.getMapStorage().setData(thisId, registry);
    }

    return registry;
  }

  public boolean hasBook = false;

  public BookRegistry(String id) {
    super(id);
  }

  @Override
  public void readFromNBT(CompoundNBT nbt) {
    hasBook = nbt.getBoolean("hasBook");
  }

  @Override
  public CompoundNBT writeToNBT(CompoundNBT compound) {
    compound.setBoolean("hasBook", hasBook);
    return compound;
  }
}
