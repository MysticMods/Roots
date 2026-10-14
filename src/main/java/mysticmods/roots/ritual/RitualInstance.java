package mysticmods.roots.ritual;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.modifier.RitualModifierSet;
import mysticmods.roots.api.ritual.IRitualInstance;
import mysticmods.roots.api.ritual.Ritual;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;

import java.util.UUID;
import java.util.function.Supplier;

public class RitualInstance implements IRitualInstance.Identified {
  public static final MapCodec<RitualInstance> MAP_CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
          Ritual.CODEC.fieldOf("ritual").forGetter(RitualInstance::getRitual),
          UUIDUtil.CODEC.fieldOf("ritualId").forGetter(RitualInstance::getId),
          Codec.INT.fieldOf("lifetime").forGetter(RitualInstance::getLifetime),
          RitualModifierSet.CODEC.fieldOf("modifiers").forGetter(RitualInstance::getEnabledModifiers),
          DataComponentPatch.CODEC.fieldOf("data")
              .forGetter(o -> o.data == null ? DataComponentPatch.EMPTY : o.data.asPatch())
      ).apply(instance, RitualInstance::new)
  );
  public static final Codec<RitualInstance> CODEC = MAP_CODEC.codec();

  private final Ritual ritual;
  private final UUID ritualId;
  private int lifetime;
  private RitualModifierSet modifiers = RitualModifierSet.EMPTY;
  private PatchedDataComponentMap data;

  public RitualInstance(Ritual ritual) {
    this.ritual = ritual;
    this.ritualId = UUID.randomUUID();
    this.lifetime = this.getDuration();
  }

  public RitualInstance (Ritual ritual, int lifetime) {
    this.ritual = ritual;
    this.lifetime = lifetime;
    this.ritualId = UUID.randomUUID();
  }

  protected RitualInstance (Ritual ritual, UUID ritualId, int lifetime, RitualModifierSet modifiers, DataComponentPatch dataPatch) {
    this.ritual = ritual;
    this.ritualId = ritualId;
    this.lifetime = lifetime;
    this.modifiers = modifiers;
    this.data = PatchedDataComponentMap.fromPatch(ritual.getComponents(), dataPatch);
  }

  @Override
  public Ritual getRitual() {
    return ritual;
  }

  @Override
  public int getLifetime() {
    return lifetime;
  }

  @Override
  public void tickLifetime() {
    this.lifetime--;
  }

  @Override
  public RitualModifierSet getEnabledModifiers() {
    return modifiers;
  }

  @Override
  public void setEnabledModifiers(RitualModifierSet modifiers) {
    this.modifiers = modifiers;
  }

  @Override
  public void setLifetime(int value) {
    this.lifetime = value;
  }

  @Override
  public UUID getId() {
    return ritualId;
  }

  @Override
  public DataComponentMap getRitualData() {
    return data;
  }

  @Override
  public void setRitualDataPatch(PatchedDataComponentMap map) {
    this.data = map;
  }

  @Override
  public DataComponentPatch getRitualDataPatch() {
    return data.asPatch();
  }

  @Override
  public <T> void setRitualData(DataComponentType<? super T> component, T value) {
    data.set(component, value);
  }

  @Override
  public <T> void setRitualData(Supplier<? extends DataComponentType<? super T>> component, T value) {
    data.set(component.get(), value);
  }
}
