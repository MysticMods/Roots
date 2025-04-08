package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class ModDamage {
  // Rose Thorns -> Primal
  // Meteor      -> Elemental fire
  // Wildfire    -> Elemental fire
  // Acid Cloud  -> Bafflecap
  // Life Drain  -> Moonglow

  public static final ResourceKey<DamageType> ROSE_THORNS = create(RootsAPI.rl("rose_thorns"));
  public static final ResourceKey<DamageType> METEOR = create(RootsAPI.rl("meteor"));
  public static final ResourceKey<DamageType> WILDFIRE = create(RootsAPI.rl("wildfire"));
  public static final ResourceKey<DamageType> ACID_CLOUD = create(RootsAPI.rl("acid_cloud"));
  public static final ResourceKey<DamageType> LIFE_DRAIN = create(RootsAPI.rl("life_drain"));

  private static DamageSource fromEntity(ResourceKey<DamageType> type, Entity direct, Entity indirect) {
    var registry = direct.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
    return new DamageSource(registry.getHolderOrThrow(type), direct, indirect, null);
  }

  public static DamageSource roseThorns(Entity direct, Entity indirect) {
    return fromEntity(ROSE_THORNS, direct, indirect);
  }

  public static DamageSource wildfire(Entity direct, Entity indirect) {
    return fromEntity(WILDFIRE, direct, indirect);
  }

  public static DamageSource acidCloud(Entity direct) {
    return fromEntity(ACID_CLOUD, direct, null);
  }

  public static DamageSource lifeDrain(Entity direct) {
    return fromEntity(LIFE_DRAIN, direct, null);
  }

  private static DamageSource meteor = null;

  public static DamageSource meteor(Level level) {
    if (meteor == null) {
      meteor = new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
          .getHolderOrThrow(METEOR), null, null, null);
    }
    return meteor;
  }

  private static ResourceKey<DamageType> create(ResourceLocation id) {
    return ResourceKey.create(Registries.DAMAGE_TYPE, id);
  }
}
