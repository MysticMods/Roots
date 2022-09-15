package mysticmods.roots.api.capability;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class GrantCapability implements ICapabilityProvider, ICapabilitySerializable<CompoundTag>, INetworkedCapability<GrantCapability.SerializedGrantRecord> {
  private boolean dirty = true;
  private final Set<Spell> GRANTED_SPELLS = new ObjectLinkedOpenHashSet<>();
  private final Set<Modifier> GRANTED_MODIFIERS = new ObjectLinkedOpenHashSet<>();
  private ImmutableSet<Spell> IMMUTABLE_GRANTED_SPELLS = null;
  private ImmutableSet<Modifier> IMMUTABLE_GRANTED_MODIFIERS = null;

  public GrantCapability() {
  }

  public boolean hasSpell(Spell spell) {
    return GRANTED_SPELLS.contains(spell);
  }

  public boolean hasModifier(Modifier modifier) {
    return GRANTED_MODIFIERS.contains(modifier);
  }

  public void grantSpell(Spell spell) {
    if (GRANTED_SPELLS.add(spell)) {
      IMMUTABLE_GRANTED_SPELLS = null;
      setDirty(true);
    }
  }

  public void grantModifier(Modifier modifier) {
    if (GRANTED_MODIFIERS.add(modifier)) {
      IMMUTABLE_GRANTED_MODIFIERS = null;
      setDirty(true);
    }
  }

  public void removeSpell(Spell spell) {
    if (GRANTED_SPELLS.remove(spell)) {
      IMMUTABLE_GRANTED_SPELLS = null;
      setDirty(true);
    }
  }

  public void removeModifier(Modifier modifier) {
    if (GRANTED_MODIFIERS.remove(modifier)) {
      IMMUTABLE_GRANTED_MODIFIERS = null;
      setDirty(true);
    }
  }

  public Set<Spell> getSpells() {
    if (IMMUTABLE_GRANTED_SPELLS == null) {
      IMMUTABLE_GRANTED_SPELLS = ImmutableSet.copyOf(GRANTED_SPELLS);
    }
    return IMMUTABLE_GRANTED_SPELLS;
  }

  public Set<Modifier> getModifiers() {
    if (IMMUTABLE_GRANTED_MODIFIERS == null) {
      IMMUTABLE_GRANTED_MODIFIERS = ImmutableSet.copyOf(GRANTED_MODIFIERS);
    }
    return IMMUTABLE_GRANTED_MODIFIERS;
  }

  @Override
  public void fromRecord(SerializedGrantRecord record) {
    this.GRANTED_MODIFIERS.clear();
    this.GRANTED_SPELLS.clear();
    this.GRANTED_MODIFIERS.addAll(record.getGrantedModifiers());
    this.GRANTED_SPELLS.addAll(record.getGrantedSpells());
    IMMUTABLE_GRANTED_MODIFIERS = null;
    IMMUTABLE_GRANTED_SPELLS = null;
    setDirty(true);
  }

  @Override
  public SerializedGrantRecord toRecord() {
    return new SerializedGrantRecord(GRANTED_SPELLS, GRANTED_MODIFIERS);
  }

  @Override
  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  @Override
  public boolean isDirty() {
    return dirty;
  }

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag result = new CompoundTag();
    ListTag spells = new ListTag();
    GRANTED_SPELLS.forEach(o ->
        spells.add(StringTag.valueOf(o.getKey().toString()))
    );
    result.put("spells", spells);
    ListTag modifiers = new ListTag();
    GRANTED_MODIFIERS.forEach(o ->
        modifiers.add(StringTag.valueOf(o.getKey().toString()))
    );
    result.put("modifiers", modifiers);
    return result;
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    GRANTED_SPELLS.clear();
    GRANTED_MODIFIERS.clear();
    ListTag spells = nbt.getList("spells", Tag.TAG_STRING);
    ListTag modifiers = nbt.getList("modifiers", Tag.TAG_STRING);
    for (int i = 0; i < spells.size(); i++) {
      ResourceLocation key = new ResourceLocation(spells.getString(i));
      Spell spell = Registries.SPELL_REGISTRY.get().getValue(key);
      if (spell != null) {
        GRANTED_SPELLS.add(spell);
      }
    }
    for (int i = 0; i < modifiers.size(); i++) {
      ResourceLocation key = new ResourceLocation(modifiers.getString(i));
      Modifier modifier = Registries.MODIFIER_REGISTRY.get().getValue(key);
      if (modifier != null) {
        GRANTED_MODIFIERS.add(modifier);
      }
    }
    setDirty(true);
  }

  @NotNull
  @Override
  public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    return Capabilities.GRANT_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this));
  }

  public static SerializedGrantRecord fromNetwork(FriendlyByteBuf buf) {
    SerializedGrantRecord result = new SerializedGrantRecord();
    result.fromNetwork(buf);
    return result;
  }

  public static class SerializedGrantRecord implements SerializedCapability {
    private final Set<Spell> GRANTED_SPELLS = new ObjectLinkedOpenHashSet<>();
    private final Set<Modifier> GRANTED_MODIFIERS = new ObjectLinkedOpenHashSet<>();

    public SerializedGrantRecord() {
    }

    public SerializedGrantRecord (Set<Spell> spells, Set<Modifier> modifiers) {
      this.GRANTED_SPELLS.addAll(spells);
      this.GRANTED_MODIFIERS.addAll(modifiers);
    }

    @Override
    public void fromNetwork(FriendlyByteBuf buf) {
      GRANTED_SPELLS.clear();
      GRANTED_MODIFIERS.clear();
      int spellCount = buf.readVarInt();
      for (int i = 0; i < spellCount; i++) {
        GRANTED_SPELLS.add(Registries.SPELL_REGISTRY.get().getValue(buf.readVarInt()));
      }
      int modifierCount = buf.readVarInt();
      for (int i = 0; i < modifierCount; i++) {
        GRANTED_MODIFIERS.add(Registries.MODIFIER_REGISTRY.get().getValue(buf.readVarInt()));
      }
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf) {
      buf.writeVarInt(GRANTED_SPELLS.size());
      for (Spell spell : GRANTED_SPELLS) {
        buf.writeVarInt(Registries.SPELL_REGISTRY.get().getID(spell));
      }
      buf.writeVarInt(GRANTED_MODIFIERS.size());
      for (Modifier modifier : GRANTED_MODIFIERS) {
        buf.writeVarInt(Registries.MODIFIER_REGISTRY.get().getID(modifier));
      }
    }

    public Set<Spell> getGrantedSpells() {
      return GRANTED_SPELLS;
    }

    public Set<Modifier> getGrantedModifiers() {
      return GRANTED_MODIFIERS;
    }
  }
}
