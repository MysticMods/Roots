package epicsquid.roots.block;

import java.util.List;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.block.BlockBase;
import epicsquid.mysticallib.util.Util;
import epicsquid.mysticalworld.item.ItemKnife;
import epicsquid.roots.entity.grove.EntityGrove;
import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRunestone extends BlockBase {

  public BlockRunestone(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name) {
    super(mat, type, hardness, name);
  }

  @Override
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX,
      float hitY, float hitZ) {
    ItemStack heldItem = playerIn.getHeldItem(hand);
    if(heldItem.getItem() instanceof ItemKnife){
      List<EntityGrove> groves = Util.getEntitiesWithinRadius(worldIn, EntityGrove.class, pos, 6);
      if(groves.size() > 0){
        EntityGrove closestGrove = null;
        double closestDistance = 0;
        for(EntityGrove grove : groves){
          if(closestGrove == null || grove.getPosition().getDistance(pos.getX(), pos.getY(), pos.getZ()) < closestDistance){
            closestGrove = grove;
            closestDistance = grove.getPosition().getDistance(pos.getX(), pos.getY(), pos.getZ());
          }
        }

        switch (closestGrove.getType()){
          case WILD:
            worldIn.setBlockState(pos, ModBlocks.runestone_wild.getDefaultState());
            break;
          case NATURAL:
            worldIn.setBlockState(pos, ModBlocks.runestone_natural.getDefaultState());
            break;
          case FAIRY:
            worldIn.setBlockState(pos, ModBlocks.runestone_fairy.getDefaultState());
            break;
          case FUNGAL:
            worldIn.setBlockState(pos, ModBlocks.runestone_fungal.getDefaultState());
            break;
          case MYSTIC:
            worldIn.setBlockState(pos, ModBlocks.runestone_mystic.getDefaultState());
            break;
          case FORBIDDEN:
            worldIn.setBlockState(pos, ModBlocks.runestone_forbidden.getDefaultState());
            break;
        }
        return true;
      }

    }

    return false;
  }
}
