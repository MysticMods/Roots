package mysticmods.roots.gen.lang;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.grove.GrovePowerGenerator;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Cycling;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.client.KeyBindings;
import mysticmods.roots.init.*;
import mysticmods.roots.item.GramaryItem;
import mysticmods.roots.spell.mode.AOEGrowthMode;
import mysticmods.roots.spell.mode.HarvestMode;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;
import java.util.StringJoiner;
import java.util.regex.Pattern;


public final class RootsLangProvider extends LanguageProvider {
  public RootsLangProvider(PackOutput output) {
    super(output, RootsAPI.MODID, "en_us");
  }

  public void addBlock(Holder<Block> holder) {
    //noinspection DataFlowIssue
    add(holder.value(), toEnglishName(holder.getKey().location().getPath()));
  }

  public void addItem(Holder<Item> holder) {
    //noinspection DataFlowIssue
    add(holder.value(), toEnglishName(holder.getKey().location().getPath()));
  }

  public void addEntity(Holder<EntityType<?>> holder) {
    //noinspection DataFlowIssue
    add(holder.value(), toEnglishName(holder.getKey().location().getPath()));
  }

  public void addEffect(Holder<MobEffect> holder) {
    //noinspection DataFlowIssue
    add(holder.value(), toEnglishName(holder.getKey().location().getPath()));
  }

  @Override
  protected void addTranslations() {
    add("curios.identifier.tome", "Tome");

    // Tag translations
    add("itemGroup.roots", "Roots");
    add("itemGroup.roots_spells", "Roots Spells");
    add("itemGroup.roots_rituals", "Roots Rituals");

    add("roots.spell.spell_light_drifter.on_ground", "You must be standing on solid ground to cast Light Drifter.");

    add("roots.reputation.decreased", "Your reputation with the %s grove has decreased by %s");
    add("roots.reputation.increased", "Your reputation with the %s grove has increased by %s");

    add("roots.tooltip.effect", "Use to gain %s for %s seconds.");
    add("roots.tooltip.chance", "Chance: %s%%");

    add("roots.tooltip.token.spell", "Spell: %s");
    add("roots.tooltip.token.modifier", "Modifier: %s");
    add("roots.tooltip.token.delete", "[Press [%s] to remove from this slot.]");
    add("roots.tooltip.token.modify", "[Press [%s] to adjust modifiers.]");
    add("roots.tooltip.token.unlock", "Right-Click to unlock.");
    add("roots.tooltip.token.unlocked", "You've already unlocked this.");
    add("roots.tooltip.token.available_modifiers", "Available modifiers:");
    add("roots.tooltip.token.enabled_modifiers", "Enabled modifiers:");
    add("roots.tooltip.token.ritual", "Ritual: %s");

    add("roots.tooltip.spell.modifiers", "Modifiers: ");
    add("roots.tooltip.cost.herb_cost", "%s %s");
    add("roots.tooltip.cost.herb_cost_full", "%s %s%s");
    add("roots.tooltip.cost.herb_cost_modified", " [±%s]");
    add("roots.tooltip.cost.cost_amount", "+%s");
    add("roots.tooltip.cost.cost_cancel", "-%s");
    // ×
    add("roots.tooltip.cost.cost_multiply_base", "+%s%% (base)");
    add("roots.tooltip.cost.cost_multiply_total", "+%s%% (total)");
    add("roots.tooltip.cost.charge_type", "[Charges %s.]");
    add("roots.tooltip.cost.charge_type.operation", "per operation");
    // TODO: ???
    add("roots.tooltip.cost.charge_type.instance", "per cast");

    add("roots.tooltip.pouch.color", "Dyed: %s");
    add("roots.tooltip.pouch.color_name", "%s");
    add("roots.tooltip.pouch.key_binding", "Press '%s' to open your pouch.");

    add("roots.hud.attributes", "%s: %s");

    add("roots.tooltip.staff.selected", "Selected Slot: %s");
    add("roots.tooltip.staff.no_spell", "No spell.");
    add("roots.tooltip.staff.cooldown", " (CD: %ss)");
    add("roots.tooltip.staff.spell_in_slot", "%s: %s%s%s");
    add("roots.tooltip.staff.is_selected", " (Selected)");
    add("roots.tooltip.staff.data", "  %s: %s");
    add("roots.tooltip.staff.is_modified", "*");

    add("roots.tooltip.staff.key_binding", "Press '%s' to open your spell library. Use ('%s') while sneaking or press '%s' to cycle spells.");
    add("roots.tooltip.hold_shift", "[Hold %s for more information.]");
    add("roots.tooltip.shift", "Shift");

    add("roots.item.staff.with_spell", "Staff (%s)");
    add("roots.item.staff.with_modified_spell", "Staff (%s*)");

    add("roots.drinks.slow_regen", "Gives a burst of revitalizing energy.");
    add("roots.drinks.wakefulness", "Perks you up, night or day; shoos those scary phantoms away!");
    add("roots.drinks.sour", "Sour and awful to drink! Leaves you hungry.");
    add("roots.message.dandelion_cordial", "You feel well-rested!");

    add("roots.message.recipe.requires", "Requires: %s");
    add("roots.message.recipe.failures", "A number of conditions were not met:");
    add("roots.message.staff.missing_herbs", "Unable to cast %s, missing herbs.");
    add("roots.message.staff.charging", "Spell charged: %s/%s");
    add("roots.message.staff.charging_percent", "Spell charged: %s%%");

    add("roots.message.spell.not_granted", "You do not know the spell: %s");
    add("roots.message.spell_modifier.not_granted", "You do not know the spell modifier: %s");
    add("roots.message.spell_modifier.invalid_spell", "The modifier %s isn't valid for the spell currently in that slot: %s");
    add("roots.message.spell_modifier.cannot_toggle", "Unable to toggle modifier %s!");
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

    add("roots.subtitles.item.knife.strip", "Knife strips");
    add("roots.subtitles.item.pouch.pickup_herb", "Herb plops");

    add("roots.commands.reset", "Successfully reset your spell cooldowns!");
    add("roots.commands.usage", "Usage: /roots staff | ritual | pyre | activate");
    add("roots.commands.staff.usage", "Usage: /roots staff <spell>");
    add("roots.commands.staff.spell_not_found", "Spell not found: %s");
    add("roots.commands.staff.no_spell_storage", "Staff missing spell storage somehow");

    add("roots.commands.ritual.usage", "Usage: /roots ritual <ritual>");
    add("roots.commands.ritual.ritual_not_found", "Ritual not found: %s");
    add("roots.commands.ritual.no_player", "Must be executed by a player to use this command");
    add("roots.commands.ritual.recipe_not_found", "No recipe found for ritual %s");
    add("roots.commands.ritual.no_space", "No space to place pyre.");
    add("roots.commands.ritual.failed_condition", "Failed to place condition: %s");

    add("roots.commands.pyre.usage", "Usage: /roots pyre <recipe>");
    add("roots.commands.pyre.no_player", "Must be executed by a player to use this command");
    add("roots.commands.pyre.recipe_not_found", "No recipe found for pyre %s");
    add("roots.commands.pyre.no_space", "No space to place pyre.");
    add("roots.commands.pyre.failed_condition", "Failed to place condition: %s");

    add("roots.commands.reputation.current_reputation", "Current reputation for %s grove: [%s] %s");
    add("roots.commands.reputation.add.usage", "Usage: /roots reputation <player> <grove> add <amount>");
    add("roots.commands.reputation.add", "Reputation for %s with %s grove increased by %s, [%s] now: %s");
    add("roots.commands.reputation.grove_not_found", "Grove not found: %s");
    add("roots.commands.reputation.no_reputation_storage", "Reputation storage not found.");
    add("roots.commands.reputation.remove.usage", "Usage: /roots reputation <player> <grove> remove <amount>");
    add("roots.commands.reputation.remove", "Reputation for %s with %s grove decreased by %s [%s], now: %s");
    add("roots.commands.reputation.set.usage", "Usage: /roots reputation <player> <grove> set <amount>");
    add("roots.commands.reputation.set", "Reputation for %s with %s grove set to [%s] %s.");
    add("roots.commands.reputation.usage", "Usage: /roots reputation <player> <grove>  <set <amount> | add <amount> | remove <amount>>");

    add("roots.advancements.root.title", "Roots");
    add("roots.advancements.root.description", "An introduction to the magic of the wilds.");

    add("roots.advancements.pacifist.title", "An Untrue Pacifist");
    add("roots.advancements.pacifist.description", "Needlessly slaughtered one of nature's peaceful creatures.");

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

    RootsRegistries.GROVE_ACTIONS.entrySet().forEach(o -> {
      add(o.getValue().getDescriptionId(), toEnglishName(o.getKey().location().getPath()));
    });

    RootsRegistries.SPELLS.entrySet().forEach(o -> {
          add(o.getValue().getDescriptionId(), toEnglishName(o.getKey().location().getPath()));
        }
    );

    add("roots.spell_mode.mode", "Mode");
    add("roots.spell_mode.mode_changed", "Mode changed to %s.");

    addCyclingMode(AOEGrowthMode.values());
    addCyclingMode(GramaryItem.GramaryMode.values());
    addCyclingMode(HarvestMode.values());

    RootsRegistries.RITUALS.entrySet().forEach(o ->
        add(o.getValue().getDescriptionId(), toEnglishName(o.getKey().location().getPath()))
    );
    RootsRegistries.HERBS.entrySet().forEach(o ->
        add(o.getValue().getDescriptionId(), toEnglishName(o.getKey().location().getPath()))
    );
    Pattern numbers = Pattern.compile("([0-9]+)");
    // TODO: These need to be adjusted to
    // a) split the '/' and ignore the spell name
    // b) convert any numbers to be in brackets i.e., (1)
    RootsRegistries.SPELL_MODIFIERS.entrySet().forEach(o -> {
          var k = o.getKey().location().getPath();
          var s = k.split("/")[1];
          var e = toEnglishName(s);
          //var m = numbers.matcher(e);

          add(o.getValue().getDescriptionId(), e);
        }
    );

    // TODO: Same as above?
    RootsRegistries.RITUAL_MODIFIERS.entrySet()
        .forEach(o -> add(o.getValue().getDescriptionId(), toEnglishName(o.getKey().location().getPath()))
        );

    add("level_condition.roots." + ModConditions.ANY_GROVE_STONE_ACTIVE.get().getName(), "Active Grove Stone");
    add("level_condition.roots." + ModConditions.ANY_GROVE_STONE_ACTIVE.get()
        .getName() + ".description", "A Grove Stone that has been activated by the ritual Grove Supplication.");
    add("level_condition.roots." + ModConditions.FAIRY_GROVE_STONE_ACTIVE.get().getName(), "Active Fairy Grove Stone");
    add("level_condition.roots." + ModConditions.FAIRY_GROVE_STONE_ACTIVE.get()
        .getName() + ".description", "A Fairy Grove Stone that has been activated by the ritual Grove Supplication.");
    add("level_condition.roots." + ModConditions.FUNGAL_GROVE_STONE_ACTIVE.get()
        .getName(), "Active Fungal Grove Stone");
    add("level_condition.roots." + ModConditions.FUNGAL_GROVE_STONE_ACTIVE.get()
        .getName() + ".description", "A Fungal Grove Stone that has been activated by the ritual Grove Supplication.");
    add("level_condition.roots." + ModConditions.ANY_GROVE_STONE_INACTIVE.get().getName(), "Inactive Grove Stone");
    add("level_condition.roots." + ModConditions.ANY_GROVE_STONE_INACTIVE.get()
        .getName() + ".description", "A Grove Stone that has not yet been activated.");
    add("level_condition.roots." + ModConditions.ANY_GROVE_STONE.get().getName(), "Any Grove Stone");
    add("level_condition.roots." + ModConditions.ANY_GROVE_STONE.get()
        .getName() + ".description", "A Grove Stone of any kind, active or inactive.");
    add("level_condition.roots." + ModConditions.RUNESTONE_PILLAR_3_HIGH.get().getName(), "3 High Runestone Pillar");
    add("level_condition.roots." + ModConditions.RUNESTONE_PILLAR_3_HIGH.get()
        .getName() + ".description", "A pillar of three (3) runestone or runed obsidian blocks, topped with a chiseled runestone or runed obsidian block.");
    add("level_condition.roots." + ModConditions.RUNESTONE_PILLAR_4_HIGH.get().getName(), "4 High Runestone Pillar");
    add("level_condition.roots." + ModConditions.RUNESTONE_PILLAR_4_HIGH.get()
        .getName() + ".description", "A pillar of four (4) runestone or runed obsidian blocks, topped with a chiseled runestone or runed obsidian block.");
    add("level_condition.roots." + ModConditions.MATURE_WILDROOT_CROP.get().getName(), "Mature Wildroot Crop");
    add("level_condition.roots." + ModConditions.MATURE_WILDROOT_CROP.get()
        .getName() + ".description", "A wildroot crop that has reached its final growth stage.");
    add("level_condition.roots." + ModConditions.OVERGROWTH.get().getName(), "Creeping Grove Moss or Water Source");
    add("level_condition.roots." + ModConditions.OVERGROWTH.get()
        .getName() + ".description", "An already existing block of creeping grove moss with space adjacent for future growth, or any solid block that has a water source block adjacent to it.");

    add("player_condition.roots." + ModConditions.FUNGAL_RANK_1.get().getName(), "Fungal Grove Rank 1+");
    add("player_condition.roots." + ModConditions.FUNGAL_RANK_1.get()
        .getName() + ".description", "Obtain a reputation rank with the Fungal Grove of at least 1.");
    add("player_condition.roots." + ModConditions.ELEMENTAL_RANK_1.get().getName(), "Elemental Grove Rank 1+");
    add("player_condition.roots." + ModConditions.ELEMENTAL_RANK_1.get()
        .getName() + ".description", "Obtain a reputation rank with the Elemental Grove of at least 1.");
    add("player_condition.roots." + ModConditions.FAIRY_RANK_1.get().getName(), "Fairy Grove Rank 1+");
    add("player_condition.roots." + ModConditions.FAIRY_RANK_1.get()
        .getName() + ".description", "Obtain a reputation rank with the Fairy Grove of at least 1.");
    add("player_condition.roots." + ModConditions.PRIMAL_RANK_1.get().getName(), "Primal Grove Rank 1+");
    add("player_condition.roots." + ModConditions.PRIMAL_RANK_1.get()
        .getName() + ".description", "Obtain a reputation rank with the Primal Grove of at least 1.");
    add("player_condition.roots." + ModConditions.CULTIVATION_RANK_1.get().getName(), "Cultivation Grove Rank 1+");
    add("player_condition.roots." + ModConditions.CULTIVATION_RANK_1.get()
        .getName() + ".description", "Obtain a reputation rank with the Cultivation Grove of at least 1.");
    add("player_condition.roots." + ModConditions.TWILIGHT_RANK_1.get().getName(), "Twilight Grove Rank 1+");
    add("player_condition.roots." + ModConditions.TWILIGHT_RANK_1.get()
        .getName() + ".description", "Obtain a reputation rank with the Twilight Grove of at least 1.");

    add("player_condition.roots." + ModConditions.FUNGAL_RANK_2.get().getName(), "Fungal Grove Rank 2+");
    add("player_condition.roots." + ModConditions.FUNGAL_RANK_2.get()
        .getName() + ".description", "Obtain a reputation rank with the Fungal Grove of at least 2.");
    add("player_condition.roots." + ModConditions.ELEMENTAL_RANK_2.get().getName(), "Elemental Grove Rank 2+");
    add("player_condition.roots." + ModConditions.ELEMENTAL_RANK_2.get()
        .getName() + ".description", "Obtain a reputation rank with the Elemental Grove of at least 2.");
    add("player_condition.roots." + ModConditions.FAIRY_RANK_2.get().getName(), "Fairy Grove Rank 2+");
    add("player_condition.roots." + ModConditions.FAIRY_RANK_2.get()
        .getName() + ".description", "Obtain a reputation rank with the Fairy Grove of at least 2.");
    add("player_condition.roots." + ModConditions.PRIMAL_RANK_2.get().getName(), "Primal Grove Rank 2+");
    add("player_condition.roots." + ModConditions.PRIMAL_RANK_2.get()
        .getName() + ".description", "Obtain a reputation rank with the Primal Grove of at least 2.");
    add("player_condition.roots." + ModConditions.CULTIVATION_RANK_2.get().getName(), "Cultivation Grove Rank 2+");
    add("player_condition.roots." + ModConditions.CULTIVATION_RANK_2.get()
        .getName() + ".description", "Obtain a reputation rank with the Cultivation Grove of at least 2.");
    add("player_condition.roots." + ModConditions.TWILIGHT_RANK_2.get().getName(), "Twilight Grove Rank 2+");
    add("player_condition.roots." + ModConditions.TWILIGHT_RANK_2.get()
        .getName() + ".description", "Obtain a reputation rank with the Twilight Grove of at least 2.");

    add("player_condition.roots." + ModConditions.FUNGAL_RANK_3.get().getName(), "Fungal Grove Rank 3+");
    add("player_condition.roots." + ModConditions.FUNGAL_RANK_3.get()
        .getName() + ".description", "Obtain a reputation rank with the Fungal Grove of at least 3.");
    add("player_condition.roots." + ModConditions.ELEMENTAL_RANK_3.get().getName(), "Elemental Grove Rank 3+");
    add("player_condition.roots." + ModConditions.ELEMENTAL_RANK_3.get()
        .getName() + ".description", "Obtain a reputation rank with the Elemental Grove of at least 3.");
    add("player_condition.roots." + ModConditions.FAIRY_RANK_3.get().getName(), "Fairy Grove Rank 3+");
    add("player_condition.roots." + ModConditions.FAIRY_RANK_3.get()
        .getName() + ".description", "Obtain a reputation rank with the Fairy Grove of at least 3.");
    add("player_condition.roots." + ModConditions.PRIMAL_RANK_3.get().getName(), "Primal Grove Rank 3+");
    add("player_condition.roots." + ModConditions.PRIMAL_RANK_3.get()
        .getName() + ".description", "Obtain a reputation rank with the Primal Grove of at least 3.");
    add("player_condition.roots." + ModConditions.CULTIVATION_RANK_3.get().getName(), "Cultivation Grove Rank 3+");
    add("player_condition.roots." + ModConditions.CULTIVATION_RANK_3.get()
        .getName() + ".description", "Obtain a reputation rank with the Cultivation Grove of at least 3.");
    add("player_condition.roots." + ModConditions.TWILIGHT_RANK_3.get().getName(), "Twilight Grove Rank 3+");
    add("player_condition.roots." + ModConditions.TWILIGHT_RANK_3.get()
        .getName() + ".description", "Obtain a reputation rank with the Twilight Grove of at least 3.");

    add("player_condition.roots." + ModConditions.FUNGAL_RANK_4.get().getName(), "Fungal Grove Rank 4");
    add("player_condition.roots." + ModConditions.FUNGAL_RANK_4.get()
        .getName() + ".description", "Obtain the maximum reputation rank of 4 with the Fungal Grove.");
    add("player_condition.roots." + ModConditions.ELEMENTAL_RANK_4.get().getName(), "Elemental Grove Rank 4");
    add("player_condition.roots." + ModConditions.ELEMENTAL_RANK_4.get()
        .getName() + ".description", "Obtain the maximum reputation rank of 4 with the Elemental Grove.");
    add("player_condition.roots." + ModConditions.FAIRY_RANK_4.get().getName(), "Fairy Grove Rank 4");
    add("player_condition.roots." + ModConditions.FAIRY_RANK_4.get()
        .getName() + ".description", "Obtain the maximum reputation rank of 4 with the Fairy Grove.");
    add("player_condition.roots." + ModConditions.PRIMAL_RANK_4.get().getName(), "Primal Grove Rank 4");
    add("player_condition.roots." + ModConditions.PRIMAL_RANK_4.get()
        .getName() + ".description", "Obtain the maximum reputation rank of 4 with the Primal Grove.");
    add("player_condition.roots." + ModConditions.CULTIVATION_RANK_4.get().getName(), "Cultivation Grove Rank 4");
    add("player_condition.roots." + ModConditions.CULTIVATION_RANK_4.get()
        .getName() + ".description", "Obtain the maximum reputation rank of 4 with the Cultivation Grove.");
    add("player_condition.roots." + ModConditions.TWILIGHT_RANK_4.get().getName(), "Twilight Grove Rank 4");
    add("player_condition.roots." + ModConditions.TWILIGHT_RANK_4.get()
        .getName() + ".description", "Obtain the maximum reputation rank of 4 with the Twilight Grove.");

    // Blocks
    addBlock(ModBlocks.THATCH);
    addBlock(ModBlocks.RUNESTONE);
    addBlock(ModBlocks.MOSSY_RUNESTONE);
    addBlock(ModBlocks.CHISELED_RUNESTONE);
    addBlock(ModBlocks.RUNESTONE_BRICK);
    addBlock(ModBlocks.RUNESTONE_TILE);
    addBlock(ModBlocks.RUNED_OBSIDIAN);
    addBlock(ModBlocks.CHISELED_RUNED_OBSIDIAN);
    addBlock(ModBlocks.RUNED_BRICK);
    addBlock(ModBlocks.RUNED_TILE);
    addBlock(ModBlocks.SILVER_ORE);
    addBlock(ModBlocks.DEEPSLATE_SILVER_ORE);
    addBlock(ModBlocks.GRANITE_QUARTZ_ORE);
    addBlock(ModBlocks.RAW_SILVER_BLOCK);
    addBlock(ModBlocks.SILVER_BLOCK);
    addBlock(ModBlocks.WILDWOOD_LOG);
    addBlock(ModBlocks.STRIPPED_WILDWOOD_LOG);
    addBlock(ModBlocks.WILDWOOD_WOOD);
    addBlock(ModBlocks.STRIPPED_WILDWOOD_WOOD);
    addBlock(ModBlocks.WILDWOOD_PLANKS);
    addBlock(ModBlocks.WILDWOOD_SAPLING);
    addBlock(ModBlocks.STONEPETAL);
    addBlock(ModBlocks.WILDWOOD_LEAVES);
    addBlock(ModBlocks.RUNESTONE_STAIRS);
    addBlock(ModBlocks.MOSSY_RUNESTONE_STAIRS);
    addBlock(ModBlocks.RUNESTONE_BRICK_STAIRS);
    addBlock(ModBlocks.RUNESTONE_TILE_STAIRS);
    addBlock(ModBlocks.RUNED_STAIRS);
    addBlock(ModBlocks.RUNED_BRICK_STAIRS);
    addBlock(ModBlocks.RUNED_TILE_STAIRS);
    addBlock(ModBlocks.WILDWOOD_STAIRS);
    addBlock(ModBlocks.RUNESTONE_SLAB);
    addBlock(ModBlocks.MOSSY_RUNESTONE_SLAB);
    addBlock(ModBlocks.RUNESTONE_BRICK_SLAB);
    addBlock(ModBlocks.RUNESTONE_TILE_SLAB);
    addBlock(ModBlocks.RUNED_SLAB);
    addBlock(ModBlocks.RUNED_BRICK_SLAB);
    addBlock(ModBlocks.RUNED_TILE_SLAB);
    addBlock(ModBlocks.WILDWOOD_SLAB);
    addBlock(ModBlocks.WILDWOOD_FENCE);
    addBlock(ModBlocks.RUNESTONE_BUTTON);
    addBlock(ModBlocks.RUNESTONE_BRICK_BUTTON);
    addBlock(ModBlocks.RUNESTONE_TILE_BUTTON);
    addBlock(ModBlocks.MOSSY_RUNESTONE_BUTTON);
    addBlock(ModBlocks.RUNED_BUTTON);
    addBlock(ModBlocks.RUNED_BRICK_BUTTON);
    addBlock(ModBlocks.RUNED_TILE_BUTTON);
    addBlock(ModBlocks.WILDWOOD_BUTTON);
    addBlock(ModBlocks.RUNESTONE_PRESSURE_PLATE);
    addBlock(ModBlocks.RUNESTONE_BRICK_PRESSURE_PLATE);
    addBlock(ModBlocks.RUNESTONE_TILE_PRESSURE_PLATE);
    addBlock(ModBlocks.MOSSY_RUNESTONE_PRESSURE_PLATE);
    addBlock(ModBlocks.RUNED_PRESSURE_PLATE);
    addBlock(ModBlocks.RUNED_BRICK_PRESSURE_PLATE);
    addBlock(ModBlocks.RUNED_TILE_PRESSURE_PLATE);
    addBlock(ModBlocks.WILDWOOD_PRESSURE_PLATE);
    addBlock(ModBlocks.WILDWOOD_DOOR);
    addBlock(ModBlocks.WILDWOOD_TRAPDOOR);
    addBlock(ModBlocks.WILDWOOD_LADDER);
    addBlock(ModBlocks.WILDWOOD_GATE);
    addBlock(ModBlocks.RUNESTONE_WALL);
    addBlock(ModBlocks.MOSSY_RUNESTONE_WALL);
    addBlock(ModBlocks.RUNESTONE_BRICK_WALL);
    addBlock(ModBlocks.RUNESTONE_TILE_WALL);
    addBlock(ModBlocks.RUNED_WALL);
    addBlock(ModBlocks.RUNED_BRICK_WALL);
    addBlock(ModBlocks.RUNED_TILE_WALL);
    addBlock(ModBlocks.ELEMENTAL_SOIL);
    addBlock(ModBlocks.AQUEOUS_SOIL);
    addBlock(ModBlocks.CAELIC_SOIL);
    addBlock(ModBlocks.MAGMATIC_SOIL);
    addBlock(ModBlocks.TERRAN_SOIL);
    addBlock(ModBlocks.ENCHANTED_TURF);
    addBlock(ModBlocks.SYLVAN_LIGHT);
    addBlock(ModBlocks.WILDWOOD_CHEST);
    addBlock(ModBlocks.RITUAL_PEDESTAL);
    addBlock(ModBlocks.REINFORCED_RITUAL_PEDESTAL);
    addBlock(ModBlocks.GROVE_CRAFTER);
    addBlock(ModBlocks.GROVE_PEDESTAL);
    addBlock(ModBlocks.WILDWOOD_PEDESTAL);
    addBlock(ModBlocks.DISPLAY_PEDESTAL);
    /*    addBlock(ModBlocks.GROWTH_AMPLIFIER);*/
    addBlock(ModBlocks.RED_FAIRY_HUT);
    addBlock(ModBlocks.CRIMSON_FAIRY_HUT);
    addBlock(ModBlocks.WARPED_FAIRY_HUT);
    addBlock(ModBlocks.BROWN_FAIRY_HUT);
    addBlock(ModBlocks.BAFFLECAP_FAIRY_HUT);
    addBlock(ModBlocks.FUNGAL_TRANSMUTER);
    addBlock(ModBlocks.WILD_ROOTS);
    addBlock(ModBlocks.CREEPING_GROVE_MOSS);
    addBlock(ModBlocks.HANGING_GROVE_MOSS);
    addBlock(ModBlocks.BAFFLECAP_BLOCK);
    addBlock(ModBlocks.PRIMAL_GROVE_STONE);
    addBlock(ModBlocks.WILD_GROVE_STONE);
    addBlock(ModBlocks.TWILIGHT_GROVE_STONE);
    addBlock(ModBlocks.ELEMENTAL_GROVE_STONE);
    addBlock(ModBlocks.CULTIVATION_GROVE_STONE);
    addBlock(ModBlocks.FAIRY_GROVE_STONE);
    addBlock(ModBlocks.FUNGAL_GROVE_STONE);
    addBlock(ModBlocks.INCENSE_BURNER);
    /*    addBlock(ModBlocks.STONE_ALTAR);*/
    addBlock(ModBlocks.MORTAR);
    addBlock(ModBlocks.PYRE);
    addBlock(ModBlocks.SOUL_PYRE);
    addBlock(ModBlocks.REINFORCED_PYRE);
    addBlock(ModBlocks.REINFORCED_SOUL_PYRE);
    addBlock(ModBlocks.DECORATIVE_PYRE);
    addBlock(ModBlocks.DECORATIVE_SOUL_PYRE);
    addBlock(ModBlocks.UNENDING_BOWL);
    addBlock(ModBlocks.BAFFLECAP);
    addBlock(ModBlocks.WILDROOT_CROP);
    addBlock(ModBlocks.CLOUD_BERRY_CROP);
    addBlock(ModBlocks.DEWGONIA_CROP);
    addBlock(ModBlocks.INFERNO_BULB_CROP);
    addBlock(ModBlocks.STALICRIPE_CROP);
    addBlock(ModBlocks.MOONGLOW_CROP);
    addBlock(ModBlocks.PERESKIA_CROP);
    addBlock(ModBlocks.SPIRITLEAF_CROP);
    addBlock(ModBlocks.WILDEWHEET_CROP);
    addBlock(ModBlocks.AUBERGINE_CROP);
    addBlock(ModBlocks.WILD_AUBERGINE);
    addBlock(ModBlocks.POTTED_BAFFLECAP);
    addBlock(ModBlocks.POTTED_STONEPETAL);
    addBlock(ModBlocks.POTTED_WILDWOOD_SAPLING);
    addBlock(ModBlocks.RUNIC_DUST);
    addBlock(ModBlocks.RUNESTONE_TICKER);

    // Some potential duplicates with ModBlocks

    // Items
    addItem(ModItems.WILDROOT);
    addItem(ModItems.GROVE_MOSS);
    addItem(ModItems.CLOUD_BERRY);
    addItem(ModItems.DEWGONIA);
    addItem(ModItems.INFERNO_BULB);
    addItem(ModItems.STALICRIPE);
    addItem(ModItems.BAFFLECAP);
    addItem(ModItems.MOONGLOW);
    addItem(ModItems.PERESKIA);
    addItem(ModItems.SPIRITLEAF);
    addItem(ModItems.WILDEWHEET);
    addItem(ModItems.MOONGLOW_SEEDS);
    addItem(ModItems.PERESKIA_BULB);
    addItem(ModItems.SPIRITLEAF_SEEDS);
    addItem(ModItems.WILDEWHEET_SEEDS);
    addItem(ModItems.GROVE_SPORES);
    addItem(ModItems.AUBERGINE_SEEDS);
    addItem(ModItems.CARAPACE);
    addItem(ModItems.PELT);
    addItem(ModItems.ANTLERS);
    addItem(ModItems.VENISON);
    addItem(ModItems.COOKED_VENISON);
    addItem(ModItems.RAW_SQUID);
    addItem(ModItems.COOKED_SQUID);
    addItem(ModItems.ASSORTED_SEEDS);
    addItem(ModItems.COOKED_SEEDS);
    addItem(ModItems.COOKED_BEETROOT);
    addItem(ModItems.COOKED_CARROT);
    addItem(ModItems.AUBERGINE);
    addItem(ModItems.COOKED_AUBERGINE);
    addItem(ModItems.STUFFED_AUBERGINE);
    addItem(ModItems.AUBERGINE_SALAD);
    addItem(ModItems.BEETROOT_SALAD);
    addItem(ModItems.STEWED_EGGPLANT);
    addItem(ModItems.APPLE_CORDIAL);
    addItem(ModItems.CACTUS_SYRUP);
    addItem(ModItems.DANDELION_CORDIAL);
    addItem(ModItems.LILAC_CORDIAL);
    addItem(ModItems.PEONY_CORDIAL);
    addItem(ModItems.ROSE_CORDIAL);
    addItem(ModItems.VINEGAR);
    addItem(ModItems.VEGETABLE_JUICE);
    addItem(ModItems.INK_BOTTLE);
    addItem(ModItems.APOTHECARY_POUCH);
    addItem(ModItems.COMPONENT_POUCH);
    addItem(ModItems.CREATIVE_POUCH);
    addItem(ModItems.SYLVAN_POUCH);
    addItem(ModItems.HERB_POUCH);
    addItem(ModItems.COOKED_PERESKIA);
    addItem(ModItems.FLOUR);
    addItem(ModItems.WILDEWHEET_BREAD);
    addItem(ModItems.WILDROOT_STEW);
    addItem(ModItems.FIRE_STARTER);
    addItem(ModItems.GRAMARY);
    addItem(ModItems.LIVING_ARROW);
    addItem(ModItems.LIVING_AXE);
    addItem(ModItems.LIVING_HOE);
    addItem(ModItems.LIVING_PICKAXE);
    addItem(ModItems.LIVING_SHOVEL);
    addItem(ModItems.LIVING_SWORD);
    addItem(ModItems.PESTLE);
    addItem(ModItems.RUNED_PICKAXE);
    addItem(ModItems.RUNED_AXE);
    addItem(ModItems.RUNED_DAGGER);
    addItem(ModItems.RUNED_HOE);
    addItem(ModItems.RUNED_SHOVEL);
    addItem(ModItems.RUNED_SWORD);
    addItem(ModItems.RUNIC_SHEARS);
    addItem(ModItems.STAFF);
    addItem(ModItems.CREATIVE_STAFF);
    addItem(ModItems.WILDWOOD_BOW);
    addItem(ModItems.WILDWOOD_QUIVER);
    addItem(ModItems.WOODEN_SHEARS);
    addItem(ModItems.WOODEN_KNIFE);
    addItem(ModItems.STONE_KNIFE);
    addItem(ModItems.COPPER_KNIFE);
    addItem(ModItems.IRON_KNIFE);
    addItem(ModItems.GOLDEN_KNIFE);
    addItem(ModItems.SILVER_KNIFE);
    addItem(ModItems.DIAMOND_KNIFE);
    addItem(ModItems.NETHERITE_KNIFE);
    addItem(ModItems.RELIQUARY);
    addItem(ModItems.SPIRIT_BAG);
    addItem(ModItems.SYLVAN_LEATHER);
    addItem(ModItems.GLASS_EYE);
    addItem(ModItems.LIFE_ESSENCE);
    addItem(ModItems.MYSTIC_FEATHER);
    addItem(ModItems.STRANGE_OOZE);
    addItem(ModItems.ANTLER_HAT);
    addItem(ModItems.BEETLE_HELMET);
    addItem(ModItems.BEETLE_CHESTPLATE);
    addItem(ModItems.BEETLE_LEGGINGS);
    addItem(ModItems.BEETLE_BOOTS);
    addItem(ModItems.RAW_SILVER);
    addItem(ModItems.SILVER_INGOT);
    addItem(ModItems.SILVER_NUGGET);
    addItem(ModItems.SILVER_STATER);
    addItem(ModItems.COPPER_NUGGET);
    addItem(ModItems.COPPER_AXE);
    addItem(ModItems.COPPER_HOE);
    addItem(ModItems.COPPER_PICKAXE);
    addItem(ModItems.COPPER_SHOVEL);
    addItem(ModItems.COPPER_SWORD);
    addItem(ModItems.COPPER_HELMET);
    addItem(ModItems.COPPER_CHESTPLATE);
    addItem(ModItems.COPPER_LEGGINGS);
    addItem(ModItems.COPPER_BOOTS);
    addItem(ModItems.ALERTNESS_CHARM);
    addItem(ModItems.HOMESICKNSES_CHARM);
    addItem(ModItems.BEETLE_SPAWN_EGG);
    addItem(ModItems.JERBOA_SPAWN_EGG);
    addItem(ModItems.DEER_SPAWN_EGG);
    addItem(ModItems.FENNEC_SPAWN_EGG);
    addItem(ModItems.GREEN_SPROUT_SPAWN_EGG);
    addItem(ModItems.TAN_SPROUT_SPAWN_EGG);
    addItem(ModItems.RED_SPROUT_SPAWN_EGG);
    addItem(ModItems.PURPLE_SPROUT_SPAWN_EGG);
    addItem(ModItems.SNOW_SPROUT_SPAWN_EGG);
    addItem(ModItems.MELODY_SPROUT_SPAWN_EGG);
    addItem(ModItems.OWL_SPAWN_EGG);
    addItem(ModItems.DUCK_SPAWN_EGG);

    addEntity(ModEntities.BEETLE);
    addEntity(ModEntities.DEER);
    addEntity(ModEntities.FENNEC);
    addEntity(ModEntities.GREEN_SPROUT);
    addEntity(ModEntities.TAN_SPROUT);
    addEntity(ModEntities.RED_SPROUT);
    addEntity(ModEntities.PURPLE_SPROUT);
    addEntity(ModEntities.SNOW_SPROUT);
    addEntity(ModEntities.MELODY_SPROUT);
    addEntity(ModEntities.OWL);
    addEntity(ModEntities.DUCK);
    addEntity(ModEntities.JERBOA);

    addEntity(ModEntities.LIVING_ARROW);
    addEntity(ModEntities.METEOR);
    addEntity(ModEntities.TEMPORAL_MORASS);
    addEntity(ModEntities.LIGHT_DRIFTER);
    addEntity(ModEntities.WILDFIRE);
    addEntity(ModEntities.ROSE_THORNS);

    addEffect(ModEffects.FRIENDLY_EARTH);
    addEffect(ModEffects.WAKEFUL);
    addEffect(ModEffects.PETAL_SHELL);
    addEffect(ModEffects.SENSE_DANGER);
    addEffect(ModEffects.SKY_SOARER);
    addEffect(ModEffects.NONDETECTION);
    addEffect(ModEffects.GEAS);
    addEffect(ModEffects.TEMPORAL_MORASS);
    addEffect(ModEffects.AQUA_BUBBLE);
    addEffect(ModEffects.HOMESICKNESS);
    addEffect(ModEffects.LIGHT_DRIFTER);
    addEffect(ModEffects.DANDELION_WINDS);
    addEffect(ModEffects.VORTEX_COOLDOWN);
    addEffect(ModEffects.MAGNETIC_COOLDOWN);

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
    add(RootsTags.Items.BOTTLES, "Bottles");
    add(RootsTags.Items.POUCHES, "Pouches");
    add(RootsTags.Items.GROVE_CRAFTER_ACTIVATION, "Grove Crafter Activators");
    add(RootsTags.Items.MORTAR_ACTIVATION, "Mortar Activators");
    add(RootsTags.Items.PYRE_ACTIVATION, "Pyre Activators");
    add(RootsTags.Items.FLINT, "Flint");
    add(RootsTags.Items.STONELIKE, "Stone-like");
    add(RootsTags.Items.CASTING_TOOLS, "Casting Tools");
    add(RootsTags.Items.RUNESTONE_HERBS, "Herbs for crafting Runestone");
    add(RootsTags.Items.NYI, "[Not Yet Implemented]");
    add(RootsTags.Items.WIP, "[Work In Progress]");
    add(RootsTags.Items.SOILS, "Soils");
    add(RootsTags.Items.WATER_SOIL, "Water Elemental Soils");
    add(RootsTags.Items.AIR_SOIL, "Air Elemental Soils");
    add(RootsTags.Items.EARTH_SOIL, "Earth Elemental Soils");
    add(RootsTags.Items.FIRE_SOIL, "Fire Elemental Soils");
    add(RootsTags.Items.ELEMENTAL_SOIL, "Elemental Soils");
    add(RootsTags.Items.RUNED_OBSIDIAN, "Runed Obsidian");
    add(RootsTags.Items.RUNESTONE, "Runestone");
    add(RootsTags.Items.WILDWOOD_LOGS, "Wildwood Logs");
    add(RootsTags.Items.GROVE_STONES, "Grove Stones");
    add(RootsTags.Items.GROVE_STONE_PRIMAL, "Primal Grove Stones");
    add(RootsTags.Items.PEDESTALS, "Pedestals");
    add(RootsTags.Items.RITUAL_PEDESTALS, "Ritual Pedestals");
    add(RootsTags.Items.GROVE_PEDESTALS, "Grove Crafting Pedestals");
    add(RootsTags.Items.PYRES, "Pyres");
    add(RootsTags.Items.GROVE_CRAFTERS, "Grove Crafters");
    add(RootsTags.Items.MORTARS, "Mortars");
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
    add(RootsTags.Items.SKIPPED_FOODS, "Ignored foods");
    add(RootsTags.Items.DEER_FOOD, "Foor for Deer");
    add(RootsTags.Items.FENNEC_FOOD, "Food for Fennecs");
    add(RootsTags.Items.BEETLE_FOOD, "Food for Beetles");
    add(RootsTags.Items.DUCK_FOOD, "Food for Ducks");
    add(RootsTags.Items.OWL_FOOD, "Food for Owls");
    add(RootsTags.Items.SPROUT_FOOD, "Food for Sprouts");

    add("roots.subtitles.spell.acid_cloud", "Acid cloud billows");
    add("roots.subtitles.spell.acid_cloud_alt", "Acid cloud billows");
    add("roots.subtitles.spell.dandelion_winds", "Dandelion winds blow");
    add("roots.subtitles.spell.desaturate", "Desaturation");
    add("roots.subtitles.spell.disarm", "Entities disarmed");
    add("roots.subtitles.spell.sylvan_light", "Sylvan light created");
    add("roots.subtitles.spell.geas", "Geas cast");
    add("roots.subtitles.spell.geas_end", "Geas ends");
    add("roots.subtitles.spell.growth_infusion", "Growth encouraged");
    add("roots.subtitles.spell.harvest", "Harvest begins");
    add("roots.subtitles.spell.aqua_bubble", "Aqua bubble encapsulates");
    add("roots.subtitles.spell.aqua_bubble_alt", "Aqua bubble encapsulates");
    add("roots.subtitles.aqua_bubble_alt_end", "Aqua bubble ends");
    add("roots.subtitles.extension", "Senses extended");
    add("roots.subtitles.spell.life_drain", "Life drained");
    add("roots.subtitles.spell.light_drifter", "Players begin drifting");
    add("roots.subtitles.spell.light_drifter_end", "Players end drifting");
    add("roots.subtitles.spell.magnetism", "Magnetism activated");
    add("roots.subtitles.spell.petal_shell", "Petal shell begins");
    add("roots.subtitles.spell.petal_shell_break", "Petal shell broken");
    add("roots.subtitles.spell.petal_shell_end", "Petal shell ends");
    add("roots.subtitles.spell.radiance", "Radiance shines");
    add("roots.subtitles.spell.rose_thorns", "Rose thorns tangle");
    add("roots.subtitles.spell.sanctuary", "Sanctuary pulses");
    add("roots.subtitles.spell.saturate", "Food consumed");
    add("roots.subtitles.spell.shatter", "Blocks shatter");
    add("roots.subtitles.spell.sky_soarer", "Sky soars");
    add("roots.subtitles.spell.storm_cloud", "Storm cloud begins");
    add("roots.subtitles.spell.storm_cloud_end", "Storm cloud ends");
    add("roots.subtitles.spell.temporal_morass", "Time slows");
    add("roots.subtitles.spell.temporal_morass_end", "Time flows normally");
    add("roots.subtitles.spell.wildfire", "Meteors unfold");
    add("roots.subtitles.event.mortar.add_item", "Mortar filled");
    add("roots.subtitles.event.mortar.remove_item", "Mortar emptied");
    add("roots.subtitles.event.mortar.use", "Pestle used");
    add("roots.subtitles.event.pyre.add_item", "Pyre filled");
    add("roots.subtitles.event.pyre.remove_item", "Pyre emptied");
    add("roots.subtitles.alert", "Targeted by enemy");
    add("roots.subtitles.block.pyre.crackle", "Pyre crackles");

    add("enchantment.roots.foraging", "Foraging");
    add("enchantment.roots.collecting", "Collecting");

    add("roots.nyi", "[Not Yet Implemented]");
    add("roots.wip", "[Work In Progress]");

    add("roots.container.herb_pouch", "Herb Pouch");
    add("roots.commands.alerts.no_player", "Sender is not a player.");
    add("roots.message.spell_modifier.already_learned", "Spell modifier '%s' already learned!");
    add("message.dandelion_cordial", "You feel more alert!");

    // JEI
    add("roots.jei.ingredient.grove_power", " Grove Power");
    add("roots.jei.ingredient.grove_reputation", " Grove Reputation");
    add("roots.jei.grove_power", "Grove Power Generation");
    add("roots.jei.grove_reputation", "Grove Reputation");
    add("roots.jei.entity_interaction", "Entity Item Interaction");
    add("roots.jei.summon_creatures", "Summon Creatures");
    add("roots.jei.runic_entity", "Runic Shears (Entity)");
    add("roots.jei.runic_block", "Runic Shears (Block)");
    add("roots.jei.knife_crafting", "Knife Carving");
    add("roots.jei.grove_crafting", "Grove Crafting");
    add("roots.jei.pyre", "Pyre");
    add("roots.jei.ritual", "Ritual");
    add("roots.jei.mortar_crafting", "Mortar Crafting");
    add("roots.jei.mortar_spell_crafting", "Spell Crafting");
    add("roots.jei.text.durability", "Durability: %s");
    add("roots.jei.text.cooldown", "Cooldown: %ss");
    add("roots.jei.sprout_gifts", "Rewards for Breeding Sprouts");
    add("roots.jei.animal_harvest", "Animal Harvest");
    add("roots.jei.grove_reputaiton", "Grove Reputation");
    add("roots.jei.animal_harvest.info", "Note: Outputs shown are default and may vary based on mod interactions.");
    add("roots.jei.fungal_transmuter", "Fungal Transmutation");
    add("roots.jei.text.grove_power", "Fungal Grove Power required: %s");

    add("roots.hud.pyre.begin1", "Light pyre to");
    add("roots.hud.pyre.begin2", "start %s");
    add("roots.hud.pyre.begin3", "craft %s");

    add("roots.hud.fake_menu", "Press [%s] to view eligible %s recipes.");
    add("roots.hud.clear", "Press [%s] to clear %s.");

    add("roots.hud.transmuter.begin1", "Right-click with");
    add("roots.hud.transmuter.begin2", "knife to");
    add("roots.hud.transmuter.begin3", "craft %s");

    add("roots.hud.pyre.restart1", "Sneak-Right-Click with");
    add("roots.hud.pyre.restart2", "empty hand to repeat");

    add("roots.hud.transmuter.restart1", "Sneak-Right-Click with");
    add("roots.hud.transmuter.restart2", "empty hand to repeat");

    add("roots.hud.transmuter.power", "Needs %s Power (%s)");

    add("roots.hud.mortar.repeat1", "Sneak-Right-Click with");
    add("roots.hud.mortar.repeat2", "empty hand to refill");

    add("roots.hud.mortar.crafting1", "Grind with pestle");
    add("roots.hud.mortar.crafting2", "%s time to craft");
    add("roots.hud.mortar.crafting3", "%s times to craft");

    add("roots.hud.item_count", "%s (%s)");

    add("roots.hud.mortar.remove_pestle", "Right-Click to remove pestle");
    add("roots.hud.mortar.store_pestle", "Right-Click to store pestle");

    add("roots.hud.grove_crafter", "Right-Click to start crafting");

    add("roots.hud.pyre.auto1", "will start automatically");

    add("roots.hud.grove_power.grove", "%s, rank %s/%s");
    add("roots.hud.grove_power.power", "Power usage: %s/%s");
    add("roots.hud.grove_power.invalid_rank", "[Requires rank 1 or higher.]");

    RootsRegistries.GROVES.entrySet().forEach(o -> {
          add(o.getValue().getDescriptionId(), toEnglishName(o.getKey().location().getPath()));
        }
    );

    add("roots.item.gramary.bound_block_entity", "Bound Block Entity to position %s/%s/%s");
    add("roots.item.gramary.bound_block_position", "Bound Gramary to position %s/%s/%s");

    add("roots.item.gramary.with_mode", "Gramary (%s)");

    add("roots.transmutation.not_enough_power", "Not enough Grove Power to transmute this item. Current power: %s, required: %s");

    add("roots.commands.alerts.synced", "Synced herb alerts for %s for all herbs (0.165)");
    add("roots.commands.alerts.synced.herb", "Cannot sync herb alerts for non-player command senders.");
    add("roots.commands.library.usage", "Usage: /roots library <add|remove|list|clear> <spell>");
    add("roots.commands.library.add.usage", "Usage: /roots library add <spell>");
    add("roots.commands.library.remove.usage", "Usage: /roots library remove <spell>");
    add("roots.commands.library.clear.success", "Cleared spell library.");
    add("roots.commands.library.clear.failure", "Spell library is already empty.");
    add("roots.commands.library.list.entry", "Library entry: %s");
    add("roots.commands.library.list.empty", "Spell library is empty.");
    add("roots.commands.library.add.success", "Added spell %s to library.");
    add("roots.commands.library.add.failure", "Failed to add spell %s to library. Spell not found or already in library.");
    add("roots.commands.library.remove.success", "Removed spell %s from library.");
    add("roots.commands.library.remove.failure", "Failed to remove spell %s from library. Spell not found or not in library.");
    add("roots.commands.library.no_player", "Command can only be executed by a player.");

    add("roots.gui.spell_library", "Spell Library");
    add("roots.gui.reputation", "Grove Reputations");
    add("roots.gui.light_drifter_overlay", "Drifting %s/%s blocks from your body!");
    add("roots.gui.effect_start_canceling", "Hold [%s] to cancel %s.");
    add("roots.gui.effect_cancel", "Press [%s] to cancel %s.");
    add("roots.gui.effect_continue_canceling", "Continue holding [%s] to cancel %s.");

    add("container.wildwoodchest", "Wildwood Chest");

    for (GrovePowerGenerator.Symmetry sym : GrovePowerGenerator.Symmetry.values()) {
      add(sym.getTranslationKey(), toEnglishName(sym.getSerializedName()));
    }

    add(GrovePowerGenerator.Symmetry.RADIAL_NOT_MATCHING.getTranslationKey() + ".description", "Requires a non-matching block positioned radially around the grove stone.");
    add(GrovePowerGenerator.Symmetry.RADIAL_SAME_BLOCK_OR_TAG.getTranslationKey() + ".description", "Requires a matching block or a block from the same tag positioned radially around the grove stone.");
    add(GrovePowerGenerator.Symmetry.RADIAL_SAME_BLOCK.getTranslationKey() + ".description", "Requires an identical block positioned radially around the grove stone.");
    add(GrovePowerGenerator.Symmetry.RADIAL_DIFFERENT_SAME_TAG.getTranslationKey() + ".description", "Requires a different block from the same tag positioned radially around the grove stone.");
    add(GrovePowerGenerator.Symmetry.NONE.getTranslationKey() + ".description", "No symmetry required.");

    spellDescription(ModSpells.SKY_SOARER, "Propels you through the air in the direction you are looking.");
    spellExtendedDescription(ModSpells.SKY_SOARER, "Propels you through the air at ×%s sprint speed [%s] for %s seconds [%s ticks] in the direction you are looking.");
    modifierDescription(ModModifiers.SKY_SOARER_AMPLIFIED_1, "Increases speed of boost effect.");
    modifierExtendedDescription(ModModifiers.SKY_SOARER_AMPLIFIED_1, "Increases boost effect to ×%s sprint speed [%s].");
    modifierDescription(ModModifiers.SKY_SOARER_AMPLIFIED_2, "Increases speed of boost effect.");
    modifierExtendedDescription(ModModifiers.SKY_SOARER_AMPLIFIED_2, "Increases boost effect to ×%s sprint speed [%s].");
    modifierDescription(ModModifiers.SKY_SOARER_SPEEDY_1, "Increases duration of boost effect.");
    modifierExtendedDescription(ModModifiers.SKY_SOARER_SPEEDY_1, "Increases duration of boost effect by [%s%%] to %s seconds [%s ticks].");
    modifierDescription(ModModifiers.SKY_SOARER_SPEEDY_2, "Increases duration of boost effect.");
    modifierExtendedDescription(ModModifiers.SKY_SOARER_SPEEDY_2, "Increases duration of boost effect by [%s%%] to %s seconds [%s ticks].");
    modifierDescriptionBoth(ModModifiers.SHATTER_MAGNETISM, "Teleports dropped blocks to the player.");
    modifierDescriptionBoth(ModModifiers.SHATTER_SILK_TOUCH, "Applies silk touch when breaking blocks.");
    modifierDescriptionBoth(ModModifiers.SHATTER_FORTUNE_I, "Applies Fortune I when breaking blocks.");
    modifierDescriptionBoth(ModModifiers.SHATTER_FORTUNE_II, "Applies Fortune II when breaking blocks.");
    modifierDescriptionBoth(ModModifiers.SHATTER_FORTUNE_III, "Applies Fortune III when breaking blocks.");
    modifierDescriptionBoth(ModModifiers.SHATTER_SMELTING, "Smelts items dropped by broken blocks.");

    modifierDescription(ModModifiers.SKY_SOARER_FRIENDLY_EARTH, "Prevents you from taking fall damage after boost effect expires.");
    modifierExtendedDescription(ModModifiers.SKY_SOARER_FRIENDLY_EARTH, "Prevents you from taking fall damage for %s seconds [%s ticks] after the boost effect ends.");

    spellDescription(ModSpells.ACID_CLOUD, "Creates a corrosive, damaging cloud around you while channeled.");
    spellExtendedDescription(ModSpells.ACID_CLOUD, "Creates a corrosive cloud that deals %s hearts of damage to up to %s entities within the cloud's radius.");

    modifierDescription(ModModifiers.ACID_CLOUD_FIRE, "Damaged entities are also set on fire.");
    modifierExtendedDescription(ModModifiers.ACID_CLOUD_FIRE, "Damaged entities are also set on fire for %s seconds [%s ticks].");
    modifierDescriptionBoth(ModModifiers.ACID_CLOUD_PEACEFUL, "Prevents damage to non-hostile entities.");

    spellDescription(ModSpells.AQUA_BUBBLE, "Surrounds you in a bubble of water, shielding your health and reducing fire damage.");
    spellExtendedDescription(ModSpells.AQUA_BUBBLE, "Surrounds you in a bubble of water for %s seconds [%s ticks] and grants %s hearts of absorption for the same duration. While active, reduces damage from lava by %s%% and damage from fire by %s%%. Triggers a base cooldown of %s seconds [%s ticks].");

    spellDescription(ModSpells.LIGHT_DRIFTER, "Expels you from your body, allowing you to float through entities and terrain for a short time and distance. Returns you to your body upon expiration.");
    spellExtendedDescription(ModSpells.LIGHT_DRIFTER, "Expels you from your body for %s seconds [%s ticks], allowing you to float through entities and terrain up to %s blocks away from your location when casting the spell. Triggers a base cooldown of %s seconds [%s ticks].");
    spellDescription(ModSpells.MAGNETISM, "Transports loose items and experience orbs to your location.");
    spellDescription(ModSpells.DANDELION_WINDS, "Has a chance to deflect projectiles aimed at you.");
    spellExtendedDescription(ModSpells.DANDELION_WINDS, "Applies a buff for %s seconds [%s ticks] that gives a %s%% chance to deflect projectiles aimed at you.");
    modifierDescription(ModModifiers.DANDELION_WINDS_DURATION_1, "Increases the duration of the Dandelion Winds effect.");
    modifierExtendedDescription(ModModifiers.DANDELION_WINDS_DURATION_1, "Increases the duration of the boost effect by %s seconds [%s ticks] to a total of %s seconds [%s ticks].");
    modifierDescription(ModModifiers.DANDELION_WINDS_DURATION_2, "Increases the duration of the Dandelion Winds effect.");
    modifierExtendedDescription(ModModifiers.DANDELION_WINDS_DURATION_2, "Increases the duration of the boost effect by %s seconds [%s ticks] to a total of %s seconds [%s ticks].");
    modifierDescription(ModModifiers.DANDELION_WINDS_DURATION_3, "Increases the duration of the Dandelion Winds effect.");
    modifierExtendedDescription(ModModifiers.DANDELION_WINDS_DURATION_3, "Increases the duration of the boost effect by %s seconds [%s ticks] to a total of %s seconds [%s ticks].");
    modifierDescription(ModModifiers.DANDELION_WINDS_DURATION_4, "Increases the duration of the Dandelion Winds effect.");
    modifierExtendedDescription(ModModifiers.DANDELION_WINDS_DURATION_4, "Increases the duration of the boost effect by %s seconds [%s ticks] to a total of %s seconds [%s ticks].");
    modifierDescription(ModModifiers.DANDELION_WINDS_DURATION_5, "Increases the duration of the Dandelion Winds effect.");
    modifierExtendedDescription(ModModifiers.DANDELION_WINDS_DURATION_5, "Increases the duration of the boost effect by %s seconds [%s ticks] to a total of %s seconds [%s ticks].");
    modifierDescription(ModModifiers.DANDELION_WINDS_CHANCE_1, "Increases the chance to deflect projectiles.");
    modifierExtendedDescription(ModModifiers.DANDELION_WINDS_CHANCE_1, "Increases the chance to deflect projectiles by %s%% to a total of %s%%.");
    modifierDescription(ModModifiers.DANDELION_WINDS_CHANCE_2, "Increases the chance to deflect projectiles.");
    modifierExtendedDescription(ModModifiers.DANDELION_WINDS_CHANCE_2, "Increases the chance to deflect projectiles by %s%% to a total of %s%% (including parent modifiers).");
    modifierDescription(ModModifiers.DANDELION_WINDS_CHANCE_3, "Increases the chance to deflect projectiles.");
    modifierExtendedDescription(ModModifiers.DANDELION_WINDS_CHANCE_3, "Increases the chance to deflect projectiles by %s%% to a total of %s%% (including parent modifiers).");
    modifierDescription(ModModifiers.DANDELION_WINDS_CHANCE_4, "Increases the chance to deflect projectiles.");
    modifierExtendedDescription(ModModifiers.DANDELION_WINDS_CHANCE_4, "Increases the chance to deflect projectiles by %s%% to a total of %s%% (including parent modifiers).");

    spellDescription(ModSpells.DECAY, "Causes undead entities to decay and shed resources, losing maximum health.");
    spellDescription(ModSpells.DESATURATE, "Directly converts saturation and food levels into health.");
    spellDescription(ModSpells.SATURATE, "Directly converts food and drink in your inventory to saturation and food levels. Some value is lost in this process.");
    spellDescription(ModSpells.DISARM, "Disarms entities around you, with a chance to drop the held items.");
    spellDescription(ModSpells.EXTENSION, "Extends your senses, allowing you to see in the dark and sense nearby enemies.");
    spellDescription(ModSpells.NONDETECTION, "Reduces the range at which enemies can detect your presence.");
    spellDescription(ModSpells.SYLVAN_LIGHT, "Creates a permanent glowing orb of light.");
    spellDescription(ModSpells.GEAS, "Places a geas on a nearby entity. While under its effects, the entity will be reluctant to attack you.");
    spellDescription(ModSpells.SUMMON_UNDEAD, "Summons an undead servant to fight for you.");
    spellDescription(ModSpells.GROWTH_INFUSION, "Causes accelerated growth to the targeted, eligible block.");
    spellDescription(ModSpells.RAMPANT_GROWTH, "Causes accelerated growth to all eligible blocks in a radius around you.");
    spellDescription(ModSpells.HARVEST, "Harvests and replants all eligible blocks in a radius around you.");
    spellDescription(ModSpells.LIFE_DRAIN, "Attempts to drain the life from enemies around you while channeled. Some of the damage taken is converted to healing for you.");
    spellDescription(ModSpells.PETAL_SHELL, "Creates a shield of impenetrable petals around you for the duration. Each attack you take can be blocked by a petal, breaking that petal until none remain.");
    spellDescription(ModSpells.RADIANCE, "Shoots a beam of destructive light in the direction you are facing. This beam will damage all entities that it touches.");
    spellDescription(ModSpells.ROSE_THORNS, "Creates a temporary cluster of viciously sharp vines in the area you are looking. For its duration, the first entity that touches it will be damaged and become trapped.");
    spellDescription(ModSpells.SHATTER, "Breaks the targeted blocks. The size and dimensions of the spell can be adjusted."); // TODO: Spell description keybinds
    spellDescription(ModSpells.JAUNT, "Teleports you a short way in the distance you are looking. It will attempt to place you on the next highest or lowest surface available, if such exists.");
    spellDescription(ModSpells.STORM_CLOUD, "Creates a vicious cloud of storms around you. For the duration, lightning from the cloud may strike nearby enemies.");
    spellDescription(ModSpells.TEMPORAL_MORASS, "Creates a temporary field of disruptive energy. All entities within this field will have their movement dramatically slowed.");
    spellDescription(ModSpells.WILDFIRE, "Flings a fiery meteor in the direction you are looking. If this meteor hits an enemy, it will damage it.");

    addDamage(ModDamage.ACID_CLOUD, "%1$s expired in a cloud of poison", "%1$s expired in a cloud of poison while fighting $2%s", "%1$s expired in a cloud of poison while fighting %2$s wielding %3$s");
    addDamage(ModDamage.LIFE_DRAIN, "%1$s was drained away to nothing", "%1$s was drained away to nothing while fighting %2$s", "%1$s was drained away to nothing while fighting %2$s wielding %3$s");
    addDamage(ModDamage.METEOR, "%1$s was killed by a falling meteor", "%1$s was killed by a falling meteor while fighting %2$s", "%1$s was killed by a falling meteor while fighting %2$s wielding %3$s");
    addDamage(ModDamage.ROSE_THORNS, "%1$s was poked to death by rose thorns", "%1$s was poked to death by rose thorns while fighting %2$s", "%1$s was poked to death by rose thorns while fighting %2$s wielding %3$s");
    addDamage(ModDamage.WILDFIRE, "%1$s was immolated by wild fire", "%1$s was immolated by wild fire while fighting %2$s", "%1$s was immolated by wild fire while fighting %2$s wielding %3$s");
  }

  private void addCyclingMode(Cycling<?>[] values) {
    for (Cycling<?> cycle : values) {
      add(cycle.getDescriptionId(), toEnglishName(cycle.getSerializedName()));
    }
  }

  // TODO: Translations for damage
  public void addDamage(ResourceKey<DamageType> damage, String death, String player, String item) {
    var prefix = "death.attack." + damage.location();
    add(prefix, death);
    add(prefix + ".player", player);
    add(prefix + ".item", item);
  }

  public void spellDescription(Holder<Spell> spell, String value) {
    add(spell.value().getTooltipDescriptionId(), value);
  }

  public void spellExtendedDescription(Holder<Spell> spell, String value) {
    add(spell.value().getTooltipExtendedDescriptionId(), value);
  }

  public void modifierDescriptionBoth(Holder<SpellModifier> spellModifier, String value) {
    modifierDescription(spellModifier, value);
    modifierExtendedDescription(spellModifier, value);
  }

  public void modifierDescription(Holder<SpellModifier> spellModifier, String value) {
    add(spellModifier.value().getTooltipDescriptionId(), value);
  }

  public void modifierExtendedDescription(Holder<SpellModifier> spellModifier, String value) {
    add(spellModifier.value().getTooltipExtendedDescriptionId(), value);
  }

  public static String toEnglishName(String internalName) {
    String[] segments = internalName.toLowerCase(Locale.ROOT).split("_");
    var joiner = new StringJoiner(" ");

    for (String seg : segments) {
      joiner.add(switch (seg) {
        case "ii" -> "II";
        case "iii" -> "III";
        case "iv" -> "IV";
        case "vi" -> "VI";
        default -> StringUtils.capitalize(seg);
      });
    }

    return joiner.toString();
  }

  public static String getComplexDescription(String defaultValue) {
    String[] split = defaultValue.split("/");
    return String.format("%s: %s", toEnglishName(split[0]), toEnglishName(split[1]));
  }
}
