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

  protected final SpellType.Charge chargeType; // If null, use the spell default; otherwise, unless a transformer(?), a chargeType of `OPERATION` will store the number of operations of this instance of the modifier; a charge type of `INSTANCE` will only charge once per spell -- depending on the condition type.
  // If null, it will use the spell's code, in which case if it's `OPERATION` it will be charged for every operation if specified, etc.
  protected final SpellType.Condition conditionType; // If `ALWAYS`, it's always charged. If `SPECIFIED`, it must be specified via `costs.charge(SpellModifier)`.
  @NotNull
  protected final GroupId groupId;

  protected final CostInstance defaultCosts;
  @javax.annotation.Nullable
  protected CostInstance costs;
  protected boolean transforming;

  protected String descriptionTooltipId;
  protected String descriptionTooltipExtendedId;
  private String groupDescriptionId = null;
  protected Component[] extendedDescription = null;

  public SpellModifier(SpellModifier.Properties properties) {
    super(properties);
    this.defaultCosts = properties.costs;
    this.groupId = properties.groupId;
    this.chargeType = properties.chargeType;
    this.conditionType = properties.conditionType;
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

  @Override
  public String getOrCreateGroupDescriptionId() {
    if (groupDescriptionId == null) {
      this.groupDescriptionId = groupId.createDescriptionId("spell_modifier_group", builtInRegistryHolder().getKey());
    }

    return this.groupDescriptionId;
  }

  public static class Properties extends Modifier.Properties<Spell, SpellModifier, SpellModifier.Properties> {
    SpellType.Charge chargeType = null;
    SpellType.Condition conditionType = SpellType.Condition.ALWAYS;
    GroupId groupId = GroupId.NONE;
    CostInstance costs = null;

    public Properties(ResourceKey<SpellModifier> key) {
      super(key);
    }

    public final Properties charge (SpellType.Charge chargeType) {
      this.chargeType = chargeType;
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

    public final Properties costs(CostInstance costs) {
      this.costs = costs;
      return this;
    }

    public final Properties cost(Cost costs) {
      this.costs = CostInstance.of(costs);
      return this;
    }

    public final Properties cost(Holder<Herb> herb, double amount) {
      this.costs = CostInstance.add(herb, amount);
      return this;
    }

    public final Properties group (GroupId group) {
      this.groupId = group;
      return this;
    }
  }
}
