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
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
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
  public ActionResultType onItemUse(PlayerEntity player, World worldIn, BlockPos pos, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
    ItemStack itemstack = player.getHeldItem(hand);
    BlockState state = worldIn.getBlockState(pos);
    if (facing == Direction.UP && player.canPlayerEdit(pos.offset(facing), facing, itemstack) && worldIn.isAirBlock(pos.up()) && (state.getBlock()
        .canSustainPlant(state, worldIn, pos, Direction.UP, this))) {
      worldIn.setBlockState(pos.up(), crop.getDefaultState());

      if (player instanceof ServerPlayerEntity) {
        CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos.up(), itemstack);
      }

      itemstack.shrink(1);
      return ActionResultType.SUCCESS;
    } else {
      return ActionResultType.FAIL;
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
