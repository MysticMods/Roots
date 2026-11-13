package mysticmods.roots.api.property;

import mysticmods.roots.api.registry.Like;
import net.minecraft.network.chat.Component;

import java.util.function.Function;

public interface LayeredProperty<T> {
  String descriptionId();

  String descriptionTooltipId();

  String descriptionTooltipExtendedId();

  Function<Like<T>, Component[]> componentCreator();
}
