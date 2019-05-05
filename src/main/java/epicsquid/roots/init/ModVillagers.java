package epicsquid.roots.init;

import epicsquid.mysticallib.block.IBlock;
import epicsquid.roots.Roots;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

@Mod.EventBusSubscriber(modid = Roots.MODID)
@SuppressWarnings("unused")
public class ModVillagers {
  public static final VillagerProfession druidProfession = new VillagerProfession(Roots.MODID + ":druid", Roots.MODID + ":textures/entity/druid_hood.png", "minecraft:textures/entity/zombie_villager/zombie_villager.png"); // TODO: Zombie druid villager!
  public static VillagerCareer druidCareer;

  @SubscribeEvent
  public static void onProfessionRegister(RegistryEvent.Register<VillagerProfession> event) {
    event.getRegistry().register(druidProfession);

    druidCareer = new VillagerCareer(druidProfession, "druid").
        addTrade(1, new EntityVillager.EmeraldForItems(ModItems.wildroot, new EntityVillager.PriceInfo(6, 12)),
            new EntityVillager.EmeraldForItems(ModItems.terra_moss, new EntityVillager.PriceInfo(4, 8)),
            new EntityVillager.EmeraldForItems(ModItems.aubergine, new EntityVillager.PriceInfo(6, 12)),
            new EntityVillager.EmeraldForItems(ModItems.runic_dust, new EntityVillager.PriceInfo(2, 5)),
            new EntityVillager.EmeraldForItems(ModItems.petals, new EntityVillager.PriceInfo(10, 20)),
            new EntityVillager.ListItemForEmeralds(ModItems.wildewheet, new EntityVillager.PriceInfo(2, 5)),
            new EntityVillager.ListItemForEmeralds(ModItems.infernal_bulb, new EntityVillager.PriceInfo(2, 5)),
            new EntityVillager.ListItemForEmeralds(ModItems.cloud_berry, new EntityVillager.PriceInfo(2, 5)),
            new EntityVillager.ListItemForEmeralds(ModItems.spirit_herb, new EntityVillager.PriceInfo(2, 5)),
            new EntityVillager.ListItemForEmeralds(ModItems.moonglow_leaf, new EntityVillager.PriceInfo(2, 5)),
            new EntityVillager.ListItemForEmeralds(ModItems.pereskia, new EntityVillager.PriceInfo(2, 5)),
            new EntityVillager.ListItemForEmeralds(ModItems.stalicripe, new EntityVillager.PriceInfo(2, 5)),
            new EntityVillager.ListItemForEmeralds(((IBlock) ModBlocks.baffle_cap_mushroom).getItemBlock(), new EntityVillager.PriceInfo(2, 5))).
        addTrade(2, new EntityVillager.ListItemForEmeralds(ModItems.living_arrow, new EntityVillager.PriceInfo(8, 12)),
            new EntityVillager.ListItemForEmeralds(new ItemStack(ModItems.component_pouch), new EntityVillager.PriceInfo(21, 35)),
            new EntityVillager.ListItemForEmeralds(new ItemStack(ModItems.runic_shears), new EntityVillager.PriceInfo(18, 27))).
        addTrade(3, new EntityVillager.ListItemForEmeralds(new ItemStack(ModBlocks.wildwood_sapling), new EntityVillager.PriceInfo(42, 64)));
  }
}
