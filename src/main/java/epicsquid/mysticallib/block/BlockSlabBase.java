package epicsquid.mysticallib.block;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.item.ItemBlockSlab;
import epicsquid.mysticallib.model.CustomModelBlock;
import epicsquid.mysticallib.model.CustomModelLoader;
import epicsquid.mysticallib.model.ICustomModeledObject;
import epicsquid.mysticallib.model.IModeledObject;
import epicsquid.mysticallib.model.block.BakedModelBlock;
import epicsquid.mysticallib.model.block.BakedModelSlab;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.ItemGroup;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public class BlockSlabBase extends SlabBlock implements IBlock, IModeledObject, ICustomModeledObject {

  public static BlockSlabBase dummy = new BlockSlabBase(Material.AIR, SoundType.SNOW, 0f, "null", Blocks.AIR.getDefaultState(), false, net.minecraft.block.Blocks.AIR);
  private Item itemBlock;
  public List<ItemStack> drops = null;
  private boolean isOpaque = false;
  private boolean hasCustomModel = false;
  private @Nonnull BlockRenderLayer layer = BlockRenderLayer.SOLID;
  private boolean isFlammable = false;
  private boolean isDouble;
  private BlockState parent;
  public String name = "";
  public Block slab;

  public BlockSlabBase(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name, @Nonnull BlockState parent, boolean isDouble, @Nullable Block slab) {
    super(mat);
    this.isDouble = isDouble;
    setTranslationKey(name);
    setRegistryName(name);
    setSoundType(type);
    setHardness(hardness);
    setLightOpacity(0);
    setOpacity(false);
    this.fullBlock = false;
    this.parent = parent;
    this.slab = slab;
    if (!isDouble) {
      itemBlock = new ItemBlockSlab(this, slab).setRegistryName(LibRegistry.getActiveModid(), name);
    } else {
      itemBlock = null;
    }
  }

  @Nonnull
  public BlockState getParent() {
    return parent;
  }

  @Nonnull
  public BlockSlabBase setFlammable(boolean flammable) {
    this.isFlammable = flammable;
    return this;
  }

  @Override
  @Nonnull
  public BlockSlabBase setResistance(float resistance) {
    super.setResistance(resistance);
    return this;
  }

  @Override
  public void getSubBlocks(@Nonnull ItemGroup tab, @Nonnull NonNullList<ItemStack> items) {
    if ((tab == this.getCreativeTab() || tab == ItemGroup.SEARCH) && !this.isDouble) {
      items.add(new ItemStack(this, 1));
    }
  }

  @Override
  @Nonnull
  public Block setCreativeTab(@Nonnull ItemGroup tab) {
    if (!isDouble) {
      itemBlock.setCreativeTab(tab);
    }
    return super.setCreativeTab(tab);
  }

  @Override
  public void getDrops(@Nonnull NonNullList<ItemStack> drops, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull BlockState state, int fortune) {
    if (isDouble) {
      drops.add(new ItemStack(Item.getItemFromBlock(this.slab), 1));
      drops.add(new ItemStack(Item.getItemFromBlock(this.slab), 1));
    } else {
      super.getDrops(drops, world, pos, state, fortune);
    }
  }

  @Nonnull
  public BlockSlabBase setModelCustom(boolean custom) {
    this.hasCustomModel = custom;
    return this;
  }

  @Nonnull
  public BlockSlabBase setHarvestReqs(@Nonnull String tool, int level) {
    setHarvestLevel(tool, level);
    return this;
  }

  @Nonnull
  public BlockSlabBase setOpacity(boolean isOpaque) {
    this.isOpaque = isOpaque;
    return this;
  }

  @Nonnull
  public BlockSlabBase setLayer(@Nonnull BlockRenderLayer layer) {
    this.layer = layer;
    return this;
  }

  @Override
  public boolean isOpaqueCube(@Nonnull BlockState state) {
    return isOpaque;
  }

  public boolean hasCustomModel() {
    return this.hasCustomModel;
  }

  @Override
  public boolean isFullCube(@Nonnull BlockState state) {
    return false;
  }

  public void setSlab(@Nonnull Block slab) {
    this.slab = slab;
  }

  @Override
  public boolean isFlammable(@Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull Direction face) {
    return isFlammable || super.isFlammable(world, pos, face);
  }

  @Override
  public int getFlammability(IBlockAccess world, BlockPos pos, Direction face) {
    return isFlammable ? 100 : super.getFlammability(world, pos, face);
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
      ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new net.minecraft.client.renderer.model.ModelResourceLocation(getRegistryName(), "handlers"));
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void initCustomModel() {
    if (hasCustomModel) {
      ResourceLocation defaultTex = new ResourceLocation(
          parent.getBlock().getRegistryName().getNamespace() + ":blocks/" + parent.getBlock().getRegistryName().getPath());
      if (isDouble) {
        CustomModelLoader.blockmodels.put(new ResourceLocation(getRegistryName().getNamespace() + ":models/block/" + getRegistryName().getPath()),
            new CustomModelBlock(getModelClass(1), defaultTex, defaultTex));
        CustomModelLoader.itemmodels.put(new ResourceLocation(getRegistryName().getNamespace() + ":" + getRegistryName().getPath() + "#handlers"),
            new CustomModelBlock(getModelClass(1), defaultTex, defaultTex));
      } else {
        CustomModelLoader.blockmodels.put(new ResourceLocation(getRegistryName().getNamespace() + ":models/block/" + getRegistryName().getPath()),
            new CustomModelBlock(getModelClass(), defaultTex, defaultTex));
        CustomModelLoader.itemmodels.put(new ResourceLocation(getRegistryName().getNamespace() + ":" + getRegistryName().getPath() + "#handlers"),
            new CustomModelBlock(getModelClass(), defaultTex, defaultTex));
      }
    }
  }

  @Nonnull
  protected Class<? extends BakedModelBlock> getModelClass() {
    return getModelClass(0);
  }

  @Nonnull
  protected Class<? extends BakedModelBlock> getModelClass(int type) {
    if (type == 1) {
      return BakedModelBlock.class;
    } else {
      return BakedModelSlab.class;
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  @Nonnull
  public BlockRenderLayer getRenderLayer() {
    return this.layer;
  }

  @Override
  @Nullable
  public Item getItemBlock() {
    return itemBlock;
  }

  @Override
  public BlockItem setItemBlock(BlockItem block) {
    this.itemBlock = block;
    return block;
  }

  @Override
  @Nonnull
  public BlockState getStateFromMeta(int meta) {
    BlockState iblockstate = this.getDefaultState();
    if (!this.isDouble())
      iblockstate = iblockstate.withProperty(HALF, (meta) == 0 ? SlabBlock.EnumBlockHalf.BOTTOM : SlabBlock.EnumBlockHalf.TOP);

    return iblockstate;
  }

  @Override
  public int getMetaFromState(@Nullable BlockState state) {
    return state.getValue(HALF) == EnumBlockHalf.BOTTOM ? 0 : 1;
  }

  @Override
  @Nonnull
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, HALF);
  }

  @Override
  @Nonnull
  public String getTranslationKey(int meta) {
    return super.getTranslationKey();
  }

  @Override
  public boolean isDouble() {
    return isDouble;
  }

  @Override
  @Nonnull
  public IProperty<?> getVariantProperty() {
    return HALF;
  }

  @Override
  @Nonnull
  public Comparable<?> getTypeForItem(@Nonnull ItemStack stack) {
    return 0;
  }
}
