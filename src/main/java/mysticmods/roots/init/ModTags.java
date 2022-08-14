package mysticmods.roots.init;

import com.tterrag.registrate.providers.ProviderType;
import mysticmods.mysticalworld.MWTags;
import mysticmods.roots.RootsTags;
import mysticmods.roots.RootsTags.Blocks;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;

import static mysticmods.roots.Roots.REGISTRATE;

@SuppressWarnings("unchecked")
public class ModTags {
  static {
    REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, b -> {
      b.tag(RootsTags.Entities.PACIFIST).add(EntityType.AXOLOTL, EntityType.CAT, EntityType.CHICKEN, EntityType.COD, EntityType.COW, EntityType.DOLPHIN, EntityType.DONKEY, EntityType.FOX, EntityType.GLOW_SQUID, EntityType.HORSE, EntityType.IRON_GOLEM, EntityType.LLAMA, EntityType.MULE, EntityType.MOOSHROOM, EntityType.OCELOT, EntityType.PARROT, EntityType.POLAR_BEAR, EntityType.RABBIT, EntityType.SALMON, EntityType.SHEEP, EntityType.SNOW_GOLEM, EntityType.SQUID, EntityType.TRADER_LLAMA, EntityType.TROPICAL_FISH, EntityType.TURTLE, EntityType.VILLAGER, EntityType.WOLF);
      b.tag(RootsTags.Entities.ANIMAL_HARVEST).addTag(RootsTags.Entities.PACIFIST);
    });

    REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, b -> {
      // Internal mod tags
      b.tag(Blocks.SOILS).addTags(Blocks.EARTH_SOIL, Blocks.AIR_SOIL, Blocks.FIRE_SOIL, Blocks.WATER_SOIL, Blocks.ELEMENTAL_SOIL);
      b.tag(Blocks.RUNED_LOGS).addTags(Blocks.RUNED_ACACIA_LOG, Blocks.RUNED_BIRCH_LOG, Blocks.RUNED_OAK_LOG, Blocks.RUNED_DARK_OAK_LOG, Blocks.RUNED_CRIMSON_STEM, Blocks.RUNED_JUNGLE_LOG, Blocks.RUNED_SPRUCE_LOG, Blocks.RUNED_WARPED_STEM, Blocks.RUNED_WILDWOOD_LOG);
      b.tag(Blocks.AIR_CROPS).addTags(Blocks.CLOUD_BERRY_CROP);
      b.tag(Blocks.FIRE_CROPS).addTags(Blocks.INFERNAL_BULB_CROP);
      b.tag(Blocks.WATER_CROPS).addTags(Blocks.DEWGONIA_CROP);
      b.tag(Blocks.EARTH_CROPS).addTags(Blocks.STALICRIPE_CROP);
      b.tag(Blocks.ELEMENTAL_CROPS).addTags(Blocks.WATER_CROPS, Blocks.AIR_CROPS, Blocks.EARTH_CROPS, Blocks.FIRE_CROPS);
      b.tag(Blocks.CROPS).addTags(Blocks.WILDEWHEET_CROP, Blocks.WILDROOT_CROP, Blocks.SPROUTNIP_CROP, Blocks.PERESKIA_CROP, Blocks.MOONGLOW_CROP, Blocks.ELEMENTAL_CROPS);

      // Forge compat tags
      b.tag(Blocks.FORGE_CROPS).addTag(Blocks.CROPS);

      // Logs that burn
      b.tag(Blocks.MINECRAFT_LOGS_THAT_BURN).addTags(Blocks.WILDWOOD_LOGS);
      b.tag(Blocks.MINECRAFT_LOGS).addTags(Blocks.WILDWOOD_LOGS);
    });

    REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, b -> {
      // Block copies

      b.copy(Blocks.SOILS, RootsTags.Items.Blocks.SOILS);
      b.copy(Blocks.WATER_SOIL, RootsTags.Items.Blocks.WATER_SOIL);
      b.copy(Blocks.AIR_SOIL, RootsTags.Items.Blocks.AIR_SOIL);
      b.copy(Blocks.EARTH_SOIL, RootsTags.Items.Blocks.EARTH_SOIL);
      b.copy(Blocks.FIRE_SOIL, RootsTags.Items.Blocks.FIRE_SOIL);
      b.copy(Blocks.ELEMENTAL_SOIL, RootsTags.Items.Blocks.ELEMENTAL_SOIL);
      b.copy(Blocks.RUNED_OBSIDIAN, RootsTags.Items.Blocks.RUNED_OBSIDIAN);
      b.copy(Blocks.RUNESTONE, RootsTags.Items.Blocks.RUNESTONE);
      b.copy(Blocks.WILDWOOD_LOGS, RootsTags.Items.Blocks.WILDWOOD_LOGS);
      b.copy(Blocks.RUNED_LOGS, RootsTags.Items.Blocks.RUNED_LOGS);
      b.copy(Blocks.RUNED_ACACIA_LOG, RootsTags.Items.Blocks.RUNED_ACACIA_LOG);
      b.copy(Blocks.RUNED_DARK_OAK_LOG, RootsTags.Items.Blocks.RUNED_DARK_OAK_LOG);
      b.copy(Blocks.RUNED_OAK_LOG, RootsTags.Items.Blocks.RUNED_OAK_LOG);
      b.copy(Blocks.RUNED_BIRCH_LOG, RootsTags.Items.Blocks.RUNED_BIRCH_LOG);
      b.copy(Blocks.RUNED_JUNGLE_LOG, RootsTags.Items.Blocks.RUNED_JUNGLE_LOG);
      b.copy(Blocks.RUNED_SPRUCE_LOG, RootsTags.Items.Blocks.RUNED_SPRUCE_LOG);
      b.copy(Blocks.RUNED_WILDWOOD_LOG, RootsTags.Items.Blocks.RUNED_WILDWOOD_LOG);
      b.copy(Blocks.RUNED_CRIMSON_STEM, RootsTags.Items.Blocks.RUNED_CRIMSON_STEM);
      b.copy(Blocks.RUNED_WARPED_STEM, RootsTags.Items.Blocks.RUNED_WARPED_STEM);

      b.tag(RootsTags.Items.AIR_CROPS).addTags(RootsTags.Items.CLOUD_BERRY_CROP);
      b.tag(RootsTags.Items.WATER_CROPS).addTags(RootsTags.Items.DEWGONIA_CROP);
      b.tag(RootsTags.Items.EARTH_CROPS).addTags(RootsTags.Items.STALICRIPE_CROP);
      b.tag(RootsTags.Items.FIRE_CROPS).addTags(RootsTags.Items.INFERNAL_BULB_CROP);

      b.tag(RootsTags.Items.CROPS).addTags(RootsTags.Items.AIR_CROPS, RootsTags.Items.FIRE_CROPS, RootsTags.Items.EARTH_CROPS, RootsTags.Items.WATER_CROPS, RootsTags.Items.WILDEWHEET_CROP, RootsTags.Items.SPROUTNIP_CROP, RootsTags.Items.PERESKIA_CROP, RootsTags.Items.WILDROOT_CROP, RootsTags.Items.MOONGLOW_CROP);

      b.tag(RootsTags.Items.BARKS).addTags(RootsTags.Items.BIRCH_BARK, RootsTags.Items.ACACIA_BARK, RootsTags.Items.DARK_OAK_BARK, RootsTags.Items.OAK_BARK, RootsTags.Items.JUNGLE_BARK, RootsTags.Items.SPRUCE_BARK, RootsTags.Items.WILDWOOD_BARK, RootsTags.Items.CRIMSON_BARK, RootsTags.Items.WARPED_BARK, RootsTags.Items.MIXED_BARK);

      b.tag(MWTags.Items.FORGE_KNIVES);
      b.tag(RootsTags.Items.GROVE_CRAFTER_ACTIVATION).addTag(MWTags.Items.FORGE_KNIVES);
      b.tag(RootsTags.Items.PYRE_ACTIVATION).add(Items.FLINT_AND_STEEL);

      b.tag(RootsTags.Herbs.MOONGLOW).addTag(RootsTags.Items.MOONGLOW_CROP);
      b.tag(RootsTags.Herbs.SPROUTNIP).addTag(RootsTags.Items.SPROUTNIP_CROP);
      b.tag(RootsTags.Herbs.CLOUD_BERRY).addTag(RootsTags.Items.CLOUD_BERRY_CROP);
      b.tag(RootsTags.Herbs.DEWGONIA).addTag(RootsTags.Items.DEWGONIA_CROP);
      b.tag(RootsTags.Herbs.PERESKIA).addTag(RootsTags.Items.PERESKIA_CROP);
      b.tag(RootsTags.Herbs.STALICRIPE).addTag(RootsTags.Items.STALICRIPE_CROP);
      b.tag(RootsTags.Herbs.INFERNAL_BULB).addTag(RootsTags.Items.INFERNAL_BULB_CROP);
      b.tag(RootsTags.Herbs.WILDROOT).addTag(RootsTags.Items.WILDROOT_CROP);
      b.tag(RootsTags.Herbs.WILDEWHEET).addTag(RootsTags.Items.WILDEWHEET_CROP);
      b.tag(RootsTags.Herbs.SPROUTNIP).addTag(RootsTags.Items.SPROUTNIP_CROP);
      b.tag(RootsTags.Herbs.SACRED_MOSS).addTag(RootsTags.Items.SACRED_MOSS_CROP);

      b.tag(RootsTags.Herbs.HERBS).addTags(RootsTags.Herbs.MOONGLOW, RootsTags.Herbs.SPROUTNIP, RootsTags.Herbs.CLOUD_BERRY, RootsTags.Herbs.DEWGONIA, RootsTags.Herbs.PERESKIA, RootsTags.Herbs.STALICRIPE, RootsTags.Herbs.INFERNAL_BULB, RootsTags.Herbs.WILDROOT, RootsTags.Herbs.WILDEWHEET, RootsTags.Herbs.SPROUTNIP, RootsTags.Herbs.SACRED_MOSS);

      // TODO: Copying crops to the actual item tag?
    });
  }

  public static void load() {
  }
}
