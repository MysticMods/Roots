package mysticmods.roots.api.description;

import mysticmods.roots.api.description.store.DescriptionStore;
import mysticmods.roots.api.description.store.IDescriptionStored;
import mysticmods.roots.api.registry.Like;
import net.minecraft.network.chat.Component;

public interface IStoredDescription<BASE extends IDescriptionStored<BASE> & Like<BASE>> extends IExtendedDescribed {
  DescriptionStore<BASE> getDescriptionStore();

  @Override
  default String getOrCreateTooltipExtendedDescriptionId() {
    return getDescriptionStore().getOrCreateTooltipExtendedDescriptionId();
  }

  @Override
  default Component[] getOrCreateDescriptionComponents() {
    return getDescriptionStore().getOrCreateDescriptionComponents();
  }

  @Override
  default String getOrCreateTooltipDescriptionId() {
    return getDescriptionStore().getOrCreateTooltipDescriptionId();
  }

  @Override
  default String getOrCreateDescriptionId() {
    return getDescriptionStore().getOrCreateDescriptionId();
  }

  @Override
  default int getExtendedTooltipLineCount() {
    return getDescriptionStore().getExtendedTooltipLineCount();
  }
}
