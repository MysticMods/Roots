package epicsquid.roots.integration.jer;

import epicsquid.mysticallib.item.ItemSeedBase;
import epicsquid.roots.init.ModItems;
import jeresources.api.IJERAPI;
import jeresources.api.IPlantRegistry;
import jeresources.api.JERPlugin;
import jeresources.api.drop.PlantDrop;
import net.minecraft.item.ItemStack;

public class JERIntegration {

  @JERPlugin
  public static IJERAPI JERApi;

  public static void init() {
    // Plant Drops
    IPlantRegistry registry = JERApi.getPlantRegistry();

    registry.register((ItemSeedBase) ModItems.moonglow_seed,
        new PlantDrop(new ItemStack(ModItems.moonglow_leaf), 1, 1),
        new PlantDrop(new ItemStack(ModItems.moonglow_seed), 1, 4));
    registry.register((ItemSeedBase) ModItems.wildroot,
        new PlantDrop(new ItemStack(ModItems.wildroot), 1, 4));
    registry.register((ItemSeedBase) ModItems.pereskia_bulb,
        new PlantDrop(new ItemStack(ModItems.pereskia), 1, 1),
        new PlantDrop(new ItemStack(ModItems.pereskia_bulb), 1, 4));
    registry.register((ItemSeedBase) ModItems.wildewheet_seed,
        new PlantDrop(new ItemStack(ModItems.wildewheet), 1, 1),
        new PlantDrop(new ItemStack(ModItems.wildewheet_seed), 1, 4));
    registry.register((ItemSeedBase) ModItems.spirit_herb_seed,
        new PlantDrop(new ItemStack(ModItems.spirit_herb), 1, 1),
        new PlantDrop(new ItemStack(ModItems.spirit_herb_seed), 1, 4));
    registry.register((ItemSeedBase) epicsquid.mysticalworld.init.ModItems.aubergine_seed,
        new PlantDrop(new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine), 1, 1),
        new PlantDrop(new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine_seed), 1, 4));
    registry.register((ItemSeedBase) ModItems.infernal_bulb,
        new PlantDrop(new ItemStack(ModItems.infernal_bulb), 1, 4));
    registry.register((ItemSeedBase) ModItems.dewgonia,
        new PlantDrop(new ItemStack(ModItems.dewgonia), 1, 4));
    registry.register((ItemSeedBase) ModItems.stalicripe,
        new PlantDrop(new ItemStack(ModItems.stalicripe), 1, 4));
    registry.register((ItemSeedBase) ModItems.cloud_berry,
        new PlantDrop(new ItemStack(ModItems.cloud_berry), 1, 4));
  }
}
