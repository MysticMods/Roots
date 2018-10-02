package epicsquid.roots.util;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;

public class Misc {
  public static Random random = new Random();

  public static EnumFacing getOppositeHorizontalFace(EnumFacing face) {
    if (face == EnumFacing.DOWN) {
      return EnumFacing.DOWN;
    } else if (face == EnumFacing.UP) {
      return EnumFacing.UP;
    } else {
      return face.getOpposite();
    }
  }

  public static int intColor(int r, int g, int b) {
    return (r * 65536 + g * 256 + b);
  }

  public static boolean matchOreDict(ItemStack stack1, ItemStack stack2) {
    int[] keys1 = OreDictionary.getOreIDs(stack1);
    int[] keys2 = OreDictionary.getOreIDs(stack2);
    for (int i = 0; i < keys1.length; i++) {
      for (int j = 0; j < keys2.length; j++) {
        if (keys1[i] == keys2[j]) {
          return true;
        }
      }
    }
    return false;
  }

  public static float yawDegreesBetweenPoints(double posX, double posY, double posZ, double posX2, double posY2, double posZ2) {
    float f = (float) ((180.0f * Math.atan2(posX2 - posX, posZ2 - posZ)) / (float) Math.PI);
    return f;
  }

  public static float pitchDegreesBetweenPoints(double posX, double posY, double posZ, double posX2, double posY2, double posZ2) {
    return (float) Math.toDegrees(Math.atan2(posY2 - posY, Math.sqrt((posX2 - posX) * (posX2 - posX) + (posZ2 - posZ) * (posZ2 - posZ))));
  }

  @SideOnly(Side.CLIENT)
  public static ArrayList<String> getLines(String s, int lineWidth) {
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> words = new ArrayList<>();
    String temp = "";
    int counter = 0;
    for (int i = 0; i < s.length(); i++) {
      temp += s.charAt(i);
      if (s.charAt(i) == ' ') {
        words.add(temp);
        temp = "";
      }
    }
    words.add(temp);
    temp = "";
    for (int i = 0; i < words.size(); i++) {
      counter += Minecraft.getMinecraft().fontRenderer.getStringWidth(words.get(i));
      if (counter > lineWidth) {
        list.add(temp);
        temp = words.get(i);
        counter = Minecraft.getMinecraft().fontRenderer.getStringWidth(words.get(i));
      } else {
        temp += words.get(i);
      }
    }
    list.add(temp);
    return list;
  }

  public static EnumFacing getOppositeFace(EnumFacing face) {
    if (face == EnumFacing.DOWN) {
      return EnumFacing.UP;
    } else if (face == EnumFacing.UP) {
      return EnumFacing.DOWN;
    } else {
      return face.getOpposite();
    }
  }

  public static void spawnInventoryInWorld(World world, double x, double y, double z, IItemHandler inventory) {
    if (inventory != null && !world.isRemote) {
      for (int i = 0; i < inventory.getSlots(); i++) {
        if (inventory.getStackInSlot(i) != ItemStack.EMPTY) {
          world.spawnEntity(new EntityItem(world, x, y, z, inventory.getStackInSlot(i)));
        }
      }
    }
  }
}