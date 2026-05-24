package mysticmods.roots.api.modifier;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.herb.ChildChargeType;
import mysticmods.roots.api.herb.ParentChargeType;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpellModifier extends Modifier<Spell, SpellModifier> {
  public static final StreamCodec<RegistryFriendlyByteBuf, SpellModifier> STREAM_CODEC = ByteBufCodecs.registry(RootsRegistries.Keys.SPELL_MODIFIERS);
  protected final ChildChargeType chargeType;

  public SpellModifier(CostInstance defaultCosts, @NotNull ResourceKey<SpellModifier> parent, ResourceKey<Spell> applicable) {
    this(defaultCosts, parent, applicable, ChildChargeType.ALWAYS);
  }

  public SpellModifier(CostInstance defaultCosts, ResourceKey<Spell> applicable) {
    this(defaultCosts, applicable, ChildChargeType.ALWAYS);
  }

  public SpellModifier(CostInstance defaultCosts, @NotNull ResourceKey<SpellModifier> parent, ResourceKey<Spell> applicable, ChildChargeType type) {
    super(defaultCosts, parent, applicable);
    this.chargeType = type;
  }

  public SpellModifier(CostInstance defaultCosts, ResourceKey<Spell> applicable, ChildChargeType type) {
    super(defaultCosts, applicable);
    this.chargeType = type;
  }

  @Override
  protected DataMapType<SpellModifier, CostInstance> getDataMapType() {
    return DataMaps.SPELL_MODIFIER_COST_DATA;
  }

  @Override
  protected DataMapType<SpellModifier, Item> getIconDataMapType() {
    return DataMaps.SPELL_MODIFIER_ICONS;
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
}
