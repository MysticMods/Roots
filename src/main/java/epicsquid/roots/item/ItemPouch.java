package epicsquid.roots.item;

import java.util.List;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.api.Herb;
import epicsquid.roots.init.HerbRegistry;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPouch extends ItemBase {
  public static final double capacity = 128.0;

  public ItemPouch(String name) {
    super(name);
    setMaxStackSize(1);
  }

  public static ItemStack createData(ItemStack stack, String plantName, double quantity) {
    if (!stack.hasTagCompound()) {
      stack.setTagCompound(new NBTTagCompound());
    }
    stack.getTagCompound().setString("plant", plantName);
    stack.getTagCompound().setDouble("quantity", quantity);
    return stack;
  }

  public static String getPlantName(ItemStack stack) {
    if (stack.hasTagCompound()) {
      if (stack.getTagCompound().hasKey("plant")) {
        return stack.getTagCompound().getString("plant");
      }
    }
    return null;
  }

  public static Herb getHerb(ItemStack stack) {
    String name = getPlantName(stack);
    if (name != null) {
      return HerbRegistry.getHerbByName(name);
    }
    return null;
  }

  public static double getQuantity(ItemStack stack, String plantName) {
    if (stack.hasTagCompound()) {
      if (stack.getTagCompound().hasKey("quantity")) {
        return stack.getTagCompound().getDouble("quantity");
      }
    }
    return 0.0;
  }

  public static void setQuantity(ItemStack stack, String plantName, double quantity) {
    if (stack.hasTagCompound()) {
      if (stack.getTagCompound().hasKey("quantity")) {
        stack.getTagCompound().setDouble("quantity", Math.min(128.0, quantity));
        if (stack.getTagCompound().getDouble("quantity") <= 0) {
          stack.getTagCompound().removeTag("quantity");
          stack.getTagCompound().removeTag("plant");
        }
      }
    }
  }

  @Override
  public boolean showDurabilityBar(ItemStack stack) {
    if (stack.hasTagCompound()) {
      if (stack.getTagCompound().hasKey("quantity")) {
        return true;
      }
    }
    return false;
  }

  @Override
  public int getRGBDurabilityForDisplay(ItemStack stack) {
    return Util.intColor(96, 255, 96);
  }

  @Override
  public double getDurabilityForDisplay(ItemStack stack) {
    if (stack.hasTagCompound()) {
      if (stack.getTagCompound().hasKey("quantity")) {
        return 1.0 - stack.getTagCompound().getDouble("quantity") / 128.0;
      }
    }
    return 0;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
    if (stack.hasTagCompound()) {
      if (stack.getTagCompound().hasKey("quantity")) {
        tooltip.add(I18n.format( HerbRegistry.getHerbByName(stack.getTagCompound().getString("plant")).getItem().getUnlocalizedName() + ".name") + I18n.format("roots.tooltip.pouch_divider") + (int) Math
            .ceil(stack.getTagCompound().getDouble("quantity")));
      }
    }
  }
}