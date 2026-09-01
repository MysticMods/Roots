package mysticmods.roots;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.config.ConfigManager;
import mysticmods.roots.init.*;
import mysticmods.roots.integration.IntegrationUtil;
import mysticmods.roots.network.PacketHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

// TODO: Main TODO list
// Edit the beetle armor layers for overlap
// Planting roots plants doesn't trigger a seedy place
// That mod that includes the wiki in-game - https://www.curseforge.com/minecraft/mc-mods/oracle-index
// Elemental crop bounding boxes are wrong
// - Geas
//    What other mobs will continue attacking when they lose a target?
// - Runic tools
// - Big Barrow loot chests
// - Big Barrow redesign, breading
// - Wildwood hanging sign? Wildwood sign?
// - Leash attachment points
// - Entity carry layers for all mobs
@Mod(RootsAPI.MODID)
public class Roots {
  protected PacketHandler packetHandler;

  public Roots(ModContainer container, IEventBus bus) {
    container.registerConfig(ModConfig.Type.COMMON, ConfigManager.COMMON_CONFIG);
    container.registerConfig(ModConfig.Type.CLIENT, ConfigManager.CLIENT_CONFIG);

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
    ModParticles.register(bus);
    ModAdvancements.register(bus);
    ModEnchantment.register(bus);
    ModContainers.register(bus);
    ModActions.register(bus);
    ModAttributes.register(bus);
    ModModifiers.register(bus);
    packetHandler = new PacketHandler(bus);

    IntegrationUtil.init(bus);
  }
}
