package epicsquid.roots.init;

import epicsquid.mysticallib.block.BlockDoorBase;
import epicsquid.mysticallib.event.RegisterContentEvent;
import epicsquid.mysticallib.item.ItemArrowBase;
import epicsquid.mysticallib.item.ItemBase;
import epicsquid.mysticallib.item.ItemFoodBase;
import epicsquid.mysticallib.item.ItemSeedBase;
import epicsquid.roots.Roots;
import epicsquid.roots.item.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class ModItems {

  // All mod items
  public static Item pestle, component_pouch, spell_dust, staff, living_pickaxe, living_axe, living_shovel, living_hoe, living_sword, runic_shears, gold_knife, diamond_knife, iron_knife, stone_knife, wood_knife, living_quiver,
          sylvan_helmet, sylvan_chestplate, sylvan_leggings, sylvan_boots, wildwood_helmet, wildwood_chestplate, wildwood_leggings, wildwood_boots, apothecary_pouch,
  copper_knife, silver_knife, petals, flour, cooked_aubergine, stuffed_aubergine, living_arrow, runic_dust, seeds, cooked_seeds, cooked_pereskia;

  public static Item moonglow_leaf, aubergine, pereskia, terra_spores, terra_moss, spirit_herb, wildewheet,
          baffle_cap, bark_oak, bark_birch, bark_spruce, bark_jungle, bark_dark_oak, bark_acacia, bark_wildwood;

  public static Item moonglow_seed, aubergine_seed, pereskia_bulb, spirit_herb_seed, wildewheet_seed, cloud_berry, infernal_bulb,
      stalicripe, dewgonia, wildroot;

  public static Item ritual_life, ritual_storm, ritual_light, ritual_fire_storm, ritual_regrowth, ritual_windwall, ritual_warden, ritual_natural_aura, ritual_purity, ritual_frost, ritual_animal_harvest, ritual_summoning, ritual_wild_growth, ritual_overgrowth, ritual_flower_growth, ritual_transmutation;

  //Armor Materials
  public static final ItemArmor.ArmorMaterial sylvanArmorMaterial = EnumHelper.addArmorMaterial("SYLVAN", Roots.MODID + ":sylvan", 8, new int[]{1, 3, 4, 2}, 20, SoundEvents.BLOCK_SNOW_PLACE, 0F);
  public static final ItemArmor.ArmorMaterial wildwoodArmorMaterial = EnumHelper.addArmorMaterial("WILDWOOD", Roots.MODID + ":wildwood", 20, new int[]{3, 5, 6, 3}, 10, SoundEvents.BLOCK_WOOD_PLACE, 0F);

  /**
   * Register all items
   */
  public static void registerItems(@Nonnull RegisterContentEvent event) {
    event.addItem(moonglow_seed = new ItemSeedBase("moonglow_seed", ModBlocks.moonglow, Blocks.DIRT).setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(moonglow_leaf = new ItemBase("moonglow_leaf").setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(aubergine_seed = new ItemSeedBase("aubergine_seed", ModBlocks.aubergine, Blocks.DIRT).setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(aubergine = new ItemFoodBase("aubergine", 4, false).setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(pereskia_bulb = new ItemSeedBase("pereskia_bulb", ModBlocks.pereskia, Blocks.DIRT).setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(pereskia = new ItemBase("pereskia").setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(terra_moss = new ItemBase("terra_moss").setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(spirit_herb_seed = new ItemSeedBase("spirit_herb_seed", ModBlocks.spirit_herb, Blocks.DIRT).setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(spirit_herb = new ItemBase("spirit_herb").setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(wildroot = new ItemSeedBase("wildroot", ModBlocks.wildroot, Blocks.DIRT).setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(wildewheet = new ItemBase("wildewheet").setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(wildewheet_seed = new ItemSeedBase("wildewheet_seed", ModBlocks.wildewheet, Blocks.DIRT).setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(cloud_berry = new ItemSeedBase("cloud_berry", ModBlocks.cloud_berry, Blocks.DIRT).setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(infernal_bulb = new ItemSeedBase("infernal_bulb", ModBlocks.infernal_bulb, Blocks.MAGMA).setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(dewgonia = new ItemSeedBase("dewgonia", ModBlocks.dewgonia, Blocks.SAND).setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(stalicripe = new ItemSeedBase("stalicripe", ModBlocks.stalicripe, Blocks.STONE).setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(terra_spores = new ItemTerraSpore("terra_spores").setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(petals = new ItemBase("petals").setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(runic_dust = new ItemBase("runic_dust").setModelCustom(true).setCreativeTab(Roots.tab));

    // Barks and Knifes
    event.addItem(bark_oak = new ItemBase("bark_oak").setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(bark_spruce = new ItemBase("bark_spruce").setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(bark_birch = new ItemBase("bark_birch").setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(bark_jungle = new ItemBase("bark_jungle").setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(bark_dark_oak = new ItemBase("bark_dark_oak").setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(bark_acacia = new ItemBase("bark_acacia").setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(bark_wildwood = new ItemBase("bark_wildwood").setModelCustom(true).setCreativeTab(Roots.tab));

    event.addItem(pestle = new ItemBase("pestle").setModelCustom(true).setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(component_pouch = new ItemPouch("component_pouch").setModelCustom(true).setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(apothecary_pouch = new ItemApothecaryPouch("apothecary_pouch").setModelCustom(true).setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(spell_dust = new ItemSpellDust("spell_dust").setModelCustom(true).setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(flour = new ItemBase("flour").setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(staff = new ItemStaff("staff").setModelCustom(true).setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(living_pickaxe = new ItemLivingPickaxe(ToolMaterial.IRON, "living_pickaxe").setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(living_axe = new ItemLivingAxe(ToolMaterial.IRON, "living_axe").setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(living_shovel = new ItemLivingShovel(ToolMaterial.IRON, "living_shovel").setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(living_hoe = new ItemLivingHoe(ToolMaterial.IRON, "living_hoe").setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(living_sword = new ItemLivingSword(ToolMaterial.IRON, "living_sword").setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(living_arrow = new ItemArrowBase("living_arrow").setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(living_quiver = new ItemQuiver("living_quiver").setModelCustom(true).setCreativeTab(Roots.tab));

    event.addItem(sylvan_helmet = new ItemSylvanArmor(sylvanArmorMaterial, EntityEquipmentSlot.HEAD, "sylvan_helmet").setMaxStackSize(1));
    event.addItem(sylvan_chestplate = new ItemSylvanArmor(sylvanArmorMaterial, EntityEquipmentSlot.CHEST, "sylvan_chestplate").setMaxStackSize(1));
    event.addItem(sylvan_leggings = new ItemSylvanArmor(sylvanArmorMaterial, EntityEquipmentSlot.LEGS, "sylvan_leggings").setMaxStackSize(1));
    event.addItem(sylvan_boots = new ItemSylvanArmor(sylvanArmorMaterial, EntityEquipmentSlot.FEET, "sylvan_boots").setMaxStackSize(1));
    event.addItem(wildwood_helmet = new ItemWildwoodArmor(wildwoodArmorMaterial, EntityEquipmentSlot.HEAD, "wildwood_helmet").setMaxStackSize(1));
    event.addItem(wildwood_chestplate = new ItemWildwoodArmor(wildwoodArmorMaterial, EntityEquipmentSlot.CHEST, "wildwood_chestplate").setMaxStackSize(1));
    event.addItem(wildwood_leggings = new ItemWildwoodArmor(wildwoodArmorMaterial, EntityEquipmentSlot.LEGS, "wildwood_leggings").setMaxStackSize(1));
    event.addItem(wildwood_boots = new ItemWildwoodArmor(wildwoodArmorMaterial, EntityEquipmentSlot.FEET, "wildwood_boots").setMaxStackSize(1));

    event.addItem(runic_shears = new ItemRunicShears("runic_shears").setModelCustom(true).setCreativeTab(Roots.tab));

    event.addItem(wood_knife = new ItemKnife("wood_knife", ToolMaterial.WOOD).setCreativeTab(Roots.tab));
    event.addItem(stone_knife = new ItemKnife("stone_knife", ToolMaterial.STONE).setCreativeTab(Roots.tab));
    event.addItem(iron_knife = new ItemKnife("iron_knife", ToolMaterial.IRON).setCreativeTab(Roots.tab));
    event.addItem(diamond_knife = new ItemKnife("diamond_knife", ToolMaterial.DIAMOND).setCreativeTab(Roots.tab));
    event.addItem(gold_knife = new ItemKnife("gold_knife", ToolMaterial.GOLD).setCreativeTab(Roots.tab));

    ToolMaterial COPPER = EnumHelper.addToolMaterial("ROOTS:COPPER", 1, 175, 4.0f, 1.0f, 7);
    event.addItem(copper_knife = new ItemKnife("copper_knife", COPPER).setCreativeTab(Roots.tab));

    ToolMaterial SILVER = EnumHelper.addToolMaterial("ROOTS:SILVER", 1, 75, 6.0f, 1.0f, 25);
    event.addItem(silver_knife = new ItemKnife("silver_knife", SILVER).setCreativeTab(Roots.tab));

    event.addItem(cooked_aubergine = new ItemFoodBase("cooked_aubergine", 5, false).setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(stuffed_aubergine = new ItemFoodBase("stuffed_aubergine", 11, false).setModelCustom(true).setCreativeTab(Roots.tab));

    event.addItem(seeds = new ItemBase("assorted_seeds").setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(cooked_seeds = new ItemFoodBase("cooked_seeds", 1, 0.4f, false) {
      @Override
      public int getMaxItemUseDuration(ItemStack stack) {
        return 8;
      }
    }.setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(cooked_pereskia = new ItemFoodBase("cooked_pereskia", 7, false).setModelCustom(true).setCreativeTab(Roots.tab));

    // Rituals
    event.addItem(ritual_life = new ItemBase("ritual_life").setModelCustom(false).setCreativeTab(null));
    event.addItem(ritual_storm = new ItemBase("ritual_storm").setModelCustom(false).setCreativeTab(null));
    event.addItem(ritual_light = new ItemBase("ritual_light").setModelCustom(false).setCreativeTab(null));
    event.addItem(ritual_fire_storm = new ItemBase("ritual_fire_storm").setModelCustom(false).setCreativeTab(null));
    event.addItem(ritual_regrowth = new ItemBase("ritual_regrowth").setModelCustom(false).setCreativeTab(null));
    event.addItem(ritual_windwall = new ItemBase("ritual_windwall").setModelCustom(false).setCreativeTab(null));
    event.addItem(ritual_warden = new ItemBase("ritual_warden").setModelCustom(false).setCreativeTab(null));
    event.addItem(ritual_natural_aura = new ItemBase("ritual_natural_aura").setModelCustom(false).setCreativeTab(null));
    event.addItem(ritual_purity = new ItemBase("ritual_purity").setModelCustom(false).setCreativeTab(null));
    event.addItem(ritual_frost = new ItemBase("ritual_frost").setModelCustom(false).setCreativeTab(null));
    event.addItem(ritual_animal_harvest = new ItemBase("ritual_animal_harvest").setModelCustom(false).setCreativeTab(null));
    event.addItem(ritual_summoning = new ItemBase("ritual_summoning").setModelCustom(false).setCreativeTab(null));
    event.addItem(ritual_wild_growth = new ItemBase("ritual_wild_growth").setModelCustom(false).setCreativeTab(null));
    event.addItem(ritual_overgrowth = new ItemBase("ritual_overgrowth").setModelCustom(false).setCreativeTab(null));
    event.addItem(ritual_flower_growth = new ItemBase("ritual_flower_growth").setModelCustom(false).setCreativeTab(null));
    event.addItem(ritual_transmutation = new ItemBase("ritual_transmutation").setModelCustom(false).setCreativeTab(null));

    // KEEP AT END
    registerSeedDrops();
  }

  /**
   * Register item oredicts here
   */
  public static void registerOredict() {
    OreDictionary.registerOre("blockWool", new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE));

    for (BlockFlower.EnumFlowerType type : BlockFlower.EnumFlowerType.values()) {
      OreDictionary.registerOre("allFlowers", new ItemStack(type.getBlockType().getBlock(), 1, type.getMeta()));
    }
    for (BlockDoublePlant.EnumPlantType type : BlockDoublePlant.EnumPlantType.values()) {
      if (type == BlockDoublePlant.EnumPlantType.FERN || type == BlockDoublePlant.EnumPlantType.GRASS) continue;

      OreDictionary.registerOre("allFlowers", new ItemStack(Blocks.DOUBLE_PLANT, 1, type.getMeta()));
    }

    for (Item bark : Arrays.asList(bark_oak, bark_wildwood, bark_birch, bark_spruce, bark_acacia, bark_dark_oak, bark_jungle)) {
      OreDictionary.registerOre("rootsBark", bark);
    }

    for (Block rune : Arrays.asList(ModBlocks.runestone, ModBlocks.chiseled_runestone, ModBlocks.runestone_brick, ModBlocks.runestone_brick_alt)) {
      OreDictionary.registerOre("runestone", rune);
    }

    OreDictionary.registerOre("logWood", ModBlocks.wildwood_log);
    OreDictionary.registerOre("plankWood", ModBlocks.wildwood_planks);
    OreDictionary.registerOre("stairWood", ModBlocks.wildwood_stairs);
    OreDictionary.registerOre("slabWood", ModBlocks.wildwood_slab);
    OreDictionary.registerOre("doorWood", new ItemStack(((BlockDoorBase) ModBlocks.wildwood_door).getItemBlock()));
  }

  private static void registerSeedDrops() {
    MinecraftForge.addGrassSeed(new ItemStack(terra_spores, 1), 5);
    MinecraftForge.addGrassSeed(new ItemStack(wildroot, 1), 5);
    MinecraftForge.addGrassSeed(new ItemStack(aubergine_seed, 1), 5);
  }
}
