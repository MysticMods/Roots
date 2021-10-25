package epicsquid.roots.event;

import com.google.common.collect.Sets;
import epicsquid.roots.Roots;
import epicsquid.roots.config.GeneralConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Set;

@Mod.EventBusSubscriber(modid = Roots.MODID)
@SuppressWarnings("unused")
public class LootHandler {
  private static Set<ResourceLocation> tables = Sets.newHashSet(LootTables.CHESTS_SIMPLE_DUNGEON, LootTables.CHESTS_ABANDONED_MINESHAFT, LootTables.CHESTS_DESERT_PYRAMID, LootTables.CHESTS_JUNGLE_TEMPLE, LootTables.CHESTS_WOODLAND_MANSION, LootTables.CHESTS_STRONGHOLD_CORRIDOR, LootTables.CHESTS_STRONGHOLD_CROSSING, LootTables.CHESTS_STRONGHOLD_LIBRARY, LootTables.CHESTS_END_CITY_TREASURE, LootTables.CHESTS_NETHER_BRIDGE);

  @SubscribeEvent
  public static void onLootLoad(LootTableLoadEvent event) {
    if (GeneralConfig.InjectLoot) {
      RandomValueRange range = new RandomValueRange(GeneralConfig.InjectMinimum, GeneralConfig.InjectMaximum);
      if (tables.contains(event.getName())) {
        LootPool pool = new LootPool(new ILootGenerator[]{
            new TableLootEntry(new ResourceLocation(Roots.MODID, "chests/inject"), 1, 0, new ILootCondition[0], "Roots")
        }, new ILootCondition[]{}, range, range, "Roots");
        event.getTable().addPool(pool);
      }
    }
  }
}
