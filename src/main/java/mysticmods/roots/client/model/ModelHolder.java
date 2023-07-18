package mysticmods.roots.client.model;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModelHolder {
  public static final ModelLayerLocation BEETLE = new ModelLayerLocation(new ResourceLocation(RootsAPI.MODID, "beetle"), "main");
  public static final ModelLayerLocation DEER = new ModelLayerLocation(new ResourceLocation(RootsAPI.MODID, "deer"), "main");
  public static final ModelLayerLocation DUCK = new ModelLayerLocation(new ResourceLocation(RootsAPI.MODID, "duck"), "main");
  public static final ModelLayerLocation HELL_SPROUT = new ModelLayerLocation(new ResourceLocation(RootsAPI.MODID, "hell_sprout"), "main");
  public static final ModelLayerLocation OWL = new ModelLayerLocation(new ResourceLocation(RootsAPI.MODID, "owl"), "main");
  public static final ModelLayerLocation FENNEC = new ModelLayerLocation(new ResourceLocation(RootsAPI.MODID, "fennec"), "main");
  public static final ModelLayerLocation SPROUT = new ModelLayerLocation(new ResourceLocation(RootsAPI.MODID, "sprout"), "main");
  public static final ModelLayerLocation BEETLE_ARMOR = new ModelLayerLocation(new ResourceLocation(RootsAPI.MODID, "beetle_armor"), "main");
  public static final ModelLayerLocation ANTLER_ARMOR = new ModelLayerLocation(new ResourceLocation(RootsAPI.MODID, "antler_armor"), "main");

  public static void init() {
  }
}
