package epicsquid.roots.ritual.conditions;

import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.tileentity.TileEntityBonfire;
import epicsquid.roots.tileentity.TileEntityOffertoryPlate;
import epicsquid.roots.util.RitualUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.List;

public class ConditionValidSummon implements ICondition {
  @Override
  public boolean checkCondition(TileEntityBonfire tile, @Nullable EntityPlayer player) {
    List<TileEntityOffertoryPlate> plates = RitualUtil.getNearbyOfferingPlates(tile.getWorld(), tile.getPos());
    List<ItemStack> plateItems = RitualUtil.getItemsFromNearbyPlates(plates);

    if (ModRecipes.findSummonCreatureEntry(plateItems) != null) {
      return true;
    }

    for (ItemStack stack : plateItems) {
      if (stack.getItem() == ModItems.life_essence) {
        return true;
      }
    }

    return false;
  }

  @Nullable
  @Override
  public ITextComponent failMessage() {
    return new TextComponentTranslation("roots.ritual.condition.invalid_summon");
  }
}
