package mysticmods.roots.api.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class LightDrifterStorage {
  public static final Codec<LightDrifterStorage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      UUIDUtil.CODEC.optionalFieldOf("id").forGetter(o -> Optional.ofNullable(o.id)),
      Codec.INT.fieldOf("entityId").forGetter(LightDrifterStorage::entityId)
  ).apply(instance, LightDrifterStorage::new));

  private UUID id;
  private int entityId;

  public LightDrifterStorage() {
    this.id = null;
    this.entityId = -1;
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  private LightDrifterStorage(Optional<UUID> uuid, int entityId) {
    this.id = uuid.orElse(null);
    this.entityId = entityId;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setEntityId(int entityId) {
    this.entityId = entityId;
  }

  @Nullable
  public UUID id() {
    return id;
  }

  public int entityId() {
    return entityId;
  }
}
