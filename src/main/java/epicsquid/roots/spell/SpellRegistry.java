package epicsquid.roots.spell;

import java.util.HashMap;
import java.util.Map;

import epicsquid.mysticalworld.init.ModItems;

public class SpellRegistry {
  public static Map<String, SpellBase> spellRegistry = new HashMap<>();

  public static SpellBase spell_orange_tulip, spell_red_tulip, spell_dandelion, spell_rose, spell_azure_bluet, spell_peony, spell_oxeye_daisy, spell_blue_orchid, spell_pink_tulip, spell_allium, spell_lilac, spell_poppy, spell_sunflower, spell_white_tulip;

  public static void init() {
    spellRegistry.put("spell_orange_tulip", spell_orange_tulip = new SpellOrangeTulip("spell_orange_tulip").addCost(ModItems.wildroot, 0.125f));
    spellRegistry.put("spell_red_tulip",
        spell_red_tulip = new SpellRedTulip("spell_red_tulip").addCost(ModItems.moonglow_leaf, 0.125f).addCost(ModItems.aubergine_seed, 0.125f));
    spellRegistry.put("spell_dandelion", spell_dandelion = new SpellDandelion("spell_dandelion").addCost(ModItems.moonglow_leaf, 0.125f));
    spellRegistry.put("spell_rose", spell_rose = new SpellRose("spell_rose").addCost(ModItems.terra_moss, 0.25f));
    spellRegistry.put("spell_azure_bluet", spell_azure_bluet = new SpellAzureBluet("spell_azure_bluet").addCost(ModItems.wildroot, 0.0625f));
    spellRegistry.put("spell_peony", spell_peony = new SpellPeony("spell_peony").addCost(ModItems.aubergine_seed, 0.5f).addCost(ModItems.moonglow_leaf, 0.25f));
    spellRegistry.put("spell_oxeye_daisy",
        spell_oxeye_daisy = new SpellOxeyeDaisy("spell_oxeye_daisy").addCost(ModItems.pereskia, 0.5f).addCost(ModItems.moonglow_leaf, 0.25f)
            .addCost(ModItems.pereskia_bulb, 0.25f));
    spellRegistry.put("spell_blue_orchid", spell_blue_orchid = new SpellBlueOrchid("spell_blue_orchid").addCost(ModItems.pereskia, 0.125f));
    spellRegistry.put("spell_pink_tulip", spell_pink_tulip = new SpellPinkTulip("spell_pink_tulip").addCost(ModItems.pereskia_bulb, 0.125f));
    spellRegistry.put("spell_allium", spell_allium = new SpellAllium("spell_allium").addCost(ModItems.terra_moss, 0.0625f));
    spellRegistry.put("spell_lilac", spell_lilac = new SpellLilac("spell_lilac").addCost(ModItems.terra_moss, 0.125f));
    spellRegistry.put("spell_poppy", spell_poppy = new SpellPoppy("spell_poppy").addCost(ModItems.aubergine_seed, 0.25f).addCost(ModItems.terra_moss, 0.25f));
    spellRegistry.put("spell_sunflower",
        spell_sunflower = new SpellSunflower("spell_sunflower").addCost(ModItems.moonglow_leaf, 0.25f).addCost(ModItems.wildroot, 0.125f)
            .addCost(ModItems.aubergine_seed, 0.25f));
    spellRegistry.put("spell_white_tulip",
        spell_white_tulip = new SpellWhiteTulip("spell_white_tulip").addCost(ModItems.pereskia, 0.5f).addCost(ModItems.wildroot, 0.25f));
  }
}