package epicsquid.mysticallib.block;

import java.util.List;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.model.IModeledObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockLadderBase extends BlockLadder implements IBlock, IModeledObject {
  private @Nonnull Item itemBlock;
  public List<ItemStack> drops = null;
  private boolean hasCustomModel = false;
  private boolean isFlammable = false;
  private Block parent;
  public String name;

  public BlockLadderBase(@Nonnull Block base, float hardness, @Nonnull String name) {
    super();
    this.parent = base;
    this.name = name;
    setCreativeTab(null);
    setTranslationKey(name);
    setRegistryName(LibRegistry.getActiveModid(), name);
    setHardness(hardness);
    this.fullBlock = false;
    itemBlock = new ItemBlock(this).setTranslationKey(name).setRegistryName(LibRegistry.getActiveModid(), name);
  }

  @Nonnull
  public BlockLadderBase setFlammable(boolean flammable) {
    this.isFlammable = flammable;
    return this;
  }

  @Override
  @Nonnull
  public BlockLadderBase setResistance(float resistance) {
    super.setResistance(resistance);
    return this;
  }

  @Nonnull
  public BlockLadderBase setModelCustom(boolean custom) {
    this.hasCustomModel = custom;
    return this;
  }

  @Nonnull
  public BlockLadderBase setHarvestReqs(String tool, int level) {
    setHarvestLevel(tool, level);
    return this;
  }

  public boolean hasCustomModel() {
    return this.hasCustomModel;
  }

  @Override
  public boolean isFlammable(@Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EnumFacing face) {
    return isFlammable || super.isFlammable(world, pos, face);
  }

  @Override
  public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
    return isFlammable ? 100 : super.getFlammability(world, pos, face);
  }

  @Override
  @Nonnull
  public Item getItemBlock() {
    return itemBlock;
  }

  @Override
  public ItemBlock setItemBlock(ItemBlock block) {
    this.itemBlock = block;
    return block;
  }

  @Override
  @SuppressWarnings("deprecation")
  public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    return new ItemStack(itemBlock);
  }

  @Nonnull
  public Block getParent() {
    return parent;
  }

  @Override
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(itemBlock, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
  }
}
