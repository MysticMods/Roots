package mysticmods.roots.recipe.fake;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.Symmetry;
import mysticmods.roots.api.grove.power.generator.SimpleEntityGenerator;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

// TODO:
public record GroveEntityPowerRecipe(ValidatedTagKey tag, Grove groveTag, int power,
                                     Symmetry symmetry, int amount) {
  public static final Codec<GroveEntityPowerRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      ValidatedTagKey.CODEC.fieldOf("block_tag").forGetter(GroveEntityPowerRecipe::tag),
      RootsRegistries.GROVES.byNameCodec().fieldOf("grove_tag").forGetter(GroveEntityPowerRecipe::groveTag),
      Codec.INT.fieldOf("power").forGetter(GroveEntityPowerRecipe::power),
      Symmetry.CODEC.fieldOf("symmetry").forGetter(GroveEntityPowerRecipe::symmetry),
      Codec.INT.fieldOf("amount").forGetter(GroveEntityPowerRecipe::amount)
  ).apply(instance, GroveEntityPowerRecipe::new));
  public static final StreamCodec<RegistryFriendlyByteBuf, GroveEntityPowerRecipe> STREAM_CODEC = StreamCodec.composite(ValidatedTagKey.STREAM_CODEC, GroveEntityPowerRecipe::tag, ByteBufCodecs.registry(RootsRegistries.Keys.GROVES), GroveEntityPowerRecipe::groveTag, ByteBufCodecs.VAR_INT, GroveEntityPowerRecipe::power, Symmetry.STREAM_CODEC, GroveEntityPowerRecipe::symmetry, ByteBufCodecs.VAR_INT, GroveEntityPowerRecipe::amount, GroveEntityPowerRecipe::new);

  public GroveEntityPowerRecipe(TagKey<EntityType<?>> blockTag, Grove groveTag, int power, Symmetry symmetry, int amount) {
    this(ValidatedTagKey.create(blockTag), groveTag, power, symmetry, amount);
  }

  public TagKey<Item> itemTag() {
    return tag.itemTag();
  }

  public TagKey<EntityType<?>> blockTag() {
    return tag.entityTag();
  }

  public Ingredient itemIngredient() {
    return tag.itemIngredient();
  }

  public static class ValidatedTagKey {
    private static final Interner<ValidatedTagKey> VALUES = Interners.newWeakInterner();

    public static final Codec<ValidatedTagKey> CODEC = TagKey.codec(Registries.ENTITY_TYPE)
        .xmap(ValidatedTagKey::new, ValidatedTagKey::entityTag);
    public static final StreamCodec<ByteBuf, ValidatedTagKey> STREAM_CODEC = ExtraStreamCodecs.ENTITY_TAG_STREAM_CODEC.map(ValidatedTagKey::new, ValidatedTagKey::entityTag);

    private final TagKey<EntityType<?>> entityTag;
    private final TagKey<Item> itemTag;
    private Ingredient itemIngredient = null;

    private boolean checked = false;

    protected ValidatedTagKey(TagKey<EntityType<?>> tag) {
      this.entityTag = tag;
      this.itemTag = TagKey.create(Registries.ITEM, tag.location());
    }

    public TagKey<EntityType<?>> entityTag() {
      return entityTag;
    }

    public TagKey<Item> itemTag() {
      if (!checked) {
        checked = true;
/*        var btag = BuiltInRegistries.BLOCK.getTag(blockTag).orElse(null);
        var itag = BuiltInRegistries.ITEM.getTag(itemTag).orElse(null);
        if (btag == null || itag == null) {
          throw new IllegalStateException("Block tag " + blockTag + " or item tag " + itemTag + " does not exist in GrovePowerRecipe. This should have been caught during validation.");
        }

        if (btag.size() != itag.size()) {
          RootsAPI.LOG.warn("Block tag {} and item tag {} have different sizes ({} vs {}) in GrovePowerRecipe, this may cause issues.", blockTag, itemTag, btag.size(), itag.size());
        }*/
      }

      return itemTag;
    }

    public Ingredient itemIngredient() {
      if (itemIngredient == null) {
        this.itemIngredient = Ingredient.of(itemTag());
      }
      return itemIngredient;
    }

    public static ValidatedTagKey create(TagKey<EntityType<?>> entityTag) {
      return VALUES.intern(new ValidatedTagKey(entityTag));
    }
  }

  // TODO:
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
        var symmetry = gen.symmetry();

        for (SimpleEntityGenerator g : generator) {
          result.add(new GroveEntityPowerRecipe(gen.tag(), o.value(), g.value(), symmetry, max));
        }
      }
    });

    return result;
  }
}
