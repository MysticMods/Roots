package epicsquid.roots.item;

import epicsquid.roots.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class LivingShovelItem extends ShovelItem implements ILivingRepair {
  public LivingShovelItem(IItemTier tier, float attackDamageIn, float attackSpeedIn, Item.Properties builder) {
    super(tier, attackDamageIn, attackSpeedIn, builder);
    // TODO: What this was
    //super(material, name, 3, 192, 22);
  }

  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    update(stack, worldIn, entityIn, itemSlot, isSelected);
    super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    /* PlayerEntity player, World
  } worldIn, BlockPos pos, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {*/
    PlayerEntity player = context.getPlayer();
    Hand hand = context.getHand();
    World worldIn = context.getWorld();
    BlockPos pos = context.getPos();
    Direction facing = context.getFace();
    ItemStack stack = player.getHeldItem(hand);
    BlockState state = worldIn.getBlockState(pos);
    Block block = state.getBlock();

    if (facing != Direction.DOWN && worldIn.isAirBlock(pos.up()) && (block == Blocks.GRASS || block == Blocks.DIRT)) {
      if (!worldIn.isRemote) {
        worldIn.playSound(null, pos, Blocks.GRASS_PATH.getSoundType(state, worldIn, pos, player).getStepSound(), SoundCategory.BLOCKS, 1F, 1F);
        worldIn.setBlockState(pos, Blocks.GRASS_PATH.getDefaultState());
        if (!player.isCreative()) {
          stack.damageItem(1, player, (p_220282_1_) -> {
            p_220282_1_.sendBreakAnimation(hand);
          });
        }
      }
      return ActionResultType.SUCCESS;
    }

    return super.onItemUse(context);
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return toRepair.getItem() == this && ModItems.barks.contains(repair.getItem());
  }
}
