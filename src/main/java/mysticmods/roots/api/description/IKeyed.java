package mysticmods.roots.api.description;

import mysticmods.roots.api.registry.Like;
import mysticmods.roots.api.tooltip.BasePropertyFormatter;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;

import java.util.List;
import java.util.function.BiFunction;

public interface IKeyed<B> {
  ResourceKey<B> getKey();

  String getSignifier();

  BiFunction<Like<B>, List<BasePropertyFormatter<?, ?>>, Component[]> getComponentBuilder ();
}
