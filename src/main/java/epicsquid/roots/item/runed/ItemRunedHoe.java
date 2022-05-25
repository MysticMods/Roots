package epicsquid.roots.item.runed;

import epicsquid.mysticallib.item.tool.ItemPloughBase;
import epicsquid.roots.config.ToolConfig;
import epicsquid.roots.item.ILivingRepair;
import epicsquid.roots.recipe.ingredient.RootsIngredients;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class ItemRunedHoe extends ItemPloughBase implements ILivingRepair {

  public ItemRunedHoe(ToolMaterial material, String name) {
    super(material, name, 3, 1992, () -> Ingredient.EMPTY);
  }

  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    update(stack, worldIn, entityIn, itemSlot, isSelected, 90);
    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
  }

  @Override
  public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
    // TODO: Some better effect
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
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return toRepair.getItem() == this && RootsIngredients.RUNED_OBSIDIAN.test(repair);
  }


  @Override
  public Set<Block> getBlockBlacklist() {
    return ToolConfig.getRunicBlockBlacklist();
  }
}
