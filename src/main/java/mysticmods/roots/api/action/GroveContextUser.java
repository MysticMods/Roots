package mysticmods.roots.api.action;

import mysticmods.roots.api.action.parameter.GroveParameter;

import java.util.Set;

public interface GroveContextUser {
  Set<GroveParameter<?>> getUsedParameters();
}
