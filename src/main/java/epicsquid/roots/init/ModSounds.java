/*package epicsquid.roots.init;

import epicsquid.mysticallib.event.RegisterContentEvent;
import epicsquid.mysticalworld.MysticalWorld;
import epicsquid.roots.Roots;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import javax.annotation.Nonnull;

public class ModSounds {
  public static SoundEvent CHIMES;

  public static void initSounds(@Nonnull RegisterContentEvent event) {
    event.addSound(CHIMES = createSoundEvent(new ResourceLocation(Roots.MODID, "chimes")));
  }

  public static SoundEvent createSoundEvent(ResourceLocation name) {
    SoundEvent result = new SoundEvent(name);
    result.setRegistryName(name);
    return result;
  }
}*/
