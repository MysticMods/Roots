package epicsquid.roots.tileentity;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.network.MessageTEUpdate;
import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.tile.TileBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityUnendingBowl extends TileBase {

  @Override
  public boolean activate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand,
      @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
    ItemStack heldItem = player.getHeldItem(hand);
    if (heldItem != ItemStack.EMPTY && heldItem.getCount() > 0) {
      if(heldItem.getItem() == Items.BUCKET){
        player.getHeldItem(hand).shrink(1);
        if (player.getHeldItem(hand).getCount() == 0) {
          player.setHeldItem(hand, ItemStack.EMPTY);
        }
        player.inventory.addItemStackToInventory(new ItemStack(Items.WATER_BUCKET));
      } else if (heldItem.getItem() == Items.GLASS_BOTTLE) {
        player.getHeldItem(hand).shrink(1);
        if (player.getHeldItem(hand).getCount() == 0) {
          player.setHeldItem(hand, ItemStack.EMPTY);
        }
        player.inventory.addItemStackToInventory(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER));
      }
    }
    return true;
  }
}
