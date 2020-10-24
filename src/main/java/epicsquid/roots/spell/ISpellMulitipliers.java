package epicsquid.roots.spell;

import net.minecraft.util.math.MathHelper;

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
    return MathHelper.floor(value - value * getAmplifyValue());
  }

  default int ampSubInt(float value) {
    return MathHelper.floor(value - value * getAmplifyValue());
  }

  default int ampSubInt(double value) {
    return MathHelper.floor(value - value * getAmplifyValue());
  }

  default float ampSubFloat(int value) {
    return (float) (value - value * getAmplifyValue());
  }

  default float ampSubFloat(float value) {
    return (float) (value - value * getAmplifyValue());
  }

  default float ampSubFloat(double value) {
    return (float) (value - value * getAmplifyValue());
  }

  default double ampSubDouble(int value) {
    return value - value * getAmplifyValue();
  }

  default double ampSubDouble(double value) {
    return value - value * getAmplifyValue();
  }

  default double ampSubDouble(float value) {
    return value - value * getAmplifyValue();
  }

  default int ampInt(int value) {
    return MathHelper.floor(value + value * getAmplifyValue());
  }

  default int ampInt(float value) {
    return MathHelper.floor(value + value * getAmplifyValue());
  }

  default int ampInt(double value) {
    return MathHelper.floor(value + value * getAmplifyValue());
  }

  default float ampFloat(int value) {
    return (float) (value + value * getAmplifyValue());
  }

  default float ampFloat(float value) {
    return (float) (value + value * getAmplifyValue());
  }

  default float ampFloat(double value) {
    return (float) (value + value * getAmplifyValue());
  }

  default double ampDouble(int value) {
    return value + value * getAmplifyValue();
  }

  default double ampDouble(double value) {
    return value + value * getAmplifyValue();
  }

  default double ampDouble(float value) {
    return value + value * getAmplifyValue();
  }

  default float speedSubFloat(int value) {
    return (float) (value - value * getSpeedValue());
  }

  default float speedSubFloat(double value) {
    return (float) (value - value * getSpeedValue());
  }

  default float speedSubFloat(float value) {
    return (float) (value - value * getSpeedValue());
  }

  default double speedSubDouble(int value) {
    return value - value * getSpeedValue();
  }

  default double speedSubDouble(float value) {
    return value - value * getSpeedValue();
  }

  default double speedSubDouble(double value) {
    return value - value * getSpeedValue();
  }

  default int speedSubInt(int value) {
    return MathHelper.floor(value - value * getSpeedValue());
  }

  default int speedSubInt(float value) {
    return MathHelper.floor(value - value * getSpeedValue());
  }

  default int speedSubInt(double value) {
    return MathHelper.floor(value - value * getSpeedValue());
  }

  enum Buff {
    NONE, BONUS, GREATER_BONUS;
  }
}
