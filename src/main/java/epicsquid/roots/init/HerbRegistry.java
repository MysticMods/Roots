package epicsquid.roots.init;

import javax.annotation.Nonnull;

import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.api.RegisterHerbEvent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

@EventBusSubscriber(modid = Roots.MODID)
public class HerbRegistry {

  private static final ResourceLocation NAME = new ResourceLocation(Roots.DOMAIN, "herb");
  private static IForgeRegistry<Herb> REGISTRY = null;

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
    event.register(ModItems.spirit_herb);
    event.register(ModItems.spirit_herb_seed);
    event.register(ModItems.aubergine);
    event.register(ModItems.aubergine_seed);
    event.register(ModItems.fungus_cap);
    event.register(ModItems.moonglow_leaf);
    event.register(ModItems.moonglow_seed);
    event.register(ModItems.pereskia);
    event.register(ModItems.pereskia_bulb);
    event.register(ModItems.terra_moss);
    event.register(ModItems.terra_moss_seed);
    event.register(ModItems.wildroot);
  }
}
