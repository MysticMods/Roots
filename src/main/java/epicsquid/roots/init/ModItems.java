package epicsquid.roots.init;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.event.RegisterContentEvent;
import epicsquid.mysticallib.item.ItemBase;
import epicsquid.mysticalworld.MysticalWorld;
import epicsquid.roots.item.ItemKnife;
import epicsquid.roots.Roots;
import epicsquid.roots.item.ItemLivingAxe;
import epicsquid.roots.item.ItemLivingHoe;
import epicsquid.roots.item.ItemLivingPickaxe;
import epicsquid.roots.item.ItemLivingShovel;
import epicsquid.roots.item.ItemLivingSword;
import epicsquid.roots.item.ItemPetalDust;
import epicsquid.roots.item.ItemPouch;
import epicsquid.roots.item.ItemRunicShears;
import epicsquid.roots.item.ItemStaff;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;

public class ModItems {

  // All mod items
  public static Item pestle, pouch, petal_dust, staff, living_pickaxe, living_axe, living_shovel, living_hoe, living_sword, runic_shears, gold_knife, diamond_knife, iron_knife, stone_knife, wood_knife;

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
