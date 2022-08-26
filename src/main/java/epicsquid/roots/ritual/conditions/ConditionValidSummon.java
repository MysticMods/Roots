package epicsquid.roots.ritual.conditions;

import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.tileentity.TileEntityCatalystPlate;
import epicsquid.roots.tileentity.TileEntityPyre;
import epicsquid.roots.util.RitualUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.List;

public class ConditionValidSummon implements ICondition {
  @Override
  public boolean checkCondition(TileEntityPyre tile, @Nullable EntityPlayer player) {
    List<TileEntityCatalystPlate> plates = RitualUtil.getNearbyCatalystPlates(tile.getWorld(), tile.getPos());
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
