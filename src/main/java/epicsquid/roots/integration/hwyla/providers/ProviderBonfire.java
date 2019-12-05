package epicsquid.roots.integration.hwyla.providers;

public class ProviderBonfire { // implements IWailaDataProvider {

  /*@Nonnull
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
  }*/
}
