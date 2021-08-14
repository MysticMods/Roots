package epicsquid.roots.properties;

import epicsquid.roots.spell.SpellBase;

import java.util.function.Predicate;

public class Property<T> extends AbstractProperty<T, Property<T>> {
  public Property(String name, Class<?> clazz) {
    this.type = clazz;
    this.name = name;
    this.defaultValue = null;
  }

  public Property(String name, T defaultValue) {
    this.type = defaultValue.getClass();
    this.name = name;
    this.defaultValue = defaultValue;
  }

  @Override
  public Property<T> setDescription(String description) {
    this.description = description;
    return this;
  }

  @Override
  public Property<T> setValidation(Predicate<T> validator, String bounds) {
    this.validator = validator;
    this.bounds = bounds;
    return this;
  }

  @Override
  public boolean validate(T value) {
    return true;
  }

  public static class PropertyCooldown extends Property<Integer> {
    public PropertyCooldown(Integer defaultValue) {
      super("cooldown", defaultValue);
    }

    @Override
    public String getDescription() {
      return "the cooldown remaining (in ticks) of the spell";
    }

    @Override
    public boolean validate(Integer value) {
      return value >= 0;
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
    public PropertyCost(SpellBase.SpellCost defaultValue) {
      super("cost_" + defaultValue.getHerb().getName(), defaultValue);
    }

    @Override
    public String getDescription() {
      return "the herb cost of one cast of this spell or its cost every tick";
    }

    @Override
    public boolean validate(SpellBase.SpellCost value) {
      return value.getCost() > 0;
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

    @Override
    public boolean validate(Float value) {
      return value > 0;
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

    @Override
    public PropertyDuration setDescription(String description) {
      return (PropertyDuration) super.setDescription(description);
    }

    @Override
    public boolean validate(Integer value) {
      return value > 0;
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

    @Override
    public boolean validate(Integer value) {
      return value > 0;
    }
  }
}
