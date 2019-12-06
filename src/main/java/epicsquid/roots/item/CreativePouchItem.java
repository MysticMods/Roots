package epicsquid.roots.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CreativePouchItem extends PouchItem {


  public CreativePouchItem(Properties properties) {
    super(properties);
  }

  @Override
  public boolean isCreative() {
    return true;
  }


  @Override
  public boolean hasEffect(ItemStack stack) {
    return true;
  }

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
    ItemStack stack = player.getHeldItem(hand);
    return new ActionResult<>(ActionResultType.SUCCESS, stack);
  }

  @Override
  @SuppressWarnings("deprecation")
  public Rarity getRarity(ItemStack stack) {
    return Rarity.EPIC;
  }

  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    tooltip.add(new TranslationTextComponent("roots.tooltip.creative_pouch1").setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)));
    tooltip.add(new StringTextComponent(""));
    tooltip.add(new TranslationTextComponent("roots.tooltip.creative_pouch2").setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)));
  }
}
