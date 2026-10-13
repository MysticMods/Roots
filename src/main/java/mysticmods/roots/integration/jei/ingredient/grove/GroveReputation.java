package mysticmods.roots.integration.jei.ingredient.grove;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.IGroveValue;
import mysticmods.roots.api.grove.SimpleGroveValue;

public record GroveReputation(SimpleGroveValue number) implements IGroveValue {
  public static Codec<GroveReputation> CODEC = IGroveValue.mapCodec(SimpleGroveValue::new).codec().xmap(GroveReputation::new, GroveReputation::number);

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
    return number.type();
  }
}
