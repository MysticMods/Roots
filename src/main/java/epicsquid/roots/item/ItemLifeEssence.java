package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemLifeEssence extends ItemBase {
  public ItemLifeEssence(@Nonnull String name) {
    super(name);
    setMaxDamage(5);
    setMaxStackSize(1);
  }

  @Override
  public String getItemStackDisplayName(ItemStack stack) {
    ResourceLocation id = getEntityID(stack);
    if (id == null) {
      return super.getItemStackDisplayName(stack);
    } else {
      return I18n.format(this.getTranslationKey() + ".entity_name", I18n.format("entity." + EntityList.getTranslationName(id) + ".name"));
    }
  }

  @Nullable
  public Class<? extends Entity> getEntityClass(ItemStack stack) {
    ResourceLocation location = getEntityID(stack);
    if (location == null) {
      return null;
    }
    return EntityList.getClass(location);
  }

  @Nullable
  public ResourceLocation getEntityID(ItemStack stack) {
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

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);

    tooltip.add("");
    int max_uses = stack.getMaxDamage();
    int uses_left = max_uses - stack.getItemDamage();
    tooltip.add(TextFormatting.AQUA + I18n.format("roots.tooltip.life_essence.uses", uses_left, max_uses));
    tooltip.add(TextFormatting.AQUA + I18n.format("roots.tooltip.life_essence"));
  }
}
