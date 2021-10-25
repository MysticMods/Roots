package epicsquid.mysticallib.block;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.model.IModeledObject;
import epicsquid.mysticallib.model.block.BakedModelBlock;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemGroup;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSaplingBase extends BlockBush implements IBlock, IModeledObject, IGrowable {
  private @Nonnull Item itemBlock;
  private List<ItemStack> drops;
  private boolean isOpaque = true;
  private boolean hasCustomModel = false;
  private boolean hasItems = true;
  private AxisAlignedBB box;
  // By default the blocks are made of wood and therefore flammable
  private boolean isFlammable = false;
  private BlockRenderLayer layer = BlockRenderLayer.CUTOUT;
  private Supplier<AbstractTreeFeature> treeGenerator;
  public @Nonnull String name;

  public BlockSaplingBase(@Nonnull String name, Supplier<AbstractTreeFeature> treeGenerator) {
    super();
    this.name = name;
    this.treeGenerator = treeGenerator;
    setBox(new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D));
    setTranslationKey(name);
    setSoundType(SoundType.PLANT);
    setRegistryName(LibRegistry.getActiveModid(), name);
    setLightOpacity(0);
    setOpacity(false);
    itemBlock = new ItemBlock(this).setRegistryName(LibRegistry.getActiveModid(), name);
  }

  @Nonnull
  public BlockSaplingBase setFlammable(boolean flammable) {
    this.isFlammable = flammable;
    return this;
  }

  @Override
  @Nonnull
  public BlockSaplingBase setResistance(float resistance) {
    super.setResistance(resistance);
    return this;
  }

  @Nonnull
  public BlockSaplingBase setDrops(@Nonnull List<ItemStack> drops) {
    this.drops = drops;
    return this;
  }

  @Nonnull
  public BlockSaplingBase setBox(@Nonnull AxisAlignedBB box) {
    this.box = box;
    return this;
  }

  @Override
  @Nonnull
  @SuppressWarnings("deprecation")
  public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
    return box;
  }

  @Nonnull
  public BlockSaplingBase setModelCustom(boolean custom) {
    this.hasCustomModel = custom;
    return this;
  }

  @Nonnull
  public BlockSaplingBase setHarvestReqs(@Nonnull String tool, int level) {
    setHarvestLevel(tool, level);
    return this;
  }

  @Nonnull
  public BlockSaplingBase setOpacity(boolean isOpaque) {
    this.isOpaque = isOpaque;
    return this;
  }

  @Nonnull
  public BlockSaplingBase setHasItem(boolean hasItem) {
    this.hasItems = hasItem;
    return this;
  }

  @SideOnly(Side.CLIENT)
  public BlockSaplingBase setLayer(@Nonnull BlockRenderLayer layer) {
    this.layer = layer;
    return this;
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isOpaqueCube(@Nonnull IBlockState state) {
    return isOpaque;
  }

  public boolean hasCustomModel() {
    return hasCustomModel;
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isFullCube(@Nonnull IBlockState state) {
    return false;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void initModel() {
    if (hasCustomModel) {
      ModelLoader.setCustomStateMapper(this, new CustomStateMapper());
    }
    if (!hasCustomModel) {
      ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "handlers"));
    } else {
      ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "handlers"));
    }
  }

  @Override
  public void getSubBlocks(@Nonnull ItemGroup tab, @Nonnull NonNullList<ItemStack> items) {
    items.add(new ItemStack(this));
  }

  @Override
  @SideOnly(Side.CLIENT)
  @Nonnull
  public BlockRenderLayer getRenderLayer() {
    return this.layer;
  }

  @Override
  public Item getItemBlock() {
    return itemBlock;
  }

  @Override
  public ItemBlock setItemBlock(ItemBlock block) {
    this.itemBlock = block;
    return block;
  }

  @Nullable
  protected IBlockState getParentState() {
    return null;
  }

  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    if (drops != null && drops.size() > 0) {
      return drops.get(0).getItem();
    }
    return super.getItemDropped(state, rand, fortune);
  }

  @Override
  public boolean isFlammable(@Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EnumFacing face) {
    return isFlammable || super.isFlammable(world, pos, face);
  }

  @Override
  public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
    return isFlammable ? 100 : super.getFlammability(world, pos, face);
  }

  @Nonnull
  protected Class<? extends BakedModelBlock> getModelClass() {
    return getModelClass(0);
  }

  @Nonnull
  protected Class<? extends BakedModelBlock> getModelClass(int type) {
    return BakedModelBlock.class;
  }

  public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(worldIn, rand, pos))
      return;
    Feature worldgenerator = treeGenerator.get();
    IBlockState iblockstate2 = Blocks.AIR.getDefaultState();
    worldIn.setBlockState(pos, iblockstate2, 4);
    if (!worldgenerator.generate(worldIn, rand, pos)) {
      worldIn.setBlockState(pos, state, 4);
    }
  }

  @Override
  public int damageDropped(IBlockState state) {
    return 0;
  }

  @Override
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (!worldIn.isRemote) {
      super.updateTick(worldIn, pos, state, rand);

      if (!worldIn.isAreaLoaded(pos, 1))
        return;
      if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0) {
        this.grow(worldIn, pos, state, rand);
      }
    }
  }

  public void grow(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (state.getValue(BlockSapling.STAGE) == 0) {
      worldIn.setBlockState(pos, state.cycleProperty(BlockSapling.STAGE), 4);
    } else {
      this.generateTree(worldIn, pos, state, rand);
    }
  }

  @Override
  public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
    return true;
  }

  @Override
  public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
    return (double) worldIn.rand.nextFloat() < 0.45D;
  }

  @Override
  public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
    this.grow(worldIn, pos, state, rand);
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, BlockSapling.STAGE);
  }

  @Override
  @SuppressWarnings("deprecation")
  public IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState().withProperty(BlockSapling.STAGE, meta);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(BlockSapling.STAGE);
  }
}
