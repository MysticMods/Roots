package mysticmods.roots.api.attachment;

import com.mojang.serialization.Codec;
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
  public static final Codec<CooldownStorage> CODEC = Codec.unboundedMap(RootsRegistries.SPELLS.byNameCodec(), Codec.INT)
      .xmap(CooldownStorage::new, CooldownStorage::getCooldownMap);
  public static final StreamCodec<RegistryFriendlyByteBuf, CooldownStorage> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.map(Object2IntOpenHashMap::new, ByteBufCodecs.registry(RootsRegistries.Keys.SPELLS), ByteBufCodecs.VAR_INT), CooldownStorage::getCooldownMap, CooldownStorage::new);

  private boolean dirty = true;
  private final Object2IntOpenHashMap<Spell> cooldownMap;

  public CooldownStorage() {
    cooldownMap = new Object2IntOpenHashMap<>();
  }

  public CooldownStorage(Map<Spell, Integer> herbMap) {
    this.cooldownMap = new Object2IntOpenHashMap<>(herbMap);
  }

  public Object2IntOpenHashMap<Spell> getCooldownMap() {
    return cooldownMap;
  }

  public int getCooldown(Spell spell) {
    return cooldownMap.getOrDefault(spell, -1);
  }

  public void setCooldown(Spell spell, int cooldown) {
    if (cooldown <= 0 && cooldownMap.containsKey(spell)) {
      cooldownMap.removeInt(spell);
    } else if (cooldownMap.getInt(spell) != cooldown) {
      cooldownMap.put(spell, cooldown);
    }
    dirty = true;
  }

  @Override
  public boolean isEmpty() {
    return cooldownMap.isEmpty();
  }

  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  public boolean isDirty() {
    return dirty;
  }

  @Override
  public void tick(Entity entity) {
    Iterator<Object2IntMap.Entry<Spell>> iterator = cooldownMap.object2IntEntrySet().iterator();
    while (iterator.hasNext()) {
      Object2IntMap.Entry<Spell> entry = iterator.next();
      if (entry == null || entry.getValue() <= 0) {
        iterator.remove();
      } else {
        cooldownMap.put(entry.getKey(), entry.getValue() - 1);
      }
      dirty = true;
    }
  }
}
