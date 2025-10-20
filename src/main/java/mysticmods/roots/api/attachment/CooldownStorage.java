package mysticmods.roots.api.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;

import java.util.Iterator;
import java.util.Map;

public class CooldownStorage implements ICleanable, ITicking {
  public static final Codec<CooldownStorage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      Codec.unboundedMap(RootsRegistries.SPELLS.byNameCodec(), Codec.INT).fieldOf("cooldown_map")
          .forGetter(o -> o.cooldownMap),
      Codec.unboundedMap(RootsRegistries.SPELLS.byNameCodec(), Codec.INT).fieldOf("max_cooldown_map")
          .forGetter(o -> o.cooldownMap)).apply(instance, CooldownStorage::new));

  public static final StreamCodec<RegistryFriendlyByteBuf, CooldownStorage> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.map(Object2IntOpenHashMap::new, ByteBufCodecs.registry(RootsRegistries.Keys.SPELLS), ByteBufCodecs.VAR_INT), CooldownStorage::getCooldownMap, ByteBufCodecs.map(Object2IntOpenHashMap::new, ByteBufCodecs.registry(RootsRegistries.Keys.SPELLS), ByteBufCodecs.VAR_INT), CooldownStorage::getMaxCooldownMap, CooldownStorage::new);

  private boolean dirty = true;
  private final Object2IntOpenHashMap<Spell> cooldownMap;
  private final Object2IntOpenHashMap<Spell> maxCooldownMap;

  public CooldownStorage() {
    cooldownMap = new Object2IntOpenHashMap<>();
    maxCooldownMap = new Object2IntOpenHashMap<>();
  }

  public CooldownStorage(Map<Spell, Integer> spellIntegerMap, Map<Spell, Integer> spellIntegerMap1) {
    this.cooldownMap = new Object2IntOpenHashMap<>(spellIntegerMap);
    this.maxCooldownMap = new Object2IntOpenHashMap<>(spellIntegerMap1);
  }

  public Object2IntOpenHashMap<Spell> getCooldownMap() {
    return cooldownMap;
  }

  public Object2IntOpenHashMap<Spell> getMaxCooldownMap() {
    return maxCooldownMap;
  }

  public int getCooldown(Spell spell) {
    return cooldownMap.getOrDefault(spell, -1);
  }

  public int getMaxCooldown(Spell spell) {
    return maxCooldownMap.getOrDefault(spell, -1);
  }

  public void setCooldown(Spell spell, int cooldown, int maxCooldown) {
    if (cooldown <= 0) {
      cooldownMap.removeInt(spell);
      maxCooldownMap.removeInt(spell);
    } else {
      cooldownMap.put(spell, cooldown);
      maxCooldownMap.put(spell, maxCooldown);
    }
    dirty = true;
  }

  @Override
  public boolean isEmpty() {
    return cooldownMap.isEmpty();
  }

  @Override
  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  @Override
  public boolean isDirty() {
    return dirty;
  }

  public void reset() {
    cooldownMap.clear();
    maxCooldownMap.clear();
    setDirty(true);
  }

  @Override
  public void tick(Entity entity) {
    Iterator<Object2IntMap.Entry<Spell>> iterator = cooldownMap.object2IntEntrySet().iterator();
    while (iterator.hasNext()) {
      Object2IntMap.Entry<Spell> entry = iterator.next();
      if (entry.getIntValue() <= 0) {
        maxCooldownMap.removeInt(entry.getKey());
        iterator.remove();
      } else {
        cooldownMap.put(entry.getKey(), entry.getIntValue() - 1);
      }
      dirty = true;
    }
  }
}
