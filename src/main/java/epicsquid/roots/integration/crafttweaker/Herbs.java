package epicsquid.roots.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import epicsquid.roots.Roots;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import epicsquid.roots.util.zen.ZenDocProperty;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;

@ZenRegister
@ZenClass("mods." + Roots.MODID + ".Herbs")
@ZenDocAppend({"docs/include/herbs.example.md"})
@ZenDocClass("mods." + Roots.MODID + ".Herbs")
@SuppressWarnings("unused")
public class Herbs {
  @ZenProperty
  @ZenDocProperty(order = 1, description = "Wildroot Herb used for Spell Cost properties.")
  public static final Herb wildroot = new Herb("wildroot");
  @ZenProperty
  @ZenDocProperty(order = 2, description = "Terra Moss Herb used for Spell Cost properties.")
  public static final Herb terra_moss = new Herb("terra_moss");
  @ZenProperty
  @ZenDocProperty(order = 3, description = "Infernal Bulb Herb used for Spell Cost properties.")
  public static final Herb infernal_bulb = new Herb("infernal_bulb");
  @ZenProperty
  @ZenDocProperty(order = 4, description = "Dewgonia Herb used for Spell Cost properties.")
  public static final Herb dewgonia = new Herb("dewgonia");
  @ZenProperty
  @ZenDocProperty(order = 5, description = "Stalicripe Herb used for Spell Cost properties.")
  public static final Herb stalicripe = new Herb("stalicripe");
  @ZenProperty
  @ZenDocProperty(order = 6, description = "Cloud Berry Herb used for Spell Cost properties.")
  public static final Herb cloud_berry = new Herb("cloud_berry");
  @ZenProperty
  @ZenDocProperty(order = 7, description = "Baffle Cap Herb used for Spell Cost properties.")
  public static final Herb baffle_cap = new Herb("baffle_cap");
  @ZenProperty
  @ZenDocProperty(order = 8, description = "Pereskia Herb used for Spell Cost properties.")
  public static final Herb pereskia = new Herb("pereskia");
  @ZenProperty
  @ZenDocProperty(order = 9, description = "Spirit Herb Herb used for Spell Cost properties.")
  public static final Herb spirit_herb = new Herb("spirit_herb");
  @ZenProperty
  @ZenDocProperty(order = 10, description = "Wildewheet Herb used for Spell Cost properties.")
  public static final Herb wildewheet = new Herb("wildewheet");
  @ZenProperty
  @ZenDocProperty(order = 11, description = "Moonglow Leaf Herb used for Spell Cost properties.")
  public static final Herb moonglow_leaf = new Herb("moonglow_leaf");

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
    @ZenDocMethod(order = 1, description = "returns the string name of the herb")
    public String getHerbName() {
      return herbName;
    }

    public epicsquid.roots.api.Herb getOriginal() {
      return original;
    }
  }
}
