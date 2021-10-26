/*
package epicsquid.roots;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.event.RegisterContentEvent;
import epicsquid.mysticallib.event.RegisterModRecipesEvent;
import epicsquid.mysticallib.event.RegisterParticleEvent;
import epicsquid.roots.init.*;
import epicsquid.roots.item.ItemLifeEssence;
import epicsquid.roots.item.ItemStaff;
import epicsquid.roots.network.RootsPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityList;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.OnlyIn;

import javax.annotation.Nonnull;

public class RegistryManager {

  @SubscribeEvent
  public static void init(@Nonnull RegisterContentEvent event) {
    LibRegistry.setActiveMod(Roots.MODID, Roots.CONTAINER);

    ModBlocks.registerBlocks(event);
    ModSounds.initSounds(event);
    ModItems.registerItems(event);

    ModEntities.registerMobs();
    RootsPacketHandler.registerMessages();
  }

  @SubscribeEvent
  public static void initRecipes(@Nonnull RegisterModRecipesEvent event) {
    LibRegistry.setActiveMod(Roots.MODID, Roots.CONTAINER);

    ModItems.registerOredict();
    ModRecipes.initRecipes(event);
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
        CompoundNBT tag = stack.getTagCompound();
        if (stack.hasTagCompound() && tag != null && tag.contains("color", Constants.NBT.TAG_INT)) {
          return DyeColor.byMetadata(tag.getInt("color")).getColorValue();
        } else {
          return DyeColor.BROWN.getColorValue();
        }
      } else {
        return 0xFFFFFF;
      }
    }, ModItems.component_pouch, ModItems.apothecary_pouch, ModItems.herb_pouch);
  }

  @SubscribeEvent
  public static void registerPotions(RegistryEvent.Register<Effect> event) {
    LibRegistry.setActiveMod(Roots.MODID, Roots.CONTAINER);

    ModPotions.registerPotions(event);
  }
}
*/
