package epicsquid.mysticallib.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemUtil {
  public static boolean equalWithoutSize(ItemStack item1, ItemStack item2) {
    if (item1.getItem() != item2.getItem()) {
      return false;
    } else if (item1.getItemDamage() != item2.getItemDamage()) {
      return false;
    } else if (item1.getTagCompound() == null && item2.getTagCompound() != null) {
      return false;
    } else {
      return (item1.getTagCompound() == null || item1.getTagCompound().equals(item2.getTagCompound())) && item1.areCapsCompatible(item2);
    }
  }

  public static boolean equalWithoutDamage(ItemStack item1, ItemStack item2) {
    if (item1.getItem() != item2.getItem()) {
      return false;
    } else if (item1.getCount() != item2.getCount()) {
      return false;
    } else if (item1.getTagCompound() == null && item2.getTagCompound() != null) {
      return false;
    } else {
      return (item1.getTagCompound() == null || item1.getTagCompound().equals(item2.getTagCompound())) && item1.areCapsCompatible(item2);
    }
  }

  public static boolean equalWithoutDamageOrNBT(ItemStack item1, ItemStack item2) {
    if (item1.getItem() != item2.getItem()) {
      return false;
    } else {
      return item1.getCount() == item2.getCount();
    }
  }

  public static EntityItem spawnItem(World world, BlockPos pos, ItemStack stack) {
    return spawnItem(world, pos, stack, -1);
  }

  public static EntityItem spawnItem(World world, BlockPos pos, ItemStack stack, float hoverStart) {
    return spawnItem(world, pos, stack, true, -1, hoverStart);
  }

  public static EntityItem spawnItem(World world, BlockPos pos, ItemStack stack, int ticks) {
    return spawnItem(world, pos, stack, true, ticks, -1);
  }

  public static EntityItem spawnItem(World world, BlockPos pos, ItemStack stack, boolean offset) {
    return spawnItem(world, pos, stack, offset, -1, -1);
  }

  public static EntityItem spawnItem(World world, BlockPos pos, ItemStack stack, boolean offset, int ticks, float hoverStart) {
    return spawnItem(world, pos.getX(), pos.getY(), pos.getZ(), offset, stack, ticks, hoverStart);
  }

  public static EntityItem spawnItem(World world, double x, double y, double z, boolean offset, ItemStack stack, int ticks, float hoverStart) {
    if (offset) {
      x += 0.5;
      y += 0.5;
      z += 0.5;
    }
    EntityItem item = new EntityItem(world, x, y, z, stack);
    if (ticks != -1) {
      item.setPickupDelay(ticks);
    }
    if (hoverStart != -1) {
      item.hoverStart = hoverStart;
    }
    return spawnItem(world, item);
  }

  public static EntityItem spawnItem(World world, EntityItem item) {
    item.motionZ = 0;
    item.motionX = 0;
    item.motionY = 0;
    world.spawnEntity(item);
    return item;
  }

  public static ItemStack stackFromState (IBlockState state) {
    Block block = state.getBlock();
    Item item = Item.getItemFromBlock(block);
    int meta = block.getMetaFromState(state);
    return new ItemStack(item, 1, meta);
  }

  @Nullable
  @SuppressWarnings("deprecation")
  public static IBlockState stateFromStack (ItemStack stack) {
    Item item = stack.getItem();
    if (!(item instanceof ItemBlock)) return null;

    Block block = ((ItemBlock) item).getBlock();
    return block.getStateFromMeta(stack.getMetadata());
  }

  public static ItemStack stackFromString (String[] parts) {
    ResourceLocation loc = null;
    int meta = 0;
    if (parts.length == 1) {
      loc = new ResourceLocation("minecraft", parts[0]);
    } else if (parts.length >= 2) {
      loc = new ResourceLocation(parts[0], parts[1]);
    }
    if (parts.length >= 3) {
      try {
        meta = Integer.parseInt(parts[2]);
      } catch (NumberFormatException ignored) {
      }
    }
    if (loc == null) {
      return ItemStack.EMPTY;
    }
    Item item = ForgeRegistries.ITEMS.getValue(loc);
    if (item == null) {
      return ItemStack.EMPTY;
    }

    return new ItemStack(item, 1, meta);
  }

  @SideOnly(Side.CLIENT)
  public static boolean shouldDisplayMore(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn, String shiftForMore, TextFormatting color) {
    if (!GuiScreen.isShiftKeyDown()) {
      tooltip.add("");
      tooltip.add(color + I18n.format(shiftForMore));
      return false;
    } else {
      tooltip.add("");
    }

    return true;
  }

  public static NBTTagCompound getOrCreateTag (ItemStack stack) {
    NBTTagCompound tag = stack.getTagCompound();
    if (tag == null) {
      tag = new NBTTagCompound();
      stack.setTagCompound(tag);
    }
    return tag;
  }

  public static List<ItemStack> transformContainers (List<ItemStack> items) {
      List<ItemStack> result = new ArrayList<>();
      for (ItemStack stack : items) {
        ItemStack container = ForgeHooks.getContainerItem(stack);
        if (!container.isEmpty()) {
          result.add(container);
        }
      }
      return result;
  }
}
