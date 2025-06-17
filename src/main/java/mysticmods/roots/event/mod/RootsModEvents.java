package mysticmods.roots.event.mod;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModAttributes;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.init.ModTabs;
import mysticmods.roots.item.Dyeable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

@EventBusSubscriber(modid = RootsAPI.MODID, bus = EventBusSubscriber.Bus.MOD)
public class RootsModEvents {
  @SubscribeEvent
  public static void creativeTabOrder(BuildCreativeModeTabContentsEvent event) {
    if (event.getTab().equals(ModTabs.ROOTS_TAB.get())) {
      event.accept(ModItems.THATCH.get());
      event.accept(ModItems.RUNESTONE.get());
      event.accept(ModItems.MOSSY_RUNESTONE.get());
      event.accept(ModItems.RUNESTONE_BRICK.get());
      event.accept(ModItems.RUNESTONE_TILE.get());
      event.accept(ModItems.CHISELED_RUNESTONE.get());
      event.accept(ModItems.RUNED_OBSIDIAN.get());
      event.accept(ModItems.RUNED_BRICK.get());
      event.accept(ModItems.RUNED_TILE.get());
      event.accept(ModItems.CHISELED_RUNED_OBSIDIAN.get());
      event.accept(ModItems.SILVER_ORE.get());
      event.accept(ModItems.DEEPSLATE_SILVER_ORE.get());
      event.accept(ModItems.GRANITE_QUARTZ_ORE.get());
      event.accept(ModItems.RAW_SILVER_BLOCK.get());
      event.accept(ModItems.SILVER_BLOCK.get());
      event.accept(ModItems.WILDWOOD_LOG.get());
      event.accept(ModItems.STRIPPED_WILDWOOD_LOG.get());
      event.accept(ModItems.WILDWOOD_WOOD.get());
      event.accept(ModItems.STRIPPED_WILDWOOD_WOOD.get());
      event.accept(ModItems.WILDWOOD_PLANKS.get());
      event.accept(ModItems.WILDWOOD_SAPLING.get());
      event.accept(ModItems.WILDWOOD_LEAVES.get());
      event.accept(ModItems.BAFFLECAP_BLOCK.get());

      event.accept(ModItems.RUNED_WILDWOOD_LOG.get());
      event.accept(ModItems.RUNED_SPRUCE_LOG.get());
      event.accept(ModItems.RUNED_JUNGLE_LOG.get());
      event.accept(ModItems.RUNED_BIRCH_LOG.get());
      event.accept(ModItems.RUNED_OAK_LOG.get());
      event.accept(ModItems.RUNED_DARK_OAK_LOG.get());
      event.accept(ModItems.RUNED_ACACIA_LOG.get());
      event.accept(ModItems.RUNED_MANGROVE_LOG.get());
      event.accept(ModItems.RUNED_WARPED_STEM.get());
      event.accept(ModItems.RUNED_CRIMSON_STEM.get());

      event.accept(ModItems.RUNESTONE_STAIRS.get());
      event.accept(ModItems.MOSSY_RUNESTONE_STAIRS.get());
      event.accept(ModItems.RUNESTONE_BRICK_STAIRS.get());
      event.accept(ModItems.RUNESTONE_TILE_STAIRS.get());
      event.accept(ModItems.RUNED_STAIRS.get());
      event.accept(ModItems.RUNED_BRICK_STAIRS.get());
      event.accept(ModItems.RUNED_TILE_STAIRS.get());
      event.accept(ModItems.WILDWOOD_STAIRS.get());

      event.accept(ModItems.RUNESTONE_SLAB.get());
      event.accept(ModItems.MOSSY_RUNESTONE_SLAB.get());
      event.accept(ModItems.RUNESTONE_BRICK_SLAB.get());
      event.accept(ModItems.RUNESTONE_TILE_SLAB.get());
      event.accept(ModItems.RUNED_SLAB.get());
      event.accept(ModItems.RUNED_BRICK_SLAB.get());
      event.accept(ModItems.RUNED_TILE_SLAB.get());
      event.accept(ModItems.WILDWOOD_SLAB.get());

      event.accept(ModItems.RUNESTONE_BUTTON.get());
      event.accept(ModItems.RUNESTONE_BRICK_BUTTON.get());
      event.accept(ModItems.RUNESTONE_TILE_BUTTON.get());
      event.accept(ModItems.MOSSY_RUNESTONE_BUTTON.get());
      event.accept(ModItems.RUNED_BUTTON.get());
      event.accept(ModItems.RUNED_BRICK_BUTTON.get());
      event.accept(ModItems.RUNED_TILE_BUTTON.get());
      event.accept(ModItems.WILDWOOD_BUTTON.get());

      event.accept(ModItems.RUNESTONE_PRESSURE_PLATE.get());
      event.accept(ModItems.RUNESTONE_BRICK_PRESSURE_PLATE.get());
      event.accept(ModItems.RUNESTONE_TILE_PRESSURE_PLATE.get());
      event.accept(ModItems.MOSSY_RUNESTONE_PRESSURE_PLATE.get());
      event.accept(ModItems.RUNED_PRESSURE_PLATE.get());
      event.accept(ModItems.RUNED_BRICK_PRESSURE_PLATE.get());
      event.accept(ModItems.RUNED_TILE_PRESSURE_PLATE.get());
      event.accept(ModItems.WILDWOOD_PRESSURE_PLATE.get());

      event.accept(ModItems.RUNESTONE_WALL.get());
      event.accept(ModItems.MOSSY_RUNESTONE_WALL.get());
      event.accept(ModItems.RUNESTONE_BRICK_WALL.get());
      event.accept(ModItems.RUNESTONE_TILE_WALL.get());
      event.accept(ModItems.RUNED_WALL.get());
      event.accept(ModItems.RUNED_BRICK_WALL.get());
      event.accept(ModItems.RUNED_TILE_WALL.get());

      event.accept(ModItems.WILDWOOD_FENCE.get());
      event.accept(ModItems.WILDWOOD_GATE.get());
      event.accept(ModItems.WILDWOOD_DOOR.get());
      event.accept(ModItems.WILDWOOD_TRAPDOOR.get());
      event.accept(ModItems.WILDWOOD_LADDER.get());

      event.accept(ModItems.ELEMENTAL_SOIL.get());
      event.accept(ModItems.AQUEOUS_SOIL.get());
      event.accept(ModItems.CAELIC_SOIL.get());
      event.accept(ModItems.MAGMATIC_SOIL.get());
      event.accept(ModItems.TERRAN_SOIL.get());
      event.accept(ModItems.ENCHANTED_TURF.get());

      event.accept(ModItems.PRIMAL_GROVE_STONE.get());
      event.accept(ModItems.WILD_GROVE_STONE.get());
      event.accept(ModItems.FAIRY_GROVE_STONE.get());
      event.accept(ModItems.ELEMENTAL_GROVE_STONE.get());
      event.accept(ModItems.TWILIGHT_GROVE_STONE.get());
      event.accept(ModItems.FUNGAL_GROVE_STONE.get());
      event.accept(ModItems.SPROUTING_GROVE_STONE.get());

      event.accept(ModItems.GROVE_CRAFTER.get());
      event.accept(ModItems.GROVE_PEDESTAL.get());
      event.accept(ModItems.WILDWOOD_PEDESTAL.get());
      event.accept(ModItems.DISPLAY_PEDESTAL.get());

      event.accept(ModItems.GROWTH_AMPLIFIER.get());

      event.accept(ModItems.RED_FAIRY_HUT.get());
      event.accept(ModItems.BROWN_FAIRY_HUT.get());
      event.accept(ModItems.BAFFLECAP_FAIRY_HUT.get());
      event.accept(ModItems.CRIMSON_FAIRY_HUT.get());
      event.accept(ModItems.WARPED_FAIRY_HUT.get());

      event.accept(ModItems.PYRE.get());
      event.accept(ModItems.SOUL_PYRE.get());
      event.accept(ModItems.REINFORCED_PYRE.get());
      event.accept(ModItems.REINFORCED_SOUL_PYRE.get());
      event.accept(ModItems.DECORATIVE_PYRE.get());
      event.accept(ModItems.DECORATIVE_SOUL_PYRE.get());
      event.accept(ModItems.RITUAL_PEDESTAL.get());
      event.accept(ModItems.REINFORCED_RITUAL_PEDESTAL.get());
      event.accept(ModItems.INCENSE_BURNER.get());
      event.accept(ModItems.STONE_ALTAR.get());

      event.accept(ModItems.MORTAR.get());
      event.accept(ModItems.PESTLE.get());

      event.accept(ModItems.UNENDING_BOWL.get());
      event.accept(ModItems.STONEPETAL.get());
      event.accept(ModItems.WILDROOT.get());
      event.accept(ModItems.GROVE_MOSS.get());
      event.accept(ModItems.CLOUD_BERRY.get());
      event.accept(ModItems.DEWGONIA.get());
      event.accept(ModItems.INFERNO_BULB.get());
      event.accept(ModItems.STALICRIPE.get());
      event.accept(ModItems.BAFFLECAP.get());
      event.accept(ModItems.MOONGLOW.get());
      event.accept(ModItems.PERESKIA.get());
      event.accept(ModItems.SPIRITLEAF.get());
      event.accept(ModItems.WILDEWHEET.get());

      event.accept(ModItems.MOONGLOW_SEEDS.get());
      event.accept(ModItems.PERESKIA_BULB.get());
      event.accept(ModItems.SPIRITLEAF_SEEDS.get());
      event.accept(ModItems.WILDEWHEET_SEEDS.get());
      event.accept(ModItems.GROVE_SPORES.get());
      event.accept(ModItems.AUBERGINE_SEEDS.get());

      event.accept(ModItems.ACACIA_BARK.get());
      event.accept(ModItems.BIRCH_BARK.get());
      event.accept(ModItems.DARK_OAK_BARK.get());
      event.accept(ModItems.JUNGLE_BARK.get());
      event.accept(ModItems.OAK_BARK.get());
      event.accept(ModItems.SPRUCE_BARK.get());
      event.accept(ModItems.WILDWOOD_BARK.get());
      event.accept(ModItems.CRIMSON_BARK.get());
      event.accept(ModItems.WARPED_BARK.get());
      event.accept(ModItems.MANGROVE_BARK.get());
      event.accept(ModItems.MIXED_BARK.get());

      event.accept(ModItems.CARAPACE.get());
      event.accept(ModItems.PELT.get());
      event.accept(ModItems.ANTLERS.get());
      event.accept(ModItems.RUNIC_DUST.get());
      event.accept(ModItems.SYLVAN_LEATHER.get());
      event.accept(ModItems.GLASS_EYE.get());
      event.accept(ModItems.LIFE_ESSENCE.get());
      event.accept(ModItems.MYSTIC_FEATHER.get());
      event.accept(ModItems.STRANGE_OOZE.get());
      event.accept(ModItems.INK_BOTTLE.get());

      event.accept(ModItems.VENISON.get());
      event.accept(ModItems.COOKED_VENISON.get());

      event.accept(ModItems.RAW_SQUID.get());
      event.accept(ModItems.COOKED_SQUID.get());

      event.accept(ModItems.FLOUR.get());
      event.accept(ModItems.WILDEWHEET_BREAD.get());
      event.accept(ModItems.ASSORTED_SEEDS.get());
      event.accept(ModItems.COOKED_SEEDS.get());

      event.accept(ModItems.AUBERGINE.get());
      event.accept(ModItems.COOKED_AUBERGINE.get());
      event.accept(ModItems.COOKED_BEETROOT.get());
      event.accept(ModItems.COOKED_CARROT.get());
      event.accept(ModItems.COOKED_PERESKIA.get());

      event.accept(ModItems.STUFFED_AUBERGINE.get());
      event.accept(ModItems.AUBERGINE_SALAD.get());
      event.accept(ModItems.BEETROOT_SALAD.get());
      event.accept(ModItems.STEWED_EGGPLANT.get());
      event.accept(ModItems.WILDROOT_STEW.get());

      event.accept(ModItems.APPLE_CORDIAL.get());
      event.accept(ModItems.CACTUS_SYRUP.get());
      event.accept(ModItems.DANDELION_CORDIAL.get());
      event.accept(ModItems.LILAC_CORDIAL.get());
      event.accept(ModItems.PEONY_CORDIAL.get());
      event.accept(ModItems.ROSE_CORDIAL.get());
      event.accept(ModItems.VINEGAR.get());
      event.accept(ModItems.VEGETABLE_JUICE.get());

      event.accept(ModItems.COPPER_NUGGET.get());
      event.accept(ModItems.RAW_SILVER.get());
      event.accept(ModItems.SILVER_INGOT.get());
      event.accept(ModItems.SILVER_NUGGET.get());
      event.accept(ModItems.SILVER_STATER.get());

      event.accept(ModItems.STAFF.get());
      event.accept(ModItems.FIRE_STARTER.get());
      event.accept(ModItems.WOODEN_SHEARS.get());
      event.accept(ModItems.RUNIC_SHEARS.get());
      event.accept(ModItems.GRAMARY.get());
      event.accept(ModItems.ALERTNESS_CHARM.get());
      event.accept(ModItems.HERB_POUCH.get());
      for (DyeColor dye : DyeColor.values()) {
        ItemStack stack = new ItemStack(ModItems.HERB_POUCH.get());
        stack.set(ModAttachments.DYEABLE, Dyeable.fromColor(dye));
        event.accept(stack);
      }
      event.accept(ModItems.COMPONENT_POUCH.get());
      for (DyeColor dye : DyeColor.values()) {
        ItemStack stack = new ItemStack(ModItems.COMPONENT_POUCH.get());
        stack.set(ModAttachments.DYEABLE, Dyeable.fromColor(dye));
        event.accept(stack);
      }
      event.accept(ModItems.APOTHECARY_POUCH.get());
      for (DyeColor dye : DyeColor.values()) {
        ItemStack stack = new ItemStack(ModItems.APOTHECARY_POUCH.get());
        stack.set(ModAttachments.DYEABLE, Dyeable.fromColor(dye));
        event.accept(stack);
      }
      event.accept(ModItems.SYLVAN_POUCH.get());
      for (DyeColor dye : DyeColor.values()) {
        ItemStack stack = new ItemStack(ModItems.SYLVAN_POUCH.get());
        stack.set(ModAttachments.DYEABLE, Dyeable.fromColor(dye));
        event.accept(stack);
      }
      event.accept(ModItems.CREATIVE_POUCH.get());

      event.accept(ModItems.WOODEN_KNIFE.get());
      event.accept(ModItems.STONE_KNIFE.get());
      event.accept(ModItems.COPPER_KNIFE.get());
      event.accept(ModItems.IRON_KNIFE.get());
      event.accept(ModItems.GOLDEN_KNIFE.get());
      event.accept(ModItems.SILVER_KNIFE.get());
      event.accept(ModItems.DIAMOND_KNIFE.get());
      event.accept(ModItems.NETHERITE_KNIFE.get());

      event.accept(ModItems.LIVING_AXE.get());
      event.accept(ModItems.LIVING_HOE.get());
      event.accept(ModItems.LIVING_PICKAXE.get());
      event.accept(ModItems.LIVING_SHOVEL.get());
      event.accept(ModItems.LIVING_SWORD.get());

      event.accept(ModItems.LIVING_ARROW.get());
      event.accept(ModItems.WILDWOOD_BOW.get());
      event.accept(ModItems.WILDWOOD_QUIVER.get());

      event.accept(ModItems.RUNED_DAGGER.get());
      event.accept(ModItems.RUNED_PICKAXE.get());
      event.accept(ModItems.RUNED_AXE.get());
      event.accept(ModItems.RUNED_HOE.get());
      event.accept(ModItems.RUNED_SHOVEL.get());
      event.accept(ModItems.RUNED_SWORD.get());

      event.accept(ModItems.COPPER_AXE.get());
      event.accept(ModItems.COPPER_HOE.get());
      event.accept(ModItems.COPPER_PICKAXE.get());
      event.accept(ModItems.COPPER_SHOVEL.get());
      event.accept(ModItems.COPPER_SWORD.get());

      event.accept(ModItems.ANTLER_HAT.get());
      event.accept(ModItems.BEETLE_HELMET.get());
      event.accept(ModItems.BEETLE_CHESTPLATE.get());
      event.accept(ModItems.BEETLE_LEGGINGS.get());
      event.accept(ModItems.BEETLE_BOOTS.get());

      event.accept(ModItems.COPPER_HELMET.get());
      event.accept(ModItems.COPPER_CHESTPLATE.get());
      event.accept(ModItems.COPPER_LEGGINGS.get());
      event.accept(ModItems.COPPER_BOOTS.get());

      event.accept(ModItems.RELIQUARY.get());
      event.accept(ModItems.SPIRIT_BAG.get());

      event.accept(ModItems.BEETLE_SPAWN_EGG.get());
      event.accept(ModItems.JERBOA_SPAWN_EGG.get());
      event.accept(ModItems.DEER_SPAWN_EGG.get());
      event.accept(ModItems.DUCK_SPAWN_EGG.get());
      event.accept(ModItems.FENNEC_SPAWN_EGG.get());
      event.accept(ModItems.OWL_SPAWN_EGG.get());

      event.accept(ModItems.GREEN_SPROUT_SPAWN_EGG.get());
      event.accept(ModItems.TAN_SPROUT_SPAWN_EGG.get());
      event.accept(ModItems.RED_SPROUT_SPAWN_EGG.get());
      event.accept(ModItems.PURPLE_SPROUT_SPAWN_EGG.get());
      event.accept(ModItems.SNOW_SPROUT_SPAWN_EGG.get());
      event.accept(ModItems.MELODY_SPROUT_SPAWN_EGG.get());
    }
    if (event.getTab().equals(ModTabs.SPELLS_TAB.get())) {
      event.accept(ModItems.SPELL_ACID_CLOUD.get());
      event.accept(ModItems.SPELL_AQUA_BUBBLE.get());
      event.accept(ModItems.SPELL_SUMMON_UNDEAD.get());
      event.accept(ModItems.SPELL_DANDELION_WINDS.get());
      event.accept(ModItems.SPELL_DESATURATE.get());
      event.accept(ModItems.SPELL_DISARM.get());
      event.accept(ModItems.SPELL_EXTENSION.get());
      event.accept(ModItems.SPELL_SYLVAN_LIGHT.get());
      event.accept(ModItems.SPELL_GEAS.get());
      event.accept(ModItems.SPELL_GROWTH_INFUSION.get());
      event.accept(ModItems.SPELL_HARVEST.get());
      event.accept(ModItems.SPELL_JAUNT.get());
      event.accept(ModItems.SPELL_LIFE_DRAIN.get());
      event.accept(ModItems.SPELL_LIGHT_DRIFTER.get());
      event.accept(ModItems.SPELL_MAGNETISM.get());
      event.accept(ModItems.SPELL_NONDETECTION.get());
      event.accept(ModItems.SPELL_PETAL_SHELL.get());
      event.accept(ModItems.SPELL_RADIANCE.get());
      event.accept(ModItems.SPELL_RAMPANT_GROWTH.get());
      event.accept(ModItems.SPELL_ROSE_THORNS.get());
      event.accept(ModItems.SPELL_SANCTUARY.get());
      event.accept(ModItems.SPELL_SATURATE.get());
      event.accept(ModItems.SPELL_SHATTER.get());
      event.accept(ModItems.SPELL_SKY_SOARER.get());
      event.accept(ModItems.SPELL_STORM_CLOUD.get());
      event.accept(ModItems.SPELL_TEMPORAL_MORASS.get());
      event.accept(ModItems.SPELL_WILDFIRE.get());
    }
    if (event.getTab().equals(ModTabs.RITUALS_TAB.get())) {
      event.accept(ModItems.RITUAL_ANIMAL_HARVEST.get());
      event.accept(ModItems.RITUAL_BLOOMING.get());
      event.accept(ModItems.RITUAL_CRAFTING.get());
      event.accept(ModItems.RITUAL_FIRE_STORM.get());
      event.accept(ModItems.RITUAL_FROST_LANDS.get());
      event.accept(ModItems.RITUAL_GATHERING.get());
      event.accept(ModItems.RITUAL_GERMINATION.get());
      event.accept(ModItems.RITUAL_GROVE_SUPPLICATION.get());
      event.accept(ModItems.RITUAL_HEALING_AURA.get());
      event.accept(ModItems.RITUAL_HEAVY_STORMS.get());
      event.accept(ModItems.RITUAL_OVERGROWTH.get());
      event.accept(ModItems.RITUAL_PROTECTION.get());
      event.accept(ModItems.RITUAL_PURITY.get());
      event.accept(ModItems.RITUAL_SPREADING_FOREST.get());
      event.accept(ModItems.RITUAL_SUMMON_CREATURES.get());
      event.accept(ModItems.RITUAL_TRANSMUTATION.get());
      event.accept(ModItems.RITUAL_WARDING.get());
      event.accept(ModItems.RITUAL_WILDROOT_GROWTH.get());
      event.accept(ModItems.RITUAL_WINDWALL.get());
    }
  }


  @SubscribeEvent
  public static void onAttributes (EntityAttributeModificationEvent event) {
    event.add(EntityType.PLAYER, ModAttributes.COOLDOWN_REDUCTION);
    event.add(EntityType.PLAYER, ModAttributes.COST_REDUCTION);
    event.add(EntityType.PLAYER, ModAttributes.FORAGING);
  }
}
