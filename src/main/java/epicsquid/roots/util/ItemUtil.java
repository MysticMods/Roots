package epicsquid.roots.util;

import net.minecraft.item.ItemStack;

public class ItemUtil {
     public static boolean equalWithoutSize (ItemStack item1, ItemStack item2)
    {
        if (item1.getItem() != item2.getItem())
        {
            return false;
        }
        else if (item1.getItemDamage() != item2.getItemDamage())
        {
            return false;
        }
        else if (item1.getTagCompound() == null && item2.getTagCompound() != null)
        {
            return false;
        }
        else
        {
            return (item1.getTagCompound() == null || item1.getTagCompound().equals(item2.getTagCompound())) && item1.areCapsCompatible(item2);
        }
    }
}
