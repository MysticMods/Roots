package teamroots.roots.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import teamroots.roots.tileentity.TileEntityImbuer;
import teamroots.roots.tileentity.TileEntityMortar;

public class BlockImbuer extends BlockTEBase {

	public BlockImbuer(Material material, SoundType type, String name, boolean addToTab) {
		super(material, type, name, addToTab);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityImbuer();
	}

}
