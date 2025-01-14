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
// - Item data component for staff
//   (simplify staff stuff)
// - Item data component for "tokens"
// - Rest of the recipes that haven't been converted
// - JEI integration
// - Ore world generation
// - Missing data maps?
//    - Spell modifier parents
// - Armor material
// - Runic tools
// - Cache spell costs from holder
// - Reach property?
// - Common setup:
//   - Flower pots
//   - Chicken tempting?
// - Item properties -> Data Components

@Mod(RootsAPI.MODID)
public class Roots {
  /*  public static RootsArmorMaterial ANTLER_MATERIAL = new RootsArmorMaterial("roots:antlers", 7, new int[]{3, 0, 0, 0}, 18, SoundEvents.ARMOR_EQUIP_TURTLE, 1f, 0f, () -> Ingredient.of(ModItems.ANTLERS.get()));
    public static RootsArmorMaterial CARAPACE_MATERIAL = new RootsArmorMaterial("roots:carapace", 25, new int[]{2, 5, 6, 2}, 18, SoundEvents.ARMOR_EQUIP_TURTLE, 0f, 0f, () -> Ingredient.of(ModItems.CARAPACE.get()));
    public static RootsArmorMaterial COPPER_MATERIAL = new RootsArmorMaterial("roots:copper", 15, new int[]{2, 5, 6, 2}, 7, SoundEvents.ARMOR_EQUIP_IRON.defaultValue(), 0.0f, 0.0f, () -> Ingredient.of(Tags.Items.STORAGE_BLOCKS_COPPER));*/
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
    packetHandler = new PacketHandler(bus);
  }
}
