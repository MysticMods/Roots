package mysticmods.roots;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.impl.RootsAPIImpl;
import mysticmods.roots.init.*;
import mysticmods.roots.network.PacketHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

// TODO:
// - The pestle is held wrongly
// - Extend JEI integration to different recipe types
// - Level and player conditions for JEI
// - Missing data maps?
// - Runic tools
// - Reach -> block interaction range, entity interaction range -> Item.getPlayerPOVHitResult
// - Block entity renderers
// - Block opacity/light block/movement block
// - Juice recipes
// - Overhaul all block properties, stop using defaults.
// - Overhaul all item properties
// - Display "ticks" of charged spells
// - Entity eye height?
// - Leash attachment points
// - Breading
// - Breaking pyres/mortars/pedestals drops contents
// - Entity carry layers for all mobs
// - Improve the Ritual command: set the last recipe and the cached recipe, why can't you remove items the first time?
// - Knife dispenser behaviour
// - Better drops with knives, better drops with better knive
// - Granite quartz ore smelting
// - Wildwood hanging sign? Wildwood sign?
// - Block placement tests for rituals
// - Pyre sound burning lah
// - Entity carried item layers
@Mod(RootsAPI.MODID)
public class Roots {
  protected PacketHandler packetHandler;

  public Roots(ModContainer container, IEventBus bus) {
    container.registerConfig(ModConfig.Type.COMMON, ConfigManager.COMMON_CONFIG);

    RootsAPI.INSTANCE = new RootsAPIImpl();

    ModBlocks.register(bus);
    ModBlockEntities.register(bus);
    ModItems.register(bus);
    ModEntities.register(bus);
    ModEffects.register(bus);
    ModHerbs.register(bus);
    ModConditions.register(bus);
    ModRituals.register(bus);
    ModSpells.register(bus);
    ModSounds.register(bus);
    ModSerializers.register(bus);
    ModRecipes.register(bus);
    ModFeatures.register(bus);
    ModLoot.register(bus);
    ModTests.register(bus);
    ModTabs.register(bus);
    ModAttachments.register(bus);
    ModGroves.register(bus);
    ModDamage.register(bus);
    ModParticles.register(bus);
    ModAdvancements.register(bus);
    packetHandler = new PacketHandler(bus);
  }
}
