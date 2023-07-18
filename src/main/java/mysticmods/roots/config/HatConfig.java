package mysticmods.mysticalworld.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class HatConfig extends AbstractConfig {
  protected ForgeConfigSpec.IntValue configAntlerFrequency;
  protected ForgeConfigSpec.IntValue configAntlerThreshold;
  protected ForgeConfigSpec.DoubleValue configAntlerHealing;
  protected ForgeConfigSpec.IntValue configAntlerRegenDuration;
  protected ForgeConfigSpec.IntValue configAntlerRegenAmplifier;
  protected ForgeConfigSpec.DoubleValue configAntlerHealthBonus;
  protected ForgeConfigSpec.IntValue configAntlerDamage;
  protected ForgeConfigSpec.DoubleValue configMaskDamageBonus;

  public int antlerFrequency = 50;
  public int antlerThreshold = -1;
  public double antlerHealing = 2.0;
  public int antlerRegenDuration = 130;
  public int antlerRegenAmplifier = 1;
  public float antlerHealthBonus = 4f;
  public int antlerDamage = 1;
  public double maskBonusDamage = 0.3;

  public HatConfig() {
    super();
  }

  @Override
  public void apply(ForgeConfigSpec.Builder builder) {
    configAntlerFrequency = builder.comment("Spawn frequency (1 in X chances per tick while spawn conditions are met, -1 for not at all)").defineInRange("antler_frequency", antlerFrequency, 0, Integer.MAX_VALUE);
    configAntlerThreshold = builder.comment("How many hearts under maximum health the player needs to be for a deer to spawn (-1 for any value under maximum health").defineInRange("antler_threshold", antlerThreshold, -1, Integer.MAX_VALUE);
    configAntlerHealing = builder.comment("How much a Spirit Deer should heal for").defineInRange("antler_healing", antlerHealing, 0.5, Double.MAX_VALUE);
    configAntlerRegenDuration = builder.comment("How long a duration Regeneration should be applied for").defineInRange("antler_regen_duration", antlerRegenDuration, 1, Integer.MAX_VALUE);
    configAntlerRegenAmplifier = builder.comment("What amplifier should be applied to the Regeneration effect (0 = I, 1 = II, etc)").defineInRange("antler_regen_amplifier", antlerRegenAmplifier, 0, Integer.MAX_VALUE);
    configAntlerHealthBonus = builder.comment("How much of a health bonus wearing the hat should give (-1 for no bonus, 2 for a single heart, 4 for two hearts, etc").defineInRange("antler_health_bonus", antlerHealthBonus, 0.5, Double.MAX_VALUE);
    configAntlerDamage = builder.comment("How much damage to the antler hat spawning a spirit deer causes (-1 for no damage)").defineInRange("antler_damage", antlerDamage, -1, Integer.MAX_VALUE);
    configMaskDamageBonus = builder.comment("How much of a damage to bonus should be provided by the mask (-1 for none)").defineInRange("mask_bonus_damage", maskBonusDamage, -1, Double.MAX_VALUE);
  }

  public int getAntlerFrequency() {
    return configAntlerFrequency.get();
  }

  public int getAntlerThreshold() {
    return configAntlerThreshold.get();
  }

  public float getAntlerHealing() {
    return (float) (double) configAntlerHealing.get();
  }

  public int getAntlerRegenDuration() {
    return configAntlerRegenDuration.get();
  }

  public int getAntlerRegenAmplifier() {
    return configAntlerRegenAmplifier.get();
  }

  public float getAntlerHealthBonus() {
    return (float) (double) configAntlerHealthBonus.get();
  }

  public int getAntlerDamage() {
    return configAntlerDamage.get();
  }

  public double getMaskBonusDamage() {
    return configMaskDamageBonus.get();
  }

  public void reset() {
  }
}
