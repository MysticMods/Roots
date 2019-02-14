package epicsquid.roots.init;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.event.RegisterContentEvent;
import epicsquid.mysticallib.item.ItemBase;
import epicsquid.mysticalworld.MysticalWorld;
import epicsquid.roots.item.*;
import epicsquid.roots.Roots;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor;

public class ModItems {

  // All mod items
  public static Item pestle, pouch, petal_dust, staff, living_pickaxe, living_axe, living_shovel, living_hoe, living_sword, runic_shears, gold_knife, diamond_knife, iron_knife, stone_knife, wood_knife, sylvan_helmet, sylvan_chestplate, sylvan_leggings, sylvan_boots, wildwood_helmet, wildwood_chestplate, wildwood_leggings, wildwood_boots;

  //Rune Ashes
  public static Item aer_ash, terra_ash;

  /**
   * Register all items
   */
  public static void registerItems(@Nonnull RegisterContentEvent event) {
    event.addItem(pestle = new ItemBase("pestle").setModelCustom(true).setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(pouch = new ItemPouch("pouch").setModelCustom(true).setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(petal_dust = new ItemPetalDust("petal_dust").setModelCustom(true).setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(staff = new ItemStaff("staff").setModelCustom(true).setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(living_pickaxe = new ItemLivingPickaxe(ToolMaterial.IRON, "living_pickaxe").setModelCustom(true).setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(living_axe = new ItemLivingAxe(ToolMaterial.IRON, "living_axe").setModelCustom(true).setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(living_shovel = new ItemLivingShovel(ToolMaterial.IRON, "living_shovel").setModelCustom(true).setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(living_hoe = new ItemLivingHoe(ToolMaterial.IRON, "living_hoe").setModelCustom(true).setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(living_sword = new ItemLivingSword(ToolMaterial.IRON, "living_sword").setModelCustom(true).setCreativeTab(Roots.tab).setMaxStackSize(1));
    event.addItem(sylvan_helmet = new ItemSylvanArmor(ItemArmor.ArmorMaterial.IRON, EntityEquipmentSlot.HEAD, "sylvan_helmet").setMaxStackSize(1));
    event.addItem(sylvan_chestplate = new ItemSylvanArmor(ItemArmor.ArmorMaterial.IRON, EntityEquipmentSlot.CHEST, "sylvan_chestplate").setMaxStackSize(1));
    event.addItem(sylvan_leggings = new ItemSylvanArmor(ItemArmor.ArmorMaterial.IRON, EntityEquipmentSlot.LEGS, "sylvan_leggings").setMaxStackSize(1));
    event.addItem(sylvan_boots = new ItemSylvanArmor(ItemArmor.ArmorMaterial.IRON, EntityEquipmentSlot.FEET, "sylvan_boots").setMaxStackSize(1));
    event.addItem(wildwood_helmet = new ItemWildwoodArmor(ItemArmor.ArmorMaterial.DIAMOND, EntityEquipmentSlot.HEAD, "wildwood_helmet").setMaxStackSize(1));
    event.addItem(wildwood_chestplate = new ItemWildwoodArmor(ItemArmor.ArmorMaterial.DIAMOND, EntityEquipmentSlot.CHEST, "wildwood_chestplate").setMaxStackSize(1));
    event.addItem(wildwood_leggings = new ItemWildwoodArmor(ItemArmor.ArmorMaterial.DIAMOND, EntityEquipmentSlot.LEGS, "wildwood_leggings").setMaxStackSize(1));
    event.addItem(wildwood_boots = new ItemWildwoodArmor(ItemArmor.ArmorMaterial.DIAMOND, EntityEquipmentSlot.FEET, "wildwood_boots").setMaxStackSize(1));

    event.addItem(aer_ash = new ItemBase("aer_ash").setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(terra_ash = new ItemBase("terra_ash").setModelCustom(true).setCreativeTab(Roots.tab));
    event.addItem(runic_shears = new ItemRunicShears("runic_shears").setModelCustom(true).setCreativeTab(Roots.tab));

    event.addItem(wood_knife = new ItemKnife("wood_knife", ToolMaterial.WOOD).setModelCustom(true).setCreativeTab(MysticalWorld.tab));
    event.addItem(stone_knife = new ItemKnife("stone_knife", ToolMaterial.STONE).setModelCustom(true).setCreativeTab(MysticalWorld.tab));
    event.addItem(iron_knife = new ItemKnife("iron_knife", ToolMaterial.IRON).setModelCustom(true).setCreativeTab(MysticalWorld.tab));
    event.addItem(diamond_knife = new ItemKnife("diamond_knife", ToolMaterial.DIAMOND).setModelCustom(true).setCreativeTab(MysticalWorld.tab));
    event.addItem(gold_knife = new ItemKnife("gold_knife", ToolMaterial.GOLD).setModelCustom(true).setCreativeTab(MysticalWorld.tab));
  }

  /**
   * Register item oredicts here
   */
  public static void registerOredict() {

  }
}
