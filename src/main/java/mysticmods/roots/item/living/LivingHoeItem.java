package mysticmods.roots.item.living;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class LivingHoeItem extends HoeItem implements ILivingRepair {
  public LivingHoeItem(Tier pTier, Properties pProperties) {
    super(pTier, pProperties);
  }

  @Override
  public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
    return pToRepair.is(this) && getTier().getRepairIngredient().test(pRepair);
  }

  @Override
  public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
    super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    livingRepair(pStack, pLevel, pEntity, pSlotId, pIsSelected, 40);
  }
}
