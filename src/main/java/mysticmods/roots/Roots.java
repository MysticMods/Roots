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

// TODO: Main TODO list
// Edit the beetle armor layers for overlap
// Planting roots plants doesn't trigger a seedy place
// That mod that includes the wiki in-game - https://www.curseforge.com/minecraft/mc-mods/oracle-index
// JEI
// - Durability costs knife/runic shears
// - Cooldowns on entity recipes
// Runic shears dispenser behaviour
// Elemental crop bounding boxes are wrong
// - Geas
//    What other mobs will continue attacking when they lose a target?
// - Reach -> block interaction range, entity interaction range -> Item.getPlayerPOVHitResult
// - Runic tools
// - Juice recipes
// - Big Barrow loot chests
// - Big Barrow redesign, breading
// - Wildwood hanging sign? Wildwood sign?
// - The pestle is held wrongly
// - Leash attachment points
// - Entity carry layers for all mobs
// - Transmutation recipes
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
