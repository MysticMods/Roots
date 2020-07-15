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
import java.util.Set;
import java.util.stream.Collectors;

@EventBusSubscriber(modid = Roots.MODID)
public class HerbRegistry {
  public static Herb spirit_herb;
  public static Herb baffle_cap;
  public static Herb moonglow_leaf;
  public static Herb pereskia;
  public static Herb terra_moss;
  public static Herb wildroot;
  public static Herb wildewheet;
  public static Herb infernal_bulb;
  public static Herb dewgonia;
  public static Herb stalicripe;
  public static Herb cloud_berry;

  private static final ResourceLocation NAME = new ResourceLocation(Roots.DOMAIN, "herb");
  public static IForgeRegistry<Herb> REGISTRY = null;
  public static Set<Item> HERB_ITEMS = null;

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
    spirit_herb = event.register(() -> ModItems.spirit_herb, "spirit_herb");
    baffle_cap = event.register(() -> Item.getItemFromBlock(ModBlocks.baffle_cap_mushroom), "baffle_cap");
    moonglow_leaf = event.register(() -> ModItems.moonglow_leaf, "moonglow_leaf");
    pereskia = event.register(() -> ModItems.pereskia, "pereskia");
    terra_moss = event.register(() -> ModItems.terra_moss, "terra_moss");
    wildroot = event.register(() -> ModItems.wildroot, "wildroot");
    wildewheet = event.register(() -> ModItems.wildewheet, "wildewheet");
    infernal_bulb = event.register(() -> ModItems.infernal_bulb, "infernal_bulb");
    dewgonia = event.register(() -> ModItems.dewgonia, "dewgonia");
    stalicripe = event.register(() -> ModItems.stalicripe, "stalicripe");
    cloud_berry = event.register(() -> ModItems.cloud_berry, "cloud_berry");

    ModRecipes.afterHerbRegisterInit();
  }

  @Nullable
  public static Herb getHerb(@Nonnull ResourceLocation key) {
    return REGISTRY.getValue(key);
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
    if (!isHerb(item)) {
      return null;
    }

    Collection<Herb> herbs = REGISTRY.getValuesCollection();
    for (Herb herb : herbs) {
      if (herb.getItem() == item) {
        return herb;
      }
    }
    // Roots.logger.warn("Herb \"" + item.getRegistryName() + "\" not found in HerbRegistry");
    return null;
  }

  public static boolean isHerb(@Nonnull Item item) {
    if (HERB_ITEMS == null) {
      HERB_ITEMS = REGISTRY.getValuesCollection().stream().map(Herb::getItem).collect(Collectors.toSet());
    }
    return HERB_ITEMS.contains(item);
  }
}
