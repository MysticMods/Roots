package epicsquid.roots.event.handlers;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.recipe.crafting.GroveCraftingRecipe;
import epicsquid.roots.recipe.crafting.ShapedGroveCraftingRecipe;
import epicsquid.roots.util.XPUtil;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber(modid = Roots.MODID)
public class CraftingHandler {
  private static IRecipe lastRecipe = null;

  @SubscribeEvent
  public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
    if (!(event.craftMatrix instanceof InventoryCrafting)) return;

    InventoryCrafting crafting = (InventoryCrafting) event.craftMatrix;

    IRecipe recipe;

    if (lastRecipe != null && lastRecipe.matches(crafting, event.player.world)) {
      recipe = lastRecipe;
    } else {
      recipe = CraftingManager.findMatchingRecipe(crafting, event.player.world);
      if (recipe == null) return;
    }

    // Last clause is set up like that in case API integration uses a different namespace
    if (recipe instanceof ShapedGroveCraftingRecipe && recipe.getRegistryName() != null && !(recipe.getRegistryName().getPath().equals("runestone") && recipe.getRegistryName().getNamespace().equals(Roots.MODID))) {
      XPUtil.spawnXP(event.player.world, event.player.getPosition(), 1);
    } // Don't grant XP for shapeless as these can just be repeated:
      // - Runestone
      // - Runic soil

    lastRecipe = recipe;
  }
}
