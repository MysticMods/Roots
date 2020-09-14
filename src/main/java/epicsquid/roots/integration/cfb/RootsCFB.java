package epicsquid.roots.integration.cfb;

import epicsquid.roots.init.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class RootsCFB {
  public static void init() {
    FMLInterModComms.sendMessage("cookingforblockheads", "RegisterTool", new ItemStack(ModItems.runic_shears));
  }
}
