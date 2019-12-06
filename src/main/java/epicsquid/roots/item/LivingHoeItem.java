package epicsquid.roots.item;

import epicsquid.roots.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;
import java.util.List;

public class LivingHoeItem extends HoeItem implements ILivingRepair {

  public LivingHoeItem(IItemTier tier, float attackSpeedIn, Item.Properties builder) {
    super(tier, attackSpeedIn, builder);
    // material, name, 3, 192, 22);
  }

  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    update(stack, worldIn, entityIn, itemSlot, isSelected);
    super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
  }

  @Override
  public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
    if (entityLiving instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) entityLiving;
      Block block = state.getBlock();
      if (block instanceof IPlantable) {
        for (int x = -2; x < 3; x++) {
          for (int z = -2; z < 3; z++) {
            BlockPos nPos = pos.add(x, 0, z);
            BlockState state2 = worldIn.getBlockState(nPos);
            block = state2.getBlock();
            if (!(block instanceof IPlantable)) continue;
            block.harvestBlock(worldIn, player, nPos, state2, worldIn.getTileEntity(nPos), stack);
            worldIn.removeBlock(nPos, false);
            // Honestly I don't know what this does
            // TODO: Find out what this did
            /*            worldIn.playEvent(2001, nPos, Block.getIdFromBlock(block) + (block.getMetaFromState(state2) << 12));*/
          }
        }
      }
    }

    return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
  }

  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);
    tooltip.add(new StringTextComponent(""));
    // TODO: Translate this
    tooltip.add(new StringTextComponent("Breaks plants in a large radius.").setStyle(new Style().setColor(TextFormatting.GREEN)));
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return toRepair.getItem() == this && ModItems.barks.contains(repair.getItem());
  }
}
