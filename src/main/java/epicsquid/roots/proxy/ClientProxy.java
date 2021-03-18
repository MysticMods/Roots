package epicsquid.roots.proxy;

import epicsquid.roots.RegistryManager;
import epicsquid.roots.Roots;
import epicsquid.roots.client.Keybinds;
import epicsquid.roots.client.PatchouliHack;
import epicsquid.roots.entity.layer.AquaBubbleRenderer;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.spell.info.SpellDustInfo;
import epicsquid.roots.spell.info.storage.DustSpellStorage;
import epicsquid.roots.tileentity.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

public class ClientProxy extends CommonProxy {
  @Override
  public void preInit(FMLPreInitializationEvent event) {
    super.preInit(event);
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMortar.class, new TileEntityMortarRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPyre.class, new TileEntityPyreRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityImbuer.class, new TileEntityImbuerRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCatalystPlate.class, new TileEntityCatalystPlateRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityIncenseBurner.class, new TileEntityIncenseBurnerRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFeyCrafter.class, new TileEntityFeyCrafterRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityImposer.class, new TileEntityImposerRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRunicCrafter.class, new TileEntityRunicCrafterRenderer());

    Keybinds.init();
  }

  @Override
  public void init(FMLInitializationEvent event) {
    super.init(event);
    RegistryManager.registerColorHandlers();
    Minecraft.getMinecraft().getRenderManager().getSkinMap().values().forEach(o -> o.addLayer(new AquaBubbleRenderer<>(o)));
  }

  @Override
  public void postInit(FMLPostInitializationEvent event) {
    super.postInit(event);
  }

  @Override
  public void loadComplete(FMLLoadCompleteEvent event) {
    super.loadComplete(event);

    PatchouliHack.init();
  }
}
