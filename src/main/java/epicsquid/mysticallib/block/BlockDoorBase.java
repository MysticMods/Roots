package epicsquid.mysticallib.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.item.ItemDoorBase;
import epicsquid.mysticallib.model.CustomModelBlock;
import epicsquid.mysticallib.model.CustomModelLoader;
import epicsquid.mysticallib.model.ICustomModeledObject;
import epicsquid.mysticallib.model.IModeledObject;
import epicsquid.mysticallib.model.block.BakedModelBlock;
import epicsquid.mysticallib.model.block.BakedModelDoor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.ItemGroup;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public class BlockDoorBase extends BlockDoor implements IBlock, IModeledObject, ICustomModeledObject {
  private @Nonnull Item itemBlock;
  public List<ItemStack> drops = null;
  private boolean isOpaque = false;
  private boolean hasCustomModel = false;
  private boolean isFlammable = false;
  private BlockRenderLayer layer = BlockRenderLayer.CUTOUT;
  private Block parent;
  public String name;

  public BlockDoorBase(@Nonnull Block base, @Nonnull SoundType type, float hardness, @Nonnull String name) {
    super(base.getDefaultState().getMaterial());
    this.parent = base;
    this.name = name;
    setCreativeTab(null);
    setTranslationKey(name);
    setRegistryName(name);
    setSoundType(type);
    setLightOpacity(0);
    setHardness(hardness);
    setOpacity(false);
    this.fullBlock = false;
    itemBlock = new ItemDoorBase(this, name).setModelCustom(true);
  }

  @Nonnull
  public BlockDoorBase setFlammable(boolean flammable) {
    this.isFlammable = flammable;
    return this;
  }

  @Override
  @Nonnull
  public BlockDoorBase setResistance(float resistance) {
    super.setResistance(resistance);
    return this;
  }

  @Nonnull
  public BlockDoorBase setModelCustom(boolean custom) {
    this.hasCustomModel = custom;
    return this;
  }

  @Nonnull
  public BlockDoorBase setHarvestReqs(String tool, int level) {
    setHarvestLevel(tool, level);
    return this;
  }

  @Nonnull
  public BlockDoorBase setOpacity(boolean isOpaque) {
    this.isOpaque = isOpaque;
    return this;
  }

  @Nonnull
  public BlockDoorBase setLayer(@Nonnull BlockRenderLayer layer) {
    this.layer = layer;
    return this;
  }

  @Override
  public boolean isOpaqueCube(@Nonnull IBlockState state) {
    return isOpaque;
  }

  public boolean hasCustomModel() {
    return this.hasCustomModel;
  }

  @Override
  public boolean isFullCube(@Nonnull IBlockState state) {
    return false;
  }

  @Override
  public boolean canPlaceTorchOnTop(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
    return true;
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
  @SideOnly(Side.CLIENT)
  @Nonnull
  public BlockRenderLayer getRenderLayer() {
    return this.layer;
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

  @Nonnull
  @Override
  public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    return new ItemStack(itemBlock);
  }

  @Nonnull
  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER ? Items.AIR : itemBlock;
  }

  @Override
  public int quantityDropped(Random random) {
    return 1;
  }

  @Nonnull
  public Block getParent() {
    return parent;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void initModel() {
    if (hasCustomModel) {
      ModelLoader.setCustomStateMapper(this, new CustomStateMapper());
    }
    if (!hasCustomModel) {
      ModelLoader.setCustomModelResourceLocation(itemBlock, 0, new ModelResourceLocation(getRegistryName(), "handlers"));
      ModelLoader.setCustomStateMapper(this, (new StateMap.Builder()).ignore(POWERED).build());
    } else {
      ModelLoader.setCustomModelResourceLocation(itemBlock, 0, new ModelResourceLocation(getRegistryName(), "handler"));
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void initCustomModel() {
    if (hasCustomModel) {
      ResourceLocation defaultTex = new ResourceLocation(parent.getRegistryName().getNamespace() + ":blocks/" + parent.getRegistryName().getPath());
      CustomModelLoader.blockmodels
          .put(new ResourceLocation(getRegistryName().getNamespace() + ":models/block/" + name), new CustomModelBlock(getModelClass(), defaultTex, defaultTex));
      CustomModelLoader.itemmodels.put(new ResourceLocation(getRegistryName().getNamespace() + ":" + name + "#handlers"),
          new CustomModelBlock(getModelClass(), defaultTex, defaultTex));
    }
  }

  @Nonnull
  protected Class<? extends BakedModelBlock> getModelClass() {
    return BakedModelDoor.class;
  }
}
