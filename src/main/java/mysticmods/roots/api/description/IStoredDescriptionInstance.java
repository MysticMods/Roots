package mysticmods.roots.api.description;

import mysticmods.roots.api.description.store.DescriptionInstanceStore;
import mysticmods.roots.api.description.store.IDescriptionStored;
import mysticmods.roots.api.property.LayeredProperty;
import mysticmods.roots.api.property.WithLayeredProperties;
import mysticmods.roots.api.registry.Like;
import net.minecraft.network.chat.Component;

public interface IStoredDescriptionInstance<BASE extends IDescriptionStored<BASE> & WithLayeredProperties<PROPERTY, BASE, INSTANCE> & Like<BASE>, INSTANCE extends Like<BASE>, PROPERTY extends LayeredProperty<BASE>> extends IStoredDescription<BASE>, IExtendedDescribedInstance<INSTANCE> {
  @Override
  DescriptionInstanceStore<BASE, INSTANCE, PROPERTY> getDescriptionStore();

  @Override
  default String getOrCreateTooltipExtendedDescriptionId(INSTANCE instance) {
    return getDescriptionStore().getOrCreateTooltipExtendedDescriptionId(instance);
  }

  @Override
  default String getOrCreateTooltipDescriptionId(INSTANCE instance) {
    return getDescriptionStore().getOrCreateTooltipDescriptionId(instance);
  }

  @Override
  default Component[] getOrCreateDescriptionComponents(INSTANCE instance) {
    return getDescriptionStore().getOrCreateDescriptionComponents(instance);
  }

  @Override
  default String getOrCreateDescriptionId(INSTANCE instance) {
    return getDescriptionStore().getOrCreateDescriptionId(instance);
  }
}
