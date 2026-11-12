package mysticmods.roots.api.tooltip;

import mysticmods.roots.api.IKeyed;
import mysticmods.roots.api.Like;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.property.LayeredProperty;
import mysticmods.roots.api.property.WithLayeredProperties;
import mysticmods.roots.api.registry.GroupId;
import mysticmods.roots.api.registry.IExtendedDescribed;
import mysticmods.roots.api.registry.IExtendedDescribedInstance;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiConsumer;

public class DescriptionStore<BASE extends IKeyed<BASE> & Like<BASE>> implements IExtendedDescribed {
  protected String descriptionId;
  protected String descriptionTooltipExtendedId;
  protected String descriptionTooltipId;
  protected Component[] extendedDescription = null;

  protected final String generatedTooltipTemplate;
  protected final String[] generatedExtendedTooltipTemplate;
  protected final List<BasePropertyFormatter<?, ?>> formatters;

  protected final BASE base;
  @Nullable
  protected final GroupId group;

  public DescriptionStore(BASE base, String generatedTooltipTemplate, String[] generatedExtendedTooltipTemplate, List<BasePropertyFormatter<?, ?>> formatters) {
    this(base, generatedTooltipTemplate, generatedExtendedTooltipTemplate, formatters, null);
  }

  public DescriptionStore(BASE base, String generatedTooltipTemplate, String[] generatedExtendedTooltipTemplate, List<BasePropertyFormatter<?, ?>> formatters, @Nullable GroupId group) {
    this.generatedTooltipTemplate = generatedTooltipTemplate;
    this.generatedExtendedTooltipTemplate = generatedExtendedTooltipTemplate;
    this.formatters = formatters;
    this.base = base;
    this.group = group;
  }


  @Override
  public String getOrCreateTooltipExtendedDescriptionId() {
    if (this.descriptionTooltipExtendedId == null) {
      this.descriptionTooltipExtendedId = getOrCreateTooltipDescriptionId() + ".extended";
    }

    return this.descriptionTooltipExtendedId;
  }

  @Override
  public Component[] getOrCreateDescriptionComponents() {
    if (this.extendedDescription == null) {
      if (this.formatters.isEmpty()) {
        this.extendedDescription = new Component[0];
      } else {
        this.extendedDescription = base.getComponentBuilder().apply(base, this.formatters);
      }
    }

    return this.extendedDescription;
  }

  @Override
  public String getOrCreateTooltipDescriptionId() {
    if (this.descriptionTooltipId == null) {
      if (this.group != null && this.group.useGroupDescription()) {
        this.descriptionTooltipId = this.group.createDescriptionId(base.getSignifier() + "_group", base.getKey()) + ".description";
      } else {
        this.descriptionTooltipId = getOrCreateDescriptionId() + ".description";
      }
    }

    return this.descriptionTooltipId;
  }

  @Override
  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId(base.getSignifier(), base.getKey().location());
    }

    return this.descriptionId;
  }

  @Override
  public int getExtendedTooltipLineCount() {
    return this.generatedExtendedTooltipTemplate.length;
  }

  public void handleTooltip(BiConsumer<String, String> strings) {
    if (this.generatedTooltipTemplate.isEmpty() || this.generatedExtendedTooltipTemplate.length == 0) {
      RootsAPI.LOG.info("Skipped {} as no generated tooltips!", this.base);
      return;
    }
    strings.accept(this.getOrCreateTooltipDescriptionId(), this.generatedTooltipTemplate);

    int count = getExtendedTooltipLineCount();
    if (count == 1) {
      strings.accept(this.getOrCreateTooltipExtendedDescriptionId(), this.generatedExtendedTooltipTemplate[0]);
    } else {
      for (int i = 0; i < count; i++) {
        strings.accept(this.getOrCreateTooltipExtendedDescriptionId() + "_" + i, this.generatedExtendedTooltipTemplate[i]);
      }
    }
  }

  public void reset() {
    this.extendedDescription = null;
  }
}
