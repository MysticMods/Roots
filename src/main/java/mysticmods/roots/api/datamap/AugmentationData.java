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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.Locale;

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

  private ResourceLocation generateName(Size size, int count) {
    return RootsAPI.rl(attribute().getKey().location().getPath() + "/" + size.name()
        .toLowerCase(Locale.ROOT) + "/" + count);
  }

  public Info getInfo(LivingEntity entity) {
    int smallCount = 0;
    int largeCount = 0;
    AttributeInstance instance = entity.getAttribute(attribute());
    if (instance == null) {
      return Info.EMPTY;
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
    return new Info(smallCount, largeCount);
  }

  public record Info(int smallCount, int largeCount) {
    public static Info EMPTY = new Info(0, 0);

    public boolean isEmpty() {
      return smallCount == 0 && largeCount == 0;
    }
  }
}
