package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.item.Rarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("deprecation")
public class ItemGlassEye extends ItemBase {
  public ItemGlassEye(@Nonnull String name) {
    super(name);
    setMaxStackSize(64);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    ItemStack stack = playerIn.getHeldItem(handIn);
    if (!worldIn.isRemote) {
      if (playerIn.getActivePotionEffect(Effects.NIGHT_VISION) != null) {
        return ActionResult.newResult(ActionResultType.SUCCESS, stack);
      }
      playerIn.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 15 * 20, 0, false, false));

      if (!playerIn.isCreative()) {
        stack.shrink(1);
      }
    }
    return ActionResult.newResult(ActionResultType.SUCCESS, stack);
  }

  @Override
  public Rarity getRarity(ItemStack stack) {
    return Rarity.UNCOMMON;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);

    tooltip.add("");
    tooltip.add(TextFormatting.YELLOW + I18n.format("roots.glass_eye.tooltip"));
  }
}
