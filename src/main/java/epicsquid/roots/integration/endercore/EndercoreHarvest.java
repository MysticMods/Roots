package epicsquid.roots.integration.endercore;

import epicsquid.mysticallib.block.BlockCropBase;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInterModComms;

import java.util.Arrays;
import java.util.List;

public class EndercoreHarvest {
  public static void init() {
    List<BlockCropBase> crops = Arrays.asList(ModBlocks.moonglow, epicsquid.mysticalworld.init.ModBlocks.aubergine, ModBlocks.pereskia, ModBlocks.wildroot, ModBlocks.spirit_herb, ModBlocks.wildewheet, ModBlocks.cloud_berry, ModBlocks.infernal_bulb, ModBlocks.dewgonia, ModBlocks.stalicripe);
    List<Item> seeds = Arrays.asList(ModItems.moonglow_seed, epicsquid.mysticalworld.init.ModItems.aubergine_seed, ModItems.pereskia_bulb, ModItems.wildroot, ModItems.spirit_herb_seed, ModItems.wildewheet_seed, ModItems.cloud_berry, ModItems.infernal_bulb, ModItems.dewgonia, ModItems.stalicripe);

    assert crops.size() == seeds.size();

    for (int i = 0; i < crops.size(); i++) {
      BlockCropBase block = crops.get(i);
      Item seed = seeds.get(i);

      String message = String.format("%s|%s|7|0", seed.getRegistryName().toString(), block.getRegistryName().toString());
      FMLInterModComms.sendMessage("endercore", "addRightClickCrop", message);
    }
  }
}
