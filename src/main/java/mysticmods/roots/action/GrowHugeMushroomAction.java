package mysticmods.roots.action;

import mysticmods.roots.api.action.GroveAction;
import mysticmods.roots.api.action.GroveContext;

import java.util.Set;

public class GrowHugeMushroomAction implements GroveAction {
  @Override
  public boolean test(GroveContext context) {
    return true;
  }

  @Override
  public Set<GroveContext.Parameter> getUsedParameters() {
    return CropGrowthAction.Context.PARAMETERS;
  }
}
