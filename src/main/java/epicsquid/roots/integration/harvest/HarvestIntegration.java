package epicsquid.roots.integration.harvest;

import epicsquid.mysticallib.block.BlockCropBase;
import epicsquid.roots.init.ModBlocks;
import net.minecraftforge.fml.common.Loader;
import tehnut.harvest.BlockStack;
import tehnut.harvest.Crop;
import tehnut.harvest.Harvest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HarvestIntegration {
	public static void init() {
		List<BlockCropBase> crops = Arrays.asList(ModBlocks.moonglow,
				ModBlocks.pereskia,
				ModBlocks.wildroot,
				ModBlocks.spirit_herb,
				ModBlocks.wildewheet,
				ModBlocks.cloud_berry,
				ModBlocks.infernal_bulb,
				ModBlocks.dewgonia,
				ModBlocks.stalicripe);
		
		if (Loader.isModLoaded("harvest")) {
			Map<BlockStack, Crop> map = Harvest.config.getCropMap();
			for (BlockCropBase block : crops) {
				BlockStack blockStack = new BlockStack(block, 7);
				Crop crop = new Crop(block, 7);
				map.put(blockStack, crop);
			}
		}
	}
}
