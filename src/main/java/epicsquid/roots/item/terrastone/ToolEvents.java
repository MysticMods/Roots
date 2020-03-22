package epicsquid.roots.item.terrastone;

import com.google.common.collect.Sets;
import epicsquid.mysticalworld.events.LeafHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.config.ToolConfig;
import epicsquid.roots.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = Roots.MODID)
public class ToolEvents {
  private static final Set<Block> SILK_TOUCH_BLOCKS = Sets.newHashSet(Blocks.MYCELIUM, Blocks.GRASS, Blocks.DIRT, Blocks.WEB);
  private static final Item DIRT_ITEM = Item.getItemFromBlock(Blocks.DIRT);

  @SubscribeEvent
  public static void onHarvestDrops(BlockEvent.HarvestDropsEvent event) {
    if (!ToolConfig.ShovelSilkTouch && !ToolConfig.SwordSilkTouch) {
      return;
    }

    final EntityPlayer player = event.getHarvester();
    final IBlockState state = event.getState();

    if (player == null || state == null) {
      return;
    }

    final Item heldItem = player.getHeldItemMainhand().getItem();
    if (heldItem != ModItems.terrastone_sword && heldItem != ModItems.terrastone_shovel) {
      return;
    } else if (heldItem == ModItems.terrastone_shovel && !ToolConfig.ShovelSilkTouch) {
      return;
    } else if (heldItem == ModItems.terrastone_sword && !ToolConfig.SwordSilkTouch) {
      return;
    }

    if (!SILK_TOUCH_BLOCKS.contains(state.getBlock())) {
      return;
    }

    ItemStack output;

    if (state.getBlock() == Blocks.DIRT) {
      if (state.getValue(BlockDirt.VARIANT) != BlockDirt.DirtType.PODZOL) {
        return;
      } else {
        output = new ItemStack(Blocks.DIRT, 1, Blocks.DIRT.getMetaFromState(state));
      }
    } else {
      output = new ItemStack(state.getBlock(), 1);
    }

    final List<ItemStack> drops = event.getDrops();
    final Iterator<ItemStack> iterator = drops.iterator();

    if (player.getHeldItemMainhand().getItem() == ModItems.terrastone_shovel) {
      boolean foundDirt = false;
      while (iterator.hasNext()) {
        final ItemStack stack = iterator.next();
        if (stack.getItem() == DIRT_ITEM) {
          foundDirt = true;
          iterator.remove();
          break;
        }
      }

      if (!foundDirt) {
        return;
      }
    } else if (player.getHeldItemMainhand().getItem() == ModItems.terrastone_sword) {
      boolean foundString = false;
      while (iterator.hasNext()) {
        final ItemStack stack = iterator.next();
        if (stack.getItem() == Items.STRING) {
          foundString = true;
          iterator.remove();
          break;
        }
      }

      if (!foundString) {
        return;
      }
    }

    drops.add(output);
  }

  @SubscribeEvent
  public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
    final BlockPos pos = event.getPos();
    if (pos.getY() == -1) {
      return;
    }

    final EntityPlayer player = event.getEntityPlayer();
    final Item heldItem = player.getHeldItemMainhand().getItem();
    if (heldItem != ModItems.terrastone_pickaxe && heldItem != ModItems.terrastone_sword && heldItem != ModItems.terrastone_axe && heldItem != ModItems.terrastone_hoe) {
      return;
    }

    final IBlockState state = event.getState();
    if (heldItem == ModItems.terrastone_sword && state.getBlock() != Blocks.WEB) {
      return;
    } else if (heldItem == ModItems.terrastone_sword) {
      if (ToolConfig.SwordCobwebBreak) {
        event.setNewSpeed(100f);
      }
      return;
    }

    if (((heldItem == ModItems.terrastone_axe && ToolConfig.AxeLeaves) || (heldItem == ModItems.terrastone_hoe && ToolConfig.HoeSilkTouch)) && LeafHandler.isLeafBlock(state.getBlock())) {
      event.setNewSpeed(100f);
      return;
    }

    final float speed = event.getNewSpeed();

    final float hardness = state.getBlockHardness(player.world, pos);
    final Material mat = state.getMaterial();

    if (hardness < 2.0f || hardness == 2.0f && mat == Material.ROCK) {
      event.setNewSpeed(speed * ToolConfig.PickaxeSoftModifier);
    } else if (hardness >= 50f) {
      event.setNewSpeed(speed * ToolConfig.PickaxeHardModifier);
    }
  }
}
