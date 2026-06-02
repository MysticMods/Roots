package mysticmods.roots.api.recipe;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.RootsAPI;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class ComplexEntityType {
  public static final ComplexEntityType EMPTY = new ComplexEntityType(EntityType.EVOKER_FANGS, null);

  public static final Codec<ComplexEntityType> CODEC = RecordCodecBuilder.create(o -> o.group(
          BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("type").forGetter(ComplexEntityType::type),
          CompoundTag.CODEC.optionalFieldOf("tag").forGetter(c -> Optional.ofNullable(c.tag())))
      .apply(o, (a, b) -> new ComplexEntityType(a, b.orElse(null))));
  public static final StreamCodec<RegistryFriendlyByteBuf, ComplexEntityType> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.registry(Registries.ENTITY_TYPE), ComplexEntityType::type,
      ByteBufCodecs.optional(ByteBufCodecs.COMPOUND_TAG), c -> Optional.ofNullable(c.tag()),
      (a, b) -> new ComplexEntityType(a, b.orElse(null))
  );

  private final EntityType<?> type;
  @Nullable
  private final CompoundTag tag;
  private WeakReference<Entity> cachedEntity;
  private boolean cachedNull = false;

  public ComplexEntityType(EntityType<?> type, @Nullable CompoundTag tag) {
    this.type = type;
    this.tag = tag;
  }

  public EntityType<?> type() {
    return type;
  }

  public CompoundTag tag() {
    return tag;
  }

  @SuppressWarnings("unchecked")
  @Nullable
  public <T extends Entity> Entity create(ServerLevel level, @Nullable Consumer<T> consumer, BlockPos pos, MobSpawnType spawnType, boolean shouldOffsetY, boolean shouldOffsetYMore) {
    T result = ((EntityType<T>) type).create(level, consumer, pos, spawnType, shouldOffsetY, shouldOffsetYMore);
    if (result != null && tag != null) {
      tryFillEntity(result, tag);
    }
    return result;
  }

  @Nullable
  public Entity cachedEntity(Level level) {
    if (cachedNull) {
      return null;
    }
    if (cachedEntity == null || cachedEntity.get() == null) {
      // TODO: Check for the armadillo stuff here (#1150)
      Entity entity = type.create(level);
      if (entity == null) {
        cachedNull = true;
        return null;
      }
      if (tag != null) {
        if (!tryFillEntity(entity, tag)) {
          cachedNull = true;
          return null;
        }
      }
      cachedEntity = new WeakReference<>(entity);
    }

    return cachedEntity.get();
  }

  public boolean isEmpty() {
    return this.equals(EMPTY);
  }

  @Override
  public final boolean equals(Object o) {
    if (!(o instanceof ComplexEntityType that)) return false;

    return type.equals(that.type) && Objects.equals(tag, that.tag);
  }

  @Override
  public int hashCode() {
    int result = type.hashCode();
    result = 31 * result + Objects.hashCode(tag);
    return result;
  }

  private static boolean tryFillEntity(Entity entity, @Nullable CompoundTag tag) {
    if (tag != null) {
      try {
        RootsAPI.getInstance().readAdditionalSavedData(entity, tag);
      } catch (Throwable throwable) {
        CrashReport crashreport = CrashReport.forThrowable(throwable, "Creating cached entity");
        CrashReportCategory crashreportcategory = crashreport.addCategory("Entity being created for ComplexEntityType");
        entity.fillCrashReportCategory(crashreportcategory);
        LogUtils.getLogger().error("{}", crashreport.getFriendlyReport(ReportType.CRASH));
        return false;
      }
    }

    return true;
  }
}
