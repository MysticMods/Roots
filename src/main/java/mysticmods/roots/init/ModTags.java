package mysticmods.roots.init;

import com.tterrag.registrate.providers.ProviderType;
import mysticmods.mysticalworld.MWTags;
import mysticmods.roots.api.RootsAPI;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;

import static mysticmods.roots.Roots.REGISTRATE;

@SuppressWarnings("unchecked")
public class ModTags {
  static {
    REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, b -> {
      b.tag(RootsAPI.Tags.Entities.PACIFIST).add(EntityType.AXOLOTL, EntityType.CAT, EntityType.CHICKEN, EntityType.COD, EntityType.COW, EntityType.DOLPHIN, EntityType.DONKEY, EntityType.FOX, EntityType.GLOW_SQUID, EntityType.HORSE, EntityType.IRON_GOLEM, EntityType.LLAMA, EntityType.MULE, EntityType.MOOSHROOM, EntityType.OCELOT, EntityType.PARROT, EntityType.POLAR_BEAR, EntityType.RABBIT, EntityType.SALMON, EntityType.SHEEP, EntityType.SNOW_GOLEM, EntityType.SQUID, EntityType.TRADER_LLAMA, EntityType.TROPICAL_FISH, EntityType.TURTLE, EntityType.VILLAGER, EntityType.WOLF);
      b.tag(RootsAPI.Tags.Entities.ANIMAL_HARVEST).addTag(RootsAPI.Tags.Entities.PACIFIST);
    });

    REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, b -> {
      // Internal mod tags
      b.tag(RootsAPI.Tags.Blocks.SOILS).addTags(RootsAPI.Tags.Blocks.EARTH_SOIL, RootsAPI.Tags.Blocks.AIR_SOIL, RootsAPI.Tags.Blocks.FIRE_SOIL, RootsAPI.Tags.Blocks.WATER_SOIL, RootsAPI.Tags.Blocks.ELEMENTAL_SOIL);
      b.tag(RootsAPI.Tags.Blocks.RUNED_LOGS).addTags(RootsAPI.Tags.Blocks.RUNED_ACACIA_LOG, RootsAPI.Tags.Blocks.RUNED_BIRCH_LOG, RootsAPI.Tags.Blocks.RUNED_OAK_LOG, RootsAPI.Tags.Blocks.RUNED_DARK_OAK_LOG, RootsAPI.Tags.Blocks.RUNED_CRIMSON_STEM, RootsAPI.Tags.Blocks.RUNED_JUNGLE_LOG, RootsAPI.Tags.Blocks.RUNED_SPRUCE_LOG, RootsAPI.Tags.Blocks.RUNED_WARPED_STEM, RootsAPI.Tags.Blocks.RUNED_WILDWOOD_LOG);
      b.tag(RootsAPI.Tags.Blocks.AIR_CROPS).addTags(RootsAPI.Tags.Blocks.CLOUD_BERRY_CROP);
      b.tag(RootsAPI.Tags.Blocks.FIRE_CROPS).addTags(RootsAPI.Tags.Blocks.INFERNAL_BULB_CROP);
      b.tag(RootsAPI.Tags.Blocks.WATER_CROPS).addTags(RootsAPI.Tags.Blocks.DEWGONIA_CROP);
      b.tag(RootsAPI.Tags.Blocks.EARTH_CROPS).addTags(RootsAPI.Tags.Blocks.STALICRIPE_CROP);
      b.tag(RootsAPI.Tags.Blocks.ELEMENTAL_CROPS).addTags(RootsAPI.Tags.Blocks.WATER_CROPS, RootsAPI.Tags.Blocks.AIR_CROPS, RootsAPI.Tags.Blocks.EARTH_CROPS, RootsAPI.Tags.Blocks.FIRE_CROPS);
      b.tag(RootsAPI.Tags.Blocks.CROPS).addTags(RootsAPI.Tags.Blocks.WILDEWHEET_CROP, RootsAPI.Tags.Blocks.WILDROOT_CROP, RootsAPI.Tags.Blocks.SPIRIT_LEAF_CROP, RootsAPI.Tags.Blocks.PERESKIA_CROP, RootsAPI.Tags.Blocks.MOONGLOW_CROP, RootsAPI.Tags.Blocks.ELEMENTAL_CROPS);

      // Forge compat tags
      b.tag(RootsAPI.Tags.Blocks.FORGE_CROPS).addTag(RootsAPI.Tags.Blocks.CROPS);

      // Logs that burn
      b.tag(RootsAPI.Tags.Blocks.MINECRAFT_LOGS_THAT_BURN).addTags(RootsAPI.Tags.Blocks.WILDWOOD_LOGS);
      b.tag(RootsAPI.Tags.Blocks.MINECRAFT_LOGS).addTags(RootsAPI.Tags.Blocks.WILDWOOD_LOGS);
    });

    REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, b -> {
      // Block copies

      b.copy(RootsAPI.Tags.Blocks.SOILS, RootsAPI.Tags.Items.Blocks.SOILS);
      b.copy(RootsAPI.Tags.Blocks.WATER_SOIL, RootsAPI.Tags.Items.Blocks.WATER_SOIL);
      b.copy(RootsAPI.Tags.Blocks.AIR_SOIL, RootsAPI.Tags.Items.Blocks.AIR_SOIL);
      b.copy(RootsAPI.Tags.Blocks.EARTH_SOIL, RootsAPI.Tags.Items.Blocks.EARTH_SOIL);
      b.copy(RootsAPI.Tags.Blocks.FIRE_SOIL, RootsAPI.Tags.Items.Blocks.FIRE_SOIL);
      b.copy(RootsAPI.Tags.Blocks.ELEMENTAL_SOIL, RootsAPI.Tags.Items.Blocks.ELEMENTAL_SOIL);
      b.copy(RootsAPI.Tags.Blocks.RUNED_OBSIDIAN, RootsAPI.Tags.Items.Blocks.RUNED_OBSIDIAN);
      b.copy(RootsAPI.Tags.Blocks.RUNESTONE, RootsAPI.Tags.Items.Blocks.RUNESTONE);
      b.copy(RootsAPI.Tags.Blocks.WILDWOOD_LOGS, RootsAPI.Tags.Items.Blocks.WILDWOOD_LOGS);
      b.copy(RootsAPI.Tags.Blocks.RUNED_LOGS, RootsAPI.Tags.Items.Blocks.RUNED_LOGS);
      b.copy(RootsAPI.Tags.Blocks.RUNED_ACACIA_LOG, RootsAPI.Tags.Items.Blocks.RUNED_ACACIA_LOG);
      b.copy(RootsAPI.Tags.Blocks.RUNED_DARK_OAK_LOG, RootsAPI.Tags.Items.Blocks.RUNED_DARK_OAK_LOG);
      b.copy(RootsAPI.Tags.Blocks.RUNED_OAK_LOG, RootsAPI.Tags.Items.Blocks.RUNED_OAK_LOG);
      b.copy(RootsAPI.Tags.Blocks.RUNED_BIRCH_LOG, RootsAPI.Tags.Items.Blocks.RUNED_BIRCH_LOG);
      b.copy(RootsAPI.Tags.Blocks.RUNED_JUNGLE_LOG, RootsAPI.Tags.Items.Blocks.RUNED_JUNGLE_LOG);
      b.copy(RootsAPI.Tags.Blocks.RUNED_SPRUCE_LOG, RootsAPI.Tags.Items.Blocks.RUNED_SPRUCE_LOG);
      b.copy(RootsAPI.Tags.Blocks.RUNED_WILDWOOD_LOG, RootsAPI.Tags.Items.Blocks.RUNED_WILDWOOD_LOG);
      b.copy(RootsAPI.Tags.Blocks.RUNED_CRIMSON_STEM, RootsAPI.Tags.Items.Blocks.RUNED_CRIMSON_STEM);
      b.copy(RootsAPI.Tags.Blocks.RUNED_WARPED_STEM, RootsAPI.Tags.Items.Blocks.RUNED_WARPED_STEM);

      b.tag(RootsAPI.Tags.Items.AIR_CROPS).addTags(RootsAPI.Tags.Items.CLOUD_BERRY_CROP);
      b.tag(RootsAPI.Tags.Items.WATER_CROPS).addTags(RootsAPI.Tags.Items.DEWGONIA_CROP);
      b.tag(RootsAPI.Tags.Items.EARTH_CROPS).addTags(RootsAPI.Tags.Items.STALICRIPE_CROP);
      b.tag(RootsAPI.Tags.Items.FIRE_CROPS).addTags(RootsAPI.Tags.Items.INFERNAL_BULB_CROP);

      b.tag(RootsAPI.Tags.Items.CROPS).addTags(RootsAPI.Tags.Items.AIR_CROPS, RootsAPI.Tags.Items.FIRE_CROPS, RootsAPI.Tags.Items.EARTH_CROPS, RootsAPI.Tags.Items.WATER_CROPS, RootsAPI.Tags.Items.WILDEWHEET_CROP, RootsAPI.Tags.Items.SPIRIT_LEAF_CROP, RootsAPI.Tags.Items.PERESKIA_CROP, RootsAPI.Tags.Items.WILDROOT_CROP, RootsAPI.Tags.Items.MOONGLOW_CROP);

      b.tag(RootsAPI.Tags.Items.BARKS).addTags(RootsAPI.Tags.Items.BIRCH_BARK, RootsAPI.Tags.Items.ACACIA_BARK, RootsAPI.Tags.Items.DARK_OAK_BARK, RootsAPI.Tags.Items.OAK_BARK, RootsAPI.Tags.Items.JUNGLE_BARK, RootsAPI.Tags.Items.SPRUCE_BARK, RootsAPI.Tags.Items.WILDWOOD_BARK, RootsAPI.Tags.Items.CRIMSON_BARK, RootsAPI.Tags.Items.WARPED_BARK, RootsAPI.Tags.Items.MIXED_BARK);

      b.tag(MWTags.Items.FORGE_KNIVES);
      b.tag(RootsAPI.Tags.Items.GROVE_CRAFTER_ACTIVATION).addTag(MWTags.Items.FORGE_KNIVES);
      b.tag(RootsAPI.Tags.Items.PYRE_ACTIVATION).add(Items.FLINT_AND_STEEL);

      b.tag(RootsAPI.Tags.Items.Herbs.MOONGLOW).addTag(RootsAPI.Tags.Items.MOONGLOW_CROP);
      b.tag(RootsAPI.Tags.Items.Herbs.SPIRIT_LEAF).addTag(RootsAPI.Tags.Items.SPIRIT_LEAF_CROP);
      b.tag(RootsAPI.Tags.Items.Herbs.CLOUD_BERRY).addTag(RootsAPI.Tags.Items.CLOUD_BERRY_CROP);
      b.tag(RootsAPI.Tags.Items.Herbs.DEWGONIA).addTag(RootsAPI.Tags.Items.DEWGONIA_CROP);
      b.tag(RootsAPI.Tags.Items.Herbs.PERESKIA).addTag(RootsAPI.Tags.Items.PERESKIA_CROP);
      b.tag(RootsAPI.Tags.Items.Herbs.STALICRIPE).addTag(RootsAPI.Tags.Items.STALICRIPE_CROP);
      b.tag(RootsAPI.Tags.Items.Herbs.INFERNAL_BULB).addTag(RootsAPI.Tags.Items.INFERNAL_BULB_CROP);
      b.tag(RootsAPI.Tags.Items.Herbs.WILDROOT).addTag(RootsAPI.Tags.Items.WILDROOT_CROP);
      b.tag(RootsAPI.Tags.Items.Herbs.WILDEWHEET).addTag(RootsAPI.Tags.Items.WILDEWHEET_CROP);
      b.tag(RootsAPI.Tags.Items.Herbs.SPIRIT_LEAF).addTag(RootsAPI.Tags.Items.SPIRIT_LEAF_CROP);
      b.tag(RootsAPI.Tags.Items.Herbs.SACRED_MOSS).addTag(RootsAPI.Tags.Items.SACRED_MOSS_CROP);

      b.tag(RootsAPI.Tags.Items.Herbs.HERBS).addTags(RootsAPI.Tags.Items.Herbs.MOONGLOW, RootsAPI.Tags.Items.Herbs.SPIRIT_LEAF, RootsAPI.Tags.Items.Herbs.CLOUD_BERRY, RootsAPI.Tags.Items.Herbs.DEWGONIA, RootsAPI.Tags.Items.Herbs.PERESKIA, RootsAPI.Tags.Items.Herbs.STALICRIPE, RootsAPI.Tags.Items.Herbs.INFERNAL_BULB, RootsAPI.Tags.Items.Herbs.WILDROOT, RootsAPI.Tags.Items.Herbs.WILDEWHEET, RootsAPI.Tags.Items.Herbs.SPIRIT_LEAF, RootsAPI.Tags.Items.Herbs.SACRED_MOSS);

      // TODO: Copying crops to the actual item tag?
    });
  }

  public static void load() {
  }
}
