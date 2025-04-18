package mysticmods.roots.api.ritual;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.registry.ICosted;
import mysticmods.roots.api.registry.IDataMapInitialize;
import mysticmods.roots.api.registry.IDescribed;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.function.Predicate;

public class RitualModifier implements IDescribed, ICosted, /*IParentChild<RitualModifier>, */TooltipComponent, IDataMapInitialize<RitualModifier> {
  public static final Codec<RitualModifier> CODEC = RootsRegistries.RITUAL_MODIFIERS.byNameCodec();
  public static final StreamCodec<RegistryFriendlyByteBuf, RitualModifier> STREAM_CODEC = ByteBufCodecs.registry(RootsRegistries.Keys.RITUAL_MODIFIERS);

  protected final Holder<Ritual> ritual;
  protected final CostInstance defaultCosts;
  protected CostInstance costs;
  private String descriptionId;

  public RitualModifier(Holder<Ritual> ritual, CostInstance defaultCosts) {
    this.ritual = ritual;
    this.defaultCosts = defaultCosts;
  }

  @Override
  public CostInstance getDefaultCosts() {
    return defaultCosts;
  }

  @Override
  public CostInstance getCosts() {
    return costs == null ? defaultCosts : costs;
  }

  public Holder<Ritual> getRitual() {
    return ritual;
  }

  @Override
  public void init(Holder<RitualModifier> holder) {
    Ritual parent = holder.getData(DataMaps.RITUAL_MODIFIER_RITUAL);
    if (parent == null) {
      RootsAPI.LOG.error("RitualModifier {} has no parent ritual!", holder.getKey());
    } else if (parent != this.ritual.value()) {
      RootsAPI.LOG.error("RitualModifier {} has a parent ritual that is not the same as the ritual it is attached to!", holder.getKey());
    } else {
      ritual.value().addModifier(this);
    }
/*    RitualModifier modifierParent = holder.getData(DataMaps.SPELL_MODIFIER_PARENT);
    if (modifierParent == null) {
      // NOP
    } else if (modifierParent != this.parent) {
      RootsAPI.LOG.error("RitualModifier {} has a parent modifier that is not the same as the parent it was constructed with!", holder.getKey());
    } else {
      modifierParent.addChild(this);
    }*/
    this.costs = holder.getData(DataMaps.RITUAL_MODIFIER_COST_DATA);
  }

  public Holder<RitualModifier> builtInRegistryHolder() {
    return RootsRegistries.RITUAL_MODIFIERS.wrapAsHolder(this);
  }

  public boolean is(ResourceLocation key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean is(ResourceKey<RitualModifier> key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean is(Predicate<ResourceKey<RitualModifier>> key) {
    return builtInRegistryHolder().is(key);
  }

  public boolean is(TagKey<RitualModifier> key) {
    return builtInRegistryHolder().is(key);
  }

/*
  @Override
  @Nullable
  public RitualModifier getParent() {
    if (parent == null) {
      return null;
    }
    return parent.value();
  }

  @Override
  public Set<RitualModifier> getChildren() {
    return children;
  }

  @Override
  public void addChild(RitualModifier child) {
    children.add(child);
  }
*/

  @Override
  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId("ritual_modifier", builtInRegistryHolder().getKey().location());
    }

    return this.descriptionId;
  }
}
