package epicsquid.roots.spell;

public interface ISpellMulitipliers {
  Buff getAmplify ();
  Buff getSpeedy ();

  default double getAmplifyValue() {
    switch (getAmplify()) {
      default:
      case NONE:
        return 0;
      case BONUS:
        return 0.1;
      case GREATER_BONUS:
        return 0.3;
    }
  }

  default double getSpeedValue() {
    switch (getSpeedy()) {
      default:
      case NONE:
        return 0;
      case BONUS:
        return 0.1;
      case GREATER_BONUS:
        return 0.3;
    }
  }

  default int ampSubInt(int value) {
    return (int) (value - Math.floor((double) value * getAmplifyValue()));
  }

  default int ampSubInt(float value) {
    return (int) (value - Math.floor((double) value * getAmplifyValue()));
  }

  default int ampSubInt(double value) {
    return (int) (value - Math.floor(value * getAmplifyValue()));
  }

  default float ampSubFloat(int value) {
    return (float) (value - Math.floor(value * getAmplifyValue()));
  }

  default float ampSubFloat(float value) {
    return (float) (value - Math.floor(value * getAmplifyValue()));
  }

  default float ampSubFloat(double value) {
    return (float) (value - Math.floor(value * getAmplifyValue()));
  }

  default double ampSubDouble(int value) {
    return value - Math.floor(value * getAmplifyValue());
  }

  default double ampSubDouble(double value) {
    return value - Math.floor(value * getAmplifyValue());
  }

  default double ampSubDouble(float value) {
    return value - Math.floor(value * getAmplifyValue());
  }

  default int ampInt(int value) {
    return (int) (value + Math.floor((double) value * getAmplifyValue()));
  }

  default int ampInt(float value) {
    return (int) (value + Math.floor((double) value * getAmplifyValue()));
  }

  default int ampInt(double value) {
    return (int) (value + Math.floor(value * getAmplifyValue()));
  }

  default float ampFloat(int value) {
    return (float) (value + Math.floor(value * getAmplifyValue()));
  }

  default float ampFloat(float value) {
    return (float) (value + Math.floor(value * getAmplifyValue()));
  }

  default float ampFloat(double value) {
    return (float) (value + Math.floor(value * getAmplifyValue()));
  }

  default double ampDouble(int value) {
    return value + Math.floor(value * getAmplifyValue());
  }

  default double ampDouble(double value) {
    return value + Math.floor(value * getAmplifyValue());
  }

  default double ampDouble(float value) {
    return value + Math.floor(value * getAmplifyValue());
  }

  default float speedSubFloat(int value) {
    return (float) (value - Math.floor((float) value * getSpeedValue()));
  }

  default float speedSubFloat(double value) {
    return (float) (value - Math.floor(value * getSpeedValue()));
  }

  default float speedSubFloat(float value) {
    return (float) (value - Math.floor(value * getSpeedValue()));
  }

  default double speedSubDouble(int value) {
    return value - Math.floor((double) value * getSpeedValue());
  }

  default double speedSubDouble(float value) {
    return value - Math.floor(value * getSpeedValue());
  }

  default double speedSubDouble(double value) {
    return value - Math.floor(value * getSpeedValue());
  }

  default int speedSubInt(int value) {
    return (int) Math.floor((double) value * getSpeedValue());
  }

  default int speedSubInt(float value) {
    return (int) Math.floor(value * getSpeedValue());
  }

  default int speedSubInt(double value) {
    return (int) Math.floor(value * getSpeedValue());
  }

  enum Buff {
    NONE, BONUS, GREATER_BONUS;
  }
}
