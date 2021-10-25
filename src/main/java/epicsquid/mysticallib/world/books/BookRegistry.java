package epicsquid.mysticallib.world.books;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class BookRegistry extends WorldSavedData {
  public static String id = "BookRegistry-";

  public static BookRegistry getBookRegistry(String modName, EntityPlayer player) {
    String thisId = id + modName + "-" + player.getCachedUniqueIdString();
    WorldServer server = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
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
  public void readFromNBT(NBTTagCompound nbt) {
    hasBook = nbt.getBoolean("hasBook");
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setBoolean("hasBook", hasBook);
    return compound;
  }
}
