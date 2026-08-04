package mysticmods.roots.integration.jei.ingredient.grove;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.GroveNumber;
import mysticmods.roots.api.grove.IGroveNumber;
import mysticmods.roots.api.registry.RootsRegistries;

import java.util.List;

public record GrovePower(GroveNumber number) implements IGroveNumber {
  public static Codec<GrovePower> CODEC = IGroveNumber.codec(GrovePower::new);

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
    return Type.POWER;
  }

  public static List<GrovePower> all(int value) {
    return RootsRegistries.GROVES.stream().map(o -> new GrovePower(new GroveNumber(o, value, Type.POWER))).toList();
  }
}
