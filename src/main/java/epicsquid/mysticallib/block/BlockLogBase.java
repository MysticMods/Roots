package epicsquid.mysticallib.block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.model.CustomModelBlock;
import epicsquid.mysticallib.model.CustomModelLoader;
import epicsquid.mysticallib.model.ICustomModeledObject;
import epicsquid.mysticallib.model.IModeledObject;
import epicsquid.mysticallib.model.block.BakedModelBlock;
import epicsquid.mysticallib.model.block.BakedModelStairs;
import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public class BlockLogBase extends BlockLog implements IBlock, IModeledObject, ICustomModeledObject {

  private @Nonnull Item itemBlock;
  private boolean isOpaque = false;
  private boolean hasCustomModel = false;
  private BlockRenderLayer layer = BlockRenderLayer.SOLID;
  public String name;

  public BlockLogBase(@Nonnull String name) {
    super();
    this.setCreativeTab(null);
    this.name = name;
    setTranslationKey(name);
    setRegistryName(name);
    itemBlock = new ItemBlock(this).setRegistryName(LibRegistry.getActiveModid(), name);
    this.setDefaultState(this.blockState.getBaseState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
  }

  @Nonnull
  public BlockLogBase setModelCustom(boolean custom) {
    this.hasCustomModel = custom;
    return this;
  }

  @Nonnull
  public BlockLogBase setHarvestReqs(@Nonnull String tool, int level) {
    setHarvestLevel(tool, level);
    return this;
  }

  @Nonnull
  public BlockLogBase setOpacity(boolean isOpaque) {
    this.isOpaque = isOpaque;
    return this;
  }

  @Nonnull
  public BlockLogBase setLayer(@Nonnull BlockRenderLayer layer) {
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
    return true;
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

  @Nonnull
  protected Class<? extends BakedModelBlock> getModelClass() {
    return BakedModelStairs.class;
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

  @Nullable
  protected IBlockState getParentState() {
    return null;
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    IBlockState state = this.getDefaultState();

    switch (meta & 0b1100) {
    case 0b0000:
      state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
      break;

    case 0b0100:
      state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
      break;

    case 0b1000:
      state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
      break;

    case 0b1100:
      state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
      break;
    }

    return state;
  }

  /**
   * Convert the BlockState into the correct metadata value
   */
  @Override
  public int getMetaFromState(IBlockState state) {
    switch (state.getValue(LOG_AXIS)) {
    case X:
      return 0b0100;
    case Y:
      return 0b0000;
    case Z:
      return 0b1000;
    case NONE:
      return 0b1100;
    }
    return 0b1100;
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { LOG_AXIS });
  }
}
