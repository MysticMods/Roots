package epicsquid.roots.spell;

import epicsquid.roots.Roots;
import epicsquid.roots.config.SpellConfig;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SpellRegistry {
  public static Map<ResourceLocation, SpellBase> spellRegistry = new HashMap<>();

  public static Collection<SpellBase> getSpells() {
    return spellRegistry.values();
  }

  public static SpellBase getSpell(String s) {
    return getSpell(new ResourceLocation(Roots.MODID, s));
  }

  public static SpellBase getSpell(ResourceLocation rl) {
    if (rl.equals(FakeSpell.INSTANCE.getRegistryName())) {
      return FakeSpell.INSTANCE;
    }

    SpellBase spell = spellRegistry.get(rl);
    if (spell == null) {
      return null;
    }

    return spell;
  }

  public static SpellBase getSpell(NBTTagString tag) {
    ResourceLocation rl = new ResourceLocation(tag.getString());
    return getSpell(rl);
  }

  public static void preInit() {
    spellRegistry.put(SpellWildfire.spellName, SpellWildfire.instance);
    SpellWildfire.instance.setDisabled(SpellConfig.disableSpellsCategory.disableWildFire);
    spellRegistry.put(SpellSanctuary.spellName, SpellSanctuary.instance);
    SpellSanctuary.instance.setDisabled(SpellConfig.disableSpellsCategory.disableSanctuary);
    spellRegistry.put(SpellDandelionWinds.spellName, SpellDandelionWinds.instance);
    SpellDandelionWinds.instance.setDisabled(SpellConfig.disableSpellsCategory.disableDandelionWinds);
    spellRegistry.put(SpellRoseThorns.spellName, SpellRoseThorns.instance);
    SpellRoseThorns.instance.setDisabled(SpellConfig.disableSpellsCategory.disableRoseThorns);
    spellRegistry.put(SpellShatter.spellName, SpellShatter.instance);
    SpellShatter.instance.setDisabled(SpellConfig.disableSpellsCategory.disableShatter);
    spellRegistry.put(SpellPetalShell.spellName, SpellPetalShell.instance);
    SpellPetalShell.instance.setDisabled(SpellConfig.disableSpellsCategory.disablePetalShell);
    spellRegistry.put(SpellTimeStop.spellName, SpellTimeStop.instance);
    SpellTimeStop.instance.setDisabled(SpellConfig.disableSpellsCategory.disableTimeStop);
    spellRegistry.put(SpellSkySoarer.spellName, SpellSkySoarer.instance);
    SpellSkySoarer.instance.setDisabled(SpellConfig.disableSpellsCategory.disableSkySoarer);
    spellRegistry.put(SpellLifeDrain.spellName, SpellLifeDrain.instance);
    SpellLifeDrain.instance.setDisabled(SpellConfig.disableSpellsCategory.disableLifeDrain);
    spellRegistry.put(SpellAcidCloud.spellName, SpellAcidCloud.instance);
    SpellAcidCloud.instance.setDisabled(SpellConfig.disableSpellsCategory.disableAcidCloud);
    spellRegistry.put(SpellGrowthInfusion.spellName, SpellGrowthInfusion.instance);
    SpellGrowthInfusion.instance.setDisabled(SpellConfig.disableSpellsCategory.disableGrowthInfusion);
    spellRegistry.put(SpellGeas.spellName, SpellGeas.instance);
    SpellGeas.instance.setDisabled(SpellConfig.disableSpellsCategory.disableGeas);
    spellRegistry.put(SpellRadiance.spellName, SpellRadiance.instance);
    SpellRadiance.instance.setDisabled(SpellConfig.disableSpellsCategory.disableRadiance);
    spellRegistry.put(SpellHarvest.spellName, SpellHarvest.instance);
    SpellHarvest.instance.setDisabled(SpellConfig.disableSpellsCategory.disableHarvest);
    spellRegistry.put(SpellFeyLight.spellName, SpellFeyLight.instance);
    SpellFeyLight.instance.setDisabled(SpellConfig.disableSpellsCategory.disableFeyLight);
    spellRegistry.put(SpellStormCloud.spellName, SpellStormCloud.instance);
    SpellStormCloud.instance.setDisabled(SpellConfig.disableSpellsCategory.disableWinterCloud);
    SpellSaturate.instance.setDisabled(SpellConfig.disableSpellsCategory.disableSaturate);
    spellRegistry.put(SpellSaturate.spellName, SpellSaturate.instance);
    SpellDesaturate.instance.setDisabled(SpellConfig.disableSpellsCategory.disableDesaturate);
    spellRegistry.put(SpellDesaturate.spellName, SpellDesaturate.instance);
    SpellChrysopoeia.instance.setDisabled(SpellConfig.disableSpellsCategory.disableChrysopoeia);
    spellRegistry.put(SpellChrysopoeia.spellName, SpellChrysopoeia.instance);
    // TODO: Where is the config for this?
    spellRegistry.put(SpellDisarm.spellName, SpellDisarm.instance);
    SpellDisarm.instance.setDisabled(SpellConfig.disableSpellsCategory.disableDisarm);
    spellRegistry.put(SpellNaturesScythe.spellName, SpellNaturesScythe.instance);
    SpellNaturesScythe.instance.setDisabled(SpellConfig.disableSpellsCategory.disableNaturesScythe);
    spellRegistry.put(SpellAquaBubble.spellName, SpellAquaBubble.instance);
    SpellAquaBubble.instance.setDisabled(SpellConfig.disableSpellsCategory.disableAquaBubble);
    spellRegistry.put(SpellAugment.spellName, SpellAugment.instance);
    SpellAugment.instance.setDisabled(SpellConfig.disableSpellsCategory.disableAugment);
    spellRegistry.put(SpellExtension.spellName, SpellExtension.instance);
    SpellExtension.instance.setDisabled(SpellConfig.disableSpellsCategory.disableExtension);
  }

  public static void init() {
    spellRegistry.values().forEach(SpellBase::init);
  }

  public static void finalise() {
    spellRegistry.values().forEach(SpellBase::finalise);
  }
}