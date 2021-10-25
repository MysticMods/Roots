package epicsquid.mysticallib.item;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.block.BlockCropBase;
import epicsquid.mysticallib.model.CustomModelItem;
import epicsquid.mysticallib.model.CustomModelLoader;
import epicsquid.mysticallib.model.ICustomModeledObject;
import epicsquid.mysticallib.model.IModeledObject;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.EnumPlantType;

public class ItemSeedBase extends ItemSeeds implements IModeledObject, ICustomModeledObject {

  private boolean hasCustomModel;
  private EnumPlantType plantType;
  private BlockCropBase crop;
  private Block base;

  /**
   * Creates a generic seed item with a given farmland and crop block
   *
   * @param name Name of the seed item
   * @param crop Crop block to plant with the seed
   * @param base Block to grow the crop on
   */
  public ItemSeedBase(@Nonnull String name, @Nonnull BlockCropBase crop, @Nonnull Block base) {
    super(crop, base);
    setTranslationKey(name);
    setRegistryName(LibRegistry.getActiveModid(), name);
    this.plantType = crop.getPlantType(null, null);
    this.crop = crop;
    this.base = base;
  }

  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    ItemStack itemstack = player.getHeldItem(hand);
    net.minecraft.block.state.IBlockState state = worldIn.getBlockState(pos);
    if (facing == EnumFacing.UP && player.canPlayerEdit(pos.offset(facing), facing, itemstack) && worldIn.isAirBlock(pos.up()) && (state.getBlock()
        .canSustainPlant(state, worldIn, pos, EnumFacing.UP, this))) {
      worldIn.setBlockState(pos.up(), crop.getDefaultState());

      if (player instanceof EntityPlayerMP) {
        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos.up(), itemstack);
      }

      itemstack.shrink(1);
      return EnumActionResult.SUCCESS;
    } else {
      return EnumActionResult.FAIL;
    }
  }

  /**
   * If set to true, the item won't need a model file defined
   */
  public ItemSeedBase setModelCustom(boolean hasCustomModel) {
    this.hasCustomModel = hasCustomModel;
    return this;
  }

  @Override
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "handlers"));
  }

  @Override
  public void initCustomModel() {
    if (this.hasCustomModel) {
      CustomModelLoader.itemmodels
          .put(getRegistryName(), new CustomModelItem(false, new ResourceLocation(getRegistryName().getNamespace() + ":items/" + getRegistryName().getPath())));
    }
  }

  @Override
  @Nonnull
  public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
    return this.plantType;
  }

}
