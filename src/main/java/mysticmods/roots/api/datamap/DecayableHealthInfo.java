package mysticmods.roots.api.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.ArrayList;
import java.util.List;

public record DecayableHealthInfo(ResourceLocation baseIdentifier, List<ResourceLocation> calculatedIdentifiers,
                                  double healthReduction, int maxApplied) {
  public static final MapCodec<DecayableHealthInfo> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(ResourceLocation.CODEC.fieldOf("baseIdentifier")
          .forGetter(DecayableHealthInfo::baseIdentifier),
      ResourceLocation.CODEC.listOf().fieldOf("calculatedIdentifiers")
          .forGetter(DecayableHealthInfo::calculatedIdentifiers),
      Codec.DOUBLE.fieldOf("healthReduction")
          .forGetter(DecayableHealthInfo::healthReduction), Codec.INT.fieldOf("maxApplied")
          .forGetter(DecayableHealthInfo::maxApplied)).apply(instance, DecayableHealthInfo::new));
  public static final Codec<DecayableHealthInfo> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<ByteBuf, DecayableHealthInfo> STREAM_CODEC = StreamCodec.composite(
      ResourceLocation.STREAM_CODEC, DecayableHealthInfo::baseIdentifier,
      ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()), DecayableHealthInfo::calculatedIdentifiers,
      ByteBufCodecs.DOUBLE, DecayableHealthInfo::healthReduction,
      ByteBufCodecs.VAR_INT, DecayableHealthInfo::maxApplied,
      DecayableHealthInfo::new
  );

  public DecayableHealthInfo(ResourceLocation baseIdentifier,
                             double healthReduction, int maxApplied) {
    this(baseIdentifier, calculateIdentifiers(baseIdentifier, maxApplied), healthReduction, maxApplied);
  }

  private static List<ResourceLocation> calculateIdentifiers(ResourceLocation baseIdentifier, int maxApplied) {
    List<ResourceLocation> temp = new ArrayList<>();
    for (int i = 0; i < maxApplied; i++) {
      temp.add(baseIdentifier.withSuffix("/" + (i + 1)));
    }
    return temp;
  }

  public boolean apply(LivingEntity attacker, LivingEntity entity) {
    var attribute = entity.getAttribute(Attributes.MAX_HEALTH);

    if (attribute == null) {
      throw new IllegalStateException("Cannot count MAX_HEALTH attributes as entity '" + entity + "' doesn't have a maximum health attribute!");
    }

    ResourceLocation toApply = null;

    for (ResourceLocation calculated : calculatedIdentifiers) {
      if (!attribute.hasModifier(calculated)) {
        toApply = calculated;
        break;
      }
    }

    if (toApply == null) {
      return false;
    }

    var reduct = healthReduction > 0 ? -healthReduction : healthReduction;

    AttributeModifier modifier = new AttributeModifier(toApply, reduct, AttributeModifier.Operation.ADD_VALUE);

    var currentHealth = entity.getHealth();
    var maximumHealth = entity.getMaxHealth();
    boolean adjustHealth = false;
    if (currentHealth < maximumHealth) {
      currentHealth += (float) reduct;
      adjustHealth = true;
    }

    attribute.addPermanentModifier(modifier);
    entity.refreshDirtyAttributes();
    if (adjustHealth) {
      entity.setHealth(currentHealth);
    }

    entity.setLastHurtByMob(attacker);

    return true;
  }
}
