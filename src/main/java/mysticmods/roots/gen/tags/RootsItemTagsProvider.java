package mysticmods.roots.gen.tags;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static mysticmods.roots.init.ModItems.*;

@SuppressWarnings({"NullableProblems", "unchecked"})
public final class RootsItemTagsProvider extends ItemTagsProvider {
  private final CompletableFuture<TagLookup<Ritual>> ritualTags;
  private final CompletableFuture<TagLookup<Spell>> spellTags;
  private final CompletableFuture<TagLookup<Grove>> groveTags;
  private final Map<TagKey<Ritual>, TagKey<Item>> ritualTagsToCopy = new HashMap<>();
  private final Map<TagKey<Spell>, TagKey<Item>> spellTagsToCopy = new HashMap<>();
  private final Map<TagKey<Grove>, TagKey<Item>> groveTagsToCopy = new HashMap<>();

  public RootsItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> blockTags, CompletableFuture<TagLookup<Spell>> spellTags, CompletableFuture<TagLookup<Ritual>> ritualRags, CompletableFuture<TagLookup<Grove>> groveTags, @Nullable ExistingFileHelper existingFileHelper) {
    super(output, provider, blockTags, RootsAPI.MODID, existingFileHelper);
    this.ritualTags = ritualRags;
    this.spellTags = spellTags;
    this.groveTags = groveTags;
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    this.tag(RootsTags.Items.DEER_FOOD).add(Items.WHEAT).add(ModItems.WILDEWHEET.get());

    this.tag(RootsTags.Items.GROVE_MOSS_CROP).add(ModItems.GROVE_MOSS.get());
    this.tag(RootsTags.Items.MOONGLOW_CROP).add(ModItems.MOONGLOW.get());
    this.tag(RootsTags.Items.PERESKIA_CROP).add(ModItems.PERESKIA.get());
    this.tag(RootsTags.Items.SPIRITLEAF_CROP).add(ModItems.SPIRITLEAF.get());
    this.tag(RootsTags.Items.WILDEWHEET_CROP).add(ModItems.WILDEWHEET.get());
    this.tag(RootsTags.Items.AUBERGINE_CROP).add(ModItems.AUBERGINE.get());
    this.tag(RootsTags.Items.BAFFLECAP_CROP).add(ModItems.BAFFLECAP.get());
    this.tag(RootsTags.Items.CLOUD_BERRY_CROP).add(ModItems.CLOUD_BERRY.get());
    this.tag(RootsTags.Items.DEWGONIA_CROP).add(ModItems.DEWGONIA.get());
    this.tag(RootsTags.Items.STALICRIPE_CROP).add(ModItems.STALICRIPE.get());
    this.tag(RootsTags.Items.WILDROOT_CROP).add(ModItems.WILDROOT.get());
    this.tag(RootsTags.Items.INFERNO_BULB_CROP).add(ModItems.INFERNO_BULB.get());

    this.tag(RootsTags.Items.MOONGLOW_SEEDS).add(ModItems.MOONGLOW_SEEDS.get());
    this.tag(RootsTags.Items.PERESKIA_SEEDS).add(ModItems.PERESKIA_BULB.get());
    this.tag(RootsTags.Items.WILDEWHEET_SEEDS).add(ModItems.WILDEWHEET_SEEDS.get());
    this.tag(RootsTags.Items.SPIRITLEAF_SEEDS).add(ModItems.SPIRITLEAF_SEEDS.get());
    this.tag(RootsTags.Items.WILDROOT_SEEDS).add(ModItems.WILDROOT.get());
    this.tag(RootsTags.Items.AUBERGINE_SEEDS).add(ModItems.AUBERGINE_SEEDS.get());

    this.tag(RootsTags.Items.SEEDS).add(ModItems.GROVE_SPORES.get());
    this.tag(ItemTags.CHICKEN_FOOD).addTag(RootsTags.Items.SEEDS);

    this.tag(RootsTags.Items.MORTAR_ACTIVATION).add(ModItems.PESTLE.get());

    //noinspection unchecked
    this.tag(RootsTags.Items.SEEDS).add(ModItems.AUBERGINE_SEEDS.get())
        .addTags(RootsTags.Items.MOONGLOW_SEEDS, RootsTags.Items.PERESKIA_SEEDS, RootsTags.Items.WILDEWHEET_SEEDS, RootsTags.Items.SPIRITLEAF_SEEDS);

    this.tag(RootsTags.Items.CARAPACE).add(ModItems.CARAPACE.get());
    this.tag(RootsTags.Items.PELT).add(ModItems.PELT.get());
    this.tag(RootsTags.Items.ANTLERS).add(ModItems.ANTLERS.get());

    this.tag(RootsTags.Items.PROTEINS)
        .add(ModItems.VENISON.get(), ModItems.COOKED_VENISON.get(), ModItems.RAW_SQUID.get(), ModItems.COOKED_SQUID.get());

    this.tag(RootsTags.Items.COOKED_VEGETABLES)
        .add(ModItems.COOKED_BEETROOT.get(), ModItems.COOKED_CARROT.get(), ModItems.COOKED_AUBERGINE.get(), ModItems.COOKED_PERESKIA.get());
    this.tag(RootsTags.Items.VEGETABLES).add(ModItems.AUBERGINE.get());

    this.tag(RootsTags.Items.RUNIC_SHEARS).add(ModItems.RUNIC_SHEARS.get());

    this.tag(RootsTags.Items.CREATIVE_CASTING_TOOLS).add(CREATIVE_STAFF.get());
    this.tag(RootsTags.Items.CASTING_TOOLS).add(ModItems.STAFF.get()).addTag(RootsTags.Items.CREATIVE_CASTING_TOOLS);

    this.tag(RootsTags.Items.KNIVES)
        .add(ModItems.WOODEN_KNIFE.get(), ModItems.STONE_KNIFE.get(), ModItems.IRON_KNIFE.get(), ModItems.GOLDEN_KNIFE.get(), ModItems.DIAMOND_KNIFE.get(), ModItems.NETHERITE_KNIFE.get(), ModItems.COPPER_KNIFE.get(), ModItems.SILVER_KNIFE.get());
    // Silver?
    this.tag(ItemTags.PIGLIN_LOVED).add(ModItems.COPPER_KNIFE.get()/*, ModItems.SILVER_KNIFE.get()*/);

    this.tag(RootsTags.Items.RUNIC_DUST).add(ModItems.RUNIC_DUST.get());

    this.tag(RootsTags.Items.RAW_SILVER).add(ModItems.RAW_SILVER.get());
    this.copy(RootsTags.Blocks.RAW_SILVER_STORAGE, RootsTags.Items.RAW_SILVER_STORAGE);
    this.tag(RootsTags.Items.SILVER_INGOT).add(ModItems.SILVER_INGOT.get());

    this.tag(RootsTags.Items.COPPER_ITEMS)
        .add(ModItems.COPPER_KNIFE.get(), ModItems.COPPER_AXE.get(), ModItems.COPPER_HOE.get(), ModItems.COPPER_PICKAXE.get(), ModItems.COPPER_SHOVEL.get(), ModItems.COPPER_SWORD.get(), ModItems.COPPER_HELMET.get(), ModItems.COPPER_CHESTPLATE.get(), ModItems.COPPER_LEGGINGS.get(), ModItems.COPPER_BOOTS.get());
    this.tag(RootsTags.Items.SILVER_ITEMS).add(ModItems.SILVER_KNIFE.get());

    this.tag(RootsTags.Items.SILVER_NUGGET).add(ModItems.SILVER_NUGGET.get());
    this.tag(RootsTags.Items.COPPER_NUGGET).add(ModItems.COPPER_NUGGET.get());
    this.tag(Tags.Items.NUGGETS).addTag(RootsTags.Items.SILVER_NUGGET).addTag(RootsTags.Items.COPPER_NUGGET);

    this.tag(RootsTags.Items.ADJUSTABLE_ITEM).add(ModItems.GRAMARY.get());
    this.tag(RootsTags.Items.APPLES).add(Items.APPLE, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE);
    this.tag(RootsTags.Items.DANDELIONS).add(Items.DANDELION);
    this.tag(RootsTags.Items.LILACS).add(Items.LILAC);
    this.tag(RootsTags.Items.POPPIES).add(Items.POPPY);
    this.tag(RootsTags.Items.PEONIES).add(Items.PEONY);
    this.tag(RootsTags.Items.ROSES).add(Items.ROSE_BUSH);
    this.tag(RootsTags.Items.EYES).add(Items.SPIDER_EYE).add(GLASS_EYE.get());

    this.copy(RootsTags.Blocks.NETHER_DOORS, RootsTags.Items.NETHER_DOORS);
    this.copy(RootsTags.Blocks.SOILS, RootsTags.Items.SOILS);
    this.copy(RootsTags.Blocks.WATER_SOIL, RootsTags.Items.WATER_SOIL);
    this.copy(RootsTags.Blocks.AIR_SOIL, RootsTags.Items.AIR_SOIL);
    this.copy(RootsTags.Blocks.EARTH_SOIL, RootsTags.Items.EARTH_SOIL);
    this.copy(RootsTags.Blocks.FIRE_SOIL, RootsTags.Items.FIRE_SOIL);
    this.copy(RootsTags.Blocks.BASE_ELEMENTAL_SOIL, RootsTags.Items.BASE_ELEMENTAL_SOIL);
    this.copy(RootsTags.Blocks.ALL_SOIL, RootsTags.Items.ALL_SOIL);
    this.copy(RootsTags.Blocks.ELEMENTAL_SOIL, RootsTags.Items.ELEMENTAL_SOIL);
    this.copy(RootsTags.Blocks.RUNED_OBSIDIAN, RootsTags.Items.RUNED_OBSIDIAN);
    this.copy(RootsTags.Blocks.RUNESTONE, RootsTags.Items.RUNESTONE);
    this.copy(RootsTags.Blocks.WILDWOOD_LOGS, RootsTags.Items.WILDWOOD_LOGS);
    this.copy(RootsTags.Blocks.WILDWOOD_PLANKS, RootsTags.Items.WILDWOOD_PLANKS);
    this.copy(RootsTags.Blocks.WILDWOOD_CHESTS, RootsTags.Items.WILDWOOD_CHESTS);
    this.copy(RootsTags.Blocks.GROVE_STONES, RootsTags.Items.GROVE_STONES);
    this.copy(RootsTags.Blocks.GROVE_STONE_PRIMAL, RootsTags.Items.GROVE_STONE_PRIMAL);
    this.copy(RootsTags.Blocks.GROVE_STONE_ELEMENTAL, RootsTags.Items.GROVE_STONE_ELEMENTAL);
    this.copy(RootsTags.Blocks.GROVE_STONE_FAIRY, RootsTags.Items.GROVE_STONE_FAIRY);
    this.copy(RootsTags.Blocks.GROVE_STONE_FUNGAL, RootsTags.Items.GROVE_STONE_FUNGAL);
    this.copy(RootsTags.Blocks.GROVE_STONE_SPROUTING, RootsTags.Items.GROVE_STONE_SPROUTING);
    this.copy(RootsTags.Blocks.GROVE_STONE_TWILIGHT, RootsTags.Items.GROVE_STONE_TWILIGHT);
    this.copy(RootsTags.Blocks.GROVE_STONE_WILD, RootsTags.Items.GROVE_STONE_WILD);
    this.copy(RootsTags.Blocks.PEDESTALS, RootsTags.Items.PEDESTALS);
    this.copy(RootsTags.Blocks.RITUAL_PEDESTALS, RootsTags.Items.RITUAL_PEDESTALS);
    this.copy(RootsTags.Blocks.GROVE_PEDESTALS, RootsTags.Items.GROVE_PEDESTALS);
    this.copy(RootsTags.Blocks.LIMITED_PEDESTALS, RootsTags.Items.LIMITED_PEDESTALS);
    this.copy(RootsTags.Blocks.DISPLAY_PEDESTALS, RootsTags.Items.DISPLAY_PEDESTALS);
    this.copy(RootsTags.Blocks.FUNCTIONAL_PYRES, RootsTags.Items.FUNCTIONAL_PYRES);
    this.copy(RootsTags.Blocks.DECORATIVE_PYRES, RootsTags.Items.DECORATIVE_PYRES);
    this.copy(RootsTags.Blocks.PYRES, RootsTags.Items.PYRES);
    this.copy(RootsTags.Blocks.GROVE_CRAFTERS, RootsTags.Items.GROVE_CRAFTERS);
    this.copy(RootsTags.Blocks.MORTARS, RootsTags.Items.MORTARS);
    this.copy(RootsTags.Blocks.RUNE_CAPSTONES, RootsTags.Items.RUNE_CAPSTONES);
    this.copy(RootsTags.Blocks.RUNE_PILLARS, RootsTags.Items.RUNE_PILLARS);
    this.copy(RootsTags.Blocks.RUNED_CAPSTONES, RootsTags.Items.RUNED_CAPSTONES);
    this.copy(RootsTags.Blocks.RUNED_PILLARS, RootsTags.Items.RUNED_PILLARS);
    this.copy(RootsTags.Blocks.RUNES_CAPSTONES, RootsTags.Items.RUNES_CAPSTONES);
    this.copy(RootsTags.Blocks.RUNES_PILLARS, RootsTags.Items.RUNES_PILLARS);
    this.copy(RootsTags.Blocks.CAPSTONES, RootsTags.Items.CAPSTONES);
    this.copy(RootsTags.Blocks.PILLARS, RootsTags.Items.PILLARS);
    this.copy(RootsTags.Blocks.STONEPETAL, RootsTags.Items.STONEPETAL);
    this.tag(ItemTags.SMALL_FLOWERS).addTags(RootsTags.Items.STONEPETAL);
    this.copy(RootsTags.Blocks.SHORT_GRASS, RootsTags.Items.SHORT_GRASS);
    this.copy(RootsTags.Blocks.TALL_GRASS, RootsTags.Items.TALL_GRASS);
    /*    this.copy(RootsTags.Blocks.GRASS, RootsTags.Items.GRASS); ??????? */
    this.copy(RootsTags.Blocks.NYI, RootsTags.Items.NYI);
    this.copy(RootsTags.Blocks.WIP, RootsTags.Items.WIP);
/*    this.copy(RootsTags.Blocks.SUPPORTS_HELL_SPROUT_SPAWN, RootsTags.Items.SUPPORTS_HELL_SPROUT_SPAWN);
    this.copy(RootsTags.Blocks.BAFFLECAP_CONVERSION, RootsTags.Items.BAFFLECAP_CONVERSION);
    this.copy(RootsTags.Blocks.GROVE_MOSS, RootsTags.Items.GROVE_MOSS);*/
    this.copy(RootsTags.Blocks.BLOOMING_ELIGIBLE_FLOWERS, RootsTags.Items.BLOOMING_ELIGIBLE_FLOWERS);
    this.copy(RootsTags.Blocks.BLOOMING_ELIGIBLE_PEDESTAL_FLOWERS, RootsTags.Items.BLOOMING_ELIGIBLE_PEDESTAL_FLOWERS);

    this.copy(RootsTags.Blocks.SILVER_STORAGE, RootsTags.Items.SILVER_STORAGE);
    this.copy(RootsTags.Blocks.SILVER_ORE, RootsTags.Items.SILVER_ORE);
    this.copy(RootsTags.Blocks.LEVERS, RootsTags.Items.LEVERS);

    this.tag(Tags.Items.SEEDS).addTag(RootsTags.Items.SEEDS);
    this.tag(RootsTags.Items.VEGETABLES).add(Items.CARROT, Items.BEETROOT);
    this.tag(RootsTags.Items.COOKED_VEGETABLES).add(Items.BAKED_POTATO);
    this.tag(RootsTags.Items.COOKED_SEAFOOD).add(Items.COOKED_COD, Items.COOKED_SALMON);

    this.tag(ItemTags.LOGS_THAT_BURN).addTag(RootsTags.Items.WILDWOOD_LOGS);

    this.tag(RootsTags.Items.FLINT).add(Items.FLINT);

    // TODO: Are there more stone blocks to go in here?
    //noinspection unchecked
    this.tag(RootsTags.Items.STONELIKE)
        .addTags(Tags.Items.SANDSTONE_BLOCKS, Tags.Items.STONES, ItemTags.STONE_BRICKS, ItemTags.STONE_CRAFTING_MATERIALS, ItemTags.STONE_TOOL_MATERIALS)
        .add(Items.DIORITE, Items.GRANITE, Items.CALCITE, Items.TUFF, Items.POLISHED_DIORITE, Items.POLISHED_GRANITE, Items.POLISHED_ANDESITE, Items.ANDESITE, Items.POLISHED_DEEPSLATE, Items.POLISHED_BLACKSTONE);

    //noinspection unchecked
    this.tag(RootsTags.Items.RUNESTONE_HERBS).addTags(RootsTags.Items.WILDROOT_CROP, RootsTags.Items.GROVE_MOSS_CROP);

    this.copy(RootsTags.Blocks.CROPS, RootsTags.Items.CROPS);

    this.tag(RootsTags.Items.GROVE_CRAFTER_ACTIVATION).addTag(RootsTags.Items.KNIVES);
    this.tag(RootsTags.Items.FUNGAL_TRANSMUTER_ACTIVATION).addTag(RootsTags.Items.KNIVES);
    this.tag(RootsTags.Items.PYRE_ACTIVATION).add(Items.FLINT_AND_STEEL);

    this.tag(RootsTags.Items.MOONGLOW_HERB).addTag(RootsTags.Items.MOONGLOW_CROP);
    this.tag(RootsTags.Items.PERESKIA_HERB).addTag(RootsTags.Items.PERESKIA_CROP);
    this.tag(RootsTags.Items.SPIRITLEAF_HERB).addTag(RootsTags.Items.SPIRITLEAF_CROP);
    this.tag(RootsTags.Items.WILDEWHEET_HERB).addTag(RootsTags.Items.WILDEWHEET_CROP);
    this.tag(RootsTags.Items.GROVE_MOSS_HERB).addTag(RootsTags.Items.GROVE_MOSS_CROP);
    this.tag(RootsTags.Items.DEWGONIA_HERB).addTag(RootsTags.Items.DEWGONIA_CROP);
    this.tag(RootsTags.Items.STALICRIPE_HERB).addTag(RootsTags.Items.STALICRIPE_CROP);
    this.tag(RootsTags.Items.WILDROOT_HERB).addTag(RootsTags.Items.WILDROOT_CROP);
    this.tag(RootsTags.Items.INFERNO_BULB_HERB).addTag(RootsTags.Items.INFERNO_BULB_CROP);
    this.tag(RootsTags.Items.BAFFLECAP_HERB).addTag(RootsTags.Items.BAFFLECAP_CROP);
    this.tag(RootsTags.Items.CLOUD_BERRY_HERB).addTag(RootsTags.Items.CLOUD_BERRY_CROP);

    //noinspection unchecked
    this.tag(RootsTags.Items.HERBS)
        .addTags(RootsTags.Items.MOONGLOW_HERB, RootsTags.Items.PERESKIA_HERB, RootsTags.Items.SPIRITLEAF_HERB, RootsTags.Items.WILDEWHEET_HERB, RootsTags.Items.GROVE_MOSS_HERB, RootsTags.Items.DEWGONIA_HERB, RootsTags.Items.STALICRIPE_HERB, RootsTags.Items.WILDROOT_HERB, RootsTags.Items.INFERNO_BULB_HERB, RootsTags.Items.BAFFLECAP_HERB, RootsTags.Items.CLOUD_BERRY_HERB);

    this.tag(RootsTags.Items.BOTTLES).add(Items.GLASS_BOTTLE);
    this.tag(RootsTags.Items.POUCHES)
        .add(ModItems.HERB_POUCH.get(), ModItems.SYLVAN_POUCH.get(), ModItems.APOTHECARY_POUCH.get(), ModItems.COMPONENT_POUCH.get());
    this.tag(RootsTags.Items.QUIVERS).add(ModItems.WILDWOOD_QUIVER.get());
    this.tag(RootsTags.Items.CREATIVE_POUCHES).add(ModItems.CREATIVE_POUCH.get());
    this.tag(RootsTags.Items.ALL_POUCHES).addTags(RootsTags.Items.POUCHES, RootsTags.Items.CREATIVE_POUCHES);

    // For Desaturate/Saturate
    this.tag(RootsTags.Items.SKIPPED_FOODS);

    // Items that cannot be disarmed using the disarm spell
    this.tag(RootsTags.Items.DISABLE_DISARMING);

    // Rewards that sprouts can drop for being bred
    // If they're not in the relevant data map they will use the default config chance
    this.tag(RootsTags.Items.SPROUT_BREEDING_REWARDS).add(
        Items.BEETROOT_SEEDS,
        Items.MELON_SEEDS,
        Items.PUMPKIN_SEEDS,
        Items.WHEAT_SEEDS,
        Items.POTATO,
        Items.CARROT,
        Items.COCOA_BEANS,
        Items.PITCHER_POD,
        Items.TORCHFLOWER_SEEDS,
        Items.NETHER_WART,
        Items.KELP,
        Items.SEAGRASS,
        Items.CHORUS_FRUIT
    ).add(
        ModItems.AUBERGINE_SEEDS.get(),
        ModItems.SPIRITLEAF_SEEDS.get(),
        ModItems.MOONGLOW_SEEDS.get(),
        ModItems.WILDEWHEET_SEEDS.get(),
        ModItems.WILDROOT.get(),
        ModItems.GROVE_SPORES.get(),
        ModItems.BAFFLECAP.get()
    );

    this.tag(RootsTags.Items.SPROUT_FOOD).addTag(RootsTags.Items.AUBERGINE_CROP);
    this.tag(RootsTags.Items.BEETLE_FOOD).add(Items.MELON_SEEDS);
    this.tag(RootsTags.Items.OWL_FOOD).add(Items.CHICKEN);
    this.tag(RootsTags.Items.DUCK_FOOD).addTag(ItemTags.CHICKEN_FOOD);
    this.tag(RootsTags.Items.FENNEC_FOOD).addTag(ItemTags.FOX_FOOD);
    this.tag(RootsTags.Items.DEER_FOOD).addTag(ItemTags.COW_FOOD);
    this.tag(RootsTags.Items.JERBOA_FOOD).addTags(Tags.Items.SEEDS, Tags.Items.CROPS);

    this.tag(RootsTags.Items.FORAGING_ELIGIBLE).addTag(RootsTags.Items.KNIVES).add(ModItems.LIVING_HOE.get());

    this.tag(RootsTags.Items.COLLECTING_ELIGIBLE).addTag(ItemTags.HEAD_ARMOR_ENCHANTABLE);

    this.tag(RootsTags.Items.SYLVAN_LEATHERS).add(ModItems.SYLVAN_LEATHER.get());
    this.tag(RootsTags.Items.DYEABLE)
        .add(ModItems.HERB_POUCH.get(), ModItems.COMPONENT_POUCH.get(), ModItems.APOTHECARY_POUCH.get(), ModItems.SYLVAN_POUCH.get());
    this.tag(RootsTags.Items.CHARM_ALERT).add(ModItems.ALERTNESS_CHARM.get());
    this.tag(RootsTags.Items.CHARMS).addTag(RootsTags.Items.CHARM_ALERT);

    this.tag(RootsTags.Items.CURIOS_CHARMS).addTag(RootsTags.Items.CHARMS);
    this.tag(RootsTags.Items.GRAMARIES).add(GRAMARY.get());
    this.tag(RootsTags.Items.CURIOS_TOMES).addTags(RootsTags.Items.GRAMARIES);
    this.tag(RootsTags.Items.CURIOS_BELTS).addTag(RootsTags.Items.ALL_POUCHES);
    this.tag(RootsTags.Items.CURIOS_BACKS).addTag(RootsTags.Items.QUIVERS);

    this.tag(ItemTags.ARROWS).add(ModItems.LIVING_ARROW.get());

    this.tag(ItemTags.HOES).add(ModItems.COPPER_HOE.get(), ModItems.LIVING_HOE.get(), ModItems.RUNED_HOE.get());
    this.tag(ItemTags.AXES).add(ModItems.COPPER_AXE.get(), ModItems.LIVING_AXE.get(), ModItems.RUNED_AXE.get());
    this.tag(ItemTags.PICKAXES)
        .add(ModItems.COPPER_PICKAXE.get(), ModItems.LIVING_PICKAXE.get(), ModItems.RUNED_PICKAXE.get());
    this.tag(ItemTags.SHOVELS)
        .add(ModItems.COPPER_SHOVEL.get(), ModItems.LIVING_SHOVEL.get(), ModItems.RUNED_SHOVEL.get());
    this.tag(ItemTags.SWORDS).add(ModItems.COPPER_SWORD.get(), ModItems.LIVING_SWORD.get(), ModItems.RUNED_SWORD.get());

    this.tag(RootsTags.Items.RUNED_LOG_HERBS).add(ModItems.WILDROOT.get(), ModItems.GROVE_MOSS.get());

    this.tag(RootsTags.Items.NYI)
        .add(ModItems.APPLE_CORDIAL.get(), ModItems.CACTUS_SYRUP.get(), ModItems.DANDELION_CORDIAL.get(), ModItems.LILAC_CORDIAL.get(), ModItems.PEONY_CORDIAL.get(), ModItems.ROSE_CORDIAL.get(), ModItems.RUNED_DAGGER.get(), ModItems.RUNED_SHOVEL.get(), ModItems.RUNED_PICKAXE.get(), ModItems.RUNED_AXE.get(), ModItems.RUNED_HOE.get(), ModItems.RUNED_SWORD.get(), ModItems.RELIQUARY.get(), ModItems.SPIRIT_BAG.get(), ModItems.LIFE_ESSENCE.get(), ModItems.GRAMARY.get(), ModItems.WILDWOOD_BOW.get(), ModItems.WILDWOOD_QUIVER.get(), ModItems.MYSTIC_FEATHER.get(), ModItems.STRANGE_OOZE.get(), ModItems.SILVER_STATER.get());
    this.tag(RootsTags.Items.WIP)
        .add(ModItems.BEETLE_BOOTS.get(), ModItems.BEETLE_CHESTPLATE.get(), ModItems.BEETLE_HELMET.get(), ModItems.BEETLE_LEGGINGS.get(), ModItems.ANTLER_HAT.get(), ModItems.ALERTNESS_CHARM.get(), ModItems.BAFFLECAP_FAIRY_HUT.get(), ModItems.RED_FAIRY_HUT.get(), ModItems.BROWN_FAIRY_HUT.get(), ModItems.CRIMSON_FAIRY_HUT.get(), ModItems.WARPED_FAIRY_HUT.get(), ModItems.FUNGAL_TRANSMUTER.get());

    this.tag(ItemTags.MINING_ENCHANTABLE).addTag(RootsTags.Items.KNIVES)
        .add(ModItems.WOODEN_SHEARS.get(), ModItems.RUNIC_SHEARS.get());
    this.tag(ItemTags.MINING_LOOT_ENCHANTABLE).addTag(RootsTags.Items.KNIVES)
        .add(ModItems.WOODEN_SHEARS.get(), ModItems.RUNIC_SHEARS.get());
    this.tag(ItemTags.SHARP_WEAPON_ENCHANTABLE).addTag(RootsTags.Items.KNIVES).add(ModItems.RUNED_DAGGER.get());
    this.tag(ItemTags.DURABILITY_ENCHANTABLE).addTag(RootsTags.Items.KNIVES)
        .add(ModItems.WOODEN_SHEARS.get(), ModItems.RUNIC_SHEARS.get());

    this.tag(ItemTags.HEAD_ARMOR)
        .add(ModItems.ANTLER_HAT.get(), ModItems.BEETLE_HELMET.get(), ModItems.COPPER_HELMET.get());
    this.tag(ItemTags.CHEST_ARMOR).add(ModItems.BEETLE_CHESTPLATE.get(), ModItems.COPPER_CHESTPLATE.get());
    this.tag(ItemTags.LEG_ARMOR).add(ModItems.BEETLE_LEGGINGS.get(), ModItems.COPPER_LEGGINGS.get());
    this.tag(ItemTags.FOOT_ARMOR).add(ModItems.BEETLE_BOOTS.get(), ModItems.COPPER_BOOTS.get());

    this.tag(RootsTags.Items.BEETLE_ARMOR)
        .add(ModItems.BEETLE_HELMET.get(), ModItems.BEETLE_LEGGINGS.get(), ModItems.BEETLE_CHESTPLATE.get(), ModItems.BEETLE_BOOTS.get());
    this.tag(RootsTags.Items.COPPER_ARMOR)
        .add(ModItems.COPPER_CHESTPLATE.get(), ModItems.COPPER_HELMET.get(), ModItems.COPPER_LEGGINGS.get(), ModItems.COPPER_BOOTS.get());
    this.tag(RootsTags.Items.ANTLER_ARMOR).add(ModItems.ANTLER_HAT.get());

    this.tag(RootsTags.Items.ROTTEN_FLESH).add(Items.ROTTEN_FLESH);

    this.copy(RootsTags.Blocks.GROWTH_AMPLIFIER_GRASSES, RootsTags.Items.GROWTH_AMPLIFIER_GRASSES);

    this.copy(RootsTags.Blocks.QUARTZ_ORE, RootsTags.Items.QUARTZ_ORE);
    this.copy(RootsTags.Blocks.ELEMENTAL_CROPS, RootsTags.Items.ELEMENTAL_CROPS);
    this.copy(RootsTags.Blocks.WATER_CROPS, RootsTags.Items.WATER_CROPS);
    this.copy(RootsTags.Blocks.EARTH_CROPS, RootsTags.Items.EARTH_CROPS);
    this.copy(RootsTags.Blocks.AIR_CROPS, RootsTags.Items.AIR_CROPS);
    this.copy(RootsTags.Blocks.FIRE_CROPS, RootsTags.Items.FIRE_CROPS);

    // Manually copied tags cannot have blocks that don't have an item equivalent
    manualCopy(RootsTags.Blocks.BLOOMING_ELIGIBLE_FLOWERS);
    manualCopy(RootsTags.Blocks.BLOOMING_ELIGIBLE_PEDESTAL_FLOWERS);
    manualCopy(RootsTags.Blocks.SPREADING_MUSHROOMS);

    manualCopy(BlockTags.CROPS);

    manualCopy(RootsTags.Blocks.CREATIVE_GROVE_GENERATORS);
    manualCopy(RootsTags.Blocks.ELEMENTAL_GROVE_GENERATORS);
    manualCopy(RootsTags.Blocks.FAIRY_GROVE_GENERATORS);
    manualCopy(RootsTags.Blocks.FAIRY_GROVE_PATHS);
    manualCopy(RootsTags.Blocks.WILD_GROVE_GENERATORS);
    manualCopy(RootsTags.Blocks.SPROUTING_GROVE_GENERATORS);
    manualCopy(RootsTags.Blocks.FUNGAL_GROVE_MUSHROOM_GENERATORS);
    manualCopy(RootsTags.Blocks.FUNGAL_GROVE_DIRT_GENERATORS);
    manualCopy(RootsTags.Blocks.FUNGAL_GROVE_OTHER_GENERATORS);
    tag(TagKey.create(Registries.ITEM, RootsTags.Blocks.TWILIGHT_GROVE_GENERATORS.location())).add(Items.CREEPER_HEAD, Items.DRAGON_HEAD, Items.PIGLIN_HEAD, Items.PLAYER_HEAD, Items.ZOMBIE_HEAD);
    tag(TagKey.create(Registries.ITEM, RootsTags.Blocks.ELEMENTAL_GROVE_LIQUID_GENERATORS.location())).add(Items.LAVA_BUCKET, Items.WATER_BUCKET);

    copySpell(RootsTags.Spells.ADJUSTABLE_SPELL);
    copySpell(RootsTags.Spells.FAIRY);
    copySpell(RootsTags.Spells.FUNGAL);
    copySpell(RootsTags.Spells.ELEMENTAL);
    copySpell(RootsTags.Spells.SPROUTING);
    copySpell(RootsTags.Spells.WILD);
    copySpell(RootsTags.Spells.TWILIGHT);
    copySpell(RootsTags.Spells.PRIMAL);
    copySpell(RootsTags.Spells.HOLLOW);
    copySpell(RootsTags.Spells.GEAS_ACTION);

    copyRitual(RootsTags.Rituals.FUNGAL);
    copyRitual(RootsTags.Rituals.SPROUTING);
    copyRitual(RootsTags.Rituals.ELEMENTAL);
    copyRitual(RootsTags.Rituals.WILD);
    copyRitual(RootsTags.Rituals.TWILIGHT);
    copyRitual(RootsTags.Rituals.PRIMAL);
    copyRitual(RootsTags.Rituals.FAIRY);
    copyRitual(RootsTags.Rituals.HOLLOW);

    copyGrove(RootsTags.Groves.FAIRY);
    copyGrove(RootsTags.Groves.FUNGAL);
    copyGrove(RootsTags.Groves.ELEMENTAL);
    copyGrove(RootsTags.Groves.SPROUTING);
    copyGrove(RootsTags.Groves.WILD);
    copyGrove(RootsTags.Groves.TWILIGHT);
    copyGrove(RootsTags.Groves.PRIMAL);

    this.tag(RootsTags.Groves.getGroveStoneTag(RootsTags.Groves.ANY_POWERABLE)).addTags(
        RootsTags.Items.GROVE_STONE_ELEMENTAL, RootsTags.Items.GROVE_STONE_FAIRY, RootsTags.Items.GROVE_STONE_FUNGAL, RootsTags.Items.GROVE_STONE_PRIMAL, RootsTags.Items.GROVE_STONE_SPROUTING, RootsTags.Items.GROVE_STONE_TWILIGHT
    );
    this.tag(RootsTags.Groves.getGroveStoneTag(RootsTags.Groves.ANY)).addTags(
        RootsTags.Items.GROVE_STONE_ELEMENTAL, RootsTags.Items.GROVE_STONE_FAIRY, RootsTags.Items.GROVE_STONE_FUNGAL, RootsTags.Items.GROVE_STONE_PRIMAL, RootsTags.Items.GROVE_STONE_SPROUTING, RootsTags.Items.GROVE_STONE_TWILIGHT, RootsTags.Items.GROVE_STONE_WILD
    );
    this.tag(RootsTags.Groves.getGroveStoneTag(RootsTags.Groves.ELEMENTAL)).addTags(
        RootsTags.Items.GROVE_STONE_ELEMENTAL
    );
    this.tag(RootsTags.Groves.getGroveStoneTag(RootsTags.Groves.FAIRY)).addTags(
        RootsTags.Items.GROVE_STONE_FAIRY
    );
    this.tag(RootsTags.Groves.getGroveStoneTag(RootsTags.Groves.FUNGAL)).addTags(
        RootsTags.Items.GROVE_STONE_FUNGAL
    );
    this.tag(RootsTags.Groves.getGroveStoneTag(RootsTags.Groves.SPROUTING)).addTags(
        RootsTags.Items.GROVE_STONE_SPROUTING
    );
    this.tag(RootsTags.Groves.getGroveStoneTag(RootsTags.Groves.TWILIGHT)).addTags(
        RootsTags.Items.GROVE_STONE_TWILIGHT
    );
    this.tag(RootsTags.Groves.getGroveStoneTag(RootsTags.Groves.PRIMAL)).addTags(
        RootsTags.Items.GROVE_STONE_PRIMAL
    );
    this.tag(RootsTags.Groves.getGroveStoneTag(RootsTags.Groves.WILD)).addTags(
        RootsTags.Items.GROVE_STONE_WILD
    );

    // Additions manually copied from RootsBlockTagProvider
    this.tag(ItemTags.SLABS)
        .add(RUNED_SLAB.get(), RUNED_BRICK_SLAB.get(), RUNED_TILE_SLAB.get(), RUNESTONE_SLAB.get(), RUNESTONE_BRICK_SLAB.get(), RUNESTONE_TILE_SLAB.get(), MOSSY_RUNESTONE_SLAB.get(), WILDWOOD_BUTTON.get());
    this.tag(ItemTags.WOODEN_SLABS).add(WILDWOOD_SLAB.get());
    this.tag(ItemTags.WOODEN_STAIRS).add(WILDWOOD_STAIRS.get());
    this.tag(ItemTags.STAIRS)
        .add(RUNESTONE_STAIRS.get(), RUNESTONE_BRICK_STAIRS.get(), RUNESTONE_TILE_STAIRS.get(), MOSSY_RUNESTONE_STAIRS.get(), RUNED_STAIRS.get(), RUNED_TILE_STAIRS.get(), RUNED_BRICK_STAIRS.get());
    this.tag(ItemTags.WALLS)
        .add(RUNED_WALL.get(), RUNED_BRICK_WALL.get(), RUNED_TILE_WALL.get(), RUNESTONE_WALL.get(), RUNESTONE_BRICK_WALL.get(), RUNESTONE_TILE_WALL.get(), MOSSY_RUNESTONE_WALL.get());
    this.tag(ItemTags.WOODEN_FENCES).add(WILDWOOD_FENCE.get());
    this.tag(Tags.Items.FENCES_WOODEN).add(WILDWOOD_FENCE.get());

    this.tag(ItemTags.FENCE_GATES).add(WILDWOOD_GATE.get());
    this.tag(Tags.Items.FENCE_GATES_WOODEN).add(WILDWOOD_GATE.get());

    this.tag(Tags.Items.ORES).addTags(RootsTags.Items.SILVER_ORE, RootsTags.Items.QUARTZ_ORE);

    this.tag(ItemTags.BUTTONS)
        .add(RUNED_BUTTON.get(), RUNED_TILE_BUTTON.get(), RUNED_BRICK_BUTTON.get(), MOSSY_RUNESTONE_BUTTON.get(), RUNESTONE_BUTTON.get(), RUNESTONE_BRICK_BUTTON.get(), RUNESTONE_TILE_BUTTON.get());
    this.tag(ItemTags.WOODEN_BUTTONS).add(WILDWOOD_BUTTON.get());
    this.tag(ItemTags.WOODEN_PRESSURE_PLATES).add(WILDWOOD_PRESSURE_PLATE.get());

    this.tag(ItemTags.WOODEN_DOORS).add(WILDWOOD_DOOR.get());
    this.tag(ItemTags.WOODEN_TRAPDOORS).add(WILDWOOD_TRAPDOOR.get());

    this.tag(ItemTags.PLANKS).add(WILDWOOD_PLANKS.get());
    this.tag(ItemTags.SAPLINGS).add(WILDWOOD_SAPLING.get());
    this.tag(ItemTags.LEAVES).add(WILDWOOD_LEAVES.get());

    this.tag(ItemTags.LOGS_THAT_BURN).addTags(RootsTags.Items.WILDWOOD_LOGS);
    //noinspection unchecked
    this.tag(ItemTags.LOGS).addTags(RootsTags.Items.WILDWOOD_LOGS);

    this.tag(ItemTags.MEAT).add(COOKED_SQUID.get(), RAW_SQUID.get(), COOKED_VENISON.get(), VENISON.get());

    this.tag(Tags.Items.CHESTS_WOODEN).addTag(RootsTags.Items.WILDWOOD_CHESTS);
  }

  private void copyGrove(TagKey<Grove> groveTag) {
    TagKey<Item> itemTag = TagKey.create(Registries.ITEM, groveTag.location());
    copyGrove(groveTag, itemTag);
  }

  private void copyGrove(TagKey<Grove> groveTag, TagKey<Item> itemTag) {
    this.groveTagsToCopy.put(groveTag, itemTag);
  }

  private void copySpell(TagKey<Spell> spellTag) {
    TagKey<Item> itemTag = TagKey.create(Registries.ITEM, spellTag.location());
    copySpell(spellTag, itemTag);
  }

  private void copySpell(TagKey<Spell> spellTag, TagKey<Item> itemTag) {
    this.spellTagsToCopy.put(spellTag, itemTag);
  }

  private void copyRitual(TagKey<Ritual> ritualTag) {
    TagKey<Item> itemTag = TagKey.create(Registries.ITEM, ritualTag.location());
    copyRitual(ritualTag, itemTag);
  }

  private void copyRitual(TagKey<Ritual> ritualTag, TagKey<Item> itemTag) {
    this.ritualTagsToCopy.put(ritualTag, itemTag);
  }

  private void manualCopy(TagKey<Block> blockTag) {
    TagKey<Item> itemTag = TagKey.create(Registries.ITEM, blockTag.location());
    copy(blockTag, itemTag);
  }

  @Override
  protected CompletableFuture<HolderLookup.Provider> createContentsProvider() {
    return super.createContentsProvider().thenCombine(this.ritualTags, (provider, tags) -> {
      this.ritualTagsToCopy.forEach((rTag, iTag) -> {
        TagBuilder tagbuilder = this.getOrCreateRawBuilder(iTag);
        Optional<TagBuilder> optional = tags.apply(rTag);
        optional.orElseThrow(() -> new IllegalStateException("Missing ritual item tag " + iTag.location())).build()
            .forEach(tagbuilder::add);
      });
      return provider;
    }).thenCombine(this.spellTags, (provider, tags) -> {
      this.spellTagsToCopy.forEach((rTag, iTag) -> {
        TagBuilder tagbuilder = this.getOrCreateRawBuilder(iTag);
        Optional<TagBuilder> optional = tags.apply(rTag);
        optional.orElseThrow(() -> new IllegalStateException("Missing spell item tag " + iTag.location())).build()
            .forEach(tagbuilder::add);
      });
      return provider;
    }).thenCombine(this.groveTags, (provider, tags) -> {
      this.groveTagsToCopy.forEach((rTag, iTag) -> {
        TagBuilder tagbuilder = this.getOrCreateRawBuilder(iTag);
        Optional<TagBuilder> optional = tags.apply(rTag);
        optional.orElseThrow(() -> new IllegalStateException("Missing grove item tag " + iTag.location())).build()
            .forEach(tagbuilder::add);
      });
      return provider;
    });
  }

  @Override
  public String getName() {
    return "Roots Item Tags";
  }
}
