package epicsquid.mysticallib.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.model.CustomModelBlock;
import epicsquid.mysticallib.model.CustomModelLoader;
import epicsquid.mysticallib.model.ICustomModeledObject;
import epicsquid.mysticallib.model.IModeledObject;
import epicsquid.mysticallib.model.block.BakedModelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public class BlockBase extends Block implements IBlock, IModeledObject, ICustomModeledObject, INoCullBlock {
  private @Nonnull Item itemBlock;
  private List<ItemStack> drops;
  private boolean isOpaque = true;
  private boolean hasCustomModel = false;
  private boolean hasItems = true;
  private boolean noCull = false;
  private boolean isBeacon = false;
  // By default the blocks are made of wood and therefore flammable
  private boolean isFlammable = false;
  private AxisAlignedBB box = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
  private BlockRenderLayer layer = BlockRenderLayer.SOLID;
  public @Nonnull String name;

  public BlockBase(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name) {
    super(mat);
    this.name = name;
    setTranslationKey(name);
    setRegistryName(LibRegistry.getActiveModid(), name);
    setSoundType(type);
    setLightOpacity(15);
    setHardness(hardness);
    itemBlock = new ItemBlock(this).setRegistryName(LibRegistry.getActiveModid(), name);
  }

  @Nonnull
  public BlockBase setFlammable(boolean flammable) {
    this.isFlammable = flammable;
    return this;
  }

  @Nonnull
  public BlockBase setIsBeacon (boolean beacon) {
    this.isBeacon = beacon;
    return this;
  }

  @Override
  @Nonnull
  public BlockBase setResistance(float resistance) {
    super.setResistance(resistance);
    return this;
  }

  @Nonnull
  public BlockBase setDrops(@Nonnull List<ItemStack> drops) {
    this.drops = drops;
    return this;
  }

  @Nonnull
  public BlockBase setBox(@Nonnull AxisAlignedBB box) {
    this.box = box;
    return this;
  }

  @Override
  @Nonnull
  public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
    return box;
  }

  @Nonnull
  public BlockBase setNoCull(boolean noCull) {
    this.noCull = noCull;
    return this;
  }

  @Override
  public boolean noCull() {
    return noCull;
  }

  @Nonnull
  public BlockBase setModelCustom(boolean custom) {
    this.hasCustomModel = custom;
    return this;
  }

  @Nonnull
  public BlockBase setHarvestReqs(@Nonnull String tool, int level) {
    setHarvestLevel(tool, level);
    return this;
  }

  @Nonnull
  public BlockBase setOpacity(boolean isOpaque) {
    this.isOpaque = isOpaque;
    return this;
  }

  @Nonnull
  public BlockBase setHasItem(boolean hasItem) {
    this.hasItems = hasItem;
    return this;
  }

  @SideOnly(Side.CLIENT)
  public BlockBase setLayer(@Nonnull BlockRenderLayer layer) {
    this.layer = layer;
    return this;
  }

  @Override
  public boolean isOpaqueCube(@Nonnull IBlockState state) {
    return isOpaque;
  }

  public boolean hasCustomModel() {
    return hasCustomModel;
  }

  @Override
  public boolean isFullCube(@Nonnull IBlockState state) {
    return isOpaque;
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
  @SideOnly(Side.CLIENT)
  public void initCustomModel() {
    if (hasCustomModel) {
      ResourceLocation defaultTex = new ResourceLocation(getRegistryName().getNamespace() + ":blocks/" + getRegistryName().getPath());
      if (getParentState() != null) {
        defaultTex = new ResourceLocation(
            getParentState().getBlock().getRegistryName().getNamespace() + ":blocks/" + getParentState().getBlock().getRegistryName().getPath());
      }
      CustomModelLoader.blockmodels
          .put(new ResourceLocation(getRegistryName().getNamespace() + ":models/block/" + name), new CustomModelBlock(getModelClass(), defaultTex, defaultTex));
      CustomModelLoader.itemmodels.put(new ResourceLocation(getRegistryName().getNamespace() + ":" + name + "#handlers"),
          new CustomModelBlock(getModelClass(), defaultTex, defaultTex));
    }
  }

  @Override
  public void getSubBlocks(@Nonnull ItemGroup tab, @Nonnull NonNullList<ItemStack> items) {
    if (hasItems) {
      super.getSubBlocks(tab, items);
    }
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

  @Override
  public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {
    return isBeacon;
  }
}
