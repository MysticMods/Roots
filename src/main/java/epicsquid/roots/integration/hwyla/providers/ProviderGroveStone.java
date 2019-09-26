package epicsquid.roots.integration.hwyla.providers;

import epicsquid.roots.block.groves.BlockGroveStone;
import epicsquid.roots.init.ModBlocks;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class ProviderGroveStone implements IWailaDataProvider {
  @Override
  public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
    if (accessor.getBlock() == ModBlocks.grove_stone) {
      if (accessor.getBlockState().getValue(BlockGroveStone.VALID)) {
        tooltip.add(TextFormatting.GREEN + "" + TextFormatting.BOLD + I18n.format("roots.hud.grove_stone.valid"));
      }
    }
    return tooltip;
  }
}
