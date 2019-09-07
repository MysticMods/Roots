package epicsquid.roots.util.types;

import epicsquid.roots.spell.SpellBase;

public class Property<T> {
  public final Class<?> type;
  public final String name;
  public final T defaultValue;

  public Property(String name, T defaultValue) {
    this.type = defaultValue.getClass();
    this.name = name;
    this.defaultValue = defaultValue;
  }

  @SuppressWarnings("unchecked")
  public T cast (Object val) {
    return (T) val;
  }

  public static class PropertyCooldown extends Property<Integer> {
    public PropertyCooldown(Integer defaultValue) {
      super("cooldown", defaultValue);
    }
  }

  public static class PropertyCastType extends Property<SpellBase.EnumCastType> {
    public PropertyCastType(SpellBase.EnumCastType defaultValue) {
      super("cast_type", defaultValue);
    }
  }

  public static class PropertyCost extends Property<SpellBase.SpellCost> {
    public PropertyCost(int index, SpellBase.SpellCost defaultValue) {
      super("cost_" + index, defaultValue);
    }
  }

  public static class PropertyDamage extends Property<Float> {
    public PropertyDamage(Float defaultValue) {
      super("damage", defaultValue);
    }
  }
}
