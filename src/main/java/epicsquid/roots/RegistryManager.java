package epicsquid.roots;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.event.RegisterContentEvent;
import epicsquid.mysticallib.event.RegisterModRecipesEvent;
import epicsquid.mysticallib.event.RegisterParticleEvent;
import epicsquid.mysticallib.event.RegisterWorldGenEvent;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.init.*;
import epicsquid.roots.item.ItemLifeEssence;
import epicsquid.roots.item.ItemStaff;
import epicsquid.roots.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityList;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.RegistryEvent;
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

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public static void onRegisterCustomModels(@Nonnull RegisterParticleEvent event) {
    ModParticles.init();
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public static void registerOredict(@Nonnull RegistryEvent.Register<Item> event) {
    LibRegistry.setActiveMod(Roots.MODID, Roots.CONTAINER);

    ModItems.registerOredict();
  }

  @SideOnly(Side.CLIENT)
  public static void registerColorHandlers() {
    LibRegistry.setActiveMod(Roots.MODID, Roots.CONTAINER);

    Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemStaff.StaffColorHandler(), ModItems.staff);
    Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) -> {
      EntityList.EntityEggInfo info = EntityList.ENTITY_EGGS.get(((ItemLifeEssence) ModItems.life_essence).getEntityID(stack));

      if (info == null) {
        return -1;
      } else {
        return tintIndex == 0 ? info.primaryColor : info.secondaryColor;
      }
    }, ModItems.life_essence);
    Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) -> {
      if (tintIndex == 0) {
        NBTTagCompound tag = stack.getTagCompound();
        if (stack.hasTagCompound() && tag != null && tag.hasKey("color", Constants.NBT.TAG_INT)) {
          return EnumDyeColor.byMetadata(tag.getInteger("color")).getColorValue();
        } else {
          return EnumDyeColor.BROWN.getColorValue();
        }
      } else {
        return 0xFFFFFF;
      }
    }, ModItems.component_pouch, ModItems.apothecary_pouch);
  }

  @SubscribeEvent
  public static void registerPotions(RegistryEvent.Register<Potion> event) {
    LibRegistry.setActiveMod(Roots.MODID, Roots.CONTAINER);

    ModPotions.registerPotions(event);
  }
}
