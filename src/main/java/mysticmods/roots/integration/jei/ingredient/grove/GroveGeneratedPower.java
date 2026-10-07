package mysticmods.roots.integration.jei.ingredient.grove;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.SimpleGroveValue;
import mysticmods.roots.api.grove.IGroveValue;

public record GroveGeneratedPower(SimpleGroveValue number) implements IGroveValue {
  public static Codec<GroveGeneratedPower> CODEC = IGroveValue.mapCodec(SimpleGroveValue::new).xmap(GroveGeneratedPower::new, GroveGeneratedPower::number).codec();

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
