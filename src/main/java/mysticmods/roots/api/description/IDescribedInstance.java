package mysticmods.roots.api.description;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public interface IDescribedInstance<T> extends IDescribed {
  String getOrCreateDescriptionId(T instance);

  default String getDescriptionId(T instance) {
    return getOrCreateDescriptionId(instance);
  }

  default MutableComponent getName(T instance) {
    return Component.translatable(this.getDescriptionId(instance));
  }
}
