package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.mysticallib.util.ItemUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemLifeEssence extends ItemBase {
  public ItemLifeEssence(@Nonnull String name) {
    super(name);
    setMaxDamage(8);
    setMaxStackSize(1);
  }

  @Override
  public String getItemStackDisplayName(ItemStack stack) {
    return super.getItemStackDisplayName(stack);
  }

  @Nullable
  public Class<? extends Entity> getEntityClass (ItemStack stack) {
    ResourceLocation location = getEntityID(stack);
    if (location == null) {
      return null;
    }
    return EntityList.getClass(location);
  }

  @Nullable
  public ResourceLocation getEntityID (ItemStack stack) {
    NBTTagCompound tag = stack.getTagCompound();
    if (tag == null) {
      tag = new NBTTagCompound();
      stack.setTagCompound(tag);
    }

    if (tag.hasKey("id")) {
      return new ResourceLocation(tag.getString("id"));
    }

    return null;
  }
}
