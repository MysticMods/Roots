package mysticmods.roots.api.modifier;

import mysticmods.roots.api.RootsItemCallbacks;
import mysticmods.roots.api.SpellType;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.registry.GroupId;
import mysticmods.roots.api.registry.ICostedChild;
import mysticmods.roots.api.registry.IExtendedDescribed;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SpellModifier extends Modifier<Spell, SpellModifier> implements IExtendedDescribed, ICostedChild {
  public static final StreamCodec<RegistryFriendlyByteBuf, SpellModifier> STREAM_CODEC = ByteBufCodecs.registry(RootsRegistries.Keys.SPELL_MODIFIERS);

  protected final SpellType.Cast castType;
  protected final SpellType.Charge chargeType;
  protected final SpellType.Condition conditionType;
  @NotNull
  protected final GroupId groupId;


  protected final CostInstance defaultCosts;
  @javax.annotation.Nullable
  protected CostInstance costs;
  protected boolean transforming;

  protected String descriptionTooltipId;
  protected String descriptionTooltipExtendedId;
  private String transformerDescriptionId = null;
  private String transformerDescriptionTooltipId = null;
  private String transformerDescriptionExtendedTooltipId = null;
  private String groupDescriptionId = null;
  protected Component[] extendedDescription = null;
  protected Component[] transformerExtendedDescription = null;

  public SpellModifier(SpellModifier.Properties properties) {
    super(properties);
    this.defaultCosts = properties.costs.get();
    this.groupId = properties.groupId;
    this.chargeType = properties.chargeType;
    this.conditionType = properties.conditionType;
    this.castType = properties.castType;
    this.transforming = properties.transformer;
  }

  @Override
  public Component[] getOrCreateDescriptionComponents() {
    if (extendedDescription == null) {
      this.extendedDescription = createExtendedDescriptionComponents();
    }

    return this.extendedDescription;
  }

  protected Component[] createExtendedDescriptionComponents() {
    return getApplicableHolder().value().createModifierDescriptionComponents(this);
  }

  public Component[] getOrCreateTransformerExtendedDescriptionComponents() {
    if (transformerExtendedDescription == null) {
      this.transformerExtendedDescription = createTransformerExtendedDescriptionComponents();
    }

    return this.transformerExtendedDescription;
  }

  // TODO:
  protected Component[] createTransformerExtendedDescriptionComponents() {
    return new Component[]{};
  }

  @Override
  public String getOrCreateTooltipDescriptionId() {
    if (this.descriptionTooltipId == null) {
      if (this.groupId.useGroupDescription()) {
        this.descriptionTooltipId = this.groupId.createDescriptionId("spell_modifier_group", builtInRegistryHolder().getKey()) + ".description";
      } else {
        this.descriptionTooltipId = getOrCreateDescriptionId() + ".description";
      }
    }

    return this.descriptionTooltipId;
  }


  @Override
  public String getOrCreateTooltipExtendedDescriptionId() {
    if (this.descriptionTooltipExtendedId == null) {
      this.descriptionTooltipExtendedId = getOrCreateTooltipDescriptionId() + ".extended";
    }

    return this.descriptionTooltipExtendedId;
  }


  @Override
  public boolean isTransforming() {
    return transforming;
  }

  @Override
  protected DataMapType<SpellModifier, CostInstance> getDataMapType() {
    return DataMaps.SPELL_MODIFIER_COST_DATA;
  }

  @Override
  public Holder<Spell> getApplicableHolder() {
    //noinspection deprecation
    return RootsRegistries.SPELLS.getHolder(getApplicable()).orElse(null);
  }

  @Nullable
  public Holder<SpellModifier> getParentHolder() {
    if (getParent() == null) {
      return null;
    }
    return RootsRegistries.SPELL_MODIFIERS.getHolder(getParent()).orElse(null);
  }

  @Override
  public Holder<SpellModifier> builtInRegistryHolder() {
    return RootsRegistries.SPELL_MODIFIERS.wrapAsHolder(this);
  }

  @Override
  protected String getSignifier() {
    return "spell_modifier";
  }

  @Override
  public SpellType.Condition getChargeCondition() {
    return conditionType;
  }

  @Override
  public @Nullable SpellType.Charge getChargeType() {
    return chargeType;
  }

  public ItemStack getIcon() {
    return RootsItemCallbacks.getItemStack(this);
  }

  @Override
  public boolean canGroup() {
    return !groupId.isEmpty();
  }

  @Override
  public GroupId getGroupKey() {
    return groupId;
  }

  @Override
  public CostInstance getDefaultCosts() {
    return defaultCosts;
  }

  @Override
  public CostInstance getCosts() {
    if (costs == null) {
      return getDefaultCosts();
    }
    return costs;
  }

  @Override
  public void init(Holder<SpellModifier> holder) {
    var costs = holder.getData(getDataMapType());
    if (costs != null) {
      this.costs = costs;
    }
  }

  public String getOrCreateTransformerDescriptionId() {
    if (transformerDescriptionId == null) {
      this.transformerDescriptionId = Util.makeDescriptionId(getSignifier() + "_transformer", getApplicable().location()
          .withSuffix("/" + getSelf().location().getPath()).withSuffix("/description"));
    }
    return transformerDescriptionId;
  }

  public String getOrCreateTransformerDescriptionTooltipId() {
    if (transformerDescriptionTooltipId == null) {
      this.transformerDescriptionTooltipId = Util.makeDescriptionId(getSignifier() + "_transformer", getApplicable().location()
          .withSuffix("/" + getSelf().location().getPath()).withSuffix("/description/tooltip"));
    }
    return transformerDescriptionTooltipId;
  }

  public String getOrCreateTransformerDescriptionExtendedTooltipId() {
    if (transformerDescriptionExtendedTooltipId == null) {
      this.transformerDescriptionExtendedTooltipId = Util.makeDescriptionId(getSignifier(), getApplicable().location()
          .withSuffix("/" + getSelf().location().getPath()).withSuffix("/description/tooltip/extended"));
    }
    return transformerDescriptionExtendedTooltipId;
  }

  @Override
  public String getOrCreateGroupDescriptionId() {
    if (groupDescriptionId == null) {
      this.groupDescriptionId = groupId.createDescriptionId("spell_modifier_group", builtInRegistryHolder().getKey());
    }

    return this.groupDescriptionId;
  }

  public static class Properties extends Modifier.Properties<Spell, SpellModifier, SpellModifier.Properties> {
    SpellType.Charge chargeType = null;
    SpellType.Cast castType = null;
    SpellType.Condition conditionType = SpellType.Condition.ALWAYS;
    GroupId groupId = GroupId.NONE;
    Supplier<CostInstance> costs = null;
    // Spells-only?

    public Properties(ResourceKey<SpellModifier> key) {
      super(key);
    }

    public final Properties charge (SpellType.Charge chargeType) {
      this.chargeType = chargeType;
      return this;
    }

    public final Properties cast (SpellType.Cast castType) {
      this.castType = castType;
      return this;
    }

    public final Properties condition (SpellType.Condition condition) {
      this.conditionType = condition;
      return this;
    }

    public final Properties transforms() {
      this.transformer = true;
      return this;
    }

    public final Properties costs(Supplier<CostInstance> costs) {
      this.costs = costs;
      return this;
    }

    public final Properties cost(Supplier<Cost> costs) {
      this.costs = () -> CostInstance.of(costs.get());
      return this;
    }

    public final Properties cost(Supplier<Holder<Herb>> herb, double amount) {
      this.costs = () -> CostInstance.add(herb.get(), amount);
      return this;
    }

    public final Properties group (GroupId group) {
      this.groupId = group;
      return this;
    }
  }
}
