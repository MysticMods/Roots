package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemBowBase;
import epicsquid.roots.init.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemWildwoodBow extends ItemBowBase implements ILivingRepair {
  public ItemWildwoodBow(String name) {
    super(name, 920, 30);

    this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
      @SideOnly(Side.CLIENT)
      public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
        if (entityIn == null) {
          return 0.0F;
        } else {
          return entityIn.getActiveItemStack().getItem() != ItemWildwoodBow.this ? 0.0F : (float) (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F;
        }
      }
    });
    this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter() {
      @SideOnly(Side.CLIENT)
      public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
        return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
      }
    });
  }

  @Override
  public EnumRarity getRarity(ItemStack stack) {
    return EnumRarity.RARE;
  }

  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    update(stack, worldIn, entityIn, itemSlot, isSelected, 120);
    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return toRepair.getItem() == this && repair.getItem() == ModItems.bark_wildwood;
  }
}
