package mysticmods.roots.item;

import mysticmods.roots.inventory.quiver.QuiverMenu;
import mysticmods.roots.util.QuiverUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class QuiverItem extends PouchItem {
  // TODO: Outsource "6"
  public static final int MAX_ARROWS = 64 * 6;

  public QuiverItem(Properties properties) {
    super(QuiverMenu::new, properties);
  }

  @Override
  public boolean isBarVisible(ItemStack stack) {
    return QuiverUtil.countArrows(stack) > 0 || super.isBarVisible(stack);
  }

  @Override
  public int getBarWidth(ItemStack stack) {
    return Math.round(13.0F - (float) (MAX_ARROWS - QuiverUtil.countArrows(stack)) * 13.0F / (float) MAX_ARROWS);
  }

  @Override
  public int getBarColor(ItemStack stack) {
    float stackMaxDamage = (float) MAX_ARROWS;
    float f = Math.max(0.0F, ((float) QuiverUtil.countArrows(stack)) / stackMaxDamage);
    return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
  }
}
