package mysticmods.roots.init;

import net.minecraft.world.level.block.ComposterBlock;

public class ModCompost {
  public static void init () {
    // Leaves
    ComposterBlock.COMPOSTABLES.put(ModBlocks.WILDWOOD_LEAVES.get(), 0.3f);

    // Saplings
    ComposterBlock.COMPOSTABLES.put(ModBlocks.WILDWOOD_SAPLING.get(), 0.6f);

    // Seeds
    ComposterBlock.COMPOSTABLES.put(ModItems.AUBERGINE_SEEDS.get(), 0.3f);
    ComposterBlock.COMPOSTABLES.put(ModItems.PERESKIA_BULB.get(), 0.3f);
    ComposterBlock.COMPOSTABLES.put(ModItems.SPIRITLEAF_SEEDS.get(), 0.3f);
    ComposterBlock.COMPOSTABLES.put(ModItems.MOONGLOW_SEEDS.get(), 0.3f);
    ComposterBlock.COMPOSTABLES.put(ModItems.WILDEWHEET_SEEDS.get(), 0.3f);
    ComposterBlock.COMPOSTABLES.put(ModItems.GROVE_SPORES.get(), 0.3f);
    ComposterBlock.COMPOSTABLES.put(ModItems.COOKED_PERESKIA.get(), 0.3f);

    // Foods
    ComposterBlock.COMPOSTABLES.put(ModItems.ASSORTED_SEEDS.get(), 0.3f);
    ComposterBlock.COMPOSTABLES.put(ModItems.COOKED_SEEDS.get(), 0.3f);
    ComposterBlock.COMPOSTABLES.put(ModItems.AUBERGINE.get(), 0.65f);
    ComposterBlock.COMPOSTABLES.put(ModItems.STUFFED_AUBERGINE.get(), 1.5f);
    ComposterBlock.COMPOSTABLES.put(ModItems.COOKED_AUBERGINE.get(), 0.95f);
    ComposterBlock.COMPOSTABLES.put(ModItems.COOKED_BEETROOT.get(), 0.65f);
    ComposterBlock.COMPOSTABLES.put(ModItems.COOKED_CARROT.get(), 0.65f);
    ComposterBlock.COMPOSTABLES.put(ModItems.FLOUR.get(), 0.65f);
    ComposterBlock.COMPOSTABLES.put(ModItems.WILDEWHEET_BREAD.get(), 1f);

    // Herbs
    ComposterBlock.COMPOSTABLES.put(ModItems.WILDROOT.get(), 0.4f);
    ComposterBlock.COMPOSTABLES.put(ModItems.CLOUD_BERRY.get(), 0.9f);
    ComposterBlock.COMPOSTABLES.put(ModItems.INFERNO_BULB.get(), 0.9f);
    ComposterBlock.COMPOSTABLES.put(ModItems.STALICRIPE.get(), 0.9f);
    ComposterBlock.COMPOSTABLES.put(ModItems.DEWGONIA.get(), 0.9f);
    ComposterBlock.COMPOSTABLES.put(ModItems.GROVE_MOSS.get(), 0.4f);
    ComposterBlock.COMPOSTABLES.put(ModItems.BAFFLECAP.get(), 0.9f);
    ComposterBlock.COMPOSTABLES.put(ModItems.SPIRITLEAF.get(), 0.9f);
    ComposterBlock.COMPOSTABLES.put(ModItems.MOONGLOW.get(), 0.9f);
    ComposterBlock.COMPOSTABLES.put(ModItems.WILDEWHEET.get(), 0.9f);
    ComposterBlock.COMPOSTABLES.put(ModItems.PERESKIA.get(), 0.9f);

    // Barks
    ComposterBlock.COMPOSTABLES.put(ModItems.ACACIA_BARK.get(), 0.3f);
    ComposterBlock.COMPOSTABLES.put(ModItems.BIRCH_BARK.get(), 0.3f);
    ComposterBlock.COMPOSTABLES.put(ModItems.DARK_OAK_BARK.get(), 0.3f);
    ComposterBlock.COMPOSTABLES.put(ModItems.JUNGLE_BARK.get(), 0.3f);
    ComposterBlock.COMPOSTABLES.put(ModItems.OAK_BARK.get(), 0.3f);
    ComposterBlock.COMPOSTABLES.put(ModItems.SPRUCE_BARK.get(), 0.3f);
    ComposterBlock.COMPOSTABLES.put(ModItems.MANGROVE_BARK.get(), 0.3f);
    ComposterBlock.COMPOSTABLES.put(ModItems.MIXED_BARK.get(), 0.3f);
    ComposterBlock.COMPOSTABLES.put(ModItems.WILDWOOD_BARK.get(), 0.6f);
    ComposterBlock.COMPOSTABLES.put(ModItems.CRIMSON_BARK.get(), 0.3f);
    ComposterBlock.COMPOSTABLES.put(ModItems.WARPED_BARK.get(), 0.3f);

    // Flowers
    ComposterBlock.COMPOSTABLES.put(ModItems.PETALS.get(), 0.65f);
    ComposterBlock.COMPOSTABLES.put(ModBlocks.STONEPETAL.get(), 0.65f);





  }
}
