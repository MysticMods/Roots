package mysticmods.roots.api.modifier;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;

import java.util.Set;

public interface IModifier {
  Set<ResourceKey<Modifier>> getParents();

  Set<ResourceKey<?>> getApplicables();

  boolean canApply (ResourceKey<?> applicable);

  boolean canApply(Holder<?> applicable);
}
