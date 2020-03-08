package epicsquid.roots.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.util.zen.ZenDocClass;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;

@ZenRegister
@ZenClass("mods." + Roots.MODID + ".Herbs")
@SuppressWarnings("unused")
@ZenDocClass("mods." + Roots.MODID + ".Herbs")
public class Herbs {
  @ZenProperty
  public static final Herb pereskia = new Herb("pereskia");
  @ZenProperty
  public static final Herb terra_moss = new Herb("terra_moss");
  @ZenProperty
  public static final Herb spirit_herb = new Herb("spirit_herb");
  @ZenProperty
  public static final Herb baffle_cap = new Herb("baffle_cap");
  @ZenProperty
  public static final Herb moonglow_leaf = new Herb("moonglow_leaf");
  @ZenProperty
  public static final Herb wildroot = new Herb("wildroot");
  @ZenProperty
  public static final Herb wildewheet = new Herb("wildewheet");
  @ZenProperty
  public static final Herb infernal_bulb = new Herb("infernal_bulb");
  @ZenProperty
  public static final Herb dewgonia = new Herb("dewgonia");
  @ZenProperty
  public static final Herb stalicripe = new Herb("stalicripe");
  @ZenProperty
  public static final Herb cloud_berry = new Herb("cloud_berry");

  @ZenRegister
  @ZenClass("mods." + Roots.MODID + ".Herb")
  public static class Herb {
    private String herbName;
    private epicsquid.roots.api.Herb original;

    public Herb(String herbName) {
      this.herbName = herbName;
      this.original = HerbRegistry.getHerbByName(herbName);
    }

    @ZenMethod
    public String getHerbName() {
      return herbName;
    }

    public epicsquid.roots.api.Herb getOriginal() {
      return original;
    }
  }


}
