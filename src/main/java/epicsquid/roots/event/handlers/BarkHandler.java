package epicsquid.roots.event.handlers;

import epicsquid.roots.Roots;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Roots.MODID)
@SuppressWarnings("unused")
public class BarkHandler {
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void barkHarvested(BlockEvent.HarvestDropsEvent event) {
    if (event.getHarvester() != null) {
      ItemStack tool = event.getHarvester().getHeldItem(Hand.MAIN_HAND);
/*      // TODO: Handle this with  better stuff
      if (tool.getItem().getToolTypes(tool).contains(ToolType.AXE)) { // getToolClasses(tool).contains("druidKnife")) {
        BlockState blockstate = event.getState();
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
      }*/
    }
  }

  private static int getAdditionalBarkAmount(ItemStack stack) {
    return Math.max(EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack) + 2, EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack) + 2);
  }
}
