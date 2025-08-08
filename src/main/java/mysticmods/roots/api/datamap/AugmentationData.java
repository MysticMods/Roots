package mysticmods.roots.api.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsAPI;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.Locale;

// Used to store configurable data for the Augmentation ritual.
// Attributes must be tagged roots:augmentable in addition to having this data map entry
public record AugmentationData(Holder<Attribute> attribute, AttributeModifier.Operation operation,
                               double largeAmplifierValue, double smallAmplifierValue, float largeAmplifierChance,
                               int maxLargeAmplifiers, int maxSmallAmplifiers) {
  public static final MapCodec<AugmentationData> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(BuiltInRegistries.ATTRIBUTE.holderByNameCodec()
              .fieldOf("attribute").forGetter(AugmentationData::attribute),
          AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(AugmentationData::operation),
          Codec.DOUBLE.fieldOf("large_amplifier_value").forGetter(AugmentationData::largeAmplifierValue),
          Codec.DOUBLE.fieldOf("small_amplifier_value").forGetter(AugmentationData::smallAmplifierValue),
          Codec.FLOAT.fieldOf("large_amplifier_chance").forGetter(AugmentationData::largeAmplifierChance),
          Codec.INT.fieldOf("max_large_amplifiers").forGetter(AugmentationData::maxLargeAmplifiers),
          Codec.INT.fieldOf("max_small_amplifiers").forGetter(AugmentationData::maxSmallAmplifiers))
      .apply(instance, AugmentationData::new));
  public static final Codec<AugmentationData> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<RegistryFriendlyByteBuf, AugmentationData> STREAM_CODEC = ExtraStreamCodecs.composite(
      ByteBufCodecs.holderRegistry(Registries.ATTRIBUTE), AugmentationData::attribute,
      AttributeModifier.Operation.STREAM_CODEC, AugmentationData::operation,
      ByteBufCodecs.DOUBLE, AugmentationData::largeAmplifierValue,
      ByteBufCodecs.DOUBLE, AugmentationData::smallAmplifierValue,
      ByteBufCodecs.FLOAT, AugmentationData::largeAmplifierChance,
      ByteBufCodecs.VAR_INT, AugmentationData::maxLargeAmplifiers,
      ByteBufCodecs.VAR_INT, AugmentationData::maxSmallAmplifiers,
      AugmentationData::new);

  public enum Size {
    LARGE, SMALL;
  }

  public boolean augment (LivingEntity entity, RandomSource random) {
    var info = getCachedInfo(entity);

    if (cantAugment(info)) {
      return false;
    }

    AttributeInstance instance = entity.getAttribute(attribute());
    if (instance == null) {
      return false;
    }

    if (info.largeCount() < maxLargeAmplifiers) {
      if (random.nextFloat() < largeAmplifierChance) {
        var modifier = new AttributeModifier(generateName(Size.LARGE, info.largeCount() + 1),
            largeAmplifierValue, operation);
        instance.addPermanentModifier(modifier);
        info = new AugmentationInfo(info.smallCount(), info.largeCount()+1);
        setCachedInfo(entity, info);
        return true;
      }
    }

    if (info.smallCount() < maxSmallAmplifiers) {
      var modifier = new AttributeModifier(generateName(Size.SMALL, info.smallCount() + 1),
          smallAmplifierValue, operation);
      instance.addPermanentModifier(modifier);
      info = new AugmentationInfo(info.smallCount() + 1, info.largeCount());
      setCachedInfo(entity, info);
      return true;
    }

    return false;
  }

  private ResourceLocation generateName(Size size, int count) {
    return RootsAPI.rl(attribute().getKey().location().getPath() + "/" + size.name()
        .toLowerCase(Locale.ROOT) + "/" + count);
  }

  private void setCachedInfo (LivingEntity entity, AugmentationInfo info) {
    if (entity == null || !entity.isAlive()) {
      return;
    }

    var data = entity.getData(RootsAPI.getInstance().getAugmentationInfoType());
    data.put(attribute(), info);
  }

  public AugmentationInfo getCachedInfo(LivingEntity entity) {
    if (entity == null || !entity.isAlive()) {
      return AugmentationInfo.EMPTY;
    }

    if (!entity.hasData(RootsAPI.getInstance().getAugmentationInfoType())) {
      var result = getInfo(entity);
      entity.getData(RootsAPI.getInstance().getAugmentationInfoType()).put(attribute(), result);
      return result;
    }

    var data = entity.getData(RootsAPI.getInstance().getAugmentationInfoType());
    if (!data.isEmpty()) {
      var result = data.get(attribute());
      if (result != null) {
        return result;
      }
    }
    var result = getInfo(entity);
    data.put(attribute(), result);
    return result;
  }

  private boolean cantAugment(AugmentationInfo info) {
    return info.smallCount() >= maxSmallAmplifiers || info.largeCount() >= maxLargeAmplifiers;
  }

  private AugmentationInfo getInfo(LivingEntity entity) {
    if (entity == null || !entity.isAlive()) {
      return AugmentationInfo.EMPTY;
    }

    int smallCount = 0;
    int largeCount = 0;
    AttributeInstance instance = entity.getAttribute(attribute());
    if (instance == null) {
      return AugmentationInfo.EMPTY;
    }
    String attrName = attribute().getKey().location().getPath();
    for (AttributeModifier modifier : instance.getModifiers()) {
      if (modifier.id().getNamespace().equals(RootsAPI.MODID)) {
        String[] name = modifier.id().getPath().split("/");
        if (name.length == 3 && name[0].equals(attrName)) {
          if (name[1].equals("small")) {
            smallCount++;
          } else if (name[1].equals("large")) {
            largeCount++;
          }
        }
      }
    }

    return new AugmentationInfo(smallCount, largeCount);
  }
}
