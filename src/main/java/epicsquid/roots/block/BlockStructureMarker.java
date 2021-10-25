package epicsquid.roots.block;

import epicsquid.mysticallib.block.BlockBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public class BlockStructureMarker extends BlockBase {

  public static final PropertyInteger marker_value = PropertyInteger.create("marker_value", 0, 15);

  public BlockStructureMarker() {
    super(Material.STRUCTURE_VOID, SoundType.ANVIL, 0.1f, "structure_marker");
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void getSubBlocks(ItemGroup tab, NonNullList<ItemStack> list) {
    if (tab == this.getCreativeTab()) {
      list.clear();
    }
  }

  @Override
  public BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, marker_value);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(marker_value);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(marker_value, meta);
  }

}