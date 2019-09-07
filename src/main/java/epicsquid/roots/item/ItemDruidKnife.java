package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemKnifeBase;
import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.RunicCarvingRecipe;
import epicsquid.mysticallib.util.ItemUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
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
  }

  @Override
  @Nonnull
  @SuppressWarnings("deprecation")
  public EnumActionResult onItemUse(@Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
    if (hand == EnumHand.MAIN_HAND) {
      ItemStack offhand = player.getHeldItemOffhand();
      if (!offhand.isEmpty() && HerbRegistry.containsHerbItem(offhand.getItem())) {
        RunicCarvingRecipe recipe = ModRecipes.getRunicCarvingRecipe(world.getBlockState(pos), HerbRegistry.getHerbByItem(offhand.getItem()));
        if (recipe != null) {
          world.setBlockState(pos, recipe.getRuneBlock());

          if (!player.isCreative()) {
            player.getHeldItemMainhand().damageItem(1, player);
          }

          return EnumActionResult.SUCCESS;
        }
      } else {
        // Used to get terramoss from a block of cobble. This can also be done using runic shears.
        IBlockState state = world.getBlockState(pos);
        ItemStack result = GeneralConfig.scrapeResult(state);
        if (!result.isEmpty()) {
          if (!world.isRemote) {
            Block block = ((ItemBlock) result.getItem()).getBlock();
            IBlockState newState = block.getStateFromMeta(result.getMetadata());
            world.setBlockState(pos, newState);
            ItemUtil.spawnItem(world, player.getPosition().add(0, 1, 0), new ItemStack(ModItems.terra_moss));
            if (!player.capabilities.isCreativeMode) {
              player.getHeldItem(hand).damageItem(1, player);
            }
          }
          world.playSound(player, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1f, 1f);
        }
      }
    }
    return EnumActionResult.PASS;
  }
}