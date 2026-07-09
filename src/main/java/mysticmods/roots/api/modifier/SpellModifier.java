package mysticmods.roots.api.modifier;

import mysticmods.roots.api.RootsItemCallbacks;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.registry.IExtendedDescribed;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpellModifier extends Modifier<Spell, SpellModifier> implements IExtendedDescribed {
  public static final StreamCodec<RegistryFriendlyByteBuf, SpellModifier> STREAM_CODEC = ByteBufCodecs.registry(RootsRegistries.Keys.SPELL_MODIFIERS);
  protected final ChildChargeType chargeType;

  protected String descriptionTooltipId;
  protected String descriptionTooltipExtendedId;
  protected Component[] extendedDescription = null;

  public SpellModifier(CostInstance defaultCosts, @Nullable ResourceKey<SpellModifier> parent, ResourceKey<Spell> applicable) {
    this(defaultCosts, parent, applicable, ChildChargeType.ALWAYS);
  }

  public SpellModifier(CostInstance defaultCosts, ResourceKey<Spell> applicable) {
    this(defaultCosts, applicable, ChildChargeType.ALWAYS);
  }

  public SpellModifier(CostInstance defaultCosts, @Nullable ResourceKey<SpellModifier> parent, ResourceKey<Spell> applicable, ChildChargeType type) {
    super(defaultCosts, parent, applicable);
    this.chargeType = type;
  }

  @SafeVarargs
  public SpellModifier(CostInstance defaultCosts, @Nullable ResourceKey<SpellModifier> parent, ResourceKey<Spell> applicable, ResourceKey<SpellModifier> ... conflicts) {
    super(defaultCosts, parent, applicable, conflicts);
    this.chargeType = ChildChargeType.ALWAYS;
  }

  @SafeVarargs
  public SpellModifier(CostInstance defaultCosts, @Nullable ResourceKey<SpellModifier> parent, ResourceKey<Spell> applicable, ChildChargeType type, ResourceKey<SpellModifier> ... conflicts) {
    super(defaultCosts, parent, applicable, conflicts);
    this.chargeType = type;
  }

  public SpellModifier(CostInstance defaultCosts, ResourceKey<Spell> applicable, ChildChargeType type) {
    super(defaultCosts, applicable);
    this.chargeType = type;
  }

  @Override
  public Component[] getOrCreateDescriptionComponents() {
    if (extendedDescription == null) {
      this.extendedDescription = createExtendedDescriptionComponents();
    }

    return this.extendedDescription;

  }

  public Component[] createExtendedDescriptionComponents() {
    return getApplicableHolder().value().createModifierDescriptionComponents(this);
  }

  @Override
  public String getOrCreateTooltipDescriptionId() {
    if (this.descriptionTooltipId == null) {
      this.descriptionTooltipId = getOrCreateDescriptionId() + ".description";
    }

    return this.descriptionTooltipId;
  }


  @Override
  public String getOrCreateTooltipExtendedDescriptionId() {
    if (this.descriptionTooltipExtendedId == null) {
      this.descriptionTooltipExtendedId = getOrCreateDescriptionId() + ".description.extended";
    }

    return this.descriptionTooltipExtendedId;
  }


  @Override
  protected DataMapType<SpellModifier, CostInstance> getDataMapType() {
    return DataMaps.SPELL_MODIFIER_COST_DATA;
  }

  @Nullable
  public Holder<Spell> getApplicableHolder() {
    return RootsRegistries.SPELLS.getHolder(getApplicable()).orElse(null);
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
  public ChildChargeType getChargeType() {
    return chargeType;
  }

  public ItemStack getIcon () {
    return RootsItemCallbacks.getItemStack(this);
  }
}
