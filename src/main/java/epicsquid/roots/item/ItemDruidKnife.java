package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemKnifeBase;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.block.BlockPyre;
import epicsquid.roots.config.MossConfig;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.item.dispenser.DispenseKnife;
import epicsquid.roots.util.RitualUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

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
      IBlockState state = world.getBlockState(pos);
      Block block = state.getBlock();
      if (player.isSneaking() && block == ModBlocks.reinforced_pyre || block == ModBlocks.pyre) {
        if (block.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ)) {
          return EnumActionResult.SUCCESS;
        }
      }
      if (!MossConfig.getBlacklistDimensions().contains(world.provider.getDimension())) {
        // Used to get terramoss from a block of cobble. This can also be done using runic shears.
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
          return EnumActionResult.SUCCESS;
        }
      }
      IBlockState result = RitualUtil.RunedWoodType.matchesAny(state);
      ItemStack offHand = player.getHeldItemOffhand();
      if (result != null && offHand.getItem() == ModItems.wildroot) {
        if (!world.isRemote) {
          world.setBlockState(pos, result);
          world.scheduleBlockUpdate(pos, result.getBlock(), 1, result.getBlock().tickRate(world));
          if (!player.capabilities.isCreativeMode) {
            player.getHeldItem(hand).damageItem(1, player);
            offHand.shrink(1);
            player.setHeldItem(EnumHand.OFF_HAND, offHand.isEmpty() ? ItemStack.EMPTY : offHand);
          }
          world.playSound(null, pos, SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 1f, 1f);
        }
        return EnumActionResult.SUCCESS;
      }
    }
    return EnumActionResult.PASS;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);

    tooltip.add("");
    tooltip.add(I18n.format("roots.tooltip.knife1", TextFormatting.GOLD + I18n.format("roots.tooltip.logs") + TextFormatting.RESET, TextFormatting.GOLD + I18n.format("roots.tooltip.bark") + TextFormatting.RESET));
    tooltip.add(I18n.format("roots.tooltip.knife2", TextFormatting.DARK_GREEN + I18n.format("roots.tooltip.eligible") + TextFormatting.RESET, TextFormatting.GREEN + "" + TextFormatting.BOLD + I18n.format("roots.tooltip.terra_moss") + TextFormatting.RESET));

    if (ItemUtil.shouldDisplayMore(stack, worldIn, tooltip, flagIn, "roots.tooltip.shift", TextFormatting.DARK_GRAY)) {
      tooltip.add(I18n.format("roots.tooltip.knife_dispenser"));
    }
  }
}