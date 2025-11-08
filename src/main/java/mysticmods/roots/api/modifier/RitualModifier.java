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

  @Override
  public Record record() {
    return Record.VALUES.intern(new Record(builtInRegistryHolder()));
  }

  public static class Record implements ModifierRecord<Ritual, RitualModifier> {
    private final Holder<RitualModifier> modifier;
    private boolean enabled;
    private boolean disabled;

    private static final Interner<Record> VALUES = Interners.newWeakInterner();

    public static final MapCodec<Record> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        RootsRegistries.RITUAL_MODIFIERS.holderByNameCodec().fieldOf("modifier").forGetter(Record::modifier),
        Codec.BOOL.fieldOf("enabled").forGetter(Record::enabled),
        Codec.BOOL.fieldOf("disabled").forGetter(Record::disabled)).apply(instance, Record::new)
    );
    public static final Codec<Record> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<RegistryFriendlyByteBuf, Record> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.holderRegistry(RootsRegistries.Keys.RITUAL_MODIFIERS), Record::modifier,
        ByteBufCodecs.BOOL, Record::enabled,
        ByteBufCodecs.BOOL, Record::disabled,
        Record::new
    );

    private Record(Holder<RitualModifier> modifier, boolean enabled, boolean disabled) {
      this.modifier = modifier;
      this.enabled = enabled;
      this.disabled = disabled;
    }

    private Record(Holder<RitualModifier> modifier) {
      this(modifier, false, false);
    }

    public Holder<RitualModifier> modifier() {
      return modifier;
    }

    public boolean enabled() {
      return enabled;
    }

    public boolean disabled() {
      return disabled;
    }

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }

    public void setDisabled(boolean disabled) {
      this.disabled = disabled;
    }
  }
}
