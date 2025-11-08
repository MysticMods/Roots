package mysticmods.roots.api.modifier;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

public class RitualModifier extends Modifier<Ritual, RitualModifier> {
  public RitualModifier(ResourceKey<Grove> grove, @NotNull ResourceKey<RitualModifier> parent, ResourceKey<Ritual> applicable) {
    super(grove, parent, applicable);
  }

  public RitualModifier(ResourceKey<Grove> grove, ResourceKey<Ritual> applicable) {
    super(grove, applicable);
  }

  @Override
  public Holder<RitualModifier> builtInRegistryHolder() {
    return RootsRegistries.RITUAL_MODIFIERS.wrapAsHolder(this);
  }

  @Override
  protected String getSignifier() {
    return "ritual_modifier";
  }

  @Override
  public CostInstance getDefaultCosts() {
    return null;
  }

  @Override
  public CostInstance getCosts() {
    return null;
  }

  @Override
  public void init(Holder<RitualModifier> holder) {

  }
}
