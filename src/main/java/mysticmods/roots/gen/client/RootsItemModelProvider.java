package mysticmods.roots.gen.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.item.TokenItem;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

@SuppressWarnings("DataFlowIssue")
public class RootsItemModelProvider extends ItemModelProvider {
  public RootsItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
    super(output, RootsAPI.MODID, existingFileHelper);
  }

  @SuppressWarnings("deprecation")
  @Override
  protected void registerModels() {
    // BLOCKS

    simpleBlockItem(ModBlocks.THATCH.get());
    simpleBlockItem(ModBlocks.RUNESTONE.get());
    simpleBlockItem(ModBlocks.MOSSY_RUNESTONE.get());
    simpleBlockItem(ModBlocks.CHISELED_RUNESTONE.get());
    simpleBlockItem(ModBlocks.RUNESTONE_BRICK.get());
    simpleBlockItem(ModBlocks.RUNESTONE_TILE.get());
    simpleBlockItem(ModBlocks.RUNED_OBSIDIAN.get());
    simpleBlockItem(ModBlocks.RUNED_BRICK.get());
    simpleBlockItem(ModBlocks.RUNED_TILE.get());
    simpleBlockItem(ModBlocks.CHISELED_RUNED_OBSIDIAN.get());
    simpleBlockItem(ModBlocks.SILVER_ORE.get());
    simpleBlockItem(ModBlocks.DEEPSLATE_SILVER_ORE.get());
    simpleBlockItem(ModBlocks.GRANITE_QUARTZ_ORE.get());
    simpleBlockItem(ModBlocks.RAW_SILVER_BLOCK.get());
    simpleBlockItem(ModBlocks.SILVER_BLOCK.get());
    simpleBlockItem(ModBlocks.WILDWOOD_LOG.get());
    simpleBlockItem(ModBlocks.STRIPPED_WILDWOOD_LOG.get());
    simpleBlockItem(ModBlocks.WILDWOOD_WOOD.get());
    simpleBlockItem(ModBlocks.STRIPPED_WILDWOOD_WOOD.get());
    simpleBlockItem(ModBlocks.WILDWOOD_PLANKS.get());
    basicItem(ModItems.WILDWOOD_SAPLING.get());
    basicItem(ModItems.STONEPETAL.get());
    simpleBlockItem(ModBlocks.WILDWOOD_LEAVES.get());
    simpleBlockItem(ModBlocks.RUNED_WILDWOOD_LOG.get());
    simpleBlockItem(ModBlocks.RUNED_SPRUCE_LOG.get());
    simpleBlockItem(ModBlocks.RUNED_BIRCH_LOG.get());
    simpleBlockItem(ModBlocks.RUNED_JUNGLE_LOG.get());
    simpleBlockItem(ModBlocks.RUNED_ACACIA_LOG.get());
    simpleBlockItem(ModBlocks.RUNED_DARK_OAK_LOG.get());
    simpleBlockItem(ModBlocks.RUNED_CRIMSON_STEM.get());
    simpleBlockItem(ModBlocks.RUNED_WARPED_STEM.get());
    simpleBlockItem(ModBlocks.RUNED_MANGROVE_LOG.get());
    simpleBlockItem(ModBlocks.RUNED_OAK_LOG.get());
    simpleBlockItem(ModBlocks.RUNESTONE_STAIRS.get());
    simpleBlockItem(ModBlocks.RUNESTONE_BRICK_STAIRS.get());
    simpleBlockItem(ModBlocks.RUNESTONE_TILE_STAIRS.get());
    simpleBlockItem(ModBlocks.MOSSY_RUNESTONE_STAIRS.get());
    simpleBlockItem(ModBlocks.RUNED_STAIRS.get());
    simpleBlockItem(ModBlocks.RUNED_BRICK_STAIRS.get());
    simpleBlockItem(ModBlocks.RUNED_TILE_STAIRS.get());
    simpleBlockItem(ModBlocks.WILDWOOD_STAIRS.get());
    simpleBlockItem(ModBlocks.RUNESTONE_SLAB.get());
    simpleBlockItem(ModBlocks.RUNESTONE_BRICK_SLAB.get());
    simpleBlockItem(ModBlocks.RUNESTONE_TILE_SLAB.get());
    simpleBlockItem(ModBlocks.MOSSY_RUNESTONE_SLAB.get());
    simpleBlockItem(ModBlocks.RUNED_SLAB.get());
    simpleBlockItem(ModBlocks.RUNED_BRICK_SLAB.get());
    simpleBlockItem(ModBlocks.RUNED_TILE_SLAB.get());
    simpleBlockItem(ModBlocks.WILDWOOD_SLAB.get());
    fenceInventory(ModBlocks.WILDWOOD_FENCE.getKey().location().getPath(), modLoc("block/" + ModBlocks.WILDWOOD_PLANKS.getKey().location().getPath()));
    simpleBlockItem(ModBlocks.WILDWOOD_GATE.get());
    buttonInventory(ModBlocks.RUNESTONE_BUTTON.getKey().location().getPath(), modLoc("block/" + ModBlocks.RUNESTONE.getKey().location().getPath()));
    buttonInventory(ModBlocks.RUNESTONE_BRICK_BUTTON.getKey().location().getPath(), modLoc("block/" + ModBlocks.RUNESTONE_BRICK.getKey().location().getPath()));
    buttonInventory(ModBlocks.MOSSY_RUNESTONE_BUTTON.getKey().location().getPath(), modLoc("block/" + ModBlocks.MOSSY_RUNESTONE.getKey().location().getPath()));
    buttonInventory(ModBlocks.RUNESTONE_TILE_BUTTON.getKey().location().getPath(), modLoc("block/" + ModBlocks.RUNESTONE_TILE.getKey().location().getPath()));
    buttonInventory(ModBlocks.RUNED_BUTTON.getKey().location().getPath(), modLoc("block/" + ModBlocks.RUNED_OBSIDIAN.getKey().location().getPath()));
    buttonInventory(ModBlocks.RUNED_BRICK_BUTTON.getKey().location().getPath(), modLoc("block/" + ModBlocks.RUNED_BRICK.getKey().location().getPath()));
    buttonInventory(ModBlocks.RUNED_TILE_BUTTON.getKey().location().getPath(), modLoc("block/" + ModBlocks.RUNED_TILE.getKey().location().getPath()));
    buttonInventory(ModBlocks.WILDWOOD_BUTTON.getKey().location().getPath(), modLoc("block/" + ModBlocks.WILDWOOD_PLANKS.getKey().location().getPath()));
    simpleBlockItem(ModBlocks.RUNESTONE_PRESSURE_PLATE.get());
    simpleBlockItem(ModBlocks.RUNESTONE_BRICK_PRESSURE_PLATE.get());
    simpleBlockItem(ModBlocks.MOSSY_RUNESTONE_PRESSURE_PLATE.get());
    simpleBlockItem(ModBlocks.RUNESTONE_TILE_PRESSURE_PLATE.get());
    simpleBlockItem(ModBlocks.RUNED_PRESSURE_PLATE.get());
    simpleBlockItem(ModBlocks.RUNED_BRICK_PRESSURE_PLATE.get());
    simpleBlockItem(ModBlocks.RUNED_TILE_PRESSURE_PLATE.get());
    simpleBlockItem(ModBlocks.WILDWOOD_PRESSURE_PLATE.get());
    // TODO: Does htis work?
    basicItem(ModItems.WILDWOOD_DOOR.get());
    withExistingParent(ModBlocks.WILDWOOD_TRAPDOOR.getKey().location().toString(), modLoc("block/wildwood_trapdoor_bottom"));
    basicItem(ModItems.WILDWOOD_LADDER.get());
    wallInventory(ModBlocks.RUNESTONE_WALL.getKey().location().getPath(), modLoc("block/" + ModBlocks.RUNESTONE.getKey().location().getPath()));
    wallInventory(ModBlocks.RUNESTONE_BRICK_WALL.getKey().location().getPath(), modLoc("block/" + ModBlocks.RUNESTONE_BRICK.getKey().location().getPath()));
    wallInventory(ModBlocks.MOSSY_RUNESTONE_WALL.getKey().location().getPath(), modLoc("block/" + ModBlocks.MOSSY_RUNESTONE.getKey().location().getPath()));
    wallInventory(ModBlocks.RUNESTONE_TILE_WALL.getKey().location().getPath(), modLoc("block/" + ModBlocks.RUNESTONE_TILE.getKey().location().getPath()));
    wallInventory(ModBlocks.RUNED_WALL.getKey().location().getPath(), modLoc("block/" + ModBlocks.RUNED_OBSIDIAN.getKey().location().getPath()));
    wallInventory(ModBlocks.RUNED_BRICK_WALL.getKey().location().getPath(), modLoc("block/" + ModBlocks.RUNED_BRICK.getKey().location().getPath()));
    wallInventory(ModBlocks.RUNED_TILE_WALL.getKey().location().getPath(), modLoc("block/" + ModBlocks.RUNED_TILE.getKey().location().getPath()));
    simpleBlockItem(ModBlocks.ELEMENTAL_SOIL.get());
    simpleBlockItem(ModBlocks.AQUEOUS_SOIL.get());
    simpleBlockItem(ModBlocks.CAELIC_SOIL.get());
    simpleBlockItem(ModBlocks.MAGMATIC_SOIL.get());
    simpleBlockItem(ModBlocks.TERRAN_SOIL.get());
    withExistingParent(ModBlocks.RITUAL_PEDESTAL.getKey().location().getPath(), modLoc("block/complex/ritual_pedestal"));
    withExistingParent(ModBlocks.REINFORCED_RITUAL_PEDESTAL.getKey().location().getPath(), modLoc("block/complex/reinforced_ritual_pedestal"));
    withExistingParent(ModBlocks.GROVE_CRAFTER.getKey().location().getPath(), modLoc("block/complex/grove_crafter"));
    withExistingParent(ModBlocks.GROVE_PEDESTAL.getKey().location().getPath(), modLoc("block/complex/grove_pedestal"));
    withExistingParent(ModBlocks.WILDWOOD_PEDESTAL.getKey().location().getPath(), modLoc("block/complex/wildwood_pedestal"));
    withExistingParent(ModBlocks.DISPLAY_PEDESTAL.getKey().location().getPath(), modLoc("block/complex/grove_pedestal"));
    // Wild roots are existing
    simpleBlockItem(ModBlocks.CREEPING_GROVE_MOSS.get());
    simpleBlockItem(ModBlocks.CREEPING_GROVE_MOSS.get());
    // TODO: is this correct?

    withExistingParent("bafflecap_block", modLoc("block/bafflecap_block_inventory"));
    // Is THIS correct? TODO
    withExistingParent(ModBlocks.PRIMAL_GROVE_STONE.getKey().location().getPath(), modLoc("block/primal_grove_stone_inventory"));
    simpleBlockItem(ModBlocks.INCENSE_BURNER.get());
    simpleBlockItem(ModBlocks.MORTAR.get());
    simpleBlockItem(ModBlocks.PYRE.get());
    simpleBlockItem(ModBlocks.SOUL_PYRE.get());
    simpleBlockItem(ModBlocks.REINFORCED_PYRE.get());
    simpleBlockItem(ModBlocks.REINFORCED_SOUL_PYRE.get());
    simpleBlockItem(ModBlocks.DECORATIVE_PYRE.get());
    simpleBlockItem(ModBlocks.DECORATIVE_SOUL_PYRE.get());
    simpleBlockItem(ModBlocks.UNENDING_BOWL.get());

    // Crops have no item models
    // Potted plants have no item models

    // ITEMS
    subfolder(ModItems.WILDROOT, "herbs");
    subfolder(ModItems.GROVE_MOSS, "herbs");
    subfolder(ModItems.CLOUD_BERRY, "herbs");
    subfolder(ModItems.DEWGONIA, "herbs");
    subfolder(ModItems.INFERNO_BULB, "herbs");
    subfolder(ModItems.STALICRIPE, "herbs");
    subfolder(ModItems.BAFFLECAP, "herbs");
    subfolder(ModItems.MOONGLOW, "herbs");
    subfolder(ModItems.PERESKIA, "herbs");
    subfolder(ModItems.SPIRITLEAF, "herbs");
    subfolder(ModItems.WILDEWHEET, "herbs");
    subfolder(ModItems.MOONGLOW_SEEDS, "herbs");
    subfolder(ModItems.PERESKIA_BULB, "herbs");
    subfolder(ModItems.SPIRITLEAF_SEEDS, "herbs");
    subfolder(ModItems.WILDEWHEET_SEEDS, "herbs");
    subfolder(ModItems.GROVE_SPORES, "herbs");
    basicItem(ModItems.AUBERGINE_SEEDS.get());

    basicItem(ModItems.CARAPACE.get());
    basicItem(ModItems.PELT.get());
    basicItem(ModItems.ANTLERS.get());
    basicItem(ModItems.VENISON.get());
    basicItem(ModItems.COOKED_VENISON.get());
    basicItem(ModItems.RAW_SQUID.get());
    basicItem(ModItems.COOKED_SQUID.get());
    basicItem(ModItems.ASSORTED_SEEDS.get());
    basicItem(ModItems.COOKED_SEEDS.get());
    basicItem(ModItems.COOKED_BEETROOT.get());
    basicItem(ModItems.COOKED_CARROT.get());
    basicItem(ModItems.AUBERGINE.get());
    basicItem(ModItems.COOKED_AUBERGINE.get());
    basicItem(ModItems.STUFFED_AUBERGINE.get());
    basicItem(ModItems.AUBERGINE_SALAD.get());
    basicItem(ModItems.BEETROOT_SALAD.get());
    basicItem(ModItems.STEWED_EGGPLANT.get());
    basicItem(ModItems.APPLE_CORDIAL.get());
    basicItem(ModItems.CACTUS_SYRUP.get());
    basicItem(ModItems.DANDELION_CORDIAL.get());
    basicItem(ModItems.LILAC_CORDIAL.get());
    basicItem(ModItems.PEONY_CORDIAL.get());
    basicItem(ModItems.ROSE_CORDIAL.get());
    basicItem(ModItems.VINEGAR.get());
    basicItem(ModItems.VEGETABLE_JUICE.get());
    basicItem(ModItems.INK_BOTTLE.get());
    subfolder(ModItems.ACACIA_BARK, "bark");
    subfolder(ModItems.BIRCH_BARK, "bark");
    subfolder(ModItems.CRIMSON_BARK, "bark");
    subfolder(ModItems.DARK_OAK_BARK, "bark");
    subfolder(ModItems.JUNGLE_BARK, "bark");
    subfolder(ModItems.MANGROVE_BARK, "bark");
    subfolder(ModItems.OAK_BARK, "bark");
    subfolder(ModItems.SPRUCE_BARK, "bark");
    subfolder(ModItems.WARPED_BARK, "bark");
    subfolder(ModItems.WILDWOOD_BARK, "bark");
    subfolder(ModItems.MIXED_BARK, "bark");

    subfolder(ModItems.APOTHECARY_POUCH, "pouches");
    subfolder(ModItems.COMPONENT_POUCH, "pouches");
    subfolder(ModItems.CREATIVE_POUCH, "pouches");
    subfolder(ModItems.FEY_POUCH, "pouches");
    subfolder(ModItems.HERB_POUCH, "pouches");

    subfolder(ModItems.COOKED_PERESKIA, "food");
    subfolder(ModItems.FLOUR, "food");
    subfolder(ModItems.WILDEWHEET_BREAD, "food");
    subfolder(ModItems.WILDROOT_STEW, "food");

    subfolder(ModItems.FIRE_STARTER, "tools");
    subfolder(ModItems.GRAMARY, "tools");
    subfolder(ModItems.LIVING_ARROW, "tools");
    subfolder(ModItems.LIVING_AXE, "tools");
    subfolder(ModItems.LIVING_HOE, "tools");
    subfolder(ModItems.LIVING_PICKAXE, "tools");
    subfolder(ModItems.LIVING_SHOVEL, "tools");
    subfolder(ModItems.LIVING_SWORD, "tools");

    subfolder(ModItems.PESTLE, "tools");
    subfolder(ModItems.RUNED_AXE, "tools");
    subfolder(ModItems.RUNED_DAGGER, "tools");
    subfolder(ModItems.RUNED_HOE, "tools");
    // TODO: what was this missed with
    subfolder(ModItems.RUNED_PICKAXE, "tools");
    subfolder(ModItems.RUNED_SHOVEL, "tools");
    subfolder(ModItems.RUNED_SWORD, "tools");
    subfolder(ModItems.RUNIC_SHEARS, "tools");

    ModelFile generated = new ModelFile.UncheckedModelFile("item/generated");
    getBuilder(ModItems.STAFF.getKey().location().toString()).parent(generated).texture("layer0", modLoc("item/tools/staff")).texture("layer1", modLoc("item/tools/staff_petal_1")).texture("layer2", modLoc("item/tools/staff_petal_2"));

    subfolder(ModItems.WILDWOOD_BOW, "tools");
    subfolder(ModItems.WILDWOOD_QUIVER, "tools");
    subfolder(ModItems.WOODEN_SHEARS, "tools");

    handheldItem(ModItems.WOODEN_KNIFE.get());
    handheldItem(ModItems.STONE_KNIFE.get());
    handheldItem(ModItems.COPPER_KNIFE.get());
    handheldItem(ModItems.IRON_KNIFE.get());
    handheldItem(ModItems.GOLD_KNIFE.get()); // TODO: golden?
    handheldItem(ModItems.SILVER_KNIFE.get());
    handheldItem(ModItems.DIAMOND_KNIFE.get());
    handheldItem(ModItems.NETHERITE_KNIFE.get());

    subfolder(ModItems.RELIQUARY, "containers");
    subfolder(ModItems.SPIRIT_BAG, "containers");

    subfolder(ModItems.FEY_LEATHER, "resources");
    subfolder(ModItems.GLASS_EYE, "resources");
    subfolder(ModItems.LIFE_ESSENCE, "resources");
    subfolder(ModItems.MYSTIC_FEATHER, "resources");
    subfolder(ModItems.PETALS, "resources");
    subfolder(ModItems.RUNIC_DUST, "resources");
    subfolder(ModItems.STRANGE_OOZE, "resources");

    basicItem(ModItems.ANTLER_HAT.get());
    basicItem(ModItems.BEETLE_HELMET.get());
    basicItem(ModItems.BEETLE_CHESTPLATE.get());
    basicItem(ModItems.BEETLE_LEGGINGS.get());
    basicItem(ModItems.BEETLE_BOOTS.get());

    basicItem(ModItems.RAW_SILVER.get());
    basicItem(ModItems.SILVER_INGOT.get());
    basicItem(ModItems.SILVER_NUGGET.get());
    basicItem(ModItems.COPPER_NUGGET.get());

    handheldItem(ModItems.COPPER_AXE.get());
    handheldItem(ModItems.COPPER_HOE.get());
    handheldItem(ModItems.COPPER_PICKAXE.get());
    handheldItem(ModItems.COPPER_SHOVEL.get());
    handheldItem(ModItems.COPPER_SWORD.get());
    basicItem(ModItems.COPPER_HELMET.get());
    basicItem(ModItems.COPPER_CHESTPLATE.get());
    basicItem(ModItems.COPPER_LEGGINGS.get());
    basicItem(ModItems.COPPER_BOOTS.get());

    spawnEggItem(ModItems.BEETLE_SPAWN_EGG.get());
    spawnEggItem(ModItems.DEER_SPAWN_EGG.get());
    spawnEggItem(ModItems.FENNEC_SPAWN_EGG.get());
    spawnEggItem(ModItems.GREEN_SPROUT_SPAWN_EGG.get());
    spawnEggItem(ModItems.TAN_SPROUT_SPAWN_EGG.get());
    spawnEggItem(ModItems.RED_SPROUT_SPAWN_EGG.get());
    spawnEggItem(ModItems.PURPLE_SPROUT_SPAWN_EGG.get());
    spawnEggItem(ModItems.OWL_SPAWN_EGG.get());
    spawnEggItem(ModItems.DUCK_SPAWN_EGG.get());

    BuiltInRegistries.ITEM.entrySet().forEach(entry -> {
      Item item = entry.getValue();
      if (entry.getKey().location().getNamespace().equals(RootsAPI.MODID)) {
        if (entry.getKey().location().getPath().startsWith("spell_") && item instanceof TokenItem.SpellTokenItem) {
          spell(item.builtInRegistryHolder());
        } else if (entry.getKey().location().getPath().startsWith("ritual_") && item instanceof TokenItem.RitualTokenItem) {
          ritual(item.builtInRegistryHolder());
        }
      }
    });
  }

  @SuppressWarnings("UnusedReturnValue")
  public ItemModelBuilder subfolder(Holder<Item> itemHolder, String subfolder) {
    ResourceLocation item = itemHolder.getKey().location();
    return getBuilder(item.toString())
        .parent(new ModelFile.UncheckedModelFile("item/generated"))
        .texture("layer0", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "item/" + subfolder + "/" + item.getPath()));
  }

  public ItemModelBuilder spell(Holder<Item> itemHolder) {
    ResourceLocation item = itemHolder.getKey().location();
    String spellLocation = "item/spells/" + item.getPath().replace("spell_", "");
    return getBuilder(item.toString())
        .parent(new ModelFile.UncheckedModelFile("item/generated"))
        .texture("layer0", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), spellLocation));
  }

  public ItemModelBuilder ritual(Holder<Item> itemHolder) {
    ResourceLocation item = itemHolder.getKey().location();
    String spellLocation = "item/rituals/" + item.getPath().replace("ritual_", "");
    return getBuilder(item.toString())
        .parent(new ModelFile.UncheckedModelFile("item/generated"))
        .texture("layer0", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), spellLocation));
  }
}
