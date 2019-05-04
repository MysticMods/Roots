package epicsquid.roots.item;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.model.CustomModelItem;
import epicsquid.mysticallib.model.CustomModelLoader;
import epicsquid.mysticallib.model.ICustomModeledObject;
import epicsquid.mysticallib.model.IModeledObject;
import epicsquid.mysticallib.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemLivingHoe extends ItemHoe implements IModeledObject, ICustomModeledObject, ILivingRepair {

  private boolean hasCustomModel = false;

  public ItemLivingHoe(ToolMaterial material, String name) {
    super(material);
    setTranslationKey(name);
    setRegistryName(LibRegistry.getActiveModid(), name);
    setHarvestLevel("hoe", 3);
    setMaxDamage(192);
  }

  @Override
  public int getItemEnchantability() {
    return 22;
  }

  public ItemLivingHoe setModelCustom(boolean custom) {
    this.hasCustomModel = custom;
    return this;
  }

  @Override
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "handlers"));
  }

  @Override
  public void initCustomModel() {
    if (this.hasCustomModel) {
      CustomModelLoader.itemmodels.put(getRegistryName(),
          new CustomModelItem(false, new ResourceLocation(getRegistryName().getNamespace() + ":items/" + getRegistryName().getPath())));
    }
  }

  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    update(stack, worldIn, entityIn, itemSlot, isSelected);
    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
  }

  @Override
  public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
    if (entityLiving instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) entityLiving;
      Block block = state.getBlock();
      if (block instanceof IPlantable) {
        for (int x = -2; x < 3; x++) {
          for (int z = -2; z < 3; z++) {
            BlockPos nPos = pos.add(x, 0, z);
            IBlockState state2 = worldIn.getBlockState(nPos);
            block = state2.getBlock();
            if (!(block instanceof IPlantable)) continue;
            block.harvestBlock(worldIn, player, nPos, state2, worldIn.getTileEntity(nPos), stack);
            worldIn.setBlockToAir(nPos);
            // Honestly I don't know what this does
            worldIn.playEvent(2001, nPos, Block.getIdFromBlock(block) + (block.getMetaFromState(state2) << 12));
          }
        }
      }
    }

    return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    tooltip.add("");
    tooltip.add(TextFormatting.GREEN + "Breaks plants in a large radius.");
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }
}
