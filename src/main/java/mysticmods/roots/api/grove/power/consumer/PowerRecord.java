package mysticmods.roots.api.grove.power.consumer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.GroveCache;
import mysticmods.roots.api.recipe.IRootsRecipe;
import mysticmods.roots.api.util.IntPair;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PowerRecord {
  public static final MapCodec<PowerRecord> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.INT.fieldOf("hash")
          .forGetter(PowerRecord::getHash), IntPair.INT_MAP_CODEC.fieldOf("accumulated").forGetter(PowerRecord::getMap))
      .apply(instance, PowerRecord::new));

  public static final Codec<PowerRecord> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<ByteBuf, PowerRecord> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, PowerRecord::hashCode, IntPair.INT_MAP_STREAM_CODEC, PowerRecord::getMap, PowerRecord::new);

  private final int hash;
  private final Int2IntMap accumulated;

  protected PowerRecord(int hash, Int2IntMap map) {
    this.hash = hash;
    this.accumulated = map;
  }

  public PowerRecord(IRootsRecipe<?> recipe) {
    this(recipe.getPowerRequirements());
  }

  public PowerRecord(PowerCollection collection) {
    this.hash = collection.hashCode();
    this.accumulated = new Int2IntOpenHashMap();
    for (PowerRequest request : collection) {
      accumulated.put(request.hashCode(), 0);
    }
  }

  public int getHash() {
    return hash;
  }

  public Int2IntMap getMap() {
    return accumulated;
  }

  public boolean isFulfilled(PowerCollection collection) {
    if (!isFor(collection)) {
      throw new IllegalStateException("Collection " + collection + " is not valid for record " + this);
    }

    for (PowerRequest request : collection) {
      if (!accumulated.containsKey(request.hashCode())) {
        return false;
      }
      int acquired = accumulated.get(request.hashCode());
      if (acquired < request.total()) {
        return false;
      }
    }

    return true;
  }

  public boolean isFor(PowerCollection collection) {
    return !collection.isEmpty() && this.hash == collection.hash();
  }

  public boolean isFor (IRootsRecipe<?> recipe) {
    return isFor(recipe.getPowerRequirements());
  }

  public void supply(PowerRequest request, int amountSupplied) {
    if (!accumulated.containsKey(request.hashCode())) {
      throw new IllegalStateException("Power request '" + request + "' is not valid for this Power Accumulation Record!");
    }

    accumulated.mergeInt(request.hashCode(), amountSupplied, Integer::sum);
  }

  public int suppliedFor (PowerRequest request) {
    if (!accumulated.containsKey(request.hashCode())) {
      throw new IllegalStateException("Power request '" + request + "' is not valid for this Power Accumulation Record!");
    }
    return accumulated.get(request.hashCode());
  }

  @Nullable
  public Typed typedFor (PowerCollection collection) {
    if (!this.isFor(collection)) {
      return null;
    }

    return new Typed(collection, this);
  }

  public static class Typed implements Iterable<TypedEntry> {
    private final List<TypedEntry> typed = new ArrayList<>();

    public Typed(PowerCollection collection, PowerRecord record) {
      for (PowerRequest request : collection) {
        typed.add(new TypedEntry(request, record.suppliedFor(request)));
      }
    }


    @Override
    public @NotNull Iterator<TypedEntry> iterator() {
      return typed.iterator();
    }
  }

  // TODO: Rate?
  public record TypedEntry(TagKey<Grove> tag, int totalRequired, int currentValue) {
    public TypedEntry(PowerRequest request, int currentValue) {
      this(request.tag(), request.total(), currentValue);
    }

    public List<Grove> getAllGroves () {
      return GroveCache.getAllGroves(tag);
    }

    public float percentage () {
      return (float) currentValue / (float) totalRequired;
    }
  }
}
