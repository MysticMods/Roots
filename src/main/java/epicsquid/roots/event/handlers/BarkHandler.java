package epicsquid.roots.event.handlers;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.BarkRecipe;
import epicsquid.mysticallib.util.ItemUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid= Roots.MODID)
@SuppressWarnings("unused")
public class BarkHandler {
  @SubscribeEvent(priority= EventPriority.HIGHEST)
  public static void barkHarvested(BlockEvent.HarvestDropsEvent event) {
    if (event.getHarvester() != null) {
      ItemStack tool = event.getHarvester().getHeldItem(EnumHand.MAIN_HAND);
      if (tool.getItem().getToolClasses(tool).contains("druidKnife")) {
        IBlockState blockstate = event.getState();
        Block block = blockstate.getBlock();
        BlockPlanks.EnumType type = (block == Blocks.LOG) ?
            blockstate.getValue(BlockOldLog.VARIANT) :
            (block == Blocks.LOG2) ? blockstate.getValue(BlockNewLog.VARIANT) : null;
        BarkRecipe bark;
        if (type == null) {
          bark = ModRecipes.getModdedBarkRecipe(blockstate);
        } else {
          bark = ModRecipes.getVanillaBarkRecipe(type);
        }
        if (bark != null) {
          event.getDrops().clear();
          ItemStack barkStack = bark.getBarkStack(Util.rand.nextInt(getAdditionalBarkAmount(tool)));
          if (!event.getWorld().isRemote) {
            ItemUtil.spawnItem(event.getWorld(), event.getPos(), barkStack);
          }
        }
      }
    }
  }

  private static int getAdditionalBarkAmount(ItemStack stack) {
    return Math.max(EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack) + 2, EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack) + 2);
  }
}
