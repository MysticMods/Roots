package mysticmods.roots.client.model;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModelHolder {
  public static final ModelLayerLocation BEETLE = new ModelLayerLocation(RootsAPI.rl("beetle"), "main");
  public static final ModelLayerLocation DEER = new ModelLayerLocation(RootsAPI.rl("deer"), "main");
  public static final ModelLayerLocation DUCK = new ModelLayerLocation(RootsAPI.rl("duck"), "main");
  public static final ModelLayerLocation HELL_SPROUT = new ModelLayerLocation(RootsAPI.rl("hell_sprout"), "main");
  public static final ModelLayerLocation OWL = new ModelLayerLocation(RootsAPI.rl("owl"), "main");
  public static final ModelLayerLocation FENNEC = new ModelLayerLocation(RootsAPI.rl("fennec"), "main");
  public static final ModelLayerLocation SPROUT = new ModelLayerLocation(RootsAPI.rl("sprout"), "main");
  public static final ModelLayerLocation BEETLE_ARMOR = new ModelLayerLocation(RootsAPI.rl("beetle_armor"), "main");
  public static final ModelLayerLocation ANTLER_ARMOR = new ModelLayerLocation(RootsAPI.rl("antler_armor"), "main");

  public static void init() {
  }
}
