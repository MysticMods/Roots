package mysticmods.roots.api.grove;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.Color;
import mysticmods.roots.api.RootsItemCallbacks;
import mysticmods.roots.api.condition.GroveType;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.property.IPropertyDataMapped;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.registry.IDataMapInitialize;
import mysticmods.roots.api.registry.IExtendedDescribed;
import mysticmods.roots.api.registry.IStyled;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.tooltip.NumberFormatter;
import mysticmods.roots.api.tooltip.TooltipBuilder;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Grove implements IStyled, IDataMapInitialize<Grove>, IExtendedDescribed, IPropertyDataMapped, TooltipComponent {
  public static final Codec<Grove> CODEC = RootsRegistries.GROVES.byNameCodec();
  public static final StreamCodec<RegistryFriendlyByteBuf, Grove> STREAM_CODEC = ByteBufCodecs.registry(RootsRegistries.Keys.GROVES);

  private Style style;
  private final GroveColors colors;

  private final ReputationRanks defaultReputationRanks = new ReputationRanks(1000, 2000, 3000, 4000);
  private ReputationRanks reputationRanks;

  private final GroveType type;

  private final ResourceKey<Grove> resourceKey;
  private final List<PropertyHolder<?>> allProperties;

  protected String descriptionId;
  protected String descriptionTooltipId;
  protected String descriptionTooltipExtendedId;
  protected Component[] extendedDescription = null;

  protected final String generatedTooltipTemplate;
  protected final String generatedExtendedTooltipTemplate;
  protected final List<NumberFormatter<?>> numberFormatters;

  public Grove(Grove.Properties properties) {
    this.type = properties.groveType;
    this.colors = properties.color;
    this.resourceKey = properties.resourceKey;
    this.generatedTooltipTemplate = properties.simpleTooltip;
    this.generatedExtendedTooltipTemplate = properties.extendedTooltip;
    this.numberFormatters = properties.numberFormatters;
    this.allProperties = properties.allProperties;
  }

  public ResourceKey<Grove> getKey() {
    return resourceKey;
  }

  @Override
  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId("grove", getKey().location());
    }

    return this.descriptionId;
  }

  public ReputationRanks getDefaultRanks() {
    return defaultReputationRanks;
  }

  public ReputationRanks getRanks() {
    if (reputationRanks == null) {
      return getDefaultRanks();
    }
    return reputationRanks;
  }

  public ItemStack getIcon() {
    return RootsItemCallbacks.getItemStack(this);
  }

  public int getColor1() {
    return colors.particles1().rgb();
  }

  public int getColor2() {
    return colors.particles1().rgb();
  }

  public TagKey<Grove> getGroveTag() {
    return type.grove();
  }

  public TagKey<Block> getBlockTag() {
    return type.tag();
  }

  public GroveType getType() {
    return this.type;
  }

  @Override
  public void init(Holder<Grove> holder) {
    this.reputationRanks = holder.getData(DataMaps.GROVE_RANKS);
  }

  @Override
  @Nullable
  public TextColor getTextColor() {
    return colors.text().text();
  }

  @Override
  public Style getOrCreateStyle() {
    if (style == null) {
      TextColor color = getTextColor();
      if (color != null) {
        style = Style.EMPTY.withColor(color).withBold(isBold());
      } else {
        style = Style.EMPTY.withBold(isBold());
      }
    }
    return style;
  }

  public Holder<Grove> builtInRegistryHolder() {
    return RootsRegistries.GROVES.wrapAsHolder(this);
  }

  public boolean is(ResourceLocation location) {
    return builtInRegistryHolder().is(location);
  }

  public boolean is(ResourceKey<Grove> key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean is(Predicate<ResourceKey<Grove>> predicate) {
    return builtInRegistryHolder().is(predicate);
  }

  public boolean is(TagKey<Grove> tag) {
    return builtInRegistryHolder().is(tag);
  }

  @Override
  public String getOrCreateTooltipExtendedDescriptionId() {
    if (this.descriptionTooltipExtendedId == null) {
      this.descriptionTooltipExtendedId = getOrCreateTooltipDescriptionId() + ".extended";
    }

    return this.descriptionTooltipExtendedId;
  }

  public static Component[] componentsFromFormatter(Grove grove, List<NumberFormatter<?>> formatters) {
    List<Component> componentList = new ArrayList<>();
    for (NumberFormatter<?> entry : formatters) {
      entry.grove(grove);
      componentList.addAll(entry.build());
    }
    return componentList.toArray(Component[]::new);
  }

  @Override
  public Component[] getOrCreateDescriptionComponents() {
    if (extendedDescription == null) {
      if (this.numberFormatters.isEmpty()) {
        return new Component[0];
      } else {
        this.extendedDescription = componentsFromFormatter(this, this.numberFormatters);
      }
    }

    return this.extendedDescription;
  }

  @Override
  public String getOrCreateTooltipDescriptionId() {
    if (this.descriptionTooltipId == null) {
      this.descriptionTooltipId = getOrCreateDescriptionId() + ".description";
    }

    return this.descriptionTooltipId;
  }

  @Override
  public PropertyDataMap getPropertyMap() {
    return builtInRegistryHolder().getData(DataMaps.GROVE_PROPERTY_DATA);
  }

  public record GroveColors(Color text, Color particles1, Color particles2) {
    public GroveColors(int text, int first, int second) {
      this(Color.rgb(text), Color.rgb(first), Color.rgb(second));
    }
  }

  public static class Properties implements TooltipBuilder.AcceptsTooltip {
    final ResourceKey<Grove> resourceKey;
    GroveType groveType;

    final List<PropertyHolder<?>> allProperties = new ArrayList<>();

    public String simpleTooltip = "";
    public String extendedTooltip = "";
    public List<NumberFormatter<?>> numberFormatters = new ArrayList<>();
    public GroveColors color;

    public Properties(ResourceKey<Grove> resourceKey) {
      this.resourceKey = resourceKey;
    }

    public Properties property(PropertyHolder<?> property) {
      this.allProperties.add(property);
      return this;
    }

    public Properties properties(PropertyHolder<?>... properties) {
      this.allProperties.addAll(Arrays.asList(properties));
      return this;
    }

    public Properties type(GroveType type) {
      this.groveType = type;
      return this;
    }

    public Properties color(int textColor, int color1, int color2) {
      this.color = new GroveColors(textColor, color1, color2);
      return this;
    }

    public Properties build() {
      return this;
    }

    public Properties tooltip(String simple) {
      return new TooltipBuilder<>(this).simple(simple).endTooltip();
    }

    public TooltipBuilder<Properties> tooltip() {
      return new TooltipBuilder<>(this);
    }

    @Override
    public void accept(String s, String s2, List<NumberFormatter<?>> numberFormatters) {
      this.simpleTooltip = s;
      this.extendedTooltip = s2;
      this.numberFormatters = numberFormatters;
    }
  }
}
