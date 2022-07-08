package mysticmods.roots.capability;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=RootsAPI.MODID)
public class Capabilities {
  public static final ResourceLocation HERB_CAPABILITY_ID = new ResourceLocation(RootsAPI.MODID, "herb_capability");

  public static final Capability<HerbCapability> HERB_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
  });

  @SubscribeEvent
  public static void attachCapability (AttachCapabilitiesEvent<Entity> event) {
    if (event.getObject().getType() == EntityType.PLAYER) {
      event.addCapability(HERB_CAPABILITY_ID, new HerbCapability());
    }
  }
}
