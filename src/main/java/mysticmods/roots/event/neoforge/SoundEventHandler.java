package mysticmods.roots.event.neoforge;

import mysticmods.roots.api.RootsAPI;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;
import org.apache.commons.lang3.StringUtils;

@EventBusSubscriber(value = Dist.CLIENT, modid = RootsAPI.MODID)
public class SoundEventHandler {
  @SubscribeEvent
  public static void onSoundEvent(PlaySoundEvent event) {
    if (event.getSound() != null && "entity.generic.eat".equals(event.getSound().getLocation().getPath())) {
      RootsAPI.LOG.error(StringUtils.join(Thread.currentThread().getStackTrace(), "\n"));
    }
  }
}
