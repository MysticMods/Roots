package mysticmods.roots.entity;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

public class SylvanSpiderEntity extends Spider {
  public SylvanSpiderEntity(EntityType<? extends Spider> entityType, Level level) {
    super(entityType, level);
  }

  @Nullable
  @Override
  public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
    return spawnGroupData;
  }

  public static AttributeSupplier.Builder createSylvanSpider() {
    return Spider.createAttributes().add(Attributes.MAX_HEALTH, 12.0);
  }
}
