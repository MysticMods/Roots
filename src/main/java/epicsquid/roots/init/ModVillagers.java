package epicsquid.roots.init;

import epicsquid.mysticallib.block.IBlock;
import epicsquid.mysticallib.entity.villager.EmeraldForRandomItem;
import epicsquid.mysticallib.entity.villager.ListRandomItemForEmerald;
import epicsquid.mysticallib.entity.villager.ListRandomItemForRandomEmeralds;
import epicsquid.mysticallib.entity.villager.ListRandomItemWithPrice;
import epicsquid.mysticallib.entity.villager.ListRandomItemWithPrice.ItemAndPriceInfo;
import epicsquid.mysticalworld.materials.Materials;
import epicsquid.roots.Roots;
import epicsquid.roots.config.GeneralConfig;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

@Mod.EventBusSubscriber(modid = Roots.MODID)
@SuppressWarnings("unused")
public class ModVillagers {
  public static final VillagerProfession mageProfession = new VillagerProfession(Roots.MODID + ":wild_mage", Roots.MODID + ":textures/entity/mage.png", "minecraft:textures/entity/zombie_villager/zombie_villager.png"); // TODO: Zombie wild mage villager!

  @SubscribeEvent
  public static void onProfessionRegister(RegistryEvent.Register<VillagerProfession> event) {
    event.getRegistry().register(mageProfession);

    if (GeneralConfig.WildMageVillager) {
      VillagerCareer mageCareer = new VillagerCareer(mageProfession, "wild_mage").
          addTrade(1,
              new EmeraldForRandomItem(new PriceInfo(5, 12), ModItems.wildroot, ModItems.terra_moss, epicsquid.mysticalworld.init.ModItems.aubergine),
              new EmeraldForRandomItem(new PriceInfo(5, 15), ModItems.bark_oak, ModItems.bark_birch, ModItems.bark_spruce, ModItems.bark_acacia, ModItems.bark_dark_oak, ModItems.bark_jungle),
              new EmeraldForRandomItem(new PriceInfo(5, 12), ModItems.runic_dust, ModItems.petals),
              new ListRandomItemForEmerald(new PriceInfo(2, 5), ModItems.dewgonia, ModItems.cloud_berry, ModItems.stalicripe, ModItems.infernal_bulb, ((IBlock) ModBlocks.baffle_cap_mushroom).getItemBlock()),
              new ListRandomItemForEmerald(new PriceInfo(16, 24), ModItems.living_axe, ModItems.living_hoe, ModItems.living_pickaxe, ModItems.living_shovel, ModItems.living_sword),
              new ListRandomItemWithPrice(new ItemAndPriceInfo(ModItems.iron_knife, 5, 11), new ItemAndPriceInfo(ModItems.gold_knife, 8, 15), new ItemAndPriceInfo(epicsquid.mysticalworld.init.ModItems.copper_knife, 5, 11), new ItemAndPriceInfo(epicsquid.mysticalworld.init.ModItems.silver_knife, 8, 15), new ItemAndPriceInfo(ModItems.diamond_knife, 21, 35), new ItemAndPriceInfo(epicsquid.mysticalworld.init.ModItems.amethyst_knife, 21, 35))
          ).
          addTrade(2,
              new EmeraldForRandomItem(new PriceInfo(3, 8), ModItems.dewgonia, ModItems.cloud_berry, ModItems.stalicripe, ModItems.infernal_bulb, ((IBlock) ModBlocks.baffle_cap_mushroom).getItemBlock()),
              new EmeraldForRandomItem(new PriceInfo(7, 12), epicsquid.mysticalworld.init.ModItems.aubergine_seed, ModItems.wildewheet_seed, ModItems.moonglow_seed, ModItems.spirit_herb_seed, ModItems.pereskia_bulb),
              new EntityVillager.ListItemForEmeralds(ModItems.living_arrow, new PriceInfo(8, 12)),
              new ListRandomItemWithPrice(new ItemAndPriceInfo(ModItems.component_pouch, 21, 35), new ItemAndPriceInfo(ModItems.runic_shears, 18, 27)),
              new ListRandomItemForEmerald(new PriceInfo(2, 5), ModItems.wildewheet, ModItems.spirit_herb, ModItems.moonglow_leaf, ModItems.pereskia)).
          addTrade(3,
              new ListRandomItemForRandomEmeralds(new PriceInfo(8, 13), Materials.amethyst.getItem(), Items.DIAMOND),
              new EntityVillager.ListItemForEmeralds(new ItemStack(ModBlocks.wildwood_sapling), new PriceInfo(42, 64)));
    }
  }
}
