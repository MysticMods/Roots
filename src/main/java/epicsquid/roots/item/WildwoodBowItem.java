package epicsquid.roots.item;

import epicsquid.roots.init.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.world.World;

public class WildwoodBowItem extends BowItem implements ILivingRepair {
  public WildwoodBowItem(Properties builder) {
    super(builder);
  }

  // How to properly handle bows
/*    this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
      @Override
      @OnlyIn(Dist.CLIENT)
      public float apply(ItemStack stack, @Nullable World worldIn, @Nullable LivingEntity entityIn) {
        if (entityIn == null) {
          return 0.0F;
        } else {
          return entityIn.getActiveItemStack().getItem() != WildwoodBowItem.this ? 0.0F : (float) (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F;
        }
      }
    });
    this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter() {
      @Override
      @OnlyIn(Dist.CLIENT)
      public float apply(ItemStack stack, @Nullable World worldIn, @Nullable LivingEntity entityIn) {
        return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
      }
    });*/

  @Override
  @SuppressWarnings("deprecation")
  public Rarity getRarity(ItemStack stack) {
    return Rarity.RARE;
  }

  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    update(stack, worldIn, entityIn, itemSlot, isSelected);
    super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return toRepair.getItem() == this && repair.getItem() == ModItems.bark_wildwood;
  }
}
