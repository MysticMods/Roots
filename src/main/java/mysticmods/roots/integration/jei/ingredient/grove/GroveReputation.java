package mysticmods.roots.integration.jei.ingredient.grove;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.GroveNumber;
import mysticmods.roots.api.grove.IGroveNumber;
import mysticmods.roots.api.registry.RootsRegistries;

import java.util.List;

public record GroveReputation(GroveNumber number) implements IGroveNumber {
  public static Codec<GroveReputation> CODEC = IGroveNumber.codec(GroveReputation::new);

  @Override
  public Grove grove() {
    return number.grove();
  }

  @Override
  public int value() {
    return number.value();
  }

  @Override
  public Type type() {
    return Type.REPUTATION;
  }

  public static List<GroveReputation> all(int value) {
    return RootsRegistries.GROVES.stream().map(o -> new GroveReputation(new GroveNumber(o, value, Type.REPUTATION)))
        .toList();
  }
}
