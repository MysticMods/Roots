package epicsquid.roots.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemCreativePouch extends ItemPouch {
  public ItemCreativePouch(@Nonnull String name) {
    super(name);
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
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    tooltip.add(TextFormatting.DARK_PURPLE + I18n.format("roots.tooltip.creative_pouch1"));
    tooltip.add("");
    tooltip.add(TextFormatting.DARK_PURPLE + I18n.format("roots.tooltip.creative_pouch2"));
  }
}
