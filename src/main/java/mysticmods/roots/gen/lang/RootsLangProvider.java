package mysticmods.roots.gen.lang;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.client.KeyBindings;
import mysticmods.roots.init.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;


public class RootsLangProvider extends LanguageProvider {
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
    // Tag translations
    add("itemGroup.roots", "Roots");
    add("itemGroup.roots_spells", "Roots Spells");
    add("itemGroup.roots_rituals", "Roots Rituals");

    add("roots.reputation.decreased", "Your reputation with the %s grove has decreased by %s");
    add("roots.reputation.increased", "Your reputation with the %s grove has increased by %s");

    add("roots.tooltip.effect", "Use to gain %s for %s seconds.");
    add("roots.tooltip.chance", "Chance: %s%%");

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
    add("roots.tooltip.cost.charge_type", "[Charges %s.]");
    add("roots.tooltip.cost.charge_type.operation", "per operation");
    add("roots.tooltip.cost.charge_type.cast", "per cast");

    add("roots.tooltip.pouch.color", "Dyed: %s");
    add("roots.tooltip.pouch.color_name", "%s");

    add("roots.tooltip.staff.selected", "Selected Slot: %s");
    add("roots.tooltip.staff.no_spell", "No spell.");
    add("roots.tooltip.staff.cooldown", " (CD: %ss)");
    add("roots.tooltip.staff.spell_in_slot", "%s: %s%s%s");
    add("roots.tooltip.staff.is_selected", " (Selected)");
    add("roots.tooltip.staff.data", "  %s: %s");

    add("roots.tooltip.staff.key_binding", "Press '%s' to open your spell library.");
    add("roots.tooltip.hold_shift", "[Hold %s for more information.]");
    add("roots.tooltip.shift", "Shift");

    add("roots.item.staff.with_spell", "Staff (%s)");

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

    RootsRegistries.SPELLS.entrySet().forEach(o -> {
          add(o.getValue().getDescriptionId(), toEnglishName(o.getKey().location().getPath()));
          for (String n : o.getValue().getDataKeys()) {
            add(o.getValue().getDescriptionId() + ".data.mode." + n, "Modifying: " + toEnglishName(n));
            // TODO: Value -> name
            add(o.getValue().getDescriptionId() + ".data." + n, "Set " + toEnglishName(n) + " to %s");
            add(o.getValue().getDescriptionId() + ".data." + n + ".name", toEnglishName(n));
          }
        }
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
    RootsRegistries.LEVEL_CONDITIONS.entrySet().forEach(o -> {
          if (o == ModConditions.OVERGROWTH_CONDITION) {
            add(o.getValue().getDescriptionId(), "Water Source or Creeping Grove Moss");

          } else {
            add(o.getValue().getDescriptionId(), toEnglishName(o.getKey().location().getPath()));
          }
        }
    );
    RootsRegistries.PLAYER_CONDITIONS.entrySet().forEach(o ->
        add(o.getValue().getDescriptionId(), toEnglishName(o.getKey().location().getPath()))
    );

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
    addBlock(ModBlocks.RUNED_WILDWOOD_LOG);
    addBlock(ModBlocks.RUNED_SPRUCE_LOG);
    addBlock(ModBlocks.RUNED_JUNGLE_LOG);
    addBlock(ModBlocks.RUNED_BIRCH_LOG);
    addBlock(ModBlocks.RUNED_OAK_LOG);
    addBlock(ModBlocks.RUNED_DARK_OAK_LOG);
    addBlock(ModBlocks.RUNED_ACACIA_LOG);
    addBlock(ModBlocks.RUNED_MANGROVE_LOG);
    addBlock(ModBlocks.RUNED_WARPED_STEM);
    addBlock(ModBlocks.RUNED_CRIMSON_STEM);
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
    addBlock(ModBlocks.FEY_LIGHT);
    addBlock(ModBlocks.RITUAL_PEDESTAL);
    addBlock(ModBlocks.REINFORCED_RITUAL_PEDESTAL);
    addBlock(ModBlocks.GROVE_CRAFTER);
    addBlock(ModBlocks.GROVE_PEDESTAL);
    addBlock(ModBlocks.WILDWOOD_PEDESTAL);
    addBlock(ModBlocks.DISPLAY_PEDESTAL);
    addBlock(ModBlocks.WILD_ROOTS);
    addBlock(ModBlocks.CREEPING_GROVE_MOSS);
    addBlock(ModBlocks.HANGING_GROVE_MOSS);
    addBlock(ModBlocks.BAFFLECAP_BLOCK);
    addBlock(ModBlocks.PRIMAL_GROVE_STONE);
    addBlock(ModBlocks.INCENSE_BURNER);
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
    addItem(ModItems.ACACIA_BARK);
    addItem(ModItems.BIRCH_BARK);
    addItem(ModItems.DARK_OAK_BARK);
    addItem(ModItems.JUNGLE_BARK);
    addItem(ModItems.OAK_BARK);
    addItem(ModItems.SPRUCE_BARK);
    addItem(ModItems.WILDWOOD_BARK);
    addItem(ModItems.CRIMSON_BARK);
    addItem(ModItems.WARPED_BARK);
    addItem(ModItems.MANGROVE_BARK);
    addItem(ModItems.MIXED_BARK);
    addItem(ModItems.APOTHECARY_POUCH);
    addItem(ModItems.COMPONENT_POUCH);
    addItem(ModItems.CREATIVE_POUCH);
    addItem(ModItems.FEY_POUCH);
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
    addItem(ModItems.FEY_LEATHER);
    addItem(ModItems.GLASS_EYE);
    addItem(ModItems.LIFE_ESSENCE);
    addItem(ModItems.MYSTIC_FEATHER);
    addItem(ModItems.PETALS);
    addItem(ModItems.RUNIC_DUST);
    addItem(ModItems.STRANGE_OOZE);
    addItem(ModItems.ANTLER_HAT);
    addItem(ModItems.BEETLE_HELMET);
    addItem(ModItems.BEETLE_CHESTPLATE);
    addItem(ModItems.BEETLE_LEGGINGS);
    addItem(ModItems.BEETLE_BOOTS);
    addItem(ModItems.RAW_SILVER);
    addItem(ModItems.SILVER_INGOT);
    addItem(ModItems.SILVER_NUGGET);
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
    addItem(ModItems.BEETLE_SPAWN_EGG);
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

    addEntity(ModEntities.LIVING_ARROW);
    addEntity(ModEntities.METEOR);
    addEntity(ModEntities.TIME_STOP);
    addEntity(ModEntities.WILDFIRE);
    addEntity(ModEntities.ROSE_THORNS);

    addEffect(ModEffects.FRIENDLY_EARTH);
    addEffect(ModEffects.WAKEFUL);
    addEffect(ModEffects.PETAL_SHELL);
    addEffect(ModEffects.SENSE_DANGER);
    addEffect(ModEffects.SKY_SOARER);
    addEffect(ModEffects.NONDETECTION);
    addEffect(ModEffects.GEAS);
    addEffect(ModEffects.TIME_STOP);
    addEffect(ModEffects.AQUA_BUBBLE);

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
    add(RootsTags.Items.FERTILIZERS, "Fertilizers");
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
    add("roots.subtitles.spell.fey_light", "Fey light created");
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
    add("roots.subtitles.spell.sanctuary", "");
    add("roots.subtitles.spell.saturate", "");
    add("roots.subtitles.spell.shatter", "Blocks shatter");
    add("roots.subtitles.spell.sky_soarer", "Sky soars");
    add("roots.subtitles.spell.storm_cloud", "Storm cloud begins");
    add("roots.subtitles.spell.storm_cloud_end", "Storm cloud ends");
    add("roots.subtitles.spell.time_stop", "Time stops");
    add("roots.subtitles.spell.time_stop_end", "Time begins anew");
    add("roots.subtitles.spell.wildfire", "Meteors unfold");
    add("roots.subtitles.event.mortar.add_item", "Mortar filled");
    add("roots.subtitles.event.mortar.remove_item", "Mortar emptied");
    add("roots.subtitles.event.mortar.use", "Pestle used");
    add("roots.subtitles.event.pyre.add_item", "Pyre filled");
    add("roots.subtitles.event.pyre.remove_item", "Pyre emptied");
    add("roots.subtitles.item.unripe_pearl.use", "");
    add("roots.subtitles.item.pearleporter.use", "");
    add("roots.subtitles.block.pyre.crackle", "Pyre crackles");

    add("enchantment.roots.foraging", "Foraging");

    add("roots.nyi", "[Not Yet Implemented]");
    add("roots.wip", "[Work In Progress]");

    // JEI
    add("roots.jei.runic_entity", "Runic Shears (Entity)");
    add("roots.jei.runic_block", "Runic Shears (Block)");
    add("roots.jei.knife_crafting", "Knife Carving");
    add("roots.jei.grove_crafting", "Grove Crafting");
    add("roots.jei.pyre", "Pyre");
    add("roots.jei.mortar_crafting", "Mortar Crafting");
    add("roots.jei.text.durability", "Durability: %s");
    add("roots.jei.text.cooldown", "Cooldown: %ss");

    RootsRegistries.GROVES.entrySet().forEach(o -> {
          add(o.getValue().getDescriptionId(), toEnglishName(o.getKey().location().getPath()));
        }
    );
  }

  // TODO: Translations for damage
/*  public void addDamage(ResourceKey<DamageType> damage, String death, String item, String player) {
    add("death.attack.")
  } */

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
