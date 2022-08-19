package mysticmods.roots.event.mod;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.gen.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = RootsAPI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Data {
  @SubscribeEvent
  public static void onDataGenerated(GatherDataEvent event) {
    RitualPropertyProvider properties = new RitualPropertyProvider(event.getGenerator());
    event.getGenerator().addProvider(properties);
    SpellPropertyProvider spell_properties = new SpellPropertyProvider(event.getGenerator());
    event.getGenerator().addProvider(spell_properties);
    SpellCostProvider spell_costs = new SpellCostProvider(event.getGenerator());
    event.getGenerator().addProvider(spell_costs);
    ModifierCostProvider modifier_costs = new ModifierCostProvider(event.getGenerator());
    event.getGenerator().addProvider(modifier_costs);
  }
}
