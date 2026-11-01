package mysticmods.roots.api.grove.power.consumer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import mysticmods.roots.api.grove.IGroveInstance;
import mysticmods.roots.api.recipe.IRootsRecipe;
import mysticmods.roots.api.util.IntPair;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class PowerRecord {
  public static final MapCodec<PowerRecord> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.INT.fieldOf("hashCode")
          .forGetter(PowerRecord::hashCode), IntPair.INT_MAP_CODEC.fieldOf("accumultated").forGetter(PowerRecord::getMap))
      .apply(instance, PowerRecord::new));
  public static final Codec<PowerRecord> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<ByteBuf, PowerRecord> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, PowerRecord::hashCode, IntPair.INT_MAP_STREAM_CODEC, PowerRecord::getMap, PowerRecord::new);

  private final int hashCode;
  private final Int2IntMap accumulated;

  protected PowerRecord(int hashCode, Int2IntMap map) {
    this.hashCode = hashCode;
    this.accumulated = map;
  }

  public PowerRecord(IRootsRecipe<?> recipe) {
    this(recipe.getPowerRequirements());
  }

  public PowerRecord(PowerCollection collection) {
    this.hashCode = collection.hashCode();
    this.accumulated = new Int2IntOpenHashMap();
    for (PowerRequest request : collection) {
      accumulated.put(request.hashCode(), 0);
    }
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
    return !collection.isEmpty() && this.hashCode == collection.hash();
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

}
