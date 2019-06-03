package epicsquid.roots.api;

import net.minecraftforge.common.EnumPlantType;

public class CustomPlantType {

  /**
   * ID: 0
   */
  public static final EnumPlantType ELEMENT_FIRE = EnumPlantType.getPlantType("element_fire");
  /**
   * ID: 1
   */
  public static final EnumPlantType ELEMENT_WATER = EnumPlantType.getPlantType("element_water");
  /**
   * ID: 2
   */
  public static final EnumPlantType ELEMENT_AIR = EnumPlantType.getPlantType("element_air");
  /**
   * ID: 3
   */
  public static final EnumPlantType ELEMENT_EARTH = EnumPlantType.getPlantType("element_earth");

  public static EnumPlantType getTypeFromId(int id)
  {
    switch (id)
    {
      case 0: return ELEMENT_FIRE;
      case 1: return ELEMENT_WATER;
      case 2: return ELEMENT_AIR;
      case 3: return ELEMENT_EARTH;
      default: return null;
    }
  }
}
