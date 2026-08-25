package mysticmods.roots.ritual;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.util.EntityUtils;
import mysticmods.roots.util.PositionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class ContainmentRitual extends Ritual {
  @Override
  protected void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, @Nullable PositionCache pCache, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {
    // TODO: Look at mob::isWithinRestriction
    if (pCache == null) {
      RootsAPI.LOG.error("Ritual {} requires a PositionCache but none was provided. This will cause the ritual to not function correctly.", getOrCreateDescriptionId());
      return;
    }
    AABB radius = AABB.of(pCache.getBoundingBox().inflatedBy(getRadiusXZ()));
    // TODO: Handle teams?
    var lastPlayer = blockEntity.getLastPlayer();
    Predicate<Entity> test = lastPlayer != null ? EntityUtils.isHostileTo(lastPlayer) : EntityUtils.isHostile();
    List<Entity> containmentEntities = pLevel.getEntities(blockEntity.getLastPlayer(), radius, test);
    for (Entity entity : containmentEntities) {

    }
  }

  @Nullable
  public static GlobalPos canMoveFromTetherBounds(Entity entity, Vec3 newPosition) {
    if (!entity.hasData(ModAttachments.CONTAINMENT_TETHER)) {
      return null;
    }
    Level level = entity.level();
    GlobalPos tether = entity.getData(ModAttachments.CONTAINMENT_TETHER);
    if (!level.dimension().equals(tether.dimension())) {
      entity.removeData(ModAttachments.CONTAINMENT_TETHER);
      return null;
    }

    BlockEntity blockEntity = level.getBlockEntity(tether.pos());
    if (!(blockEntity instanceof PyreBlockEntity pyreBlockEntity)) {
      entity.removeData(ModAttachments.CONTAINMENT_TETHER);
      return null;
    }

    var ritual = pyreBlockEntity.getCurrentRitual();

    if (ritual == null) {
      entity.removeData(ModAttachments.CONTAINMENT_TETHER);
      return null;
    }

    if (!ritual.is(ModRituals.CONTAINMENT.getKey())) {
      entity.removeData(ModAttachments.CONTAINMENT_TETHER);
      return null;
    }

    return tether;
  }


  @Override
  protected void initialize(Holder<Ritual> holder) {

  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getDurationProperty() {
    return ModRituals.CONTAINMENT_DURATION;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusXZProperty() {
    return null;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return null;
  }

  @Override
  protected @Nullable PropertyHolder<Property.IntegerProperty> getRadiusProperty() {
    return ModRituals.CONTAINMENT_RADIUS;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getIntervalProperty() {
    return ModRituals.CONTAINMENT_INTERVAL;
  }
}
