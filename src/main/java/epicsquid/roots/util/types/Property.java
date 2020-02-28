package epicsquid.roots.util.types;

import epicsquid.roots.spell.SpellBase;

public class Property<T> {
  protected Class<?> type;
  protected String name;
  protected T defaultValue;
  protected String description = "unknown";

  public Property (String name, Class<?> clazz) {
    this.type = clazz;
    this.name = name;
    this.defaultValue = null;
  }

  public Property(String name, T defaultValue) {
    this.type = defaultValue.getClass();
    this.name = name;
    this.defaultValue = defaultValue;
  }

  public String getDescription() {
    return description;
  }

  public Property<T> setDescription(String description) {
    this.description = description;
    return this;
  }

  public Class<?> getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public T getDefaultValue() {
    return defaultValue;
  }

  public boolean hasDefaultValue () {
    return true;
  }

  @SuppressWarnings("unchecked")
  public T cast (Object val) {
    return (T) val;
  }

  public static class PropertyCooldown extends Property<Integer> {
    public PropertyCooldown(Integer defaultValue) {
      super("cooldown", defaultValue);
    }

    @Override
    public String getDescription() {
      return "the cooldown time in ticks of the spell";
    }
  }

  public static class PropertyCastType extends Property<SpellBase.EnumCastType> {
    public PropertyCastType(SpellBase.EnumCastType defaultValue) {
      super("cast_type", defaultValue);
    }

    @Override
    public String getDescription() {
      return "the spell cast type (can be continuous or instantaneous)";
    }
  }

  public static class PropertyCost extends Property<SpellBase.SpellCost> {
    public PropertyCost(int index, SpellBase.SpellCost defaultValue) {
      super("cost_" + index, defaultValue);
    }

    @Override
    public String getDescription() {
      return "the herb cost of one cast of this spell";
    }
  }

  public static class PropertyDamage extends Property<Float> {
    public PropertyDamage(Float defaultValue) {
      super("damage", defaultValue);
    }

    @Override
    public PropertyDamage setDescription(String description) {
      return ((PropertyDamage) super.setDescription(description));
    }
  }

  public static class PropertyDuration extends Property<Integer> {

    public PropertyDuration(Integer defaultValue) {
      super("duration", defaultValue);
    }

    @Override
    public String getDescription() {
      return "the duration in ticks of the ritual/spell";
    }
  }

  public static class PropertyInterval extends Property<Integer> {
    public PropertyInterval(Integer defaultValue) {
      super("interval", defaultValue);
    }

    @Override
    public PropertyInterval setDescription(String description) {
      return ((PropertyInterval) super.setDescription(description));
    }
  }
}
