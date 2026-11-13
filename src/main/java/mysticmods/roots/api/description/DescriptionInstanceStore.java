package mysticmods.roots.api.description;

import mysticmods.roots.api.registry.Like;
import mysticmods.roots.api.property.LayeredProperty;
import mysticmods.roots.api.property.WithLayeredProperties;
import mysticmods.roots.api.registry.GroupId;
import mysticmods.roots.api.tooltip.BasePropertyFormatter;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DescriptionInstanceStore<BASE extends IKeyed<BASE> & WithLayeredProperties<PROPERTY, BASE, INSTANCE> & Like<BASE>, INSTANCE extends Like<BASE>, PROPERTY extends LayeredProperty<BASE>> extends DescriptionStore<BASE> implements IExtendedDescribedInstance<INSTANCE> {
  public DescriptionInstanceStore(BASE base, String generatedTooltipTemplate, String[] generatedExtendedTooltipTemplate, List<BasePropertyFormatter<?, ?>> formatters) {
    super(base, generatedTooltipTemplate, generatedExtendedTooltipTemplate, formatters);
  }

  public DescriptionInstanceStore(BASE base, String generatedTooltipTemplate, String[] generatedExtendedTooltipTemplate, List<BasePropertyFormatter<?, ?>> formatters, @Nullable GroupId group) {
    super(base, generatedTooltipTemplate, generatedExtendedTooltipTemplate, formatters, group);
  }

  @Override
  public String getOrCreateTooltipExtendedDescriptionId(INSTANCE instance) {
    return base.getLayer(instance).descriptionTooltipExtendedId();
  }

  @Override
  public String getOrCreateTooltipDescriptionId(INSTANCE instance) {
    return base.getLayer(instance).descriptionTooltipId();
  }

  @Override
  public Component[] getOrCreateDescriptionComponents(INSTANCE instance) {
    return base.getLayer(instance).componentCreator().apply(instance.asLike());
  }

  @Override
  public String getOrCreateDescriptionId(INSTANCE instance) {
    return base.getLayer(instance).descriptionId();
  }
}
