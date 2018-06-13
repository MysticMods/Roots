package teamroots.roots.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import teamroots.roots.tileentity.TileEntityMortar;

public class BlockMortar extends BlockTEBase {

	public BlockMortar(Material material, SoundType type, String name, boolean addToTab) {
		super(material, type, name, addToTab);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityMortar();
	}

}
