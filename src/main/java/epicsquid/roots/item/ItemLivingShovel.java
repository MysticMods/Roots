package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemShovelBase;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemLivingShovel extends ItemShovelBase implements ILivingRepair {
  public ItemLivingShovel(ToolMaterial material, String name) {
    super(material, name, material.getHarvestLevel(), material.getMaxUses(), material.getEnchantability());
  }

  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    update(stack, worldIn, entityIn, itemSlot, isSelected);
    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
  }

  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    ItemStack stack = player.getHeldItem(hand);
    Block block = worldIn.getBlockState(pos).getBlock();

    if(facing != EnumFacing.DOWN && worldIn.isAirBlock(pos.up()) && (block == Blocks.GRASS || block == Blocks.DIRT)){
      if(!worldIn.isRemote) {
        worldIn.playSound(null, pos, Blocks.GRASS_PATH.getSoundType().getStepSound(), SoundCategory.BLOCKS,1F,1F);
        worldIn.setBlockState(pos, Blocks.GRASS_PATH.getDefaultState());
        if(!player.capabilities.isCreativeMode){
          stack.damageItem(1,player);
        }
      }
      return EnumActionResult.SUCCESS;
    }

    return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
  }
}
