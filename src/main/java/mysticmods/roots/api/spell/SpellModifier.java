package mysticmods.roots.api.spell;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.registry.*;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class SpellModifier implements IDescribed, ICosted, TooltipComponent, IDataMapInitialize<SpellModifier> {
  public static final Codec<SpellModifier> CODEC = RootsRegistries.SPELL_MODIFIERS.byNameCodec();
  public static final StreamCodec<RegistryFriendlyByteBuf, SpellModifier> STREAM_CODEC = ByteBufCodecs.registry(RootsRegistries.Keys.SPELL_MODIFIERS);

  @Nullable
  protected final ResourceKey<SpellModifier> parent;
  protected final ResourceKey<Spell> spell;
  protected final CostInstance defaultCosts;
  protected Holder<Spell> cachedSpell;
  protected Holder<SpellModifier> cachedParent;
  protected CostInstance costs;
  private String descriptionId;

  // Modifier with parent
  public SpellModifier(@Nullable ResourceKey<SpellModifier> parent, ResourceKey<Spell> spell, CostInstance defaultCosts) {
    this.spell = spell;
    this.parent = parent;
    this.defaultCosts = defaultCosts;
  }

  // Modifier with no parent
  public SpellModifier(ResourceKey<Spell> spell, CostInstance defaultCosts) {
    this(null, spell, defaultCosts);
  }

  @Override
  public CostInstance getDefaultCosts() {
    return defaultCosts;
  }

  @Override
  public CostInstance getCosts() {
    return costs == null ? defaultCosts : costs;
  }

  public Holder<Spell> getSpell() {
    if (cachedSpell == null) {
      cachedSpell = RootsRegistries.SPELLS.getHolderOrThrow(spell);
    }
    return cachedSpell;
  }

  @Override
  public void init(Holder<SpellModifier> holder) {
    Spell spellParent = RootsRegistries.SPELLS.get(spell);
    if (spellParent == null) {
      RootsAPI.LOG.error("SpellModifier {} has no parent spell!", holder.getKey());
    } else {
      spellParent.addModifier(this);
      cachedSpell = spellParent.builtInRegistryHolder();
    }
    SpellModifier modifierParent = RootsRegistries.SPELL_MODIFIERS.get(parent);
    if (modifierParent != null) {
      cachedParent = modifierParent.builtInRegistryHolder();
    }
    this.costs = holder.getData(DataMaps.SPELL_MODIFIER_COST_DATA);
  }

  public Holder<SpellModifier> builtInRegistryHolder() {
    return RootsRegistries.SPELL_MODIFIERS.wrapAsHolder(this);
  }

  @Nullable
  public Holder<SpellModifier> getParent () {
    return cachedParent;
  }

  public Holder<Spell> getSpellParent () {
    return cachedSpell;
  }

  public boolean is(ResourceLocation key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean is(ResourceKey<SpellModifier> key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean is(Predicate<ResourceKey<SpellModifier>> key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean is(TagKey<SpellModifier> key) {
    return builtInRegistryHolder().is(key);
  }

  @Override
  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId("spell_modifier", builtInRegistryHolder().getKey().location());
    }

    return this.descriptionId;
  }
}
