package epicsquid.roots.integration.hwyla.providers;

import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.tileentity.TileEntityBonfire;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import java.util.List;

public class ProviderBonfire implements IWailaDataProvider {

  @Nonnull
  @Override
  public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
    TileEntityBonfire te = (TileEntityBonfire) accessor.getTileEntity();
    if (te != null && te.getBurnTime() > 0) {
      int duration;
      if (te.getLastRecipeUsed() != null) {
        ItemStack result = te.getLastRecipeUsed().getResult();
        duration = te.getLastRecipeUsed().getBurnTime();
        tooltip.add(TextFormatting.GOLD + I18n.format("roots.hud.pyre.recipe", result.getDisplayName()));
      } else if (te.getLastRitualUsed() != null) {
        RitualBase ritual = te.getLastRitualUsed();
        duration = ritual.getDuration();
        tooltip.add(I18n.format("roots.hud.pyre.ritual", ritual.getFormat() + I18n.format("roots.ritual." + ritual.getName() + ".name")));
      } else {
        return tooltip;
      }
      int remaining = duration - (duration - te.getBurnTime());
      tooltip.add(I18n.format("roots.hud.pyre.progress", String.format("%.2f", remaining / 20.0 / 60.0)));
    }
    return tooltip;
  }
}
