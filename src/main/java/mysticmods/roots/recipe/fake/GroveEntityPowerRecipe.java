package mysticmods.roots.recipe.fake;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.power.generator.SimpleEntityGenerator;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public record GroveEntityPowerRecipe(ValidatedTagKey tag, Grove groveTag, int power, int amount) {
  public static final Codec<GroveEntityPowerRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      ValidatedTagKey.CODEC.fieldOf("block_tag").forGetter(GroveEntityPowerRecipe::tag),
      RootsRegistries.GROVES.byNameCodec().fieldOf("grove_tag").forGetter(GroveEntityPowerRecipe::groveTag),
      Codec.INT.fieldOf("power").forGetter(GroveEntityPowerRecipe::power),
      Codec.INT.fieldOf("amount").forGetter(GroveEntityPowerRecipe::amount)
  ).apply(instance, GroveEntityPowerRecipe::new));
  public static final StreamCodec<RegistryFriendlyByteBuf, GroveEntityPowerRecipe> STREAM_CODEC = StreamCodec.composite(ValidatedTagKey.STREAM_CODEC, GroveEntityPowerRecipe::tag, ByteBufCodecs.registry(RootsRegistries.Keys.GROVES), GroveEntityPowerRecipe::groveTag, ByteBufCodecs.VAR_INT, GroveEntityPowerRecipe::power, ByteBufCodecs.VAR_INT, GroveEntityPowerRecipe::amount, GroveEntityPowerRecipe::new);

  public GroveEntityPowerRecipe(TagKey<EntityType<?>> entityTag, Grove groveTag, int power, int amount) {
    this(ValidatedTagKey.create(entityTag), groveTag, power, amount);
  }

  public TagKey<EntityType<?>> entityTag () {
    return tag.entityTag();
  }

  public Ingredient itemIngredient() {
    return tag.itemIngredient();
  }

  // TODO: COncurrent hash map
  public static class ValidatedTagKey {
    private static final Interner<ValidatedTagKey> VALUES = Interners.newWeakInterner();

    public static final Codec<ValidatedTagKey> CODEC = TagKey.codec(Registries.ENTITY_TYPE)
        .xmap(ValidatedTagKey::new, ValidatedTagKey::entityTag);
    public static final StreamCodec<ByteBuf, ValidatedTagKey> STREAM_CODEC = ExtraStreamCodecs.ENTITY_TAG_STREAM_CODEC.map(ValidatedTagKey::new, ValidatedTagKey::entityTag);

    private final TagKey<EntityType<?>> entityTag;
    private Ingredient itemIngredient = null;

    protected ValidatedTagKey(TagKey<EntityType<?>> tag) {
      this.entityTag = tag;
    }

    public TagKey<EntityType<?>> entityTag() {
      return entityTag;
    }

    public Ingredient itemIngredient() {
      if (itemIngredient == null) {
        List<Item> items = new ArrayList<>();
        for (Holder<EntityType<?>> type : BuiltInRegistries.ENTITY_TYPE.getTagOrEmpty(entityTag())) {
          var egg = SpawnEggItem.byId(type.value());
          if (egg != null) {
            items.add(egg);
          }
        }
        this.itemIngredient = Ingredient.of(items.toArray(Item[]::new));
      }
      return itemIngredient;
    }

    public static ValidatedTagKey create(TagKey<EntityType<?>> entityTag) {
      return VALUES.intern(new ValidatedTagKey(entityTag));
    }
  }

  public static List<GroveEntityPowerRecipe> generate() {
    List<GroveEntityPowerRecipe> result = new ArrayList<>();

    RootsRegistries.GROVES.holders().forEach(o -> {
      var generators = o.getData(DataMaps.GROVE_ENTITY_GENERATION_ENTRIES);
      if (generators == null) {
        return;
      }
      for (var gen : generators) {
        var tag = BuiltInRegistries.ENTITY_TYPE.getTag(gen.tag()).flatMap(p -> p.stream().findFirst()).orElse(null);
        if (tag == null) {
          continue;
        }

        var generator = tag.getData(DataMaps.GROVE_ENTITY_POWER_GENERATORS);
        if (generator == null) {
          continue;
        }

        int max = gen.maxCount();

        for (SimpleEntityGenerator g : generator) {
          result.add(new GroveEntityPowerRecipe(gen.tag(), o.value(), g.value(), max));
        }
      }
    });

    return result;
  }
}
