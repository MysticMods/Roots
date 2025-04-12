package mysticmods.roots.api.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.RootsAPI;

public class RitualInformation {
  public static final MapCodec<RitualInformation> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.BOOL.fieldOf("heavy_storms_active").forGetter(o -> o.heavyStormsActive),
      Codec.BOOL.fieldOf("protection_active").forGetter(o -> o.protectionActive)).apply(instance, RitualInformation::new));
  public static final Codec<RitualInformation> CODEC = MAP_CODEC.codec();

  private boolean heavyStormsActive = false;
  private boolean protectionActive = false;

  public RitualInformation() {
  }

  public RitualInformation(boolean heavyStormsActive, boolean protectionActive) {
    this.heavyStormsActive = heavyStormsActive;
    this.protectionActive = protectionActive;
  }

  public void startHeavyStorms () {
    this.heavyStormsActive = true;
  }

  public void stopHeavyStorms () {
    this.heavyStormsActive = false;
  }

  public void startProtection () {
    this.protectionActive = true;
  }

  public void stopProtection () {
    this.protectionActive = false;
  }

  public boolean shouldStopWeather () {
    return switch (RootsAPI.getInstance().getRitualResolutionType()) {
      case AGE_PRIORITY, STORM_PRIORITY -> !this.heavyStormsActive;
      case PROTECTION_PRIORITY -> !this.protectionActive;
    };
  }

  public boolean shouldStartWeather () {
    return switch (RootsAPI.getInstance().getRitualResolutionType()) {
      case AGE_PRIORITY, PROTECTION_PRIORITY -> !this.protectionActive;
      case STORM_PRIORITY -> !this.heavyStormsActive;
    };
  }

  public enum RitualResolutionType {
    STORM_PRIORITY,
    PROTECTION_PRIORITY,
    AGE_PRIORITY
  }
}
