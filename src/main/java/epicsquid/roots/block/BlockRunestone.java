package epicsquid.roots.block;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.block.BlockBase;
import epicsquid.roots.Roots;
import epicsquid.roots.block.blockenum.RuneType;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class BlockRunestone extends BlockBase {

  public static PropertyEnum<RuneType> RUNE_PROPERTY = PropertyEnum.create("runetype", RuneType.class);

  public BlockRunestone(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name) {
    super(mat, type, hardness, name);
    setDefaultState(this.blockState.getBaseState().withProperty(RUNE_PROPERTY, RuneType.NORMAL));

  }


  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, RUNE_PROPERTY);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState().withProperty(RUNE_PROPERTY, RuneType.intOf(meta));
  }


  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(RUNE_PROPERTY).getMeta();
  }

  @Override
  public void getSubBlocks(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
    for (RuneType enumType : RuneType.values()) {
      items.add(new ItemStack(this, 1, enumType.getMeta()));
    }
  }
}
