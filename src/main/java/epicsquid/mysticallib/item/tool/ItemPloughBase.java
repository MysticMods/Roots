package epicsquid.mysticallib.item.tool;

import com.google.common.collect.Sets;
import epicsquid.mysticallib.item.ItemHoeBase;
import epicsquid.mysticallib.types.OneTimeSupplier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public abstract class ItemPloughBase extends ItemHoeBase implements IEffectiveTool, ILimitAxis, IBlacklistingTool {
  public static Set<Block> EFFECTIVE_BLOCKS = Sets.newHashSet(Blocks.GRASS, Blocks.GRASS_PATH, Blocks.DIRT, Blocks.FARMLAND);

  public ItemPloughBase(ToolMaterial material, String name, int toolLevel, int maxDamage, Supplier<Ingredient> repair) {
    super(material, name, toolLevel, maxDamage, repair);
    setHarvestLevel("shovel", material.getHarvestLevel());
  }

  @Override
  public int getWidth(ItemStack stack) {
    return 3;
  }

  @SuppressWarnings("incomplete-switch")
  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos origin, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    ItemStack itemstack = player.getHeldItem(hand);

    // TODO: HAndle Sneaking

    boolean failed = false;
    int width = getWidth(itemstack);
    if (width % 2 == 0) {
      width /= 2;
    } else {
      width = (width - 1) / 2;
    }

    int uses = 0;

    for (int x = -width; x < width + 1; x++) {
      for (int z = -width; z < width + 1; z++) {
        BlockPos pos = origin.add(x, 0, z);

        if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack)) {
          failed = true;
        } else {
          failed = false;
          int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(itemstack, player, worldIn, pos);
          if (hook != 0) {
            if (hook <= 0) {
              failed = true;
            }
          } else {
            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();
            IBlockState newState = Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 7);

            if (facing != EnumFacing.DOWN && worldIn.isAirBlock(pos.up())) {
              if (block == Blocks.GRASS || block == Blocks.GRASS_PATH) {
                if (!worldIn.isRemote) {
                  worldIn.setBlockState(pos, newState, 11);
                  worldIn.notifyBlockUpdate(pos, newState, newState, 8);
                }
                uses++;
              } else if (block == Blocks.FARMLAND && iblockstate.getValue(BlockFarmland.MOISTURE) < 7) {
                if (!worldIn.isRemote) {
                  worldIn.setBlockState(pos, newState, 11);
                  worldIn.notifyBlockUpdate(pos, newState, newState, 8);
                }
                uses++;
              } else if (block == Blocks.DIRT) {
                switch (iblockstate.getValue(BlockDirt.VARIANT)) {
                  case DIRT:
                    if (!worldIn.isRemote) {
                      worldIn.setBlockState(pos, newState, 11);
                      worldIn.notifyBlockUpdate(pos, newState, newState, 8);
                    }
                    uses++;
                    break;
                  case COARSE_DIRT:
                    if (!worldIn.isRemote) {
                      newState = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT);
                      worldIn.setBlockState(pos, newState, 11);
                      worldIn.notifyBlockUpdate(pos, newState, newState, 8);
                    }
                    uses++;
                  default:
                    break;
                }
              }
            }
          }
        }
      }
    }
    if (uses > 0 && !worldIn.isRemote) {
      worldIn.playSound(null, origin, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
      itemstack.damageItem(3 + itemRand.nextInt(Math.max(1, uses - 3)), player);
    }
    if (failed || uses == 0) {
      return EnumActionResult.FAIL;
    } else {
      return EnumActionResult.PASS;
    }
  }

  private static Set<Material> EFFECTIVE_MATERIALS = Sets.newHashSet(Material.GROUND);

  @Override
  public Set<Material> getEffectiveMaterials() {
    return EFFECTIVE_MATERIALS;
  }

  @Override
  public boolean displayBreak() {
    return false;
  }

  private static Set<EnumFacing.Axis> AXES_LIMIT = Sets.newHashSet(EnumFacing.Axis.Y);

  @Override
  public Set<EnumFacing.Axis> getLimits() {
    return AXES_LIMIT;
  }
}
