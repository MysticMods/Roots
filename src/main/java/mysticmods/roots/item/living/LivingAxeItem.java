package mysticmods.roots.item.living;

import mysticmods.roots.item.ILivingRepair;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class LivingAxeItem extends AxeItem implements ILivingRepair {
  public LivingAxeItem(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
    super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
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