package epicsquid.roots.integration.patchouli;

import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.Map;

public class ConfigKeys {
  public static void init() {
    for (Map.Entry<String, SpellBase> spell : SpellRegistry.spellRegistry.entrySet()) {
      PatchouliAPI.instance.setConfigFlag("roots:" + spell.getKey(), !spell.getValue().isDisabled());
    }
    for (Map.Entry<String, RitualBase> ritual : RitualRegistry.ritualRegistry.entrySet()) {
      PatchouliAPI.instance.setConfigFlag("roots:" + ritual.getKey(), !ritual.getValue().isDisabled());
    }
  }
}
