package epicsquid.roots.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.oredict.IOreDictEntry;
import epicsquid.mysticallib.types.OneTimeSupplier;
import epicsquid.roots.Roots;
import epicsquid.roots.recipe.SpiritDrops.StackItem;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocArg;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;

@ZenRegister
@ZenClass("mods." + Roots.MODID + ".SpiritDrops")
@ZenDocAppend({"docs/include/spiritdrops.example.md"})
@ZenDocClass("mods." + Roots.MODID + ".SpiritDrops")
public class SpiritDrops {
	@ZenMethod
	@ZenDocMethod(order = 1, description = {
			"Adds an item or oredict entry to the spell:Harvest -> \"Gifts of Undeath\" loot pouch."
	}, args = {
			@ZenDocArg(arg = "ingredient", info = "The OreDict type or Item to add."),
			@ZenDocArg(arg = "weight", info = "The weight of the item on the loot table.")
	})
	public static void addPouchDrop(IIngredient ingredient, int weight) {
		epicsquid.roots.recipe.SpiritDrops.SpiritItem out = getSpiritItem(ingredient, weight);
		if (out == null) {
			CraftTweakerAPI.logError("Invalid ingredient given to addPouchDrop. Must be an instance of Item or OreDictEntry.");
			return;
		}
		epicsquid.roots.recipe.SpiritDrops.addPouch(out);
	}
	
	@ZenMethod
	@ZenDocMethod(order = 2, description = "Clears the Spirit Pouch loot table.")
	public static void clearPouch() {
		epicsquid.roots.recipe.SpiritDrops.clearPouch();
	}
	
	@ZenMethod
	@ZenDocMethod(order = 3, description = {
			"Adds an item or oredict entry to the spell:Harvest -> \"Gifts of Undeath\" reliquary drop."
	}, args = {
			@ZenDocArg(arg = "ingredient", info = "The OreDict type or Item to add."),
			@ZenDocArg(arg = "weight", info = "The weight of the item on the loot table.")
	})
	public static void addReliquaryDrop(IIngredient ingredient, int weight) {
		epicsquid.roots.recipe.SpiritDrops.SpiritItem out = getSpiritItem(ingredient, weight);
		if (out == null) {
			CraftTweakerAPI.logError("Invalid ingredient given to addReliquaryDrop. Must be an ItemStack or OreDictEntry.");
			return;
		}
		epicsquid.roots.recipe.SpiritDrops.addReliquary(out);
	}
	
	@ZenMethod
	@ZenDocMethod(order = 4, description = "Clears the Reliquary loot table.")
	public static void clearReliquary() {
		epicsquid.roots.recipe.SpiritDrops.clearReliquary();
	}
	
	
	@Nullable
	private static epicsquid.roots.recipe.SpiritDrops.SpiritItem getSpiritItem(IIngredient ingredient, int weight) {
		epicsquid.roots.recipe.SpiritDrops.SpiritItem out = null;
		if (ingredient instanceof IItemStack) {
			ItemStack item = CraftTweakerMC.getItemStack(ingredient);
			out = new StackItem(new OneTimeSupplier<>(() -> item), weight);
		} else if (ingredient instanceof IOreDictEntry) {
			out = new epicsquid.roots.recipe.SpiritDrops.OreSpiritItem(((IOreDictEntry) ingredient).getName(), weight);
		}
		return out;
	}
}
