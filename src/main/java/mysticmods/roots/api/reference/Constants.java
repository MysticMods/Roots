package mysticmods.roots.api.reference;

public class Constants {
  // Pedestal & Grove Crafter animation int constants
  public static final int PEDESTAL_ANIMATION_TICKS = 20 * 2;
  public static final int BASE_GROVE_CRAFTER_ANIMATION_TICKS = 20 * 6;

  public static final int GROVE_CRAFTING_ANIMATION_TICKS = PEDESTAL_ANIMATION_TICKS + BASE_GROVE_CRAFTER_ANIMATION_TICKS; // Total value for both, used for the item particles

  public static final int GROVE_PARTICLE_PEDESTAL_DELAY = PEDESTAL_ANIMATION_TICKS; // How long the particles are hidden for

  public static final int GROVE_PARTICLE_EXPAND_TICKS = 6; // How many ticks particles should spend spreading out

  public static final int GROVE_PARTICLE_PAUSE_TICKS = 10; // How many ticks after particles arrive at their destination should they stop moving for

  public static final int GROVE_PARTICLE_BEZIER_BEGIN = GROVE_PARTICLE_PEDESTAL_DELAY + GROVE_PARTICLE_EXPAND_TICKS; // How many ticks until the particles begin moving according to the bezier

  public static final int GROVE_PARTICLE_BEZIER_SHORTENING = GROVE_PARTICLE_PAUSE_TICKS + GROVE_PARTICLE_EXPAND_TICKS + GROVE_PARTICLE_BEZIER_BEGIN; // How many ticks should be subtracted from the entire lifetime in order to correctly calculate the duration of the bezier movement

  public static final int GROVE_PARTICLE_PEDESTAL_FADE_IN_TICKS = 4; // How many ticks (subtracted from the pedestal delay) from the particles to go from 0 alpha to 1 alpha

  public static final int GROVE_PARTICLE_PEDESTAL_FADE_IN_START = GROVE_PARTICLE_PEDESTAL_DELAY - GROVE_PARTICLE_PEDESTAL_FADE_IN_TICKS;

  // Fairy Hut constants
  public static final int FAIRY_HUT_MORNING_RESET = 2000; // Start of the "villager workday"
  public static final int FAIRY_HUT_AFTERNOON_RESET = 9000; // End of the "villager workday";

}
