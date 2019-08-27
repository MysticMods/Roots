package epicsquid.roots.init;

import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.integration.patchouli.RegisterHerbEvent;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

@EventBusSubscriber(modid = Roots.MODID)
public class HerbRegistry {

  private static final ResourceLocation NAME = new ResourceLocation(Roots.DOMAIN, "herb");
  public static IForgeRegistry<Herb> REGISTRY = null;

  public static void init() {
    MinecraftForge.EVENT_BUS.post(new RegisterHerbEvent(NAME, REGISTRY));
  }

  @SubscribeEvent
  public static void registerRegistry(@Nonnull RegistryEvent.NewRegistry event) {
    REGISTRY = new RegistryBuilder<Herb>().setName(NAME).setType(Herb.class).setIDRange(0, 0x00FFFFFF).create();
  }

  // Register all herbs
  @SubscribeEvent
  public static void registerHerbs(@Nonnull RegisterHerbEvent event) {
    event.register(ModItems.spirit_herb, "spirit_herb");
    event.register(Item.getItemFromBlock(ModBlocks.baffle_cap_mushroom), "baffle_cap");
    event.register(ModItems.moonglow_leaf, "moonglow_leaf");
    event.register(ModItems.pereskia, "pereskia");
    event.register(ModItems.terra_moss, "terra_moss");
    event.register(ModItems.wildroot, "wildroot");
    event.register(ModItems.wildewheet, "wildewheet");
    event.register(ModItems.infernal_bulb, "infernal_bulb");
    event.register(ModItems.dewgonia, "dewgonia");
    event.register(ModItems.stalicripe, "stalicripe");
    event.register(ModItems.cloud_berry, "cloud_berry");

    ModRecipes.afterHerbRegisterInit();
  }

  @Nullable
  public static Herb getHerbByName(@Nonnull String name) {
    Collection<Herb> herbs = REGISTRY.getValuesCollection();
    for (Herb herb : herbs) {
      if (herb.getName().equals(name)) {
        return herb;
      }
    }
    Roots.logger.warn("Herb \"" + name + "\" not found in HerbRegistry");
    return null;
  }

  @Nullable
  public static Herb getHerbByItem(@Nonnull Item item) {
    Collection<Herb> herbs = REGISTRY.getValuesCollection();
    for (Herb herb : herbs) {
      if (herb.getItem() == item) {
        return herb;
      }
    }
    // Roots.logger.warn("Herb \"" + item.getRegistryName() + "\" not found in HerbRegistry");
    return null;
  }

  public static boolean containsHerbItem(@Nonnull Item item) {
    return REGISTRY.getValuesCollection().stream()
        .anyMatch(herb -> herb.getItem() == item);
  }
}
