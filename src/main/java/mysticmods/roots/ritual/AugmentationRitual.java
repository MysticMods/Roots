package mysticmods.roots.ritual;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.util.PositionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.w3c.dom.Attr;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class AugmentationRitual extends Ritual {
  private static final Map<EntityType<?>, Set<Holder<Attribute>>> ELIGIBLE_ATTRIBUTES = new HashMap<>();
  private int count, glowDuration;

  private static Set<Holder<Attribute>> getAttributes (EntityType<?> entity) {
    //noinspection deprecation
    var e = entity.builtInRegistryHolder();

    var data = e.getData(DataMaps.ENTITY_AUGMENTATION_DATA);
    if (data == null) {
      return Collections.emptySet();
    }

    return new HashSet<>(data);
  }

  @Override
  public void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, @Nullable PositionCache pCache, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {
    if (pCache == null && requiresCache()) {
      RootsAPI.LOG.error("Ritual {} requires a PositionCache but none was provided. This will cause the ritual to not function correctly.", getOrCreateDescriptionId());
      return;
    }

    HolderSet<Attribute> attributes = BuiltInRegistries.ATTRIBUTE.getTag(RootsTags.Atrtibutes.AUGMENTABLE).orElse(null); // All attributes must be included in the tag but that's not the only attributes that are considered
    if (attributes == null) {
      RootsAPI.LOG.error("Ritual {} requires attributes from the AUGMENTABLE tag but none were found. This will cause the ritual to not function correctly.", getOrCreateDescriptionId());
      return;
    }

    if (duration % getInterval() == 0) {
      List<LivingEntity> entities = blockEntity.getLevel()
          .getEntitiesOfClass(LivingEntity.class, getAABB().move(blockEntity.getBlockPos()), EntitySelector.NO_SPECTATORS.and(Entity::isAlive)
              .and((o) -> o.getType().is(RootsTags.Entities.AUGMENTABLE) && !o.getType()
                  .is(RootsTags.Entities.AUGMENTABLE_EXCLUDE)));
      if (entities.isEmpty()) {
        return;
      }
      int adjusted = 0;
      // TODO: Start the loop over again to handle the count
      outer:
      for (LivingEntity entity : entities) {
        if (adjusted >= count) {
          break;
        }

        Set<Holder<Attribute>> eligible = ELIGIBLE_ATTRIBUTES.get(entity.getType());
        if (eligible == null) {
          eligible = getAttributes(entity.getType());
          ELIGIBLE_ATTRIBUTES.put(entity.getType(), eligible);
        }

        List<Holder<Attribute>> attributes1 = attributes.stream().filter(eligible::contains).collect(Collectors.toList());

        while (!attributes1.isEmpty()) {
          Holder<Attribute> attribute = attributes1.remove(randomSource.nextInt(attributes1.size()));
          var data = attribute.getData(DataMaps.AUGMENTATION_DATA);
          if (data == null) {
            RootsAPI.LOG.error("Ritual {} requires augmentation data for attribute {} but none was found. This will cause the ritual to not function correctly.", getOrCreateDescriptionId(), attribute);
            continue;
          }

          if (data.augment(entity, randomSource)) {
            adjusted++;
            if (glowDuration > 0) {
              entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, glowDuration, 0, false, false));
            }
            continue outer;
          }
        }
      }
    }
  }

  @Override
  public void animationTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {

  }

  @Override
  public void buildProperties(List<PropertyHolder<?>> properties) {
    super.buildProperties(properties);
    properties.add(ModRituals.AUGMENTATION_COUNT);
    properties.add(ModRituals.AUGMENTATION_GLOW_DURATION);
  }

  @Override
  public void initialize(Holder<Ritual> holder) {
    PropertyDataMap properties = holder.getData(DataMaps.RITUAL_PROPERTY_DATA);
    glowDuration = properties.get(ModRituals.AUGMENTATION_GLOW_DURATION);
    count = properties.get(ModRituals.AUGMENTATION_COUNT);
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getDurationProperty() {
    return ModRituals.AUGMENTATION_DURATION;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusXZProperty() {
    return ModRituals.AUGMENTATION_RADIUS_XZ;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModRituals.AUGMENTATION_RADIUS_Y;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getIntervalProperty() {
    return ModRituals.AUGMENTATION_INTERVAL;
  }
}
