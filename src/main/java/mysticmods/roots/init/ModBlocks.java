package mysticmods.roots.init;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import mysticmods.roots.blocks.*;
import mysticmods.roots.RootsTags;
import mysticmods.roots.blocks.crops.CropBlock;
import mysticmods.roots.blocks.crops.ElementalCropBlock;
import mysticmods.roots.blocks.crops.ThreeStageCropBlock;
import mysticmods.roots.blocks.crops.WaterElementalCropBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModBlocks {
  private static <T extends Block> void cropBlockstate(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider p) {
    String prefix = ctx.getName().replace("crop", "");
    ModelFile crop = p.models().getExistingFile(new ResourceLocation("minecraft", "block/crop"));
    p.getVariantBuilder(ctx.getEntry())
        .forAllStates(state -> {
          String file = prefix + state.getValue(((CropBlock) state.getBlock()).getAgeProperty());
          ModelFile stage = p.models().getBuilder(file)
              .parent(crop)
              .texture("crop", p.modLoc("block/crops/" + file));
          return ConfiguredModel.builder().modelFile(stage).build();
        });
  }

  private static <T extends Block> void crossBlockstate(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider p) {
    String prefix = ctx.getName().replace("crop", "");
    ModelFile crop = p.models().getExistingFile(new ResourceLocation("minecraft", "block/cross"));
    p.getVariantBuilder(ctx.getEntry())
        .forAllStates(state -> {
          String file = prefix + state.getValue(((CropBlock) state.getBlock()).getAgeProperty());
          ModelFile stage = p.models().getBuilder(file)
              .parent(crop)
              .texture("cross", p.modLoc("block/crops/" + file));
          return ConfiguredModel.builder().modelFile(stage).build();
        });
  }

  public static class Crops {
    public static BlockEntry<ThreeStageCropBlock> WILDROOT_CROP = REGISTRATE.block("wildroot_crop", (p) -> new ThreeStageCropBlock(p, ModItems.Herbs.WILDROOT))
        .properties(o -> AbstractBlock.Properties.copy(Blocks.WHEAT))
        .blockstate(ModBlocks::cropBlockstate)
        .tag(RootsTags.Blocks.WILDROOT_CROP)
        .register();

    public static BlockEntry<ElementalCropBlock> CLOUD_BERRY_CROP = REGISTRATE.block("cloud_berry_crop", (p) -> new ElementalCropBlock(p, ModItems.Herbs.CLOUD_BERRY))
        .properties(o -> AbstractBlock.Properties.copy(Blocks.WHEAT))
        .blockstate(ModBlocks::cropBlockstate)
        .tag(RootsTags.Blocks.CLOUD_BERRY_CROP)
        .register();
    public static BlockEntry<WaterElementalCropBlock> DEWGONIA_CROP = REGISTRATE.block("dewgonia_crop", (p) -> new WaterElementalCropBlock(p, ModItems.Herbs.DEWGONIA))
        .properties(o -> AbstractBlock.Properties.copy(Blocks.WHEAT))
        .blockstate(ModBlocks::cropBlockstate)
        .tag(RootsTags.Blocks.DEWGONIA_CROP)
        .register();
    public static BlockEntry<ElementalCropBlock> INFERNAL_BULB_CROP = REGISTRATE.block("infernal_bulb_crop", (p) -> new ElementalCropBlock(p, ModItems.Herbs.INFERNAL_BULB))
        .properties(o -> AbstractBlock.Properties.copy(Blocks.WHEAT))
        .blockstate(ModBlocks::crossBlockstate)
        .tag(RootsTags.Blocks.INFERNAL_BULB_CROP)
        .register();
    public static BlockEntry<ElementalCropBlock> STALICRIPE_CROP = REGISTRATE.block("stalicripe_crop", (p) -> new ElementalCropBlock(p, ModItems.Herbs.STALICRIPE))
        .properties(o -> AbstractBlock.Properties.copy(Blocks.WHEAT))
        .blockstate(ModBlocks::cropBlockstate)
        .tag(RootsTags.Blocks.STALICRIPE_CROP)
        .register();

    public static BlockEntry<CropBlock> MOONGLOW_LEAF_CROP = REGISTRATE.block("moonglow_leaf_crop", (p) -> new CropBlock(p, ModItems.Seeds.MOONGLOW_LEAF_SEEDS))
        .properties(o -> AbstractBlock.Properties.copy(Blocks.WHEAT))
        .blockstate(ModBlocks::cropBlockstate)
        .tag(RootsTags.Blocks.MOONGLOW_LEAF_CROP)
        .register();
    public static BlockEntry<CropBlock> PERESKIA_CROP = REGISTRATE.block("pereskia_crop", (p) -> new CropBlock(p, ModItems.Seeds.PERESKIA_BULB))
        .properties(o -> AbstractBlock.Properties.copy(Blocks.WHEAT))
        .blockstate(ModBlocks::crossBlockstate)
        .tag(RootsTags.Blocks.PERESKIA_CROP)
        .register();
    public static BlockEntry<ThreeStageCropBlock> SPIRIT_HERB_CROP = REGISTRATE.block("spirit_herb_crop", (p) -> new ThreeStageCropBlock(p, ModItems.Seeds.SPIRIT_HERB_SEEDS))
        .properties(o -> AbstractBlock.Properties.copy(Blocks.WHEAT))
        .blockstate(ModBlocks::cropBlockstate)
        .tag(RootsTags.Blocks.SPIRIT_HERB_CROP)
        .register();
    public static BlockEntry<CropBlock> WILDEWHEET_CROP = REGISTRATE.block("wildewheet_crop", (p) -> new CropBlock(p, ModItems.Seeds.WILDEWHEET_SEEDS))
        .properties(o -> AbstractBlock.Properties.copy(Blocks.WHEAT))
        .blockstate(ModBlocks::cropBlockstate)
        .tag(RootsTags.Blocks.WILDEWHEET_CROP)
        .register();
  }

   public static BlockEntry<FeyLightBlock> FEYLIGHT = REGISTRATE.block("feylight", FeyLightBlock::new)
        .properties(o -> AbstractBlock.Properties.copy(Blocks.TORCH))
        .register();
  public static BlockEntry<CatalystPlateBlock> CATALYST_PLATE = REGISTRATE.block("catalyst_plate", CatalystPlateBlock::new).register();
  public static BlockEntry<ElementalSoilBlock> ELEMENTAL_SOIL = REGISTRATE.block("elemental_soil", ElementalSoilBlock::new).register();
  public static BlockEntry<FeyCrafterBlock> FEY_CRAFTER = REGISTRATE.block("fey_crafter", FeyCrafterBlock::new).register();
  public static BlockEntry<ImbuerBlock> IMBUER = REGISTRATE.block("imbuer", ImbuerBlock::new).register();
  public static BlockEntry<ImposerBlock> IMPOSER = REGISTRATE.block("imposer", ImposerBlock::new).register();
  public static BlockEntry<IncenseBurnerBlock> INCENSE_BURNER = REGISTRATE.block("incense_burner", IncenseBurnerBlock::new).register();
  public static BlockEntry<MortarBlock> MORTAR = REGISTRATE.block("mortar", MortarBlock::new).register();
  public static BlockEntry<PyreBlock> PYRE = REGISTRATE.block("pyre", PyreBlock::new).register();

  public static void load() {
  }
}
