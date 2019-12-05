package epicsquid.roots;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.event.RegisterContentEvent;
import epicsquid.mysticallib.event.RegisterModRecipesEvent;
import epicsquid.mysticallib.event.RegisterParticleEvent;
import epicsquid.mysticallib.event.RegisterWorldGenEvent;
import epicsquid.roots.init.*;
import epicsquid.roots.item.StaffItem;
import epicsquid.roots.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class RegistryManager {

  @SubscribeEvent
  public static void init(@Nonnull RegisterContentEvent event) {
    LibRegistry.setActiveMod(Roots.MODID, Roots.CONTAINER);

    ModBlocks.registerBlocks(event);
    ModSounds.initSounds(event);
    ModItems.registerItems(event);

    ModEntities.registerMobs();
    PacketHandler.registerMessages();
  }

  @SubscribeEvent
  public static void initRecipes(@Nonnull RegisterModRecipesEvent event) {
    LibRegistry.setActiveMod(Roots.MODID, Roots.CONTAINER);

    ModItems.registerOredict();
    ModRecipes.initRecipes(event);
  }

  @SubscribeEvent
  public static void worldGenInit(RegisterWorldGenEvent event) {
    LibRegistry.setActiveMod(Roots.MODID, Roots.CONTAINER);

    /*GameRegistry.registerWorldGenerator(new WorldGenWildlandGrove(), 102);
    GameRegistry.registerWorldGenerator(new WorldGenNaturalGrove(), 103);*/
  }

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public static void onRegisterCustomModels(@Nonnull RegisterParticleEvent event) {
    ModParticles.init();
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public static void registerOredict(@Nonnull RegistryEvent.Register<Item> event) {
    LibRegistry.setActiveMod(Roots.MODID, Roots.CONTAINER);

    ModItems.registerOredict();
  }

  @OnlyIn(Dist.CLIENT)
  public static void registerColorHandlers() {
    LibRegistry.setActiveMod(Roots.MODID, Roots.CONTAINER);

    Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new StaffItem.StaffColorHandler(), ModItems.staff);
  }

  @SubscribeEvent
  public static void registerPotions(RegistryEvent.Register<Effect> event) {
    LibRegistry.setActiveMod(Roots.MODID, Roots.CONTAINER);

    ModPotions.registerPotions(event);
  }
}
