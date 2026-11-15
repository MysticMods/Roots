package mysticmods.roots.item.living;

import mysticmods.roots.util.TooltipUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class LivingSwordItem extends SwordItem implements ILivingRepair {
  public LivingSwordItem(Tier pTier, Properties pProperties) {
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

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    tooltipComponents.add(TooltipUtil.tooltip(this));
  }
}
