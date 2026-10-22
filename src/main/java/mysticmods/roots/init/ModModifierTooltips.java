package mysticmods.roots.init;

import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.tooltip.Formatters;
import mysticmods.roots.api.tooltip.TooltipBuilder;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class ModModifierTooltips {
  public static class Tooltip implements UnaryOperator<TooltipBuilder<SpellModifier.Properties>> {
    private final Consumer<TooltipBuilder<SpellModifier.Properties>> modifier;

    public Tooltip(Consumer<TooltipBuilder<SpellModifier.Properties>> modifier) {
      this.modifier = modifier;
    }

    public Tooltip(String message) {
      this((tooltip) -> tooltip.simple(message));
    }

    @Override
    public TooltipBuilder<SpellModifier.Properties> apply(TooltipBuilder<SpellModifier.Properties> propertiesTooltipBuilder) {
      this.modifier.accept(propertiesTooltipBuilder);
      return propertiesTooltipBuilder;
    }
  }

  public static final Tooltip ACID_CLOUD_FIRE = new Tooltip(tooltip ->
      tooltip.simple("Damaged entities are also set on fire.")
          .segment("Damaged entities that are not currently on fire are set on fire for %s.", Formatters.forProperty(ModSpells.ACID_CLOUD_FIRE_TICKS)
              .by(Formatters.TICKS))
  );

  public static final Tooltip ACID_CLOUD_PEACEFUL = new Tooltip("Prevents damage to non-hostile entities.");

  public static final Tooltip ACID_CLOUD_KNOCKBACK = new Tooltip("Prevents damaged entities from being knocked back when damaged by acid cloud.");

  public static final Tooltip ACID_CLOUD_SLOWNESS = new Tooltip(tooltip ->
      tooltip.simple("Damaged entities are also slowed.")
          .segment("Damaged entities are afflicted with Slowness %s", Formatters.forProperty(ModSpells.ACID_CLOUD_SLOW_AMPLIFIER)
              .by(Formatters.LEVEL))
          .segment("for %s. This effect will not be refreshed until it expires.", Formatters.forProperty(ModSpells.ACID_CLOUD_SLOW_DURATION)
              .by(Formatters.TICKS)));

  public static final Tooltip ACID_CLOUD_TEMPORAL_MORASS = new Tooltip(tooltip -> tooltip.simple("Summons a Temporal Morass to the location where Acid Cloud was cast. The morass drastically slows any entities that pass through it.")
      .segment("Summons a Temporal Morass to the location where Acid Cloud was cast. The morass covers an area of %s blocks", Formatters.forProperty(ModSpells.TEMPORAL_MORASS_RADIUS_XZ, ModSpells.TEMPORAL_MORASS_RADIUS_Y)
          .by(Formatters.BLOCK_RANGE))
      .segment("and lasts for %s.", Formatters.forProperty(ModSpells.TEMPORAL_MORASS_DURATION).by(Formatters.TICKS))
      .segment("Entities are afflicted with Slowness %s as long as they remain in the morass.", Formatters.forProperty(ModSpells.TEMPORAL_MORASS_AMPLIFIER)
          .by(Formatters.LEVEL))
      .segment("Summoning a morass triggers a cooldown of %s.", Formatters.forProperty(ModSpells.TEMPORAL_MORASS_COOLDOWN)
          .by(Formatters.TICKS)));

  public static final Tooltip DANDELION_WINDS_DURATION = new Tooltip(tooltip -> tooltip.simple("Increases the duration of the Dandelion Winds effect.")
      .segment("Increases the duration of the boost effect by %s", Formatters.forProperty(ModSpells.DANDELION_WINDS_DURATION_INCREASE)
          .by(Formatters.TICKS).multipliedByTier())
      .segment("to a total of %s.", Formatters.forProperty(ModSpells.DANDELION_WINDS_DURATION).by(Formatters.TICKS)
          .multipliedByTier(ModSpells.DANDELION_WINDS_DURATION_INCREASE)));

  public static final Tooltip DANDELION_WINDS_CHANCE = new Tooltip(tooltip -> tooltip.simple("Increases the chance to deflect projectiles.")
      .segment("Increases the chance to deflect projectiles by %s", Formatters.forProperty(ModSpells.DANDELION_WINDS_PROJECTILE_DEFLECTION_INCREASE)
          .by(Formatters.PERCENT).multipliedByTier())
      .segment("to a total of %s.", Formatters.forProperty(ModSpells.DANDELION_WINDS_PROJECTILE_DEFLECTION_CHANCE)
          .multipliedByTier(ModSpells.DANDELION_WINDS_PROJECTILE_DEFLECTION_INCREASE).by(Formatters.PERCENT)));

  // TODO:
  public static final Tooltip DANDELION_WINDS_VORTEX = new Tooltip("Causes ranged attackers to be pulled towards you.");
  public static final Tooltip DANDELION_WINDS_GUSTS = new Tooltip("Causes melee attackers to be pushed away from you.");

  public static final Tooltip DANDELION_WINDS_VORTEX_COOLDOWN = new Tooltip(tooltip -> tooltip.simple("Reduces the cooldown of the Vortex effect.")
      .segment("Reduces the cooldown of the Vortex effect by %s.", Formatters.forProperty(ModSpells.DANDELION_WINDS_VORTEX_COOLDOWN_DECREASE)
          .by(Formatters.TICKS).multipliedByTier()));

  public static final Tooltip DANDELION_WINDS_GUSTS_COOLDOWN = new Tooltip(tooltip -> tooltip.simple("Reduces the cooldown of the Gusts effect.")
      .segment("Reduces the cooldown of the Gusts effect by %s.", Formatters.forProperty(ModSpells.DANDELION_WINDS_GUSTS_COOLDOWN_DECREASE)
          .by(Formatters.TICKS).multipliedByTier()));

  public static final Tooltip DANDELION_WINDS_STATUE = new Tooltip("Increases your knockback resistance. [NYI]");
  public static final Tooltip DANDELION_WINDS_INFERNO = new Tooltip("Enflames nearby enemies. [NYI]");

  public static final Tooltip GROWTH_INFUSION_TARGETED_GROWTH = new Tooltip("Causes accelerated growth to only the targeted block. [NYI]");

  public static final Tooltip GROWTH_INFUSION_HYDRATION = new Tooltip("Farmland beneath grown crops has its moisture level increased by 1.");

  public static final Tooltip GROWTH_INFUSION_RAMPANT_GROWTH = new Tooltip(tooltip ->
      tooltip.simple("Causes accelerated growth to all eligible blocks in the area around you.")
          .segment("Causes accelerated growth for up to %s eligible blocks", Formatters.forProperty(ModSpells.GROWTH_INFUSION_RAMPANT_GROWTH_COUNT)
              .by(Formatters.NUMBER))
          .segment("within %s blocks", Formatters.forProperty(ModSpells.GROWTH_INFUSION_RAMPANT_GROWTH_RADIUS_XZ, ModSpells.GROWTH_INFUSION_RAMPANT_GROWTH_RADIUS_Y)
              .by(Formatters.BLOCK_RANGE))
          .segment("every %s while channeled.", Formatters.forProperty(ModSpells.GROWTH_INFUSION_RAMPANT_GROWTH_INTERVAL)
              .by(Formatters.TICKS))
          .segment("The number of ticks applied to each block varies."));

  public static final Tooltip GROWTH_INFUSION_FERTILIZER = new Tooltip(tooltip -> tooltip.simple("Duplicates the effects of using bone meal on the targeted block.")
      .segment("Applies up to %s stacks of bone meal to the targeted block.", Formatters.forProperty(ModSpells.GROWTH_INFUSION_BONE_MEAL_COUNT)
          .by(Formatters.NUMBER)));

  public static final Tooltip SHATTER_ADJUSTABLE = new Tooltip("Allows adjustment of the width, depth and height.");

  public static final Tooltip SHATTER_DEPTH = new Tooltip(tooltip -> tooltip.simple("Increases the depth of the Shatter spell.")
      .segment("Increases the depth of the Shatter spell to %s blocks.", Formatters.forProperty(ModSpells.SHATTER_DEPTH_INCREASE)
          .multipliedByTier().by(Formatters.NUMBER)));

  public static final Tooltip SHATTER_WIDTH = new Tooltip(tooltip -> tooltip.simple("Increases the width of the Shatter spell.")
      .segment("Increases the width of the Shatter spell to %s blocks.", Formatters.forProperty(ModSpells.SHATTER_WIDTH_INCREASE)
          .multipliedByTier().by(Formatters.NUMBER)));

  public static final Tooltip SHATTER_HEIGHT = new Tooltip(tooltip -> tooltip.simple("Increases the height of the Shatter spell.")
      .segment("Increases the height of the Shatter spell to %s blocks.", Formatters.forProperty(ModSpells.SHATTER_HEIGHT_INCREASE)
          .multipliedByTier().by(Formatters.NUMBER)));

  public static final Tooltip SHATTER_MAGNETISM = new Tooltip("Teleports dropped blocks to the player.");
  public static final Tooltip SHATTER_SILK_TOUCH = new Tooltip("Blocks are broken as though with shears or a Silk Touch enchanted tool.");
  public static final Tooltip SHATTER_SMELTING = new Tooltip("Drops from broken blocks are smelted.");
  public static final Tooltip SHATTER_FORTUNE_1 = new Tooltip("Applies Fortune I when breaking blocks.");
  public static final Tooltip SHATTER_FORTUNE_2 = new Tooltip("Applies Fortune II when breaking blocks.");
  public static final Tooltip SHATTER_FORTUNE_3 = new Tooltip("Applies Fortune III when breaking blocks.");

  public static final Tooltip SKY_SOARER_AMPLIFIED = new Tooltip(tooltip -> tooltip.simple("Increases speed of boost effect.")
      .segment("Increases boost effect to %s.", Formatters.forProperty(ModSpells.SKY_SOARER_AMPLIFIER_INCREASE)
          .multipliedByTier().by(Formatters.SPRINT_SPEED)));

  public static final Tooltip SKY_SOARER_SPEEDY = new Tooltip(tooltip -> tooltip.simple("Increases duration of boost effect.")
      .segment("Increases duration of boost effect [by %s]", Formatters.forProperty(ModSpells.SKY_SOARER_DURATION_INCREASE)
          .multipliedByTier().by(Formatters.PERCENT))
      .segment("to %s.", Formatters.forProperty(ModSpells.SKY_SOARER_DURATION)
          .multipliedByTier(ModSpells.SKY_SOARER_DURATION_INCREASE).by(Formatters.TICKS)));

  public static final Tooltip SKY_SOARER_FRIENDLY_EARTH = new Tooltip(tooltip -> tooltip.simple("Prevents you from taking fall damage after the boost effect expires.")
      .segment("Prevents you from taking fall damage for %s after the boost effect ends.", Formatters.forProperty(ModSpells.SKY_SOARER_FRIENDLY_EARTH_DURATION)
          .by(Formatters.TICKS)));

  public static final Tooltip SYLVAN_LIGHT_COLOR = new Tooltip("Allows Sylvan Light to be different colours.");

  public static final Tooltip SYLVAN_LIGHT_CYAN = new Tooltip("Colours the Sylvan Light cyan.");
  public static final Tooltip SYLVAN_LIGHT_LIME = new Tooltip("Colours the Sylvan Light lime.");
  public static final Tooltip SYLVAN_LIGHT_ORANGE = new Tooltip("Colours the Sylvan Light orange.");
  public static final Tooltip SYLVAN_LIGHT_PINK = new Tooltip("Colours the Sylvan Light pink.");
  public static final Tooltip SYLVAN_LIGHT_WHITE = new Tooltip("Colours the Sylvan Light white.");
}
