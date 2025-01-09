package mysticmods.roots.gen;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.client.KeyBindings;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModEffects;
import mysticmods.roots.init.ModEntities;
import mysticmods.roots.init.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;


public class RootsLangProvider extends LanguageProvider {
  public RootsLangProvider(PackOutput output) {
    super(output, RootsAPI.MODID, "en_us");
  }

  @Override
  protected void addTranslations() {
    // Tag translations
    add("itemGroup.roots", "Roots");

    add("roots.tooltip.token.spell", "Spell: %s");
    add("roots.tooltip.token.modifier", "Modifier: %s");
    add("roots.tooltip.token.unlock", "Right-Click to unlock.");
    add("roots.tooltip.token.unlocked", "You've already unlocked this.");
    add("roots.tooltip.token.available_modifiers", "Available modifiers:");
    add("roots.tooltip.token.enabled_modifiers", "Enabled modifiers:");
    add("roots.tooltip.token.ritual", "Ritual: %s");

    add("roots.tooltip.cost.herb_cost", "%s %s");
    add("roots.tooltip.cost.cost_amount", "x%s");
    add("roots.tooltip.cost.cost_multiplier", "+%s");

    add("roots.tooltip.staff.selected", "Selected Slot: %s");
    add("roots.tooltip.staff.no_spell", "No spell.");
    add("roots.tooltip.staff.spell_in_slot", "%s: %s%s");
    add("roots.tooltip.staff.is_selected", " (Selected)");

    add("roots.tooltip.hold_shift", "[Hold %s for more information.]");
    add("roots.tooltip.shift", "Shift");

    add("roots.item.staff.with_spell", "Staff (%s)");

    add("roots.drinks.slow_regen", "Gives a burst of revitalizing energy.");
    add("roots.drinks.wakefulness", "Perks you up, night or day; shoos those scary phantoms away!");
    add("roots.drinks.sour", "Sour and awful to drink! Leaves you hungry.");
    add("message.dandelion_cordial", "You feel well-rested!");

    add("roots.message.recipe.requires", "Requires: %s");
    add("roots.message.recipe.failures", "A number of conditions were not met:");
    add("roots.message.staff.missing_herbs", "Unable to cast %s, missing herbs.");

    add("roots.message.spell.learned", "Learned new spell: %s");
    add("roots.message.spell.already_learned", "You already know the spell: %s");
    add("roots.message.modifier.learned", "Learned new modifier: %s");
    add("roots.message.modifier.already_learned", "You already know the modifier: %s");
    add("roots.message.grants_failed", "You've already learned everything this recipe can teach you!");

    // Squid-related stuff
    add("roots.message.squid.cooldown", "Give it time to produce more ink!");
    add("roots.message.runic_shears.cooldown", "More time must pass before this entity can be sheared again.");
    add("roots.subtitles.entity.squid.milk", "Squid milked");

    add("roots.subtitles.entity.fennec.aggro", "Fennec yips");
    add("roots.subtitles.entity.fennec.bark", "Fennec barks");
    add("roots.subtitles.entity.fennec.bite", "Fennec bites");
    add("roots.subtitles.entity.fennec.death", "Fennec dies");
    add("roots.subtitles.entity.fennec.eat", "Fennec eats");
    add("roots.subtitles.entity.fennec.idle", "Fennec yips");
    add("roots.subtitles.entity.fennec.sleep", "Fennec sleeps");
    add("roots.subtitles.entity.fennec.sniff", "Fennec sniffs");
    add("roots.subtitles.entity.fennec.spit", "Fennec spits");
    add("roots.subtitles.entity.sprout.ambient", "Sprout wanders");
    add("roots.subtitles.entity.duck.quack", "Duck quacks");
    add("roots.subtitles.entity.deer.ambient", "Deer squeals");

    add(KeyBindings.CATEGORY, "Roots");
    for (KeyMapping bind : KeyBindings.MAPPINGS) {
      String key = bind.getName();
      String[] segments = key.split("\\.");
      if (segments.length > 0) {
        add(bind.getName(), toEnglishName(segments[segments.length - 1]));
      } else {
        throw new IllegalStateException("Invalid keybind name: " + key);
      }
    }

    RootsRegistries.SPELLS.entrySet().forEach(o ->
        add(o.getValue().getDescriptionId(), toEnglishName(o.getKey().location().getPath()))
    );
    RootsRegistries.RITUALS.entrySet().forEach(o ->
        add(o.getValue().getDescriptionId(), toEnglishName(o.getKey().location().getPath()))
    );
    RootsRegistries.HERBS.entrySet().forEach(o ->
        add(o.getValue().getDescriptionId(), toEnglishName(o.getKey().location().getPath()))
    );
    RootsRegistries.SPELL_MODIFIERS.entrySet().forEach(o ->
        add(o.getValue().getDescriptionId(), toEnglishName(o.getKey().location().getPath()))
    );
    RootsRegistries.LEVEL_CONDITIONS.entrySet().forEach(o ->
        add(o.getValue().getDescriptionId(), toEnglishName(o.getKey().location().getPath()))
    );
    RootsRegistries.PLAYER_CONDITIONS.entrySet().forEach(o ->
        add(o.getValue().getDescriptionId(), toEnglishName(o.getKey().location().getPath()))
    );

    // Blocks
    add(ModBlocks.THATCH.get(), toEnglishName(ModBlocks.THATCH.getKey().location().getPath()));
    add(ModBlocks.RUNESTONE.get(), toEnglishName(ModBlocks.RUNESTONE.getKey().location().getPath()));
    add(ModBlocks.MOSSY_RUNESTONE.get(), toEnglishName(ModBlocks.MOSSY_RUNESTONE.getKey().location().getPath()));
    add(ModBlocks.CHISELED_RUNESTONE.get(), toEnglishName(ModBlocks.CHISELED_RUNESTONE.getKey().location().getPath()));
    add(ModBlocks.RUNESTONE_BRICK.get(), toEnglishName(ModBlocks.RUNESTONE_BRICK.getKey().location().getPath()));
    add(ModBlocks.RUNESTONE_TILE.get(), toEnglishName(ModBlocks.RUNESTONE_TILE.getKey().location().getPath()));
    add(ModBlocks.RUNED_OBSIDIAN.get(), toEnglishName(ModBlocks.RUNED_OBSIDIAN.getKey().location().getPath()));
    add(ModBlocks.CHISELED_RUNED_OBSIDIAN.get(), toEnglishName(ModBlocks.CHISELED_RUNED_OBSIDIAN.getKey().location().getPath()));
    add(ModBlocks.RUNED_BRICK.get(), toEnglishName(ModBlocks.RUNED_BRICK.getKey().location().getPath()));
    add(ModBlocks.RUNED_TILE.get(), toEnglishName(ModBlocks.RUNED_TILE.getKey().location().getPath()));
    add(ModBlocks.SILVER_ORE.get(), toEnglishName(ModBlocks.SILVER_ORE.getKey().location().getPath()));
    add(ModBlocks.DEEPSLATE_SILVER_ORE.get(), toEnglishName(ModBlocks.DEEPSLATE_SILVER_ORE.getKey().location().getPath()));
    add(ModBlocks.GRANITE_QUARTZ_ORE.get(), toEnglishName(ModBlocks.GRANITE_QUARTZ_ORE.getKey().location().getPath()));
    add(ModBlocks.RAW_SILVER_BLOCK.get(), toEnglishName(ModBlocks.RAW_SILVER_BLOCK.getKey().location().getPath()));
    add(ModBlocks.SILVER_BLOCK.get(), toEnglishName(ModBlocks.SILVER_BLOCK.getKey().location().getPath()));
    add(ModBlocks.WILDWOOD_LOG.get(), toEnglishName(ModBlocks.WILDWOOD_LOG.getKey().location().getPath()));
    add(ModBlocks.STRIPPED_WILDWOOD_LOG.get(), toEnglishName(ModBlocks.STRIPPED_WILDWOOD_LOG.getKey().location().getPath()));
    add(ModBlocks.WILDWOOD_WOOD.get(), toEnglishName(ModBlocks.WILDWOOD_WOOD.getKey().location().getPath()));
    add(ModBlocks.STRIPPED_WILDWOOD_WOOD.get(), toEnglishName(ModBlocks.STRIPPED_WILDWOOD_WOOD.getKey().location().getPath()));
    add(ModBlocks.WILDWOOD_PLANKS.get(), toEnglishName(ModBlocks.WILDWOOD_PLANKS.getKey().location().getPath()));
    add(ModBlocks.WILDWOOD_SAPLING.get(), toEnglishName(ModBlocks.WILDWOOD_SAPLING.getKey().location().getPath()));
    add(ModBlocks.STONEPETAL.get(), toEnglishName(ModBlocks.STONEPETAL.getKey().location().getPath()));
    add(ModBlocks.WILDWOOD_LEAVES.get(), toEnglishName(ModBlocks.WILDWOOD_LEAVES.getKey().location().getPath()));
    add(ModBlocks.RUNED_WILDWOOD_LOG.get(), toEnglishName(ModBlocks.RUNED_WILDWOOD_LOG.getKey().location().getPath()));
    add(ModBlocks.RUNED_SPRUCE_LOG.get(), toEnglishName(ModBlocks.RUNED_SPRUCE_LOG.getKey().location().getPath()));
    add(ModBlocks.RUNED_JUNGLE_LOG.get(), toEnglishName(ModBlocks.RUNED_JUNGLE_LOG.getKey().location().getPath()));
    add(ModBlocks.RUNED_BIRCH_LOG.get(), toEnglishName(ModBlocks.RUNED_BIRCH_LOG.getKey().location().getPath()));
    add(ModBlocks.RUNED_OAK_LOG.get(), toEnglishName(ModBlocks.RUNED_OAK_LOG.getKey().location().getPath()));
    add(ModBlocks.RUNED_DARK_OAK_LOG.get(), toEnglishName(ModBlocks.RUNED_DARK_OAK_LOG.getKey().location().getPath()));
    add(ModBlocks.RUNED_ACACIA_LOG.get(), toEnglishName(ModBlocks.RUNED_ACACIA_LOG.getKey().location().getPath()));
    add(ModBlocks.RUNED_MANGROVE_LOG.get(), toEnglishName(ModBlocks.RUNED_MANGROVE_LOG.getKey().location().getPath()));
    add(ModBlocks.RUNED_WARPED_STEM.get(), toEnglishName(ModBlocks.RUNED_WARPED_STEM.getKey().location().getPath()));
    add(ModBlocks.RUNED_CRIMSON_STEM.get(), toEnglishName(ModBlocks.RUNED_CRIMSON_STEM.getKey().location().getPath()));
    add(ModBlocks.RUNESTONE_STAIRS.get(), toEnglishName(ModBlocks.RUNESTONE_STAIRS.getKey().location().getPath()));
    add(ModBlocks.MOSSY_RUNESTONE_STAIRS.get(), toEnglishName(ModBlocks.MOSSY_RUNESTONE_STAIRS.getKey().location().getPath()));
    add(ModBlocks.RUNESTONE_BRICK_STAIRS.get(), toEnglishName(ModBlocks.RUNESTONE_BRICK_STAIRS.getKey().location().getPath()));
    add(ModBlocks.RUNESTONE_TILE_STAIRS.get(), toEnglishName(ModBlocks.RUNESTONE_TILE_STAIRS.getKey().location().getPath()));
    add(ModBlocks.RUNED_STAIRS.get(), toEnglishName(ModBlocks.RUNED_STAIRS.getKey().location().getPath()));
    add(ModBlocks.RUNED_BRICK_STAIRS.get(), toEnglishName(ModBlocks.RUNED_BRICK_STAIRS.getKey().location().getPath()));
    add(ModBlocks.RUNED_TILE_STAIRS.get(), toEnglishName(ModBlocks.RUNED_TILE_STAIRS.getKey().location().getPath()));
    add(ModBlocks.WILDWOOD_STAIRS.get(), toEnglishName(ModBlocks.WILDWOOD_STAIRS.getKey().location().getPath()));
    add(ModBlocks.RUNESTONE_SLAB.get(), toEnglishName(ModBlocks.RUNESTONE_SLAB.getKey().location().getPath()));
    add(ModBlocks.MOSSY_RUNESTONE_SLAB.get(), toEnglishName(ModBlocks.MOSSY_RUNESTONE_SLAB.getKey().location().getPath()));
    add(ModBlocks.RUNESTONE_BRICK_SLAB.get(), toEnglishName(ModBlocks.RUNESTONE_BRICK_SLAB.getKey().location().getPath()));
    add(ModBlocks.RUNESTONE_TILE_SLAB.get(), toEnglishName(ModBlocks.RUNESTONE_TILE_SLAB.getKey().location().getPath()));
    add(ModBlocks.RUNED_SLAB.get(), toEnglishName(ModBlocks.RUNED_SLAB.getKey().location().getPath()));
    add(ModBlocks.RUNED_BRICK_SLAB.get(), toEnglishName(ModBlocks.RUNED_BRICK_SLAB.getKey().location().getPath()));
    add(ModBlocks.RUNED_TILE_SLAB.get(), toEnglishName(ModBlocks.RUNED_TILE_SLAB.getKey().location().getPath()));
    add(ModBlocks.WILDWOOD_SLAB.get(), toEnglishName(ModBlocks.WILDWOOD_SLAB.getKey().location().getPath()));
    add(ModBlocks.WILDWOOD_FENCE.get(), toEnglishName(ModBlocks.WILDWOOD_FENCE.getKey().location().getPath()));
    add(ModBlocks.RUNESTONE_BUTTON.get(), toEnglishName(ModBlocks.RUNESTONE_BUTTON.getKey().location().getPath()));
    add(ModBlocks.RUNESTONE_BRICK_BUTTON.get(), toEnglishName(ModBlocks.RUNESTONE_BRICK_BUTTON.getKey().location().getPath()));
    add(ModBlocks.RUNESTONE_TILE_BUTTON.get(), toEnglishName(ModBlocks.RUNESTONE_TILE_BUTTON.getKey().location().getPath()));
    add(ModBlocks.MOSSY_RUNESTONE_BUTTON.get(), toEnglishName(ModBlocks.MOSSY_RUNESTONE_BUTTON.getKey().location().getPath()));
    add(ModBlocks.RUNED_BUTTON.get(), toEnglishName(ModBlocks.RUNED_BUTTON.getKey().location().getPath()));
    add(ModBlocks.RUNED_BRICK_BUTTON.get(), toEnglishName(ModBlocks.RUNED_BRICK_BUTTON.getKey().location().getPath()));
    add(ModBlocks.RUNED_TILE_BUTTON.get(), toEnglishName(ModBlocks.RUNED_TILE_BUTTON.getKey().location().getPath()));
    add(ModBlocks.WILDWOOD_BUTTON.get(), toEnglishName(ModBlocks.WILDWOOD_BUTTON.getKey().location().getPath()));
    add(ModBlocks.RUNESTONE_PRESSURE_PLATE.get(), toEnglishName(ModBlocks.RUNESTONE_PRESSURE_PLATE.getKey().location().getPath()));
    add(ModBlocks.RUNESTONE_BRICK_PRESSURE_PLATE.get(), toEnglishName(ModBlocks.RUNESTONE_BRICK_PRESSURE_PLATE.getKey().location().getPath()));
    add(ModBlocks.RUNESTONE_TILE_PRESSURE_PLATE.get(), toEnglishName(ModBlocks.RUNESTONE_TILE_PRESSURE_PLATE.getKey().location().getPath()));
    add(ModBlocks.MOSSY_RUNESTONE_PRESSURE_PLATE.get(), toEnglishName(ModBlocks.MOSSY_RUNESTONE_PRESSURE_PLATE.getKey().location().getPath()));
    add(ModBlocks.RUNED_PRESSURE_PLATE.get(), toEnglishName(ModBlocks.RUNED_PRESSURE_PLATE.getKey().location().getPath()));
    add(ModBlocks.RUNED_BRICK_PRESSURE_PLATE.get(), toEnglishName(ModBlocks.RUNED_BRICK_PRESSURE_PLATE.getKey().location().getPath()));
    add(ModBlocks.RUNED_TILE_PRESSURE_PLATE.get(), toEnglishName(ModBlocks.RUNED_TILE_PRESSURE_PLATE.getKey().location().getPath()));
    add(ModBlocks.WILDWOOD_PRESSURE_PLATE.get(), toEnglishName(ModBlocks.WILDWOOD_PRESSURE_PLATE.getKey().location().getPath()));
    add(ModBlocks.WILDWOOD_DOOR.get(), toEnglishName(ModBlocks.WILDWOOD_DOOR.getKey().location().getPath()));
    add(ModBlocks.WILDWOOD_TRAPDOOR.get(), toEnglishName(ModBlocks.WILDWOOD_TRAPDOOR.getKey().location().getPath()));
    add(ModBlocks.WILDWOOD_LADDER.get(), toEnglishName(ModBlocks.WILDWOOD_LADDER.getKey().location().getPath()));
    add(ModBlocks.WILDWOOD_GATE.get(), toEnglishName(ModBlocks.WILDWOOD_GATE.getKey().location().getPath()));
    add(ModBlocks.RUNESTONE_WALL.get(), toEnglishName(ModBlocks.RUNESTONE_WALL.getKey().location().getPath()));
    add(ModBlocks.MOSSY_RUNESTONE_WALL.get(), toEnglishName(ModBlocks.MOSSY_RUNESTONE_WALL.getKey().location().getPath()));
    add(ModBlocks.RUNESTONE_BRICK_WALL.get(), toEnglishName(ModBlocks.RUNESTONE_BRICK_WALL.getKey().location().getPath()));
    add(ModBlocks.RUNESTONE_TILE_WALL.get(), toEnglishName(ModBlocks.RUNESTONE_TILE_WALL.getKey().location().getPath()));
    add(ModBlocks.RUNED_WALL.get(), toEnglishName(ModBlocks.RUNED_WALL.getKey().location().getPath()));
    add(ModBlocks.RUNED_BRICK_WALL.get(), toEnglishName(ModBlocks.RUNED_BRICK_WALL.getKey().location().getPath()));
    add(ModBlocks.RUNED_TILE_WALL.get(), toEnglishName(ModBlocks.RUNED_TILE_WALL.getKey().location().getPath()));
    add(ModBlocks.ELEMENTAL_SOIL.get(), toEnglishName(ModBlocks.ELEMENTAL_SOIL.getKey().location().getPath()));
    add(ModBlocks.AQUEOUS_SOIL.get(), toEnglishName(ModBlocks.AQUEOUS_SOIL.getKey().location().getPath()));
    add(ModBlocks.CAELIC_SOIL.get(), toEnglishName(ModBlocks.CAELIC_SOIL.getKey().location().getPath()));
    add(ModBlocks.MAGMATIC_SOIL.get(), toEnglishName(ModBlocks.MAGMATIC_SOIL.getKey().location().getPath()));
    add(ModBlocks.TERRAN_SOIL.get(), toEnglishName(ModBlocks.TERRAN_SOIL.getKey().location().getPath()));
    add(ModBlocks.FEY_LIGHT.get(), toEnglishName(ModBlocks.FEY_LIGHT.getKey().location().getPath()));
    add(ModBlocks.RITUAL_PEDESTAL.get(), toEnglishName(ModBlocks.RITUAL_PEDESTAL.getKey().location().getPath()));
    add(ModBlocks.REINFORCED_RITUAL_PEDESTAL.get(), toEnglishName(ModBlocks.REINFORCED_RITUAL_PEDESTAL.getKey().location().getPath()));
    add(ModBlocks.GROVE_CRAFTER.get(), toEnglishName(ModBlocks.GROVE_CRAFTER.getKey().location().getPath()));
    add(ModBlocks.GROVE_PEDESTAL.get(), toEnglishName(ModBlocks.GROVE_PEDESTAL.getKey().location().getPath()));
    add(ModBlocks.WILDWOOD_PEDESTAL.get(), toEnglishName(ModBlocks.WILDWOOD_PEDESTAL.getKey().location().getPath()));
    add(ModBlocks.DISPLAY_PEDESTAL.get(), toEnglishName(ModBlocks.DISPLAY_PEDESTAL.getKey().location().getPath()));
    add(ModBlocks.WILD_ROOTS.get(), toEnglishName(ModBlocks.WILD_ROOTS.getKey().location().getPath()));
    add(ModBlocks.CREEPING_GROVE_MOSS.get(), toEnglishName(ModBlocks.CREEPING_GROVE_MOSS.getKey().location().getPath()));
    add(ModBlocks.HANGING_GROVE_MOSS.get(), toEnglishName(ModBlocks.HANGING_GROVE_MOSS.getKey().location().getPath()));
    add(ModBlocks.BAFFLECAP_BLOCK.get(), toEnglishName(ModBlocks.BAFFLECAP_BLOCK.getKey().location().getPath()));
    add(ModBlocks.PRIMAL_GROVE_STONE.get(), toEnglishName(ModBlocks.PRIMAL_GROVE_STONE.getKey().location().getPath()));
    add(ModBlocks.INCENSE_BURNER.get(), toEnglishName(ModBlocks.INCENSE_BURNER.getKey().location().getPath()));
    add(ModBlocks.MORTAR.get(), toEnglishName(ModBlocks.MORTAR.getKey().location().getPath()));
    add(ModBlocks.PYRE.get(), toEnglishName(ModBlocks.PYRE.getKey().location().getPath()));
    add(ModBlocks.REINFORCED_PYRE.get(), toEnglishName(ModBlocks.REINFORCED_PYRE.getKey().location().getPath()));
    add(ModBlocks.DECORATIVE_PYRE.get(), toEnglishName(ModBlocks.DECORATIVE_PYRE.getKey().location().getPath()));
    add(ModBlocks.UNENDING_BOWL.get(), toEnglishName(ModBlocks.UNENDING_BOWL.getKey().location().getPath()));
    add(ModBlocks.BAFFLECAP.get(), toEnglishName(ModBlocks.BAFFLECAP.getKey().location().getPath()));
    add(ModBlocks.WILDROOT_CROP.get(), toEnglishName(ModBlocks.WILDROOT_CROP.getKey().location().getPath()));
    add(ModBlocks.CLOUD_BERRY_CROP.get(), toEnglishName(ModBlocks.CLOUD_BERRY_CROP.getKey().location().getPath()));
    add(ModBlocks.DEWGONIA_CROP.get(), toEnglishName(ModBlocks.DEWGONIA_CROP.getKey().location().getPath()));
    add(ModBlocks.INFERNO_BULB_CROP.get(), toEnglishName(ModBlocks.INFERNO_BULB_CROP.getKey().location().getPath()));
    add(ModBlocks.STALICRIPE_CROP.get(), toEnglishName(ModBlocks.STALICRIPE_CROP.getKey().location().getPath()));
    add(ModBlocks.MOONGLOW_CROP.get(), toEnglishName(ModBlocks.MOONGLOW_CROP.getKey().location().getPath()));
    add(ModBlocks.PERESKIA_CROP.get(), toEnglishName(ModBlocks.PERESKIA_CROP.getKey().location().getPath()));
    add(ModBlocks.SPIRITLEAF_CROP.get(), toEnglishName(ModBlocks.SPIRITLEAF_CROP.getKey().location().getPath()));
    add(ModBlocks.WILDEWHEET_CROP.get(), toEnglishName(ModBlocks.WILDEWHEET_CROP.getKey().location().getPath()));
    add(ModBlocks.AUBERGINE_CROP.get(), toEnglishName(ModBlocks.AUBERGINE_CROP.getKey().location().getPath()));
    add(ModBlocks.WILD_AUBERGINE.get(), toEnglishName(ModBlocks.WILD_AUBERGINE.getKey().location().getPath()));
    add(ModBlocks.POTTED_BAFFLECAP.get(), toEnglishName(ModBlocks.POTTED_BAFFLECAP.getKey().location().getPath()));
    add(ModBlocks.POTTED_STONEPETAL.get(), toEnglishName(ModBlocks.POTTED_STONEPETAL.getKey().location().getPath()));
    add(ModBlocks.POTTED_WILDWOOD_SAPLING.get(), toEnglishName(ModBlocks.POTTED_WILDWOOD_SAPLING.getKey().location().getPath()));

    // Some potential duplicates with ModBlocks

    // Items
    add(ModItems.WILDROOT.get(), toEnglishName(ModItems.WILDROOT.getKey().location().getPath()));
    add(ModItems.GROVE_MOSS.get(), toEnglishName(ModItems.GROVE_MOSS.getKey().location().getPath()));
    add(ModItems.CLOUD_BERRY.get(), toEnglishName(ModItems.CLOUD_BERRY.getKey().location().getPath()));
    add(ModItems.DEWGONIA.get(), toEnglishName(ModItems.DEWGONIA.getKey().location().getPath()));
    add(ModItems.INFERNO_BULB.get(), toEnglishName(ModItems.INFERNO_BULB.getKey().location().getPath()));
    add(ModItems.STALICRIPE.get(), toEnglishName(ModItems.STALICRIPE.getKey().location().getPath()));
    add(ModItems.BAFFLECAP.get(), toEnglishName(ModItems.BAFFLECAP.getKey().location().getPath()));
    add(ModItems.MOONGLOW.get(), toEnglishName(ModItems.MOONGLOW.getKey().location().getPath()));
    add(ModItems.PERESKIA.get(), toEnglishName(ModItems.PERESKIA.getKey().location().getPath()));
    add(ModItems.SPIRITLEAF.get(), toEnglishName(ModItems.SPIRITLEAF.getKey().location().getPath()));
    add(ModItems.WILDEWHEET.get(), toEnglishName(ModItems.WILDEWHEET.getKey().location().getPath()));
    add(ModItems.MOONGLOW_SEEDS.get(), toEnglishName(ModItems.MOONGLOW_SEEDS.getKey().location().getPath()));
    add(ModItems.PERESKIA_BULB.get(), toEnglishName(ModItems.PERESKIA_BULB.getKey().location().getPath()));
    add(ModItems.SPIRITLEAF_SEEDS.get(), toEnglishName(ModItems.SPIRITLEAF_SEEDS.getKey().location().getPath()));
    add(ModItems.WILDEWHEET_SEEDS.get(), toEnglishName(ModItems.WILDEWHEET_SEEDS.getKey().location().getPath()));
    add(ModItems.GROVE_SPORES.get(), toEnglishName(ModItems.GROVE_SPORES.getKey().location().getPath()));
    add(ModItems.AUBERGINE_SEEDS.get(), toEnglishName(ModItems.AUBERGINE_SEEDS.getKey().location().getPath()));
    add(ModItems.CARAPACE.get(), toEnglishName(ModItems.CARAPACE.getKey().location().getPath()));
    add(ModItems.PELT.get(), toEnglishName(ModItems.PELT.getKey().location().getPath()));
    add(ModItems.ANTLERS.get(), toEnglishName(ModItems.ANTLERS.getKey().location().getPath()));
    add(ModItems.VENISON.get(), toEnglishName(ModItems.VENISON.getKey().location().getPath()));
    add(ModItems.COOKED_VENISON.get(), toEnglishName(ModItems.COOKED_VENISON.getKey().location().getPath()));
    add(ModItems.RAW_SQUID.get(), toEnglishName(ModItems.RAW_SQUID.getKey().location().getPath()));
    add(ModItems.COOKED_SQUID.get(), toEnglishName(ModItems.COOKED_SQUID.getKey().location().getPath()));
    add(ModItems.ASSORTED_SEEDS.get(), toEnglishName(ModItems.ASSORTED_SEEDS.getKey().location().getPath()));
    add(ModItems.COOKED_SEEDS.get(), toEnglishName(ModItems.COOKED_SEEDS.getKey().location().getPath()));
    add(ModItems.COOKED_BEETROOT.get(), toEnglishName(ModItems.COOKED_BEETROOT.getKey().location().getPath()));
    add(ModItems.COOKED_CARROT.get(), toEnglishName(ModItems.COOKED_CARROT.getKey().location().getPath()));
    add(ModItems.AUBERGINE.get(), toEnglishName(ModItems.AUBERGINE.getKey().location().getPath()));
    add(ModItems.COOKED_AUBERGINE.get(), toEnglishName(ModItems.COOKED_AUBERGINE.getKey().location().getPath()));
    add(ModItems.STUFFED_AUBERGINE.get(), toEnglishName(ModItems.STUFFED_AUBERGINE.getKey().location().getPath()));
    add(ModItems.AUBERGINE_SALAD.get(), toEnglishName(ModItems.AUBERGINE_SALAD.getKey().location().getPath()));
    add(ModItems.BEETROOT_SALAD.get(), toEnglishName(ModItems.BEETROOT_SALAD.getKey().location().getPath()));
    add(ModItems.STEWED_EGGPLANT.get(), toEnglishName(ModItems.STEWED_EGGPLANT.getKey().location().getPath()));
    add(ModItems.APPLE_CORDIAL.get(), toEnglishName(ModItems.APPLE_CORDIAL.getKey().location().getPath()));
    add(ModItems.CACTUS_SYRUP.get(), toEnglishName(ModItems.CACTUS_SYRUP.getKey().location().getPath()));
    add(ModItems.DANDELION_CORDIAL.get(), toEnglishName(ModItems.DANDELION_CORDIAL.getKey().location().getPath()));
    add(ModItems.LILAC_CORDIAL.get(), toEnglishName(ModItems.LILAC_CORDIAL.getKey().location().getPath()));
    add(ModItems.PEONY_CORDIAL.get(), toEnglishName(ModItems.PEONY_CORDIAL.getKey().location().getPath()));
    add(ModItems.ROSE_CORDIAL.get(), toEnglishName(ModItems.ROSE_CORDIAL.getKey().location().getPath()));
    add(ModItems.VINEGAR.get(), toEnglishName(ModItems.VINEGAR.getKey().location().getPath()));
    add(ModItems.VEGETABLE_JUICE.get(), toEnglishName(ModItems.VEGETABLE_JUICE.getKey().location().getPath()));
    add(ModItems.INK_BOTTLE.get(), toEnglishName(ModItems.INK_BOTTLE.getKey().location().getPath()));
    add(ModItems.ACACIA_BARK.get(), toEnglishName(ModItems.ACACIA_BARK.getKey().location().getPath()));
    add(ModItems.BIRCH_BARK.get(), toEnglishName(ModItems.BIRCH_BARK.getKey().location().getPath()));
    add(ModItems.DARK_OAK_BARK.get(), toEnglishName(ModItems.DARK_OAK_BARK.getKey().location().getPath()));
    add(ModItems.JUNGLE_BARK.get(), toEnglishName(ModItems.JUNGLE_BARK.getKey().location().getPath()));
    add(ModItems.OAK_BARK.get(), toEnglishName(ModItems.OAK_BARK.getKey().location().getPath()));
    add(ModItems.SPRUCE_BARK.get(), toEnglishName(ModItems.SPRUCE_BARK.getKey().location().getPath()));
    add(ModItems.WILDWOOD_BARK.get(), toEnglishName(ModItems.WILDWOOD_BARK.getKey().location().getPath()));
    add(ModItems.CRIMSON_BARK.get(), toEnglishName(ModItems.CRIMSON_BARK.getKey().location().getPath()));
    add(ModItems.WARPED_BARK.get(), toEnglishName(ModItems.WARPED_BARK.getKey().location().getPath()));
    add(ModItems.MANGROVE_BARK.get(), toEnglishName(ModItems.MANGROVE_BARK.getKey().location().getPath()));
    add(ModItems.MIXED_BARK.get(), toEnglishName(ModItems.MIXED_BARK.getKey().location().getPath()));
    add(ModItems.APOTHECARY_POUCH.get(), toEnglishName(ModItems.APOTHECARY_POUCH.getKey().location().getPath()));
    add(ModItems.COMPONENT_POUCH.get(), toEnglishName(ModItems.COMPONENT_POUCH.getKey().location().getPath()));
    add(ModItems.CREATIVE_POUCH.get(), toEnglishName(ModItems.CREATIVE_POUCH.getKey().location().getPath()));
    add(ModItems.FEY_POUCH.get(), toEnglishName(ModItems.FEY_POUCH.getKey().location().getPath()));
    add(ModItems.HERB_POUCH.get(), toEnglishName(ModItems.HERB_POUCH.getKey().location().getPath()));
    add(ModItems.COOKED_PERESKIA.get(), toEnglishName(ModItems.COOKED_PERESKIA.getKey().location().getPath()));
    add(ModItems.FLOUR.get(), toEnglishName(ModItems.FLOUR.getKey().location().getPath()));
    add(ModItems.WILDEWHEET_BREAD.get(), toEnglishName(ModItems.WILDEWHEET_BREAD.getKey().location().getPath()));
    add(ModItems.WILDROOT_STEW.get(), toEnglishName(ModItems.WILDROOT_STEW.getKey().location().getPath()));
    add(ModItems.FIRE_STARTER.get(), toEnglishName(ModItems.FIRE_STARTER.getKey().location().getPath()));
    add(ModItems.GRAMARY.get(), toEnglishName(ModItems.GRAMARY.getKey().location().getPath()));
    add(ModItems.LIVING_ARROW.get(), toEnglishName(ModItems.LIVING_ARROW.getKey().location().getPath()));
    add(ModItems.LIVING_AXE.get(), toEnglishName(ModItems.LIVING_AXE.getKey().location().getPath()));
    add(ModItems.LIVING_HOE.get(), toEnglishName(ModItems.LIVING_HOE.getKey().location().getPath()));
    add(ModItems.LIVING_PICKAXE.get(), toEnglishName(ModItems.LIVING_PICKAXE.getKey().location().getPath()));
    add(ModItems.LIVING_SHOVEL.get(), toEnglishName(ModItems.LIVING_SHOVEL.getKey().location().getPath()));
    add(ModItems.LIVING_SWORD.get(), toEnglishName(ModItems.LIVING_SWORD.getKey().location().getPath()));
    add(ModItems.PESTLE.get(), toEnglishName(ModItems.PESTLE.getKey().location().getPath()));
    add(ModItems.RUNED_AXE.get(), toEnglishName(ModItems.RUNED_AXE.getKey().location().getPath()));
    add(ModItems.RUNED_HOE.get(), toEnglishName(ModItems.RUNED_HOE.getKey().location().getPath()));
    add(ModItems.RUNED_SHOVEL.get(), toEnglishName(ModItems.RUNED_SHOVEL.getKey().location().getPath()));
    add(ModItems.RUNED_SWORD.get(), toEnglishName(ModItems.RUNED_SWORD.getKey().location().getPath()));
    add(ModItems.RUNIC_SHEARS.get(), toEnglishName(ModItems.RUNIC_SHEARS.getKey().location().getPath()));
    add(ModItems.STAFF.get(), toEnglishName(ModItems.STAFF.getKey().location().getPath()));
    add(ModItems.WILDWOOD_BOW.get(), toEnglishName(ModItems.WILDWOOD_BOW.getKey().location().getPath()));
    add(ModItems.WILDWOOD_QUIVER.get(), toEnglishName(ModItems.WILDWOOD_QUIVER.getKey().location().getPath()));
    add(ModItems.WOODEN_SHEARS.get(), toEnglishName(ModItems.WOODEN_SHEARS.getKey().location().getPath()));
    add(ModItems.WOODEN_KNIFE.get(), toEnglishName(ModItems.WOODEN_KNIFE.getKey().location().getPath()));
    add(ModItems.STONE_KNIFE.get(), toEnglishName(ModItems.STONE_KNIFE.getKey().location().getPath()));
    add(ModItems.COPPER_KNIFE.get(), toEnglishName(ModItems.COPPER_KNIFE.getKey().location().getPath()));
    add(ModItems.IRON_KNIFE.get(), toEnglishName(ModItems.IRON_KNIFE.getKey().location().getPath()));
    add(ModItems.GOLD_KNIFE.get(), toEnglishName(ModItems.GOLD_KNIFE.getKey().location().getPath()));
    add(ModItems.SILVER_KNIFE.get(), toEnglishName(ModItems.SILVER_KNIFE.getKey().location().getPath()));
    add(ModItems.DIAMOND_KNIFE.get(), toEnglishName(ModItems.DIAMOND_KNIFE.getKey().location().getPath()));
    add(ModItems.NETHERITE_KNIFE.get(), toEnglishName(ModItems.NETHERITE_KNIFE.getKey().location().getPath()));
    add(ModItems.RELIQUARY.get(), toEnglishName(ModItems.RELIQUARY.getKey().location().getPath()));
    add(ModItems.SPIRIT_BAG.get(), toEnglishName(ModItems.SPIRIT_BAG.getKey().location().getPath()));
    add(ModItems.FEY_LEATHER.get(), toEnglishName(ModItems.FEY_LEATHER.getKey().location().getPath()));
    add(ModItems.GLASS_EYE.get(), toEnglishName(ModItems.GLASS_EYE.getKey().location().getPath()));
    add(ModItems.LIFE_ESSENCE.get(), toEnglishName(ModItems.LIFE_ESSENCE.getKey().location().getPath()));
    add(ModItems.MYSTIC_FEATHER.get(), toEnglishName(ModItems.MYSTIC_FEATHER.getKey().location().getPath()));
    add(ModItems.PETALS.get(), toEnglishName(ModItems.PETALS.getKey().location().getPath()));
    add(ModItems.RUNIC_DUST.get(), toEnglishName(ModItems.RUNIC_DUST.getKey().location().getPath()));
    add(ModItems.STRANGE_OOZE.get(), toEnglishName(ModItems.STRANGE_OOZE.getKey().location().getPath()));
    add(ModItems.BEETLE_HELMET.get(), toEnglishName(ModItems.BEETLE_HELMET.getKey().location().getPath()));
    add(ModItems.BEETLE_CHESTPLATE.get(), toEnglishName(ModItems.BEETLE_CHESTPLATE.getKey().location().getPath()));
    add(ModItems.BEETLE_LEGGINGS.get(), toEnglishName(ModItems.BEETLE_LEGGINGS.getKey().location().getPath()));
    add(ModItems.BEETLE_BOOTS.get(), toEnglishName(ModItems.BEETLE_BOOTS.getKey().location().getPath()));
    add(ModItems.RAW_SILVER.get(), toEnglishName(ModItems.RAW_SILVER.getKey().location().getPath()));
    add(ModItems.SILVER_INGOT.get(), toEnglishName(ModItems.SILVER_INGOT.getKey().location().getPath()));
    add(ModItems.SILVER_NUGGET.get(), toEnglishName(ModItems.SILVER_NUGGET.getKey().location().getPath()));
    add(ModItems.COPPER_NUGGET.get(), toEnglishName(ModItems.COPPER_NUGGET.getKey().location().getPath()));
    add(ModItems.COPPER_AXE.get(), toEnglishName(ModItems.COPPER_AXE.getKey().location().getPath()));
    add(ModItems.COPPER_HOE.get(), toEnglishName(ModItems.COPPER_HOE.getKey().location().getPath()));
    add(ModItems.COPPER_PICKAXE.get(), toEnglishName(ModItems.COPPER_PICKAXE.getKey().location().getPath()));
    add(ModItems.COPPER_SHOVEL.get(), toEnglishName(ModItems.COPPER_SHOVEL.getKey().location().getPath()));
    add(ModItems.COPPER_SWORD.get(), toEnglishName(ModItems.COPPER_SWORD.getKey().location().getPath()));
    add(ModItems.COPPER_HELMET.get(), toEnglishName(ModItems.COPPER_HELMET.getKey().location().getPath()));
    add(ModItems.COPPER_CHESTPLATE.get(), toEnglishName(ModItems.COPPER_CHESTPLATE.getKey().location().getPath()));
    add(ModItems.COPPER_LEGGINGS.get(), toEnglishName(ModItems.COPPER_LEGGINGS.getKey().location().getPath()));
    add(ModItems.COPPER_BOOTS.get(), toEnglishName(ModItems.COPPER_BOOTS.getKey().location().getPath()));
    add(ModItems.BEETLE_SPAWN_EGG.get(), toEnglishName(ModItems.BEETLE_SPAWN_EGG.getKey().location().getPath()));
    add(ModItems.DEER_SPAWN_EGG.get(), toEnglishName(ModItems.DEER_SPAWN_EGG.getKey().location().getPath()));
    add(ModItems.FENNEC_SPAWN_EGG.get(), toEnglishName(ModItems.FENNEC_SPAWN_EGG.getKey().location().getPath()));
    add(ModItems.GREEN_SPROUT_SPAWN_EGG.get(), toEnglishName(ModItems.GREEN_SPROUT_SPAWN_EGG.getKey().location().getPath()));
    add(ModItems.TAN_SPROUT_SPAWN_EGG.get(), toEnglishName(ModItems.TAN_SPROUT_SPAWN_EGG.getKey().location().getPath()));
    add(ModItems.RED_SPROUT_SPAWN_EGG.get(), toEnglishName(ModItems.RED_SPROUT_SPAWN_EGG.getKey().location().getPath()));
    add(ModItems.PURPLE_SPROUT_SPAWN_EGG.get(), toEnglishName(ModItems.PURPLE_SPROUT_SPAWN_EGG.getKey().location().getPath()));
    add(ModItems.OWL_SPAWN_EGG.get(), toEnglishName(ModItems.OWL_SPAWN_EGG.getKey().location().getPath()));
    add(ModItems.DUCK_SPAWN_EGG.get(), toEnglishName(ModItems.DUCK_SPAWN_EGG.getKey().location().getPath()));
    add(ModItems.TOKEN.get(), toEnglishName(ModItems.TOKEN.getKey().location().getPath()));

    addEntityType(ModEntities.BEETLE, toEnglishName(ModEntities.BEETLE.getKey().location().getPath()));
    addEntityType(ModEntities.DEER, toEnglishName(ModEntities.DEER.getKey().location().getPath()));
    addEntityType(ModEntities.FENNEC, toEnglishName(ModEntities.FENNEC.getKey().location().getPath()));
    addEntityType(ModEntities.GREEN_SPROUT, toEnglishName(ModEntities.GREEN_SPROUT.getKey().location().getPath()));
    addEntityType(ModEntities.TAN_SPROUT, toEnglishName(ModEntities.TAN_SPROUT.getKey().location().getPath()));
    addEntityType(ModEntities.RED_SPROUT, toEnglishName(ModEntities.RED_SPROUT.getKey().location().getPath()));
    addEntityType(ModEntities.PURPLE_SPROUT, toEnglishName(ModEntities.PURPLE_SPROUT.getKey().location().getPath()));
    addEntityType(ModEntities.OWL, toEnglishName(ModEntities.OWL.getKey().location().getPath()));
    addEntityType(ModEntities.DUCK, toEnglishName(ModEntities.DUCK.getKey().location().getPath()));

    addEffect(ModEffects.FRIENDLY_EARTH, toEnglishName(ModEffects.FRIENDLY_EARTH.getKey().location().getPath()));
    addEffect(ModEffects.WAKEFUL, toEnglishName(ModEffects.WAKEFUL.getKey().location().getPath()));
    addEffect(ModEffects.PETAL_SHELL, toEnglishName(ModEffects.PETAL_SHELL.getKey().location().getPath()));
    addEffect(ModEffects.SENSE_DANGER, toEnglishName(ModEffects.SENSE_DANGER.getKey().location().getPath()));
    addEffect(ModEffects.SKY_SOARER, toEnglishName(ModEffects.SKY_SOARER.getKey().location().getPath()));


    add(RootsTags.Items.SEEDS, "Seeds");
    add(RootsTags.Items.CLOUD_BERRY_SEEDS, "Cloud Berry Seeds");
    add(RootsTags.Items.DEWGONIA_SEEDS, "Dewgonia Seeds");
    add(RootsTags.Items.INFERNO_BULB_SEEDS, "Inferno Bulb Seeds");
    add(RootsTags.Items.MOONGLOW_SEEDS, "Moonglow Seeds");
    add(RootsTags.Items.PERESKIA_SEEDS, "Pereskia Seeds");
    add(RootsTags.Items.SPIRITLEAF_SEEDS, "Spiritleaf Seeds");
    add(RootsTags.Items.STALICRIPE_SEEDS, "Stalicripe Seeds");
    add(RootsTags.Items.WILDEWHEET_SEEDS, "Wildewheet Seeds");
    add(RootsTags.Items.WILDROOT_SEEDS, "Wildroot Seeds");
    add(RootsTags.Items.CROPS, "Crops");
    add(RootsTags.Items.ELEMENTAL_CROPS, "Elemental Crops");
    add(RootsTags.Items.WATER_CROPS, "Water Elemental Crops");
    add(RootsTags.Items.EARTH_CROPS, "Earth Elemental Crops");
    add(RootsTags.Items.AIR_CROPS, "Air Elemental Crops");
    add(RootsTags.Items.FIRE_CROPS, "Fire Elemental Crops");
    add(RootsTags.Items.CLOUD_BERRY_CROP, "Cloud Berry Crops");
    add(RootsTags.Items.DEWGONIA_CROP, "Dewgonia Crops");
    add(RootsTags.Items.SPIRITLEAF_CROP, "Spiritleaf Crops");
    add(RootsTags.Items.STALICRIPE_CROP, "Stalicripe Crops");
    add(RootsTags.Items.WILDEWHEET_CROP, "Wildewheet Crops");
    add(RootsTags.Items.WILDROOT_CROP, "Wildroot Crops");
    add(RootsTags.Items.GROVE_MOSS_CROP, "Grove Moss Crops");
    add(RootsTags.Items.INFERNO_BULB_CROP, "Inferno Bulb Crops");
    add(RootsTags.Items.MOONGLOW_CROP, "Moonglow Crops");
    add(RootsTags.Items.PERESKIA_CROP, "Pereskia Crops");
    add(RootsTags.Items.AUBERGINE_CROP, "Aubergine Crops");
    add(RootsTags.Items.BAFFLECAP_CROP, "Bafflecap Crops");
    add(RootsTags.Items.BARKS, "Barks");
    add(RootsTags.Items.ACACIA_BARK, "Acacia Barks");
    add(RootsTags.Items.BIRCH_BARK, "Birch Barks");
    add(RootsTags.Items.DARK_OAK_BARK, "Dark Oak Barks");
    add(RootsTags.Items.JUNGLE_BARK, "Jungle Barks");
    add(RootsTags.Items.OAK_BARK, "Oak Barks");
    add(RootsTags.Items.SPRUCE_BARK, "Spruce Barks");
    add(RootsTags.Items.WILDWOOD_BARK, "Wildwood Barks");
    add(RootsTags.Items.MANGROVE_BARK, "Mangrove Barks");
    add(RootsTags.Items.CRIMSON_BARK, "Crimson Barks");
    add(RootsTags.Items.WARPED_BARK, "Warped Barks");
    add(RootsTags.Items.MIXED_BARK, "Mixed Barks");
    add(RootsTags.Items.BOTTLES, "Bottles");
    add(RootsTags.Items.POUCHES, "Pouches");
    add(RootsTags.Items.GROVE_CRAFTER_ACTIVATION, "Grove Crafter Activators");
    add(RootsTags.Items.MORTAR_ACTIVATION, "Mortar Activators");
    add(RootsTags.Items.PYRE_ACTIVATION, "Pyre Activators");
    add(RootsTags.Items.FLINT, "Flint");
    add(RootsTags.Items.STONELIKE, "Stone-like");
    add(RootsTags.Items.CASTING_TOOLS, "Casting Tools");
    add(RootsTags.Items.RUNESTONE_HERBS, "Herbs for crafting Runestone");
    add(RootsTags.Items.NYI, "Not Yet Implemented");
    add(RootsTags.Items.SOILS, "Soils");
    add(RootsTags.Items.WATER_SOIL, "Water Elemental Soils");
    add(RootsTags.Items.AIR_SOIL, "Air Elemental Soils");
    add(RootsTags.Items.EARTH_SOIL, "Earth Elemental Soils");
    add(RootsTags.Items.FIRE_SOIL, "Fire Elemental Soils");
    add(RootsTags.Items.ELEMENTAL_SOIL, "Elemental Soils");
    add(RootsTags.Items.RUNED_OBSIDIAN, "Runed Obsidian");
    add(RootsTags.Items.RUNESTONE, "Runestone");
    add(RootsTags.Items.WILDWOOD_LOGS, "Wildwood Logs");
    // TODO: Rune-carved?
    add(RootsTags.Items.RUNED_LOGS, "Runed Logs");
    add(RootsTags.Items.RUNED_ACACIA_LOG, "Runed Acacia Logs");
    add(RootsTags.Items.RUNED_DARK_OAK_LOG, "Runed Dark Oak Logs");
    add(RootsTags.Items.RUNED_OAK_LOG, "Runed Oak Logs");
    add(RootsTags.Items.RUNED_BIRCH_LOG, "Runed Birch Logs");
    add(RootsTags.Items.RUNED_JUNGLE_LOG, "Runed Jungle Logs");
    add(RootsTags.Items.RUNED_SPRUCE_LOG, "Runed Spruce Logs");
    add(RootsTags.Items.RUNED_MANGROVE_LOG, "Runed Mangrove Logs");
    add(RootsTags.Items.RUNED_WILDWOOD_LOG, "Runed Wildwood Logs");
    add(RootsTags.Items.RUNED_CRIMSON_STEM, "Runed Crimson Logs");
    add(RootsTags.Items.RUNED_WARPED_STEM, "Runed Warped Logs");
    add(RootsTags.Items.GROVE_STONES, "Grove Stones");
    add(RootsTags.Items.GROVE_STONE_PRIMAL, "Primal Grove Stones");
    add(RootsTags.Items.PEDESTALS, "Pedestals");
    add(RootsTags.Items.RITUAL_PEDESTALS, "Ritual Pedestals");
    add(RootsTags.Items.GROVE_PEDESTALS, "Grove Crafting Pedestals");
    add(RootsTags.Items.PYRES, "Pyres");
    add(RootsTags.Items.GROVE_CRAFTERS, "Grove Crafters");
    add(RootsTags.Items.MORTARS, "Mortars");
    add(RootsTags.Items.PETALS, "Pestles");
    add(RootsTags.Items.RUNIC_DUST, "Runic Dust");
    add(RootsTags.Items.RUNIC_SHEARS, "Runic Shears");
    add(RootsTags.Items.VEGETABLES, "Vegetables");
    add(RootsTags.Items.COOKED_VEGETABLES, "Cooked vegetables");
    add(RootsTags.Items.PROTEINS, "Proteins");
    add(RootsTags.Items.COOKED_SEAFOOD, "Cooked seafood");
    add(RootsTags.Items.KNIVES, "Knives");
    add(RootsTags.Items.LEVERS, "Levers");
    add(RootsTags.Items.GROVE_MOSS_HERB, "Grove Moss Herbs");
    add(RootsTags.Items.INFERNO_BULB_HERB, "Inferno Bulb Herbs");
    add(RootsTags.Items.MOONGLOW_HERB, "Moonglow Herbs");
    add(RootsTags.Items.PERESKIA_HERB, "Pereskia Herbs");
    add(RootsTags.Items.SPIRITLEAF_HERB, "Spiritleaf Herbs");
    add(RootsTags.Items.STALICRIPE_HERB, "Stalicripe Herbs");
    add(RootsTags.Items.WILDEWHEET_HERB, "Wildewheet Herbs");
    add(RootsTags.Items.WILDROOT_HERB, "Wildroot Herbs");
    add(RootsTags.Items.CLOUD_BERRY_HERB, "Cloud Berry Herbs");
    add(RootsTags.Items.DEWGONIA_HERB, "Dewgonia Herbs");
    add(RootsTags.Items.BAFFLECAP_HERB, "Bafflecap Herbs");
    add(RootsTags.Items.HERBS, "Herbs");
    add(RootsTags.Items.CARAPACE, "Carapaces");
    add(RootsTags.Items.PELT, "Pelts");
    add(RootsTags.Items.ANTLERS, "Antlers");
    add(RootsTags.Items.COPPER_ITEMS, "Copper Items");
    add(RootsTags.Items.COPPER_NUGGET, "Copper Nuggets");
    add(RootsTags.Items.RAW_SILVER, "Raw Silvers");
    add(RootsTags.Items.SILVER_INGOT, "Silver Ingots");
    add(RootsTags.Items.STONEPETAL, "Stonepetals");
    add(RootsTags.Items.SILVER_ORE, "Silver Ores");
    add(RootsTags.Items.QUARTZ_ORE, "Quartz Ores");
    add(RootsTags.Items.SILVER_STORAGE, "Silver Storage Blocks");
    add(RootsTags.Items.RAW_SILVER_STORAGE, "Raw Silver Storage Blocks");
    add(RootsTags.Items.SILVER_NUGGET, "Silver Nuggets");
    add(RootsTags.Items.SILVER_ITEMS, "Silver Items");
    add(RootsTags.Items.WILDWOOD_PLANKS, "Wildwood Planks");
    // TODO:
    add(RootsTags.Items.SKIPPED_FOODS, "Ignored foods");
    add(RootsTags.Items.DEER_FOOD, "Foor for Deer");
    add(RootsTags.Items.FENNEC_FOOD, "Food for Fennecs");
    add(RootsTags.Items.BEETLE_FOOD, "Food for Beetles");
    add(RootsTags.Items.DUCK_FOOD, "Food for Ducks");
    add(RootsTags.Items.OWL_FOOD, "Food for Owls");
    add(RootsTags.Items.SPROUT_FOOD, "Food for Sprouts");

    // TODO: Block tags?
  }

  public static String toEnglishName(String internalName) {
    return Arrays.stream(internalName.toLowerCase(Locale.ROOT).split("_"))
        .map(StringUtils::capitalize)
        .collect(Collectors.joining(" "));
  }

  public static String getComplexDescription(String defaultValue) {
    String[] split = defaultValue.split("/");
    return String.format("%s: %s", toEnglishName(split[0]), toEnglishName(split[1]));
  }
}
