package epicsquid.roots;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.event.RegisterContentEvent;
import epicsquid.mysticallib.event.RegisterModRecipesEvent;
import epicsquid.mysticallib.event.RegisterParticleEvent;
import epicsquid.mysticallib.event.RegisterWorldGenEvent;
import epicsquid.roots.effect.PotionFreeze;
import epicsquid.roots.init.*;
import epicsquid.roots.item.ItemStaff;
import epicsquid.roots.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class RegistryManager {

  public static Potion freeze;

  @SubscribeEvent
  public void init(@Nonnull RegisterContentEvent event) {
    LibRegistry.setActiveMod(Roots.MODID, Roots.CONTAINER);

    ModBlocks.registerBlocks(event);
    ModSounds.initSounds(event);
    ModItems.registerItems(event);

    ModEntities.registerMobs();
    ModEntities.registerMobSpawn();
    PacketHandler.registerMessages();
  }

  @SubscribeEvent
  public void initRecipes(@Nonnull RegisterModRecipesEvent event) {
    LibRegistry.setActiveMod(Roots.MODID, Roots.CONTAINER);

    ModItems.registerOredict();
    ModRecipes.initRecipes(event);
  }

  @SubscribeEvent
  public void worldGenInit(RegisterWorldGenEvent event) {
    LibRegistry.setActiveMod(Roots.MODID, Roots.CONTAINER);

    /*GameRegistry.registerWorldGenerator(new WorldGenWildlandGrove(), 102);
    GameRegistry.registerWorldGenerator(new WorldGenNaturalGrove(), 103);*/
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

  @SubscribeEvent
  public void registerPotions(RegistryEvent.Register<Potion> event) {
    event.getRegistry().register(freeze = new PotionFreeze(0xFFFFFF).setRegistryName("freeze"));
  }

}
