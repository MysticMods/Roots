package epicsquid.roots.init;

import epicsquid.mysticallib.block.BlockDoorBase;
import epicsquid.mysticallib.event.RegisterContentEvent;
import epicsquid.mysticallib.item.ItemArrowBase;
import epicsquid.mysticallib.item.ItemBase;
import epicsquid.mysticallib.item.ItemFoodBase;
import epicsquid.mysticallib.item.ItemSeedBase;
import epicsquid.mysticallib.material.MaterialTypes;
import epicsquid.roots.Roots;
import epicsquid.roots.item.*;
import net.minecraft.block.*;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.Rarity;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ArmorItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModItems {

  // All mod items
  public static Item pestle, component_pouch, spell_dust, staff, living_pickaxe, living_axe, living_shovel, living_hoe, living_sword, runic_shears, gold_knife, diamond_knife, iron_knife, stone_knife, wood_knife, wildwood_quiver, wildwood_bow,
      sylvan_helmet, sylvan_chestplate, sylvan_leggings, sylvan_boots, wildwood_helmet, wildwood_chestplate, wildwood_leggings, wildwood_boots, apothecary_pouch,
      petals, flour, living_arrow, runic_dust, seeds, cooked_seeds, cooked_pereskia, fey_leather, wildewheet_bread;

  public static Item moonglow_leaf, pereskia, terra_spores, terra_moss, spirit_herb, wildewheet,
      baffle_cap, bark_oak, bark_birch, bark_spruce, bark_jungle, bark_dark_oak, bark_acacia, bark_wildwood,
      fairy_dust;

  public static Item moonglow_seed, pereskia_bulb, spirit_herb_seed, wildewheet_seed, cloud_berry, infernal_bulb,
      stalicripe, dewgonia, wildroot;

  public static Item ritual_healing_aura, ritual_heavy_storms, ritual_divine_protection, ritual_fire_storm, ritual_spreading_forest, ritual_windwall, ritual_warding_protection, ritual_germination, ritual_purity, ritual_frost_lands, ritual_animal_harvest, ritual_summon_creatures, ritual_wild_growth, ritual_overgrowth, ritual_flower_growth, ritual_transmutation, ritual_gathering;

  public static Item creative_pouch, fey_fire;

  public static List<Item> barks;

  // Auto-populated
  public static List<Item> knives = new ArrayList<>();

  // TODO: Refactor this out of this file
  //Armor Materials
  public static final ArmorItem.ArmorMaterial sylvanArmorMaterial = EnumHelper.addArmorMaterial("SYLVAN", Roots.MODID + ":sylvan", 12, new int[]{2, 4, 5, 3}, 20, SoundEvents.BLOCK_SNOW_PLACE, 0F);
  public static final ArmorItem.ArmorMaterial wildwoodArmorMaterial = EnumHelper.addArmorMaterial("WILDWOOD", Roots.MODID + ":wildwood", 20, new int[]{2, 6, 7, 2}, 20, SoundEvents.BLOCK_WOOD_PLACE, 1F);

  /**
   * Register all items
   */
  public static void registerItems(@Nonnull RegisterContentEvent event) {
    event.addItem(moonglow_seed = new ItemSeedBase("moonglow_seed", ModBlocks.moonglow, Blocks.DIRT).setCreativeTab(Roots.tab));
    event.addItem(moonglow_leaf = new ItemBase("moonglow_leaf").setCreativeTab(Roots.tab));
    event.addItem(pereskia_bulb = new ItemSeedBase("pereskia_bulb", ModBlocks.pereskia, Blocks.DIRT).setCreativeTab(Roots.tab));
    event.addItem(pereskia = new ItemBase("pereskia").setCreativeTab(Roots.tab)); // 0xff8cc2
    event.addItem(terra_moss = new ItemBase("terra_moss").setCreativeTab(Roots.tab));
    event.addItem(spirit_herb_seed = new ItemSeedBase("spirit_herb_seed", ModBlocks.spirit_herb, Blocks.DIRT).setCreativeTab(Roots.tab));
    event.addItem(spirit_herb = new ItemBase("spirit_herb").setCreativeTab(Roots.tab));
    event.addItem(wildroot = new ItemSeedBase("wildroot", ModBlocks.wildroot, Blocks.DIRT).setCreativeTab(Roots.tab));
    event.addItem(wildewheet = new ItemBase("wildewheet").setCreativeTab(Roots.tab));
    event.addItem(wildewheet_seed = new ItemSeedBase("wildewheet_seed", ModBlocks.wildewheet, Blocks.DIRT).setCreativeTab(Roots.tab));
    event.addItem(cloud_berry = new ItemSeedBase("cloud_berry", ModBlocks.cloud_berry, Blocks.DIRT).setCreativeTab(Roots.tab));
    event.addItem(infernal_bulb = new ItemSeedBase("infernal_bulb", ModBlocks.infernal_bulb, Blocks.MAGMA).setCreativeTab(Roots.tab));
    event.addItem(dewgonia = new ItemSeedBase("dewgonia", ModBlocks.dewgonia, Blocks.SAND).setCreativeTab(Roots.tab));
    event.addItem(stalicripe = new ItemSeedBase("stalicripe", ModBlocks.stalicripe, Blocks.STONE).setCreativeTab(Roots.tab));
    event.addItem(terra_spores = new TerraSporeItem("terra_spores").setCreativeTab(Roots.tab));
    event.addItem(petals = new ItemBase("petals").setCreativeTab(Roots.tab));
    event.addItem(runic_dust = new ItemBase("runic_dust").setCreativeTab(Roots.tab));

    // Barks and Knifes
    event.addItem(bark_oak = new ItemBase("bark_oak").setCreativeTab(Roots.tab));
    event.addItem(bark_spruce = new ItemBase("bark_spruce").setCreativeTab(Roots.tab));
    event.addItem(bark_birch = new ItemBase("bark_birch").setCreativeTab(Roots.tab));
    event.addItem(bark_jungle = new ItemBase("bark_jungle").setCreativeTab(Roots.tab));
    event.addItem(bark_dark_oak = new ItemBase("bark_dark_oak").setCreativeTab(Roots.tab));
    event.addItem(bark_acacia = new ItemBase("bark_acacia").setCreativeTab(Roots.tab));
    event.addItem(bark_wildwood = new ItemBase("bark_wildwood").setCreativeTab(Roots.tab));

    barks = Arrays.asList(bark_oak, bark_spruce, bark_birch, bark_jungle, bark_dark_oak, bark_acacia, bark_wildwood);

    event.addItem(fey_leather = new ItemBase("fey_leather") {
      @Override
      @SuppressWarnings("deprecation")
      public Rarity getRarity(ItemStack stack) {
        return Rarity.RARE;
      }
    }.setCreativeTab(Roots.tab));

    event.addItem(pestle = new ItemBase("pestle").setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(component_pouch = new Pouch("component_pouch").setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(creative_pouch = new CreativePouchItem("creative_pouch").setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(apothecary_pouch = new ApothecaryPouchItem("apothecary_pouch").setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(spell_dust = new SpellDustItem("spell_dust").setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(flour = new ItemBase("flour").setCreativeTab(Roots.tab));
    event.addItem(staff = new StaffItem("staff").setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(living_pickaxe = new LivingPickaxeItem(ToolMaterial.IRON, "living_pickaxe").setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(living_axe = new LivingAxeItem(ToolMaterial.IRON, "living_axe").setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(living_shovel = new LivingShovelItem(ToolMaterial.IRON, "living_shovel").setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(living_hoe = new LivingHoeItem(ToolMaterial.IRON, "living_hoe").setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(living_sword = new LivingSwordItem(ToolMaterial.IRON, "living_sword").setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(living_arrow = new LivingArrowItem("living_arrow").setCreativeTab(Roots.tab));
    event.addItem(wildwood_quiver = new QuiverItem("wildwood_quiver").setCreativeTab(Roots.tab));
    event.addItem(wildwood_bow = new WildwoodBowItem("wildwood_bow").setCreativeTab(Roots.tab));

    event.addItem(sylvan_helmet = new SylvanArmorItem(sylvanArmorMaterial, EquipmentSlotType.HEAD, "sylvan_helmet").setMaxStackSize(1));
    event.addItem(sylvan_chestplate = new SylvanArmorItem(sylvanArmorMaterial, EquipmentSlotType.CHEST, "sylvan_chestplate").setMaxStackSize(1));
    event.addItem(sylvan_leggings = new SylvanArmorItem(sylvanArmorMaterial, EquipmentSlotType.LEGS, "sylvan_leggings").setMaxStackSize(1));
    event.addItem(sylvan_boots = new SylvanArmorItem(sylvanArmorMaterial, EquipmentSlotType.FEET, "sylvan_boots").setMaxStackSize(1));
    event.addItem(wildwood_helmet = new WildwoodArmorItem(wildwoodArmorMaterial, EquipmentSlotType.HEAD, "wildwood_helmet").setMaxStackSize(1));
    event.addItem(wildwood_chestplate = new WildwoodArmorItem(wildwoodArmorMaterial, EquipmentSlotType.CHEST, "wildwood_chestplate").setMaxStackSize(1));
    event.addItem(wildwood_leggings = new WildwoodArmorItem(wildwoodArmorMaterial, EquipmentSlotType.LEGS, "wildwood_leggings").setMaxStackSize(1));
    event.addItem(wildwood_boots = new WildwoodArmorItem(wildwoodArmorMaterial, EquipmentSlotType.FEET, "wildwood_boots").setMaxStackSize(1));

    event.addItem(runic_shears = new RunicShearsItem("runic_shears").setCreativeTab(Roots.tab));

    MaterialTypes.addMaterial("vanilla:wood", ToolMaterial.WOOD, ToolMaterial.WOOD.getAttackDamage(), -1.7f);
    MaterialTypes.addMaterial("vanilla:stone", ToolMaterial.STONE, ToolMaterial.STONE.getAttackDamage(), -1.7f);
    MaterialTypes.addMaterial("vanilla:iron", ToolMaterial.IRON, ToolMaterial.IRON.getAttackDamage(), -1.5f);
    MaterialTypes.addMaterial("vanilla:diamond", ToolMaterial.DIAMOND, ToolMaterial.DIAMOND.getAttackDamage(), -1.0f);
    MaterialTypes.addMaterial("vanilla:gold", ToolMaterial.GOLD, ToolMaterial.GOLD.getAttackDamage(), -1.0f);

    event.addItem(wood_knife = new DruidKnifeItem("wood_knife", ToolMaterial.WOOD).setCreativeTab(Roots.tab));
    event.addItem(stone_knife = new DruidKnifeItem("stone_knife", ToolMaterial.STONE).setCreativeTab(Roots.tab));
    event.addItem(iron_knife = new DruidKnifeItem("iron_knife", ToolMaterial.IRON).setCreativeTab(Roots.tab));
    event.addItem(diamond_knife = new DruidKnifeItem("diamond_knife", ToolMaterial.DIAMOND).setCreativeTab(Roots.tab));
    event.addItem(gold_knife = new DruidKnifeItem("gold_knife", ToolMaterial.GOLD).setCreativeTab(Roots.tab));

    event.addItem(seeds = new ItemBase("assorted_seeds").setCreativeTab(Roots.tab));
    event.addItem(cooked_seeds = new ItemFoodBase("cooked_seeds", 1, 0.4f, false) {
      @Override
      public int getMaxItemUseDuration(ItemStack stack) {
        return 8;
      }
    }.setCreativeTab(Roots.tab));
    event.addItem(cooked_pereskia = new ItemFoodBase("cooked_pereskia", 5, false).setCreativeTab(Roots.tab));

    event.addItem(wildewheet_bread = new ItemFoodBase("wildewheet_bread", 7, false).setCreativeTab(Roots.tab));

    // Fairy dust
    event.addItem(fairy_dust = new ItemBase("fairy_dust").setCreativeTab(Roots.tab));

    // Rituals
    event.addItem(ritual_healing_aura = new ItemBase("ritual_healing_aura").setCreativeTab(null));
    event.addItem(ritual_heavy_storms = new ItemBase("ritual_heavy_storms").setCreativeTab(null));
    event.addItem(ritual_divine_protection = new ItemBase("ritual_divine_protection").setCreativeTab(null));
    event.addItem(ritual_fire_storm = new ItemBase("ritual_fire_storm").setCreativeTab(null));
    event.addItem(ritual_spreading_forest = new ItemBase("ritual_spreading_forest").setCreativeTab(null));
    event.addItem(ritual_windwall = new ItemBase("ritual_windwall").setCreativeTab(null));
    event.addItem(ritual_warding_protection = new ItemBase("ritual_warding_protection").setCreativeTab(null));
    event.addItem(ritual_germination = new ItemBase("ritual_germination").setCreativeTab(null));
    event.addItem(ritual_purity = new ItemBase("ritual_purity").setCreativeTab(null));
    event.addItem(ritual_frost_lands = new ItemBase("ritual_frost_lands").setCreativeTab(null));
    event.addItem(ritual_animal_harvest = new ItemBase("ritual_animal_harvest").setCreativeTab(null));
    event.addItem(ritual_summon_creatures = new ItemBase("ritual_summon_creatures").setCreativeTab(null));
    event.addItem(ritual_wild_growth = new ItemBase("ritual_wild_growth").setCreativeTab(null));
    event.addItem(ritual_overgrowth = new ItemBase("ritual_overgrowth").setCreativeTab(null));
    event.addItem(ritual_flower_growth = new ItemBase("ritual_flower_growth").setCreativeTab(null));
    event.addItem(ritual_transmutation = new ItemBase("ritual_transmutation").setCreativeTab(null));
    event.addItem(ritual_gathering = new ItemBase("ritual_gathering").setCreativeTab(null));

    event.addItem(fey_fire = new ItemBase("fey_fire").setCreativeTab(null));

    // KEEP AT END
    registerSeedDrops();
  }

  /**
   * Register item oredicts here
   */
  public static void registerOredict() {
    OreDictionary.registerOre("blockWool", new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE));

    for (FlowerBlock.EnumFlowerType type : FlowerBlock.EnumFlowerType.values()) {
      OreDictionary.registerOre("allFlowers", new ItemStack(type.getBlockType().getBlock(), 1, type.getMeta()));
    }
    for (DoublePlantBlock.EnumPlantType type : DoublePlantBlock.EnumPlantType.values()) {
      if (type == DoublePlantBlock.EnumPlantType.FERN || type == DoublePlantBlock.EnumPlantType.GRASS) continue;

      OreDictionary.registerOre("allTallFlowers", new ItemStack(Blocks.DOUBLE_PLANT, 1, type.getMeta()));
    }

    for (Item bark : Arrays.asList(bark_oak, bark_wildwood, bark_birch, bark_spruce, bark_acacia, bark_dark_oak, bark_jungle)) {
      OreDictionary.registerOre("rootsBark", bark);
    }

    for (Block rune : Arrays.asList(ModBlocks.runestone, ModBlocks.chiseled_runestone, ModBlocks.runestone_brick, ModBlocks.runestone_brick_alt)) {
      OreDictionary.registerOre("runestone", rune);
    }

    OreDictionary.registerOre("wildroot", ModItems.wildroot);

    OreDictionary.registerOre("logWood", ModBlocks.wildwood_log);
    OreDictionary.registerOre("plankWood", ModBlocks.wildwood_planks);
    OreDictionary.registerOre("stairWood", ModBlocks.wildwood_stairs);
    OreDictionary.registerOre("slabWood", ModBlocks.wildwood_slab);
    OreDictionary.registerOre("doorWood", new ItemStack(((BlockDoorBase) ModBlocks.wildwood_door).getItemBlock()));
    OreDictionary.registerOre("foodBread", Items.BREAD);
    OreDictionary.registerOre("foodBread", ModItems.wildewheet_bread);
    OreDictionary.registerOre("foodFlour", ModItems.flour);
    OreDictionary.registerOre("dustWheat", ModItems.flour);
    OreDictionary.registerOre("treeLeaves", ModBlocks.wildwood_leaves);
    OreDictionary.registerOre("leather", ModItems.fey_leather);
  }

  private static void registerSeedDrops() {
    MinecraftForge.addGrassSeed(new ItemStack(terra_spores, 1), 5);
    MinecraftForge.addGrassSeed(new ItemStack(wildroot, 1), 5);
  }
}
