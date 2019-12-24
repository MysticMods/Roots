package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemKnifeBase;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.config.MossConfig;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.item.dispenser.DispenseKnife;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemDruidKnife extends ItemKnifeBase {

  public ItemDruidKnife(String name, ToolMaterial material) {
    super(name, material);
    ModItems.knives.add(this);

    BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DispenseKnife.getInstance());
  }

  @Override
  @Nonnull
  @SuppressWarnings("deprecation")
  public EnumActionResult onItemUse(@Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
    if (hand == EnumHand.MAIN_HAND) {
      if (!MossConfig.getBlacklistDimensions().contains(world.provider.getDimension())) {
        // Used to get terramoss from a block of cobble. This can also be done using runic shears.
        IBlockState state = world.getBlockState(pos);
        IBlockState result = MossConfig.scrapeResult(state);
        if (result != null) {
          if (!world.isRemote) {
            world.setBlockState(pos, result);
            world.scheduleBlockUpdate(pos, result.getBlock(), 1, result.getBlock().tickRate(world));
            ItemUtil.spawnItem(world, player.getPosition().add(0, 1, 0), new ItemStack(ModItems.terra_moss));
            if (!player.capabilities.isCreativeMode) {
              player.getHeldItem(hand).damageItem(1, player);
            }
          }
          world.playSound(player, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1f, 1f);
        }
      }
      return EnumActionResult.SUCCESS;
    }
    return EnumActionResult.PASS;
  }
}