package mysticmods.roots.action;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.action.GroveAction;
import mysticmods.roots.api.action.GroveContext;

import java.util.Set;

public class GrowHugeMushroomAction extends GroveAction {
  @Override
  public void log(GroveContext context) {
    RootsAPI.LOG.error("GrowHugeMushroomAction fired by '{}' with new block state '{}'",
        context.player().getName().getString(), context.blockState());
  }

  @Override
  public boolean test(GroveContext context) {
    return true;
  }

  @Override
  public Set<GroveContext.Parameter> getUsedParameters() {
    return CropGrowthAction.Context.PARAMETERS;
  }
}
