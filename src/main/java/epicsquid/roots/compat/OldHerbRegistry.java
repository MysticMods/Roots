package epicsquid.roots.compat;

import epicsquid.roots.Roots;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Roots.MODID)
public class OldHerbRegistry {
  public static class Herb extends IForgeRegistryEntry.Impl<Herb> {
  }

  private static final ResourceLocation NAME = new ResourceLocation(Roots.DOMAIN, "herb");
  public static IForgeRegistry<Herb> REGISTRY = null;

  public static void init() {
  }

  @SubscribeEvent
  public static void registerRegistry(@Nonnull RegistryEvent.NewRegistry event) {
    REGISTRY = new RegistryBuilder<Herb>().setName(NAME).setType(Herb.class).setIDRange(0, 0x00FFFFFF).create();
  }
}
