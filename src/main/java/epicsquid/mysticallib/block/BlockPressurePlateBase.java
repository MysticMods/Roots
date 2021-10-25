package epicsquid.mysticallib.block;

import java.util.List;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.model.CustomModelBlock;
import epicsquid.mysticallib.model.CustomModelLoader;
import epicsquid.mysticallib.model.ICustomModeledObject;
import epicsquid.mysticallib.model.IModeledObject;
import epicsquid.mysticallib.model.block.BakedModelBlock;
import epicsquid.mysticallib.model.block.BakedModelPressurePlate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPressurePlateBase extends BlockPressurePlate implements IBlock, IModeledObject, ICustomModeledObject {
  private @Nonnull Item itemBlock;
  public List<ItemStack> drops = null;
  private boolean isOpaque = false;
  private boolean hasCustomModel = false;
  private boolean isFlammable = false;
  private BlockRenderLayer layer = BlockRenderLayer.CUTOUT;
  private Block parent;
  public String name;

  private final PressurePlateType plateType;

  public BlockPressurePlateBase(@Nonnull Block base, @Nonnull PressurePlateType plateType, @Nonnull SoundType type, float hardness, @Nonnull String name) {
    super(base.getDefaultState().getMaterial(), Sensitivity.EVERYTHING);
    this.parent = base;
    this.name = name;
    setCreativeTab(null);
    setTranslationKey(name);
    setRegistryName(LibRegistry.getActiveModid(), name);
    setSoundType(type);
    setLightOpacity(0);
    setHardness(hardness);
    setOpacity(false);
    this.fullBlock = false;
    itemBlock = new ItemBlock(this).setTranslationKey(name).setRegistryName(LibRegistry.getActiveModid(), name);
    this.plateType = plateType;
  }

  @Nonnull
  public BlockPressurePlateBase setFlammable(boolean flammable) {
    this.isFlammable = flammable;
    return this;
  }

  @Override
  @Nonnull
  public BlockPressurePlateBase setResistance(float resistance) {
    super.setResistance(resistance);
    return this;
  }

  @Nonnull
  public BlockPressurePlateBase setModelCustom(boolean custom) {
    this.hasCustomModel = custom;
    return this;
  }

  @Nonnull
  public BlockPressurePlateBase setHarvestReqs(String tool, int level) {
    setHarvestLevel(tool, level);
    return this;
  }

  @Nonnull
  public BlockPressurePlateBase setOpacity(boolean isOpaque) {
    this.isOpaque = isOpaque;
    return this;
  }

  @Nonnull
  public BlockPressurePlateBase setLayer(@Nonnull BlockRenderLayer layer) {
    this.layer = layer;
    return this;
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isOpaqueCube(@Nonnull IBlockState state) {
    return isOpaque;
  }

  public boolean hasCustomModel() {
    return this.hasCustomModel;
  }

  @Override
  @SuppressWarnings("deprecation")
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
      ResourceLocation defaultTex = new ResourceLocation(parent.getRegistryName().getNamespace() + ":blocks/" + parent.getRegistryName().getPath());
      CustomModelLoader.blockmodels
          .put(new ResourceLocation(getRegistryName().getNamespace() + ":models/block/" + name), new CustomModelBlock(getModelClass(), defaultTex, defaultTex));
      CustomModelLoader.itemmodels.put(new ResourceLocation(getRegistryName().getNamespace() + ":" + name + "#handlers"),
          new CustomModelBlock(getModelClass(), defaultTex, defaultTex));
    }
  }

  @Override
  protected int computeRedstoneStrength(@Nonnull World worldIn, @Nonnull BlockPos pos) {
    AxisAlignedBB axisalignedbb = PRESSURE_AABB.offset(pos);
    List<? extends Entity> list;

    switch (this.plateType) {
    case ALL:
      list = worldIn.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb);
      break;
    case MOBS:
      list = worldIn.<Entity>getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
      break;
    case ITEMS:
      list = worldIn.getEntitiesWithinAABB(EntityItem.class, axisalignedbb);
      break;
    case PLAYER:
      list = worldIn.getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);
      break;
    default:
      return 0;
    }

    if (!list.isEmpty()) {
      for (Entity entity : list) {
        if (!entity.doesEntityNotTriggerPressurePlate()) {
          return 15;
        }
      }
    }

    return 0;
  }

  @Nonnull
  protected Class<? extends BakedModelBlock> getModelClass() {
    return BakedModelPressurePlate.class;
  }

  public static enum PressurePlateType {
    ALL, ITEMS, MOBS, PLAYER
  }
}
