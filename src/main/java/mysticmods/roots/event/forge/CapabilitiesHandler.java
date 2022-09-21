package mysticmods.roots.event.forge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.capability.GrantCapability;
import mysticmods.roots.api.capability.HerbCapability;
import mysticmods.roots.api.capability.SnapshotCapability;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RootsAPI.MODID)
public class CapabilitiesHandler {
  @SubscribeEvent
  public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
    if (event.getObject().getType() == EntityType.PLAYER) {
      event.addCapability(RootsAPI.HERB_CAPABILITY_ID, new HerbCapability());
      event.addCapability(RootsAPI.GRANT_CAPABILITY_ID, new GrantCapability());
      event.addCapability(RootsAPI.SNAPSHOT_CAPABILITY_ID, new SnapshotCapability());
    }
  }
}
