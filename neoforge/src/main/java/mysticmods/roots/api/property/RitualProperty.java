package mysticmods.roots.api.property;

import com.google.common.base.Suppliers;
import mysticmods.roots.api.registry.IDescribedRegistryEntry;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemNameBlockItem;

import java.util.function.Supplier;

public class RitualProperty<V> extends Property<V> implements IDescribedRegistryEntry {
  protected String descriptionId;
  protected Supplier<Ritual> ritual;

  private final Holder.Reference<RitualProperty<?>> builtinRegistryHolder = RootsRegistries.RITUAL_PROPERTIES.createIntrusiveHolder(this);

  public RitualProperty(Supplier<Ritual> ritual, V defaultValue, Serializer<V> serializer, String comment) {
    super(defaultValue, serializer, comment);
    this.ritual = Suppliers.memoize(ritual::get);
  }

  public Ritual getRitual() {
    return ritual.get();
  }

  public Holder.Reference<RitualProperty<?>> getBuiltinRegistryHolder() {
    return builtinRegistryHolder;
  }

  @Override
  public ResourceLocation getKey() {
    return getBuiltinRegistryHolder().getKey().location();
  }

  @Override
  public String getOrCreateDescriptionId() {
    if (this.descriptionId == null) {
      this.descriptionId = Util.makeDescriptionId("ritual_property", getKey());
    }

    return this.descriptionId;
  }
}
