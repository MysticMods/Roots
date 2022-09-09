package mysticmods.roots.event.mod;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.gen.provider.ModifierCostProvider;
import mysticmods.roots.gen.provider.RitualPropertyProvider;
import mysticmods.roots.gen.provider.SpellCostProvider;
import mysticmods.roots.gen.provider.SpellPropertyProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RootsAPI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Data {
  @SubscribeEvent
  public static void onDataGenerated(GatherDataEvent event) {
    RitualPropertyProvider properties = new RitualPropertyProvider(event.getGenerator());
    event.getGenerator().addProvider(event.includeServer(), properties);
    SpellPropertyProvider spell_properties = new SpellPropertyProvider(event.getGenerator());
    event.getGenerator().addProvider(event.includeServer(), spell_properties);
    SpellCostProvider spell_costs = new SpellCostProvider(event.getGenerator());
    event.getGenerator().addProvider(event.includeServer(), spell_costs);
    ModifierCostProvider modifier_costs = new ModifierCostProvider(event.getGenerator());
    event.getGenerator().addProvider(event.includeServer(), modifier_costs);
  }
}
