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
// Start work on a wiki
// That mod that includes the wiki in-game - https://www.curseforge.com/minecraft/mc-mods/oracle-index
// JEI
// - Runic entity
// - Runic block
// - Player conditions (none exist)
// - Pouch dye recipe
// Summon creatures recipes
// Transmutation recipes
// Runic shears dispenser behaviour
// Elemental crop bounding boxes are wrong
// - Nondetection
//     Doesn't stop alerts
// - Geas
//    What other mobs will continue attacking when they lose a target?
// Drops aren't right
//   - Getting better but not perfect
// - Overhaul all block properties, stop using defaults.
// - Reach -> block interaction range, entity interaction range -> Item.getPlayerPOVHitResult
// - Runic tools
// - Block entity renderers
// - Block opacity/light block/movement block
// - Juice recipes
// - Breading
// - Big Barrow loot chests
// - Big Barrow redesign, breading
// - Wildwood hanging sign? Wildwood sign?
// - Block placement tests for rituals
// - The pestle is held wrongly
// - Leash attachment points
// - Entity carry layers for all mobs
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
    ModEnchantment.register(bus);
    ModContainers.register(bus);
    packetHandler = new PacketHandler(bus);
  }
}
