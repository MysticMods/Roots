package epicsquid.roots;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.event.RegisterContentEvent;
import epicsquid.mysticallib.event.RegisterModRecipesEvent;
import epicsquid.mysticallib.event.RegisterParticleEvent;
import epicsquid.mysticallib.event.RegisterWorldGenEvent;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModEntities;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModParticles;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.item.ItemStaff;
import epicsquid.roots.network.PacketHandler;
import epicsquid.roots.world.WorldGenBarrow;
import epicsquid.roots.world.WorldGenHut;
import epicsquid.roots.world.WorldGenNaturalGrove;
import epicsquid.roots.world.WorldGenWildlandGrove;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RegistryManager {

  @SubscribeEvent
  public void init(@Nonnull RegisterContentEvent event) {
    LibRegistry.setActiveMod(Roots.MODID, Roots.CONTAINER);

    ModBlocks.registerBlocks(event);

    ModItems.registerItems(event);

    ModEntities.registerMobs();
    ModEntities.registerMobSpawn();
    PacketHandler.registerMessages();
  }

  @SubscribeEvent
  public void initRecipes(@Nonnull RegisterModRecipesEvent event) {
    LibRegistry.setActiveMod(Roots.MODID, Roots.CONTAINER);

    ModRecipes.initRecipes(event);
  }

  @SubscribeEvent
  public void worldGenInit(RegisterWorldGenEvent event) {
    LibRegistry.setActiveMod(Roots.MODID, Roots.CONTAINER);

    GameRegistry.registerWorldGenerator(new WorldGenBarrow(), 100);
    GameRegistry.registerWorldGenerator(new WorldGenHut(), 101);
    GameRegistry.registerWorldGenerator(new WorldGenWildlandGrove(), 102);
    GameRegistry.registerWorldGenerator(new WorldGenNaturalGrove(), 103);
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onRegisterCustomModels(@Nonnull RegisterParticleEvent event) {
    ModParticles.init();
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void registerOredict(@Nonnull RegistryEvent.Register<Item> event) {
    LibRegistry.setActiveMod(Roots.MODID, Roots.CONTAINER);

    ModItems.registerOredict();
  }

  @SideOnly(Side.CLIENT)
  public static void registerColorHandlers() {
    Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemStaff.StaffColorHandler(), ModItems.staff);
  }

}
