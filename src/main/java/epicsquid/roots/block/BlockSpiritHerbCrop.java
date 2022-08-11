package epicsquid.roots.block;

import epicsquid.mysticallib.block.BlockCropBase;
import epicsquid.roots.init.ModItems;
import net.minecraft.item.Item;
import net.minecraftforge.common.EnumPlantType;

import javax.annotation.Nonnull;

public class BlockSpiritHerbCrop extends BlockCropBase {
	
	public BlockSpiritHerbCrop(@Nonnull String name, @Nonnull EnumPlantType plantType) {
		super(name, plantType);
	}
	
	/**
	 * Gets the seed to drop for the crop
	 */
	@Override
	@Nonnull
	public Item getSeed() {
		return ModItems.spirit_herb_seed;
	}
	
	/**
	 * Gets the crop to drop for the plant
	 */
	@Override
	@Nonnull
	public Item getCrop() {
		return ModItems.spirit_herb;
	}
	
}
